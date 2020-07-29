package com.example.restulator;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.CheckIngredients;
import com.example.restulator.Models.Cook;
import com.example.restulator.Models.Customer;
import com.example.restulator.Models.Dish;
import com.example.restulator.Models.DishType;
import com.example.restulator.Models.Order;
import com.example.restulator.Models.PossibleDishes;
import com.example.restulator.Models.Table;
import com.example.restulator.Models.User;
import com.example.restulator.Models.Waiter;
import com.example.restulator.Models.WaiterData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestulatorAPI {

    // send a GET request to local:3000/api/tables/all
    @GET("tables/all")
    Call<ApiResponse<Table>> getTables();

    @POST("user/")
    Call<ApiResponse<Waiter>> waiterLogin(@Body User user);

    @GET("allOrders/")
    Call<ApiResponse<Order>> getAllOrders();

    @GET("employeeCategory/getWaiter")
    Call<ApiResponse<WaiterData>> getWaiter();

    @GET("employeeCategory/getCook/name")
    Call<ApiResponse<Cook>> getCook();

    @GET("customer/")
    Call<ApiResponse<Customer>> getCustomer();

    @GET("dishType/")
    Call<ApiResponse<DishType>> getdishType();

    @GET("dish/{dishType}")
    Call<ApiResponse<Dish>> getDish(@Path("dishType") int id);

    @POST("order/")
    Call<ApiResponse<Order>> placeOrder(@Body Order order);

    @POST("ingredients/check/quantity/{dishId}")
    Call<ApiResponse<PossibleDishes>> checkIngredients(@Path("dishId") int dishId, @Body CheckIngredients checkIngredients);



}