package com.example.digibook.Networking;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class APIclient {

    // Define base url
    public static String base_url = "http://192.168.1.4:3000/";

    //Retrofit instance
    public static Retrofit getClient(){

        //here u set timeouts and okhttp settings
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60000, TimeUnit.SECONDS)
                .connectTimeout(60000, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static APIinterface apIinterface(){
        return getClient().create(APIinterface.class);
    }
}
