package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.PaymentOrder;
import com.example.restulator.Models.PaymentUpdate;

public class OrderDishesActivity extends AppCompatActivity {
PaymentOrder[] paymentOrders;
RestulatorAPI apiInterface;
RecyclerView orderDishesRecyclerView;
int orderId;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dishes);

        orderDishesRecyclerView = findViewById(R.id.recyclerview_OrderDishes);
        orderDishesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderDishesRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        Intent intentFromUnpaidOrderDetail = getIntent();
        orderId = intentFromUnpaidOrderDetail.getExtras().getInt("OrderId");


        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<PaymentOrder>> call = apiInterface.getOrderPayments(orderId,accessToken);
        call.enqueue(new Callback<ApiResponse<PaymentOrder>>() {
            @Override
            public void onResponse(Call<ApiResponse<PaymentOrder>> call, Response<ApiResponse<PaymentOrder>> response) {
                if(response.body() != null ? response.body().getStatus() : false){
                    paymentOrders = response.body().getData();
                    orderDishesRecyclerView.setAdapter(new OrderDishesAdapter(paymentOrders,getApplicationContext()));





                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PaymentOrder>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });





//        Intent intentFromUnpaidOrder = getIntent();
//        if(intentFromUnpaidOrder.getExtras() != null){
//            paymentOrders = (PaymentOrder[]) intentFromUnpaidOrder.getSerializableExtra("PaymentOrders");
//            orderDishesRecyclerView.setAdapter(new OrderDishesAdapter(paymentOrders,getApplicationContext()));
//
//        }
//        else{
//            Toast.makeText(getApplicationContext(), "Dishes not Found",Toast.LENGTH_LONG).show();
//
//        }


    }
}