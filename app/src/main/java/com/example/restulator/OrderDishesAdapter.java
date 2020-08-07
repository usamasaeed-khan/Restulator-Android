package com.example.restulator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.EditDish;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.Order;
import com.example.restulator.Models.PaymentOrder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDishesAdapter extends RecyclerView.Adapter<OrderDishesAdapter.OrderDishesAdapterViewHolder> {
    private PaymentOrder[] paymentOrders;
    private Context context;
    RestulatorAPI apiInterface;

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
    public void onBindViewHolder(@NonNull OrderDishesAdapterViewHolder holder, final int position) {
        final PaymentOrder paymentOrder = paymentOrders[position];
        holder.DishQuantityTextview.setText(String.valueOf(paymentOrder.getDish_quantity()));
        holder.DishNameTextView.setText(paymentOrder.getDish_name());
        holder.DishPriceTextview.setText(String.valueOf(paymentOrder.getPrice()));
        holder.EditDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditDishActivity.class);
                intent.putExtra("dishDetails",paymentOrders[position]);
                context.startActivity(intent);
            }
        });
        holder.DeleteDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "u clicked delete button!!", Toast.LENGTH_SHORT).show();
                deleteOrder(paymentOrder.getOrder_id(), paymentOrder.getDish_id(), paymentOrder.getDish_quantity(), paymentOrder.getPrice());
            }
        });
    }
    @Override
    public int getItemCount() {
        return paymentOrders.length;
    }
    public class OrderDishesAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView DishQuantityTextview;
        TextView DishNameTextView;
        TextView DishPriceTextview;
        Button EditDish;
        Button DeleteDish;

        public OrderDishesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            DishQuantityTextview = itemView.findViewById(R.id.DishQuantityData);
            DishNameTextView = itemView.findViewById(R.id.DishName);
            DishPriceTextview = itemView.findViewById(R.id.DishPriceData);
            EditDish = itemView.findViewById(R.id.editDishButton);
            DeleteDish = itemView.findViewById(R.id.dish_delete);
        }
    }
    public void deleteOrder(final int order_id, final int dish_id, final int dish_quantity, final float dish_price) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle(R.string.deleteAlertTitle);
        builderSingle.setMessage(R.string.deleteAlertMessage);
        builderSingle.setIcon(R.drawable.ic_delete_icon);
        builderSingle.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getApplicationContext(), "Cancel",Toast.LENGTH_LONG).show();
                    }
                });
        builderSingle.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteDish(order_id, dish_id, dish_quantity, dish_price);
                    }
                });
        builderSingle.show();
    }
    public void deleteDish(int order_id, int dish_id, int dish_quantity, float dish_price){
        SharedPreferences pref = context.getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        float total_price = dish_quantity * dish_price;
        EditDish delDish = new EditDish(order_id,dish_id,total_price);

        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<MySqlResult>> call = apiInterface.deleteDish(delDish, accessToken);
        call.enqueue(new Callback<ApiResponse<MySqlResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<MySqlResult>> call, Response<ApiResponse<MySqlResult>> response) {
                if(response.body() != null ? response.body().getStatus() : false){
                    Toast.makeText(context, "Dish deleted Successful!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, UnpaidOrders.class);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Unsuccessful",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<MySqlResult>> call, Throwable t) {
            }
        });
    }
}