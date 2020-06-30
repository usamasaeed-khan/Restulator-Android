package com.example.restulator;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    // This is the base URL, for all the api requests.
    private final static String BASE_URL = "http://10.0.2.2:3000/api/";

    // Set BASE_URL, GsonConverterFactory( which converts json to our models) and return retrofit instance.
    static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
