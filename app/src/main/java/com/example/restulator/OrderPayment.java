package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class OrderPayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

        Intent intentFromUnpaidOrderDetail = getIntent();
        int orderId = intentFromUnpaidOrderDetail.getExtras().getInt("OrderId");
        Toast.makeText(getApplicationContext(), "Order ID : " +orderId,Toast.LENGTH_LONG).show();

    }
}