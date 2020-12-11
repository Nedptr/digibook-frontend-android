package com.example.digibook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digibook.Networking.APIclient;
import com.example.digibook.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView loginAct;
    EditText regUsername;
    EditText regEmail;
    EditText regPassword;
    EditText regConfirmPassword;
    Button regButton;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //GO back to login Activity
        loginAct = findViewById(R.id.login);
        loginAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        //Handle Register
        regUsername = findViewById(R.id.regUsername);
        regEmail = findViewById(R.id.regEmail);
        regPassword = findViewById(R.id.regPassword);
        regConfirmPassword = findViewById(R.id.regConfirmPassword);
        regButton = findViewById(R.id.regButton);
        errorText = findViewById(R.id.regError);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : put all this in a function & confirm password!
                String name = regUsername.getText().toString();
                String email = regEmail.getText().toString();
                String password = regPassword.getText().toString();
                String ConfirmPassword = regConfirmPassword.getText().toString();
                User player = new User();
                player.setEmail(email);
                player.setName(name);
                player.setPassword(password);
                player.setConfirmPassword(ConfirmPassword);
                //doing the call
                Call<User> register = APIclient.apIinterface().registerUser(player);
                register.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()){
                            Log.d("RegisterNet", String.valueOf(response.code()));
                            Toast.makeText(RegisterActivity.this,"Successfully Registered!",Toast.LENGTH_SHORT).show();
                            Intent goProfile = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(goProfile);
                        }else{
                            Log.d("RegisterNet", "unsucc response");
                            errorText.setVisibility(View.VISIBLE);
                            try {
                                errorText.setText(response.errorBody().string());
                            } catch (IOException e) {
                                Log.d("RegisterNet", "failed to string errorBody()");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d("RegisterNet", t.toString());
                    }
                });

            }
        });


    }
}