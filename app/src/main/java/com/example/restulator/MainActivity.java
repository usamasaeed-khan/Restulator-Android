package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Table;

import java.sql.ResultSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Retrofit Instance object declaration.
    RestulatorAPI apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating retrofit instance to call the getTables() method.
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);

        // Initialize call to the api method getTables()
        Call<ApiResponse> call = apiInterface.getTables();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                // Checking api response status.
                Toast.makeText(getApplicationContext(), response.body().status.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                // If incomplete, toast the error message.
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
