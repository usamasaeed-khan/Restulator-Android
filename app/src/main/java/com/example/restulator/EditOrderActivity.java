package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Cook;
import com.example.restulator.Models.Customer;
import com.example.restulator.Models.EditOrder;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.PaymentOrder;
import com.example.restulator.Models.Table;
import com.example.restulator.Models.UnpaidOrder;
import com.example.restulator.Models.WaiterData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EditOrderActivity extends AppCompatActivity {

    Spinner waiterSpinner, cookSpinner, orderStatusSpinner, tableNumberSpinner;
    Button editOrder;
    RestulatorAPI apiInterface;
    WaiterData[] waiterData;
    Cook[] cookData;
    List<WaiterData> waiterList = new ArrayList<>();
    List<Cook> cookList = new ArrayList<>();
    List<Table> tableList = new ArrayList<>();
    Table[] tableData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        waiterSpinner = findViewById(R.id.waiterSpinner);
        cookSpinner = findViewById(R.id.cookSpinner);
        orderStatusSpinner = findViewById(R.id.orderStatusSpinner);
        tableNumberSpinner = findViewById(R.id.tableNumberSpinner);
        editOrder = findViewById(R.id.editOrder);

        insertWaiterData();
        insertCookData();
        insertTableData();
        insertOrderStatus();

        editOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
                String accessToken = pref.getString("ACCESS_TOKEN", null);

                Intent intent = getIntent();
                UnpaidOrder unpaidOrder = (UnpaidOrder) intent.getSerializableExtra("unPaidOrder");
                final int order_id = unpaidOrder.getId();
                
                WaiterData waiter = (WaiterData) waiterSpinner.getSelectedItem();
                int waiter_id = waiter.getId();
                Cook cook = (Cook) cookSpinner.getSelectedItem();
                int cook_id = cook.getId();
                Table table = (Table) tableNumberSpinner.getSelectedItem();
                int table_number = table.getId();
                String orderStatus = orderStatusSpinner.getSelectedItem().toString();
                EditOrder editOrder = new EditOrder(order_id,orderStatus,cook_id,waiter_id,table_number);
                apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
                Call<ApiResponse<MySqlResult>> call = apiInterface.updateOrder(editOrder,accessToken);
                call.enqueue(new Callback<ApiResponse<MySqlResult>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<MySqlResult>> call, Response<ApiResponse<MySqlResult>> response) {
                        if(response.body() != null ? response.body().getStatus() : false){
                            Toast.makeText(getApplicationContext(), "Order Updated Successful!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), UnpaidOrders.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Unsuccessful",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<MySqlResult>> call, Throwable t) {
                        Toast.makeText(EditOrderActivity.this, "No response!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void insertWaiterData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<WaiterData>> call = apiInterface.getWaiter(accessToken);
        call.enqueue(new Callback<ApiResponse<WaiterData>>() {
            @Override
            public void onResponse(Call<ApiResponse<WaiterData>> call, Response<ApiResponse<WaiterData>> response) {
                if (response.body() != null ? response.body().getStatus() : false) {
                    waiterData = response.body().getData();

                    for(WaiterData waiters: waiterData) {
                        String waitersName = waiters.getName();
                        int waitersId = waiters.getId();

                        WaiterData waiter = new WaiterData(waitersId, waitersName);
                        waiterList.add(waiter);
                    }
                    ArrayAdapter<WaiterData> adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_spinner_item,waiterList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    waiterSpinner.setAdapter(adapter);

                    Intent intent = getIntent();
                    UnpaidOrder unpaidOrder = (UnpaidOrder) intent.getSerializableExtra("unPaidOrder");
                    final String waiter_name = unpaidOrder.getWaiter_name();
                    final int waiter_id = unpaidOrder.getWaiter_id();

                    WaiterData waiterData1 = new WaiterData(waiter_id,waiter_name);
                        for (int i=0;i<waiterSpinner.getCount();i++){
                            if (waiterSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(String.valueOf(waiterData1))){
                               waiterSpinner.setSelection(i);
                            }
                        }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<WaiterData>> call, Throwable t) {
            }
        });
        waiterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                WaiterData waiter = waiterList.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void insertCookData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<Cook>> call = apiInterface.getCook(accessToken);

        call.enqueue(new Callback<ApiResponse<Cook>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cook>> call, Response<ApiResponse<Cook>> response) {
                if(response.body() != null ? response.body().getStatus(): false) {
                    cookData = response.body().getData();
                    for(Cook cooks: cookData){
                        int cookId = cooks.getId();
                        String cookName = cooks.getName();
                        Cook cook = new Cook(cookId, cookName);
                        cookList.add(cook);
                    }
                    ArrayAdapter<Cook> adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_spinner_item,cookList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    cookSpinner.setAdapter(adapter);

                    Intent intent = getIntent();
                    UnpaidOrder unpaidOrder = (UnpaidOrder) intent.getSerializableExtra("unPaidOrder");
                    final String cook_name = unpaidOrder.getCook_name();
                    final int cook_id = unpaidOrder.getCook_id();

                    Cook cook1 = new Cook(cook_id,cook_name);

                    for (int i=0;i<cookSpinner.getCount();i++){
                        if (cookSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(String.valueOf(cook1))){
                            cookSpinner.setSelection(i);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cook>> call, Throwable t) {

            }
        });

        cookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void insertTableData() {
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<Table>> call = apiInterface.getInactiveTables();

        call.enqueue(new Callback<ApiResponse<Table>>() {
            @Override
            public void onResponse(Call<ApiResponse<Table>> call, Response<ApiResponse<Table>> response) {
                if(response.body() != null ? response.body().getStatus(): false) {
                    tableData = response.body().getData();

                    for(Table tables: tableData){
                        int table_id = tables.getId();
                        Table table = new Table(table_id);
                        tableList.add(table);
                    }

                    ArrayAdapter<Table> adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_spinner_item,tableList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    tableNumberSpinner.setAdapter(adapter);

                    Intent intent = getIntent();
                    UnpaidOrder unpaidOrder = (UnpaidOrder) intent.getSerializableExtra("unPaidOrder");
                    final int table_number = unpaidOrder.getTable_number();

                    Table table1 = new Table(table_number);
                    if(tableList.contains(table1)) {
                        for (int i=0;i<tableNumberSpinner.getCount();i++){
                            if (tableNumberSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(String.valueOf(table1))){
                                tableNumberSpinner.setSelection(i);
                            }
                        }
                    }
                    else {
                        tableList.add(table1);
                        Table compareValue = table1;
                        if (compareValue != null) {
                            int spinnerPosition = adapter.getPosition(compareValue);
                            tableNumberSpinner.setSelection(spinnerPosition);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Table>> call, Throwable t) {
            }
        });

        tableNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    public void insertOrderStatus() {
        Intent intent = getIntent();
        UnpaidOrder unpaidOrder = (UnpaidOrder) intent.getSerializableExtra("unPaidOrder");
        final String order_status = unpaidOrder.getOrder_status();

        for (int i=0;i<orderStatusSpinner.getCount();i++){
            if (orderStatusSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(String.valueOf(order_status))){
                orderStatusSpinner.setSelection(i);
            }
        }
    }
}