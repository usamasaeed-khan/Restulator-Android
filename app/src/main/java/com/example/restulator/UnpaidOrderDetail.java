package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
}