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
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.OrderOnTable;
import com.example.restulator.Models.PaymentOrder;
import com.example.restulator.Models.PaymentUpdate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ActiveOrder extends AppCompatActivity {


    int table_id;
    RestulatorAPI apiInterface;
    TextView OrderIdData,CustomerNameData,CookNameData,OrderDateData,CompleteTimeData,OrderStatusData,BillData;
    Button paymentButton;
    OrderOnTable order;
    Date endTime,nowTime;
    String completeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_order);
        OrderIdData = findViewById(R.id.OrderId);
        CustomerNameData = findViewById(R.id.CustomerName);
        CookNameData = findViewById(R.id.CookName);
        OrderDateData = findViewById(R.id.OrderDate);
        CompleteTimeData = findViewById(R.id.CompleteTime);
        OrderStatusData = findViewById(R.id.OrderStatus);
        BillData = findViewById(R.id.Bill);
        paymentButton = findViewById(R.id.paymentButton);


        Intent intentFromTables = getIntent();
        table_id = intentFromTables.getExtras().getInt("table_id");

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<OrderOnTable>> call = apiInterface.getOrder(table_id,accessToken);
        call.enqueue(new Callback<ApiResponse<OrderOnTable>>() {
            @Override
            public void onResponse(Call<ApiResponse<OrderOnTable>> call, Response<ApiResponse<OrderOnTable>> response) {
                if(response.body() != null ? response.body().getStatus() : false){
                    order = response.body().getData()[0];
                    OrderIdData.setText(String.valueOf(order.getId()));
                    CookNameData.setText(order.getCook_name());
                    CustomerNameData.setText(order.getCustomer_name());
                    BillData.setText(String.valueOf(order.getBill()));
                    OrderStatusData.setText(order.getOrder_status());
                    OrderDateData.setText(formatDateTime(order.getOrder_time()));

                    if(order.getPayment_status().equals("non_paid")){
                        paymentButton.setVisibility(View.VISIBLE);
                    }
                    SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
                    try {
                        endTime=format1.parse(formatDateTime(order.getComplete_time()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String currentTimeString = format1.format(Calendar.getInstance().getTime());
                    try {
                        nowTime= format1.parse(currentTimeString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if(nowTime.before(endTime)){
                        String remainingTime = DateUtils.formatElapsedTime ((endTime.getTime() - nowTime.getTime())/1000);
                        Toast.makeText(getApplicationContext(), "Remaining Time : " + remainingTime,Toast.LENGTH_LONG).show();
                        CompleteTimeData.setText(remainingTime);
                    }
                    else{
                        CompleteTimeData.setText("Complete Time Have Passed");

                    }






                }
            }

            @Override
            public void onFailure(Call<ApiResponse<OrderOnTable>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Found",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderPayment.class);
                intent.putExtra("OrderId",  order.getId());
                startActivity(intent);

            }
        });
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

    public Date parseDateTime(Date obj){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Date outputstr = null;
        String input;
        try {

             input = outputFormat.format(obj);
            outputstr  = inputFormat.parse(input);

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Cannot format Order or Complete time" ,Toast.LENGTH_LONG).show();
        }
        return outputstr;

    }
}