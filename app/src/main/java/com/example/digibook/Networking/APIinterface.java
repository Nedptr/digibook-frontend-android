package com.example.digibook.Networking;

import com.example.digibook.models.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("/api/CurrentSession/{email}")
    Call<User> getCurrentUser(@Path("email") String email);




/*    @POST("api/user/register")
    Call<JsonObject> registerUser(@Body User user);*/

}
