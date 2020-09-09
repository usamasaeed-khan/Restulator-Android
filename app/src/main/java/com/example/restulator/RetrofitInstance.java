package com.example.restulator;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Interceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Context context;

    // This is the base URL, for all the api requests.
    private final static String BASE_URL = "http://192.168.10.7:3000/api/";
//10.0.2.2
    // Set BASE_URL, GsonConverterFactory( which converts json to our models) and return retrofit instance.

    private final static OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new AddCookiesInterceptor(context)).addInterceptor(new ReceivedCookiesInterceptor(context)).build();
//
//    OkHttpClient client = new OkHttpClient();
//    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//    AddCookiesInterceptor addCookiesInterceptor = new AddCookiesInterceptor(context);
//    builder.addInterceptor(addCookiesInterceptor); // VERY VERY IMPORTANT
//    builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
//    client = builder.build();

    static OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " )
                        .build();
                return chain.proceed(newRequest);
            }
        }).build();


    static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


}


