package com.example.digibook.utilities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;

import com.example.digibook.LoginActivity;
import com.example.digibook.MainNavActivity;
import com.example.digibook.Networking.APIclient;
import com.example.digibook.SearchResults;
import com.example.digibook.fragments.SearchFragment;
import com.example.digibook.models.User;
import com.example.digibook.models.booksearchmodels.BookSearch;

import java.io.File;
import java.io.Serializable;
import java.util.zip.CheckedOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentSession {


    // static useful variables
    public static User CurrentUser = new User();
    public static String CurrentUserEmail;
    public static String CurrentUserToken;



    public static void getCurrentUser(String email) {
        Call<User> getUserCall = APIclient.apIinterface().getCurrentUser(email);
        getUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                CurrentUser = response.body();
                Log.d("CurrentSessionTest", CurrentUser.getName());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("CurrentSessionTestF", t.toString());
            }
        });

    }

    // TODO: handle erros and exeptions , for example if totalitems = 0 > NOT FOUND BOOK ...
    public static void uploadImageSearch(String imageurl, Context ct){
        File image = new File(imageurl);
        RequestBody reqbody = RequestBody.create(MediaType.parse("multipart/form-data"),image);
        MultipartBody.Part part = MultipartBody.Part.createFormData("textimage", image.getName() , reqbody);

        Call<BookSearch> uploadCall = APIclient.apIinterface().uploadImage(part);
        uploadCall.enqueue(new Callback<BookSearch>() {
            @Override
            public void onResponse(Call<BookSearch> call, Response<BookSearch> response) {
                if(response.isSuccessful()){
                    Log.d("uploadImageNet", response.body().getTotalItems().toString());
                    Intent data = new Intent( ct, SearchResults.class);
                    data.putExtra("data", response.body());
                    ct.startActivity(data);
                }else {
                    Log.d("uploadImageNet", "unsuc");
                }
            }

            @Override
            public void onFailure(Call<BookSearch> call, Throwable t) {
                Log.d("uploadImageNet", t.toString());
            }
        });
    }

}
