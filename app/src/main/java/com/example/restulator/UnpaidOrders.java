package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.UnpaidOrder;
import com.example.restulator.Models.Waiter;

public class UnpaidOrders extends AppCompatActivity {
    RestulatorAPI apiInterface;
    RelativeLayout progressBar;
    RecyclerView recyclerView;
    UnpaidOrder[] orders;
    UnpaidOrdersScreenAdapter unpaidOrdersScreenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid_orders);

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recyclerview_unpaidOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        Call<ApiResponse<UnpaidOrder>> call = apiInterface.getNonPaidOrders(accessToken);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ApiResponse<UnpaidOrder>>() {
            @Override
            public void onResponse(Call<ApiResponse<UnpaidOrder>> call, Response<ApiResponse<UnpaidOrder>> response) {
                if(response.body() != null ? response.body().getStatus() : false){
                    orders = response.body().getData();
                    progressBar.setVisibility(View.INVISIBLE);
                    recyclerView.setAdapter(new UnpaidOrdersScreenAdapter(orders,getApplicationContext()));

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UnpaidOrder>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);

//                SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.clear();
//                editor.apply();
            }
        });
    }
}