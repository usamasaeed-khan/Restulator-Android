package com.example.restulator;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restulator.Models.Order;
import com.example.restulator.Models.PaymentOrder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderDishesAdapter extends RecyclerView.Adapter<OrderDishesAdapter.OrderDishesAdapterViewHolder> {
    private PaymentOrder[] paymentOrders;
    private Context context;

    public OrderDishesAdapter(PaymentOrder[] paymentOrders, Context context) {
        this.paymentOrders = paymentOrders;
        this.context = context;
    }


    @NonNull
    @Override
    public OrderDishesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new OrderDishesAdapter.OrderDishesAdapterViewHolder(LayoutInflater.from(context).inflate(R.layout.order_dishes_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull OrderDishesAdapterViewHolder holder, int position) {
        final PaymentOrder paymentOrder = paymentOrders[position];
        holder.DishIdTextview.setText(String.valueOf(paymentOrder.getDish_id()));
        holder.DishNameTextView.setText(paymentOrder.getDish_name());
        holder.DishQuantityTextView.setText(String.valueOf(paymentOrder.getDish_quantity()));


    }



    @Override
    public int getItemCount() {
        return paymentOrders.length;
    }
    public class OrderDishesAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView DishIdTextview;
        TextView DishNameTextView;
        TextView DishQuantityTextView;


        public OrderDishesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            DishIdTextview = itemView.findViewById(R.id.DishId);
            DishNameTextView = itemView.findViewById(R.id.DishName);
            DishQuantityTextView = itemView.findViewById(R.id.DishQuantity);
        }
    }
}
