package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.restulator.Models.PaymentOrder;

public class OrderDishesActivity extends AppCompatActivity {
PaymentOrder[] paymentOrders;
RecyclerView orderDishesRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dishes);

        orderDishesRecyclerView = findViewById(R.id.recyclerview_OrderDishes);
        orderDishesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderDishesRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        Intent intentFromUnpaidOrder = getIntent();
        if(intentFromUnpaidOrder.getExtras() != null){
            paymentOrders = (PaymentOrder[]) intentFromUnpaidOrder.getSerializableExtra("PaymentOrders");
            orderDishesRecyclerView.setAdapter(new OrderDishesAdapter(paymentOrders,getApplicationContext()));

        }
        else{
            Toast.makeText(getApplicationContext(), "Dishes not Found",Toast.LENGTH_LONG).show();

        }


    }
}