package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.PixelCopy;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.PaymentOrder;
import com.example.restulator.Models.PaymentUpdate;
import com.example.restulator.Models.UnpaidOrder;

public class OrderPayment extends AppCompatActivity {

    PaymentOrder[] paymentOrders;
    RestulatorAPI apiInterface;
    TextView totalPriceTextview, taxTextview, billTextview,changeTextview;
    EditText paymentEdittext;
    PaymentUpdate paymentUpdate;
    Integer orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

        Intent intentFromUnpaidOrderDetail = getIntent();
        orderId = intentFromUnpaidOrderDetail.getExtras().getInt("OrderId");
        Toast.makeText(getApplicationContext(), "Order ID : " +orderId,Toast.LENGTH_LONG).show();

        totalPriceTextview = findViewById(R.id.TotalPriceData);
        taxTextview = findViewById(R.id.TaxData);
        billTextview = findViewById(R.id.BillData);
        paymentEdittext = (EditText) findViewById(R.id.PaymentBox);
        changeTextview = findViewById(R.id.ChangeData);




        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<PaymentOrder>> call = apiInterface.getOrderPayments(orderId,accessToken);
        call.enqueue(new Callback<ApiResponse<PaymentOrder>>() {
            @Override
            public void onResponse(Call<ApiResponse<PaymentOrder>> call, Response<ApiResponse<PaymentOrder>> response) {
                if(response.body() != null ? response.body().getStatus() : false){
                    paymentOrders = response.body().getData();
                    totalPriceTextview.setText(String.valueOf(paymentOrders[0].getPrice()));
                    taxTextview.setText(String.valueOf(paymentOrders[0].getTax()));
                    billTextview.setText(String.valueOf(paymentOrders[0].getBill()));

                    paymentEdittext.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if(!String.valueOf(s).matches("")){
                                float payment = Float.parseFloat(String.valueOf(s));
                                paymentUpdate = new PaymentUpdate(payment);
                                if(paymentOrders[0].getBill() - payment > 0){

                                    changeTextview.setText(String.valueOf(paymentOrders[0].getBill() - payment));

                                }
                                else{
                                    changeTextview.setText(String.valueOf(0));
                                }
                            }
                            else{
                                changeTextview.setText(String.valueOf(0));
                            }




                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PaymentOrder>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });







    }

    public void makePayment(View view) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<MySqlResult>> paymentCall = apiInterface.updatePayment(orderId,paymentUpdate,accessToken);
        paymentCall.enqueue(new Callback<ApiResponse<MySqlResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<MySqlResult>> call, Response<ApiResponse<MySqlResult>> response) {
                if(response.body() != null ? response.body().getStatus() : false){
                    Toast.makeText(getApplicationContext(), "Payment Successful!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), UnpaidOrders.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<MySqlResult>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }
}