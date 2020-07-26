package com.example.restulator;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Customer;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.Table;
import com.example.restulator.Models.UnpaidOrder;
import com.example.restulator.Models.User;
import com.example.restulator.Models.Waiter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestulatorAPI {
    @POST("user/")
    Call<ApiResponse<Waiter>> waiterLogin(@Body User user);

    // send a GET request to localhost:3000/api/tables/
    @GET("tables/all")
    Call<ApiResponse<Table>> getTables(
            @Header("authorization") String authorization
    );

    @GET("nonPaid/")
    Call<ApiResponse<UnpaidOrder>> getNonPaidOrders(
            @Header("authorization") String authorization
    );

    @POST("customer/")
    Call<ApiResponse<MySqlResult>> addCustomerToDb(@Body Customer customer, @Header("authorization") String authorization);



}
