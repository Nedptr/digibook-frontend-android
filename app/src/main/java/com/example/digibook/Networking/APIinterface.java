package com.example.digibook.Networking;

import com.example.digibook.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIinterface {

    @GET("dbtest")
    Call<List<User>> getUsers();

    @GET("hey")
    Call<String> getHey();

}
