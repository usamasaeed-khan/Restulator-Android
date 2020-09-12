package com.example.restulator;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.Cook;
import com.example.restulator.Models.Customer;
import com.example.restulator.Models.Order;
import com.example.restulator.Models.Waiter;
import com.example.restulator.Models.WaiterData;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderActivity extends BaseActivity implements Validator.ValidationListener {

    Spinner waiterSpinner, cookSpinner, customerSpinner, orderStatusSpinner;
    RestulatorAPI apiInterface;
    WaiterData[] waiterData;
    Cook[] cookData;
    Customer[] customerData;
    List<WaiterData> waiterList = new ArrayList<>();
    List<Cook> cookList = new ArrayList<>();
    List<Customer> customerList = new ArrayList<>();

    @NotEmpty
    TextView order_time;

    @NotEmpty
    TextView complete_time;

    Button order_details;
    private Validator validator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        validator = new Validator(this);
        validator.setValidationListener(this);
        waiterSpinner = findViewById(R.id.waiterSpinner);
        cookSpinner = findViewById(R.id.cookSpinner);
        customerSpinner = findViewById(R.id.customerSpinner);
        order_time = findViewById(R.id.orderTime);
        complete_time = findViewById(R.id.completeTime);
        order_details = findViewById(R.id.addOrderDetails);
        orderStatusSpinner = findViewById(R.id.orderStatusSpinner);

        insertWaiterData();
        insertCookData();
        insertCustomerData();

        order_time.setInputType(InputType.TYPE_NULL);
        complete_time.setInputType(InputType.TYPE_NULL);

        order_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(order_time);
            }

            public void showDateTimeDialog(final TextView order_time) {
              final Calendar calendar = Calendar.getInstance();
              DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                  @Override
                  public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                      calendar.set(Calendar.YEAR, i);
                      calendar.set(Calendar.MONTH, i1);
                      calendar.set(Calendar.DAY_OF_MONTH, i2);

                      TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                          @Override
                          public void onTimeSet(TimePicker timePicker, int i, int i1) {
                              calendar.set(Calendar.HOUR_OF_DAY, i);
                              calendar.set(Calendar.MINUTE, i1);

                              SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

                              order_time.setText(simpleDateFormat.format(calendar.getTime()));
                          }
                      };

                      new TimePickerDialog(OrderActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
                  }
              };
              new DatePickerDialog(OrderActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        complete_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(complete_time);
            }

            public void showDateTimeDialog(final TextView complete_time) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        calendar.set(Calendar.YEAR, i);
                        calendar.set(Calendar.MONTH, i1);
                        calendar.set(Calendar.DAY_OF_MONTH, i2);

                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                calendar.set(Calendar.HOUR_OF_DAY, i);
                                calendar.set(Calendar.MINUTE, i1);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");

                                complete_time.setText(simpleDateFormat.format(calendar.getTime()));
                            }
                        };

                        new TimePickerDialog(OrderActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
                    }
                };

                new DatePickerDialog(OrderActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
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
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<WaiterData>> call, Throwable t) {

            }
        });
        waiterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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

    public void insertCustomerData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<Customer>> call = apiInterface.getCustomer(accessToken);

        call.enqueue(new Callback<ApiResponse<Customer>>() {
            @Override
            public void onResponse(Call<ApiResponse<Customer>> call, Response<ApiResponse<Customer>> response) {
                if(response.body() != null ? response.body().getStatus() : false) {
                    customerData = response.body().getData();

                    for(Customer customers: customerData){
                        int customerId = customers.getId();
                        String customerName = customers.getName();
                        Customer customer = new Customer(customerId, customerName);
                        customerList.add(customer);
                    }

                    ArrayAdapter<Customer> adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_spinner_item,customerList);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    customerSpinner.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Customer>> call, Throwable t) {
            }
        });
        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    @Override
    public void onValidationSucceeded() {
        String orderTime = order_time.getText().toString();
        String completeTime = complete_time.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
        try {
            Date d1 = sdf.parse(order_time.getText().toString());
            Date d2 = sdf.parse(complete_time.getText().toString());

            if(d1.before(d2)){

                WaiterData waiter = (WaiterData) waiterSpinner.getSelectedItem();
                int waiterId = waiter.getId();

                Cook cook = (Cook) cookSpinner.getSelectedItem();
                int cookId = cook.getId();

                Customer customer = (Customer) customerSpinner.getSelectedItem();
                int customerId = customer.getId();

                String orderStatus = orderStatusSpinner.getSelectedItem().toString();

                final int tableId = getIntent().getIntExtra("table_id",0);

                Intent intent = new Intent(OrderActivity.this, DishActivity.class);
                intent.putExtra("waiterId", waiterId);
                intent.putExtra("cookId", cookId);
                intent.putExtra("customerId", customerId);
                intent.putExtra("orderTime", orderTime);
                intent.putExtra("completeTime", completeTime);
                intent.putExtra("table_id", tableId);
                intent.putExtra("orderStatus", orderStatus);

                startActivity(intent);
            }
            else {
                Toast.makeText(this, "Order Complete Time should be Greater than Order Time", Toast.LENGTH_LONG).show();
                complete_time.setError("Complete Time should be Greater than Order Time");

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof TextView) {
                ((TextView) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }

    }
}

