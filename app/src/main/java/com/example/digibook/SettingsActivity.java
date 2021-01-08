package com.example.digibook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.models.User;
import com.example.digibook.utilities.CurrentSession;
import com.example.digibook.utilities.RealPathUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 1;

    Button update;
    EditText name,email,password;
    ImageView pic;
    String imageurl;
    Uri imageURi;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name = findViewById(R.id.settingsName);
        email = findViewById(R.id.settingsEmail);
        password = findViewById(R.id.settingsPassword);
        update= findViewById(R.id.settingsUpdateButton);
        pic = findViewById(R.id.settingsImage);

        name.setText(CurrentSession.CurrentUser.getName());
        email.setText(CurrentSession.CurrentUser.getEmail());
        password.setText(CurrentSession.CurrentUser.getPassword());

        // toolbar handle
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Choose pic from gallery
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("testtt", APIclient.base_url + CurrentSession.CurrentUser.getPicurl().toString());

                // PERMISSION
                if(checkPermissionForReadExtertalStorage() == false) {
                    try {
                        requestPermissionForReadExtertalStorage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                startActivityForResult(chooserIntent, 1);

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User updatedUser = new User();

                updatedUser.setPassword(CurrentSession.CurrentUser.getPassword());
                updatedUser.setEmail(CurrentSession.CurrentUser.getEmail());
                updatedUser.setName(CurrentSession.CurrentUser.getName());
                updatedUser.setDate(CurrentSession.CurrentUser.getDate());
                updatedUser.setId(CurrentSession.CurrentUser.getId());
                updatedUser.setPicurl(CurrentSession.CurrentUser.getPicurl());
                updatedUser.setV(CurrentSession.CurrentUser.getV());

                updatedUser.setName(name.getText().toString());
                updatedUser.setEmail(email.getText().toString());
                updatedUser.setPassword(password.getText().toString());

//                File image = new File("file://" +imageURi.getHost().toString() + imageURi.getPath().toString());
/*                File imagetest = new File(imageURi.toString());

                URL pathimage = null;
                try {
                    pathimage = imagetest.toURL();
                    Log.d("tourl", pathimage.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                File image = new File("file:///content:/com.android.providers.media.documents/document/image:41430");

                Log.d("imagework", image.getName().toString());*/


                File image = CurrentSession.bitmapToFile(getApplicationContext(), bitmap, CurrentSession.CurrentUser.getEmail() + ".png");
                RequestBody reqbody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
                MultipartBody.Part part = MultipartBody.Part.createFormData("profilepicture", image.getName() , reqbody);
                Log.d("imagework", image.getName().toString());
                Call<String> uploadCall = APIclient.apIinterface().uploadProfilePicture(CurrentSession.CurrentUser.getEmail(), part);
                uploadCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            CurrentSession.CurrentUser.setPicurl(response.body());
                            updatedUser.setPicurl(response.body());
                            CurrentSession.updateUser(updatedUser, getApplicationContext(), CurrentSession.CurrentUser.getEmail(), CurrentSession.CurrentUser.getPassword().toString());

                        }else {
                            Log.d("uploadImageNet", "unsuc");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("uploadImageNet", t.toString());
                    }
                });
            }
        });

        Glide.with(getApplicationContext())
                .load(APIclient.base_url + CurrentSession.CurrentUser.getPicurl())
                .into(pic);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri uri = data.getData();

/*            Log.d("FILEPATHBRO", uri.getPathSegments().toString() + " " + uri.getPath() + " " + uri.getEncodedPath() + " " + uri.getLastPathSegment());
            String path = null;
            if (Build.VERSION.SDK_INT < 11)
                path = RealPathUtils.getRealPathFromURI_BelowAPI11(getApplicationContext(), uri);

                // SDK >= 11 && SDK < 19
            else if (Build.VERSION.SDK_INT < 19)
                path = RealPathUtils.getRealPathFromURI_API11to18(getApplicationContext(), uri);

                // SDK > 19 (Android 4.4)
            else
                path = RealPathUtils.getRealPathFromURI_API19(getApplicationContext(), uri);
            Log.d("FILEPATHBRO", "File Path: " + path);*/

            // Get the file instance

//            Glide.with(getContext())
//                    .load(APIclient.base_url + CurrentSession.CurrentUser.getPicurl())
//    .into(image);
            //imageurl = path;
            imageURi = uri;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURi);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.d("imageworks", uri.getPath() + " " + uri.getLastPathSegment() + " " + uri.getAuthority() + " " + uri .getPathSegments().toString() + " " + uri.getFragment() + " " + uri.getHost() + " " + uri.getUserInfo());
            pic.setImageURI(uri);
        }else{
            Toast.makeText(getApplicationContext(),"Please choose a picture!",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = getApplicationContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}