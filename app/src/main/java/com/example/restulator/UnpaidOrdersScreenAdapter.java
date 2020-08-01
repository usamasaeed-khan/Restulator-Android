package com.example.restulator;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restulator.Models.UnpaidOrder;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UnpaidOrdersScreenAdapter extends RecyclerView.Adapter<UnpaidOrdersScreenAdapter.UnpaidOrdersScreenAdapteViewHolder> {
    private UnpaidOrder[] unpaidOrders;
    private Context context;

    public UnpaidOrdersScreenAdapter(UnpaidOrder[] unpaidOrders,Context context) {
        this.unpaidOrders = unpaidOrders;
        this.context = context;
    }


    @NonNull
    @Override
    public UnpaidOrdersScreenAdapteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new UnpaidOrdersScreenAdapter.UnpaidOrdersScreenAdapteViewHolder(LayoutInflater.from(context).inflate(R.layout.unpaid_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnpaidOrdersScreenAdapteViewHolder holder, int position) {
        final UnpaidOrder unpaidOrderResponse = unpaidOrders[position];
        holder.OrderIdTextview.setText("Order#: "+ String.valueOf(unpaidOrderResponse.getId()));
        holder.CustomerNameTextView.setText("Customer: "+unpaidOrderResponse.getCustomer_name());

        holder.OrderTotalTextView.setText(String.valueOf(unpaidOrderResponse.getBill()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UnpaidOrderDetail.class);
                intent.putExtra("UnpaidOrder",  unpaidOrderResponse);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return unpaidOrders.length;
    }

    public class UnpaidOrdersScreenAdapteViewHolder extends RecyclerView.ViewHolder {
        TextView OrderIdTextview;
        TextView CustomerNameTextView;
        TextView OrderTotalTextView;


        public UnpaidOrdersScreenAdapteViewHolder(@NonNull View itemView) {
            super(itemView);
            OrderIdTextview = itemView.findViewById(R.id.OrderId);
            CustomerNameTextView = itemView.findViewById(R.id.CustomerName);
            OrderTotalTextView = itemView.findViewById(R.id.OrderTotal);
        }
    }
}