package com.example.restulator;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Table;
import com.example.restulator.Models.User;
import com.example.restulator.Models.Waiter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestulatorAPI {

    // send a GET request to local:3000/api/tables/all
    @GET("tables/all")
    Call<ApiResponse<Table>> getTables();

    @POST("user/")
    Call<ApiResponse<Waiter>> waiterLogin(@Body User user);



}
