package com.example.digibook.utilities;

import android.util.Log;

import com.example.digibook.Networking.APIclient;
import com.example.digibook.models.User;

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


}
