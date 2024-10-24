package com.edunexa;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static volatile Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitInstance.class) {
                if (retrofit == null) {
                    Gson gson = new GsonBuilder()
                            .setLenient()
                            .create();
                    retrofit = new Retrofit.Builder()
                            .baseUrl("http://192.168.1.7:8080/")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                }
            }
        }
        return retrofit;
    }
}
