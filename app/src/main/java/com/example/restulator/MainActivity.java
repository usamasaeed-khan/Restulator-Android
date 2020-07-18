package com.example.restulator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Table;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Retrofit Instance object declaration.
    RestulatorAPI apiInterface;
    RelativeLayout progressBar;
    GridLayoutManager layoutManager;
    RecyclerView recyclerView;
    Table[] tablesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.recycler_view_tables);
        recyclerView.setLayoutManager(layoutManager);


        // Creating retrofit instance to call the getTables() method.
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);

        // Initialize call to the api method getTables()
        Intent intent = getIntent();
        String access_token = intent.getStringExtra("ACCESS_TOKEN");
        Call<ApiResponse<Table>> call = apiInterface.getTables(access_token);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ApiResponse<Table>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Table>> call,@NonNull Response<ApiResponse<Table>> response) {


                // Checking api response status.
                if(response.body() != null ? response.body().getStatus() : false){
                    tablesData = response.body().getData();

                    progressBar.setVisibility(View.INVISIBLE);

                    recyclerView.setAdapter(new TablesScreenAdapter(getApplicationContext(), tablesData));

               }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Table>> call,@NonNull Throwable t) {

                // If incomplete, toast the error message.
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
