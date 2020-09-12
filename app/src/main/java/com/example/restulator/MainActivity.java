package com.example.restulator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Table;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

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


        // Access the token from the shared preference.
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);

        Call<ApiResponse<Table>> call = apiInterface.getTables(accessToken);
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
//                Toast.makeText(getApplicationContext(), "Signing Off", Toast.LENGTH_LONG).show();

                // Deleting all shared preferences data for the current user to logout.
//                SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.clear();
//                editor.apply();
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                finish();
            }
        });
    }




}
