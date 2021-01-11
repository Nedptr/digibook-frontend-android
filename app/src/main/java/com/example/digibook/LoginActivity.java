package com.example.digibook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digibook.Networking.APIclient;
import com.example.digibook.models.User;
import com.example.digibook.models.booksearchmodels.BookSearch;
import com.example.digibook.utilities.CurrentSession;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    TextView registerAct;
    GoogleSignInClient mGoogleSignInClient;
    Button loginButton;
    EditText loginUsername;
    EditText loginPassword;
    TextView loginError;
    CheckBox remember;

    public static final String FILE_NAME = "com.example.digibook.shared";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        //Initializing Views
        signInButton = findViewById(R.id.sign_in_button);
        registerAct = findViewById(R.id.login);
        loginButton = findViewById(R.id.loginButton);


        //login Button onclick listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginIn();
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //Go to Register Activity
        registerAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("text", "Italians have a little joke, that the world is so hard a man must have two fathers to look after him, and that's why they have godfathers");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<BookSearch> SearchCall = APIclient.apIinterface().searchBook(json);
                SearchCall.enqueue(new Callback<BookSearch>() {
                    @Override
                    public void onResponse(Call<BookSearch> call, Response<BookSearch> response) {
                        if(response.isSuccessful()){
                            Log.d("searchnet", "success");
                            Log.d("searchnet", response.body().getItems().get(0).getVolumeInfo().getTitle().toString());
                        }else {
                            Log.d("searchnet", "UNsucc");
                            Log.d("searchnet", response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<BookSearch> call, Throwable t) {
                        Log.d("searchnetFail", t.toString());
                    }
                });

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loginIn() {
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        loginError = findViewById(R.id.loginError);
        remember = findViewById(R.id.login_checkbox);
        String email = loginUsername.getText().toString();
        String password = loginPassword.getText().toString();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // doing the call
        Call<String> loginCall = APIclient.apIinterface().loginUser(user);
        loginCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                    //cache

                    if(remember.isChecked()){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("EMAIL", email);
                        editor.putString("PASSWORD", email);
                        editor.apply();
                    }

                    Toast.makeText(LoginActivity.this, response.body() ,Toast.LENGTH_SHORT).show();
                    assert response.body() != null;
                    Log.d("loginNet", response.body());
                    loginError.setVisibility(View.INVISIBLE);

                    //CurrentSession Handler:
                    //assign static variables!
                    //I might need to create a CurrentSession instance here to access none static variables and remove public.
                    CurrentSession.CurrentUserEmail = email;
                    CurrentSession.CurrentUserToken = "";

                    CurrentSession.getCurrentUser(email);

                    // go to mainNavActivity Home Fragment
                    Intent goMainNavAct = new Intent(LoginActivity.this, MainNavActivity.class);
                    startActivity(goMainNavAct);

                } else {
                    Log.d("loginNet", "unsucc request");
                    loginError.setVisibility(View.VISIBLE);
                    try {
                        loginError.setText(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("loginNet1" , t.toString());
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        super.onStart();
    }










}