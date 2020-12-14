package com.example.digibook.Networking;

import android.app.DownloadManager;


import com.example.digibook.models.User;
import com.example.digibook.models.booksearchmodels.BookSearch;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIinterface {

    @GET("dbtest")
    Call<List<User>> getUsers();

    @GET("hey")
    Call<String> getHey();

    @POST("api/user/register")
    Call<User> registerUser(@Body User user);

    @POST("api/user/login")
    Call<String> loginUser(@Body User user);

    @GET("api/CurrentSession/{email}")
    Call<User> getCurrentUser(@Path("email") String email);

    @POST("booksearch/test")
    Call<BookSearch> searchBook(@Body JSONObject json);

    @Multipart
    @POST("booksearch/search")
    Call<BookSearch> uploadImage(@Part MultipartBody.Part file);


/*    @POST("api/user/register")
    Call<JsonObject> registerUser(@Body User user);*/

}
