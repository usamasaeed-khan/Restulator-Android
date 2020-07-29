package com.example.restulator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.Order;
import com.example.restulator.Models.UnpaidOrder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class UnpaidOrderDetail extends AppCompatActivity{
    UnpaidOrder unpaidOrder;
    TextView customerTextView, orderIdTextview, orderDateTextview, cookNameTextView, waiterNameTextview, completeTimeTextView, orderStatusTextview, billTextview;
    int OrderId;
    RestulatorAPI apiInterface;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unpaid_order_detail);
        customerTextView = findViewById(R.id.CustomerNameData);
        orderIdTextview = findViewById(R.id.OrderIdData);
        orderDateTextview = findViewById(R.id.OrderDateData);
        waiterNameTextview = findViewById(R.id.WaiterNameData);
        cookNameTextView = findViewById(R.id.CookNameData);
        completeTimeTextView = findViewById(R.id.CompleteTimeData);
        orderStatusTextview = findViewById(R.id.OrderStatusData);
        billTextview = findViewById(R.id.BillData);

        setDetails();



    }
    public String formatDateTime(String datetimestr){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String outputstr="";
        try {
            Date inputDatetimestr  = inputFormat.parse(datetimestr);
            outputstr = outputFormat.format(inputDatetimestr);

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cannot format Order or Complete time" ,Toast.LENGTH_LONG).show();
        }
        return outputstr;

    }

    public void setDetails()  {
        Intent intentFromUnpaidOrders = getIntent();


        if(intentFromUnpaidOrders.getExtras() != null){
            unpaidOrder = (UnpaidOrder) intentFromUnpaidOrders.getSerializableExtra("UnpaidOrder");
            String customerName = unpaidOrder.getCustomer_name();
            String orderId = ""+ unpaidOrder.getId();

            OrderId = unpaidOrder.getId();

            orderIdTextview.setText(orderId);
            customerTextView.setText(customerName);
            waiterNameTextview.setText(unpaidOrder.getWaiter_name());
            cookNameTextView.setText(unpaidOrder.getCook_name());
            billTextview.setText(String.valueOf(unpaidOrder.getBill()));
            orderStatusTextview.setText(unpaidOrder.getOrder_status());

            String OrderTimeStr = formatDateTime(unpaidOrder.getOrder_time());
            String CompleteTimeStr = formatDateTime(unpaidOrder.getComplete_time());
            orderDateTextview.setText(OrderTimeStr);
            completeTimeTextView.setText(CompleteTimeStr);


        }
        else{
            Toast.makeText(getApplicationContext(), "Order Details Not Found" ,Toast.LENGTH_LONG).show();
        }
    }

    public void orderPayment(View view) {
        Toast.makeText(getApplicationContext(), "Order Id:" + OrderId,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), OrderPayment.class);
        intent.putExtra("OrderId",  OrderId);
        startActivity(intent);
    }

    public void deleteOrder(View view) {
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
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
                        deleteOrderAPI();

//                        Log.d(TAG, "onClick: OK Called.");
//                        if(okMethod.equals("okMethod1")){
//                            okMethod1();
//                        }
//                        else if(okMethod.equals("okMethod2")){
//                            okMethod2();
//                        }
                    }
                });



        builderSingle.show();

    }
    public void deleteOrderAPI(){
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        Call<ApiResponse<MySqlResult>> deleteOrderCall = apiInterface.deleteOrder(unpaidOrder.getId(),accessToken);
        deleteOrderCall.enqueue(new Callback<ApiResponse<MySqlResult>>() {
            @Override
            public void onResponse(Call<ApiResponse<MySqlResult>> call, Response<ApiResponse<MySqlResult>> response) {
                if(response.body() != null ? response.body().getStatus() : false){
                    Toast.makeText(getApplicationContext(), "Deleted Successfully! ",Toast.LENGTH_LONG).show();
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