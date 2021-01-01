package com.example.digibook.Networking;

import android.app.DownloadManager;


import com.example.digibook.models.Comment;
import com.example.digibook.models.Notification;
import com.example.digibook.models.Post;
import com.example.digibook.models.User;
import com.example.digibook.models.booksearchmodels.BookSearch;
import com.example.digibook.models.likepostResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Date;
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

    @GET("api/user/home/getallposts")
    Call<List<Post>> getallposts();

    @POST("api/user/home/addpost")
    Call<Post> addPost(@Body Post post);

    @GET("api/user/home/likepost/{currentUserEmail}/{postOwnerEmail}/{postID}")
    Call<likepostResponse> likepost(@Path("currentUserEmail") String currentUserEmail, @Path("postOwnerEmail") String postOwnerEmail, @Path("postID") String postID);

    @Multipart
    @POST("api/user/profile/uploadpic/{CurrentUserEmail}")
    Call<String> uploadProfilePicture(@Path("CurrentUserEmail") String CurrentUserEmail, @Part MultipartBody.Part file);

    @POST("api/user/profile/update/{email}/{password}")
    Call<User> updateUser(@Path("email") String email, @Path("password") String password, @Body User user);

    @GET("api/user/home/allpostcomments/{date}")
    Call<List<Comment>> getallpostcomments(@Path("date") String date);

    @POST("api/user/home/addcomment")
    Call<Comment> addComment(@Body Comment comment);

    @GET("api/user/notifications/getallnotifications/{currentemail}")
    Call<List<Notification>> getallnotifications(@Path("currentemail") String currentemail);

}
