package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Table;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Retrofit Instance object declaration.
    RestulatorAPI apiInterface;
    RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);


        // Creating retrofit instance to call the getTables() method.
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);

        // Initialize call to the api method getTables()
        Call<ApiResponse<Table>> call = apiInterface.getTables();
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ApiResponse<Table>>() {
            @Override
            public void onResponse(Call<ApiResponse<Table>> call, Response<ApiResponse<Table>> response) {

                if(response.body() != null ? response.body().getStatus() : false){
                    // Checking api response status.
                    ArrayList<Table> tableList = response.body().getData();

                    progressBar.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onFailure(Call<ApiResponse<Table>> call, Throwable t) {

                // If incomplete, toast the error message.
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
