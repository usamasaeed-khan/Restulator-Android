package com.example.restulator;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Customer;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.PaymentOrder;
import com.example.restulator.Models.PaymentUpdate;
import com.example.restulator.Models.Table;
import com.example.restulator.Models.UnpaidOrder;
import com.example.restulator.Models.User;
import com.example.restulator.Models.Waiter;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("nonPaid/payment-order/{orderId}")
    Call<ApiResponse<PaymentOrder>> getOrderPayments(
            @Path("orderId") Integer orderId,
            @Header("authorization") String authorization);

    @PUT("nonPaid/payment-order/payment/{orderId}")
    Call<ApiResponse<MySqlResult>> updatePayment(
            @Path("orderId") Integer orderId,
            @Body PaymentUpdate paymentUpdate,
            @Header("authorization") String authorization);




}
