package com.example.restulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Customer;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.Table;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerActivity extends AppCompatActivity {

    private String customerName, customerEmail;
    RelativeLayout progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        progressBar = findViewById(R.id.progress_bar);
        Button addCustomerButton = findViewById(R.id.add_customer_btn);
        EditText addCustomerName = findViewById(R.id.add_customer_name);
        EditText addCustomerEmail = findViewById(R.id.add_customer_email);

        customerName = addCustomerName.getText().toString();
        customerEmail = addCustomerEmail.getText().toString();

        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer(customerName, customerEmail);
            }
        });




    }

    private void addCustomer(String addCustomerName, String addCustomerEmail) {

        Customer customer = new Customer(addCustomerName, addCustomerEmail);

        RestulatorAPI apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);



        // Access the token from the shared preference.
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);

        Call<ApiResponse<MySqlResult>> call = apiInterface.addCustomerToDb(customer, accessToken);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<ApiResponse<MySqlResult>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<MySqlResult>> call, @NonNull Response<ApiResponse<MySqlResult>> response) {
                // Checking api response status.
                if(response.body() != null ? response.body().getStatus() : false){
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);

                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<MySqlResult>> call,@NonNull Throwable t) {

                // If incomplete, toast the error message.
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}