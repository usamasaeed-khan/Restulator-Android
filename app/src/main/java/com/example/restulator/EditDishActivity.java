package com.example.restulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.CheckIngredients;
import com.example.restulator.Models.Dish;
import com.example.restulator.Models.EditDish;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.PaymentOrder;
import com.example.restulator.Models.PossibleDishes;
import com.example.restulator.Models.UnpaidOrder;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDishActivity extends BaseActivity implements Validator.ValidationListener {
    TextView dishType, dish, dishPrice, dishTotalPrice,possibleDishes;

    @NotEmpty
    @Min(1)
    EditText dishQuantity;
    private Validator validator;
    Button editDish;
    RestulatorAPI apiInterface;
    PossibleDishes check;
    public static float totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dish);

        dishType = findViewById(R.id.dishTypeData);
        dish = findViewById(R.id.dishData);
        dishQuantity = findViewById(R.id.enterQuantity);
        dishPrice = findViewById(R.id.originalPrice);
        dishTotalPrice = findViewById(R.id.calculatedPrice);
        possibleDishes = findViewById(R.id.approxDishes);
        editDish = findViewById(R.id.EditDish);
        validator = new Validator(this);
        validator.setValidationListener(this);

        insertDishTypeData();
        insertDishData();
        insertDishPrice();
        insertDishQuantity();
        calculateTotalPrice();

        editDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
    }
    public void insertDishTypeData() {
        Intent intent = getIntent();
        PaymentOrder paymentOrder = (PaymentOrder) intent.getSerializableExtra("dishDetails");
        String dish_type = paymentOrder.getType();

        dishType.setText(dish_type);
    }
    public void insertDishData() {
        Intent intent = getIntent();
        PaymentOrder paymentOrder = (PaymentOrder) intent.getSerializableExtra("dishDetails");
        String dish_name = paymentOrder.getDish_name();

        dish.setText(dish_name);
    }
    public void insertDishPrice() {
        Intent intent = getIntent();
        PaymentOrder paymentOrder = (PaymentOrder) intent.getSerializableExtra("dishDetails");
        String dish_price = String.valueOf(paymentOrder.getPrice());

        dishPrice.setText(dish_price);
    }
    public void insertDishQuantity() {
        Intent intent = getIntent();
        PaymentOrder paymentOrder = (PaymentOrder) intent.getSerializableExtra("dishDetails");
        String dish_quantity = String.valueOf(paymentOrder.getDish_quantity());
        dishQuantity.setText(dish_quantity);
        CharSequence d = dishPrice.getText();
        float price= Float.valueOf(d.toString());

        if(dishQuantity.getText().length() != 0 ) {
            String q = dishQuantity.getText().toString();
            int quantity = Integer.parseInt(q);
            totalPrice = price * quantity;
            String total_price = String.valueOf(totalPrice);
            dishTotalPrice.setText(total_price);
            int dish_id = paymentOrder.getDish_id();

            checkIngredients(dish_id, quantity);
        }
    }
    public void calculateTotalPrice() {
        dishQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CharSequence d = dishPrice.getText();
                float price= Float.valueOf(d.toString());

                if(dishQuantity.getText().length() != 0 ) {
                    String q = dishQuantity.getText().toString();
                    int quantity = Integer.parseInt(q);
                    totalPrice = price * quantity;
                    String total_price = String.valueOf(totalPrice);
                    dishTotalPrice.setText(total_price);

                    Intent intent = getIntent();
                    PaymentOrder paymentOrder = (PaymentOrder) intent.getSerializableExtra("dishDetails");
                    int dish_id = paymentOrder.getDish_id();

                    checkIngredients(dish_id, quantity);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    public void checkIngredients(int dishId, int quantity) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        final CheckIngredients checkIngredients = new CheckIngredients(quantity);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<PossibleDishes>> call = apiInterface.checkIngredients(dishId,checkIngredients,accessToken);
        call.enqueue(new Callback<ApiResponse<PossibleDishes>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<PossibleDishes>> call, @NonNull Response<ApiResponse<PossibleDishes>> response) {
                // Checking api response status.
                if(response.body() != null ? response.body().getStatus() : false){
                    check = response.body().getData()[0];
                    Double possibeDish = check.getPossible();
                    int floorPossible = (int) Math.floor(possibeDish);

                    possibleDishes.setText(String.valueOf(floorPossible));
                }
            }
            @Override
            public void onFailure(@NonNull Call<ApiResponse<PossibleDishes>> call,@NonNull Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onValidationSucceeded() {
        int possible = Integer.parseInt((String) possibleDishes.getText());
        int quantity = Integer.parseInt(String.valueOf(dishQuantity.getText()));

        if(possible > 0) {
            if(quantity <= possible) {
                Intent intent = getIntent();
                PaymentOrder paymentOrder = (PaymentOrder) intent.getSerializableExtra("dishDetails");
                int dish_quantity = paymentOrder.getDish_quantity();
                float prev_price = paymentOrder.getPrice();
                float prev_total_price = dish_quantity * prev_price;
                float total_price = prev_total_price - totalPrice;
                int order_id = paymentOrder.getOrder_id();
                int dish_id = paymentOrder.getDish_id();

                SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
                String accessToken = pref.getString("ACCESS_TOKEN", null);
                EditDish editDish = new EditDish(order_id,dish_id,quantity,total_price);
                apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
                Call<ApiResponse<MySqlResult>> call = apiInterface.updateDish(editDish,accessToken);
                call.enqueue(new Callback<ApiResponse<MySqlResult>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<MySqlResult>> call, Response<ApiResponse<MySqlResult>> response) {
                        if(response.body() != null ? response.body().getStatus() : false){
                            Toast.makeText(getApplicationContext(), "Dish Updated Successful!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), UnpaidOrders.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Unsuccessful",Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<MySqlResult>> call, Throwable t) {
                    }
                });
            }
            else {
                dishQuantity.setError("Dishes Quantity should be less than or equal to "+ possible);
                Toast.makeText(this, "Dishes Quantity should be less than or equal to "+ possible, Toast.LENGTH_LONG).show();
            }
        }
        else {
            dishQuantity.setError("Dish is not available! Please select some other dish");
            Toast.makeText(this, "Dish is not available! Please select some other dish", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}