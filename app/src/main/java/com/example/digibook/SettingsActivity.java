package com.example.digibook;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.digibook.fragments.HomeRVAdapter;
import com.example.digibook.models.Post;
import com.example.digibook.models.User;
import com.example.digibook.utilities.CurrentSession;
import com.example.digibook.utilities.RealPathUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

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
    TextView seterror;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name = findViewById(R.id.settingsName);
        email = findViewById(R.id.settingsEmail);
        password = findViewById(R.id.settingsPassword);
        update= findViewById(R.id.settingsUpdateButton);
        pic = findViewById(R.id.settingsImage);
        seterror = findViewById(R.id.settingsError);


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



                final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Choose your profile picture");

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (options[item].equals("Take Photo")) {
                            if(checkPermissionForReadExtertalStorage() == false) {
                                try {
                                    requestPermissionForReadExtertalStorage();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);

                        } else if (options[item].equals("Choose from Gallery")) {
                            if(checkPermissionForReadExtertalStorage() == false) {
                                try {
                                    requestPermissionForReadExtertalStorage();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
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
                updatedUser.setConfirmPassword(password.getText().toString());

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

                if(bitmap != null) {
                    File image = CurrentSession.bitmapToFile(getApplicationContext(), bitmap, CurrentSession.CurrentUser.getEmail() + ".png");
                    RequestBody reqbody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
                    MultipartBody.Part part = MultipartBody.Part.createFormData("profilepicture", image.getName(), reqbody);
                    Call<String> uploadCall = APIclient.apIinterface().uploadProfilePicture(CurrentSession.CurrentUser.getEmail(), part);
                    uploadCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.isSuccessful()) {
                                Log.d("crashfixImageUpload", response.body().toString());
                                CurrentSession.CurrentUser.setPicurl(response.body());
                                updatedUser.setPicurl(response.body());
                                CurrentSession.updateUser(updatedUser, getApplicationContext(), CurrentSession.CurrentUser.getEmail(), CurrentSession.CurrentUser.getPassword().toString(), seterror);

                            } else {
                                Log.d("uploadImageNet", "unsuc");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("uploadImageNet", t.toString());
                        }

                    });
                }else {
                    CurrentSession.updateUser(updatedUser, getApplicationContext(), CurrentSession.CurrentUser.getEmail(), CurrentSession.CurrentUser.getPassword().toString(), seterror);
                }
            }
        });

        Glide.with(getApplicationContext())
                .load(APIclient.base_url + CurrentSession.CurrentUser.getPicurl())
                .into(pic);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {

                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");

                        //pic.setImageBitmap(selectedImage);
                        bitmap = selectedImage;
                        pic.setImageBitmap(bitmap);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        imageURi = selectedImage;
                        pic.setImageURI(imageURi);
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageURi);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                //pic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(),"Please Choose a Picture!",Toast.LENGTH_SHORT).show();
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