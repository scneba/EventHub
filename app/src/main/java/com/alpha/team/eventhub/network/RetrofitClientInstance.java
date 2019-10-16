package com.alpha.team.eventhub.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by clasence on 24,November,2018
 * On my honor, as a Carnegie-Mellon Africa student, I have neither given nor received unauthorized assistance on this work.
 */
public class RetrofitClientInstance {

    private static Retrofit retrofit;

    public static final String BASE_URL = "http://192.168.8.100:8080";
    //public static final String BASE_URL = "http://172.29.53.157:8080";
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}