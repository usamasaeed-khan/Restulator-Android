package com.example.restulator;

import com.example.restulator.Models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;


public interface RestulatorAPI {

    // send a GET request to local:3000/api/tables/all
    @GET("tables/all")
    Call<ApiResponse> getTables();
}
