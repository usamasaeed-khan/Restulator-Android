package com.example.restulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restulator.Models.AddDishInOrder;
import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.CheckIngredients;
import com.example.restulator.Models.Dish;
import com.example.restulator.Models.DishType;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.PossibleDishes;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDishToOrder extends AppCompatActivity implements Validator.ValidationListener {

    Spinner dishTypeSpinner, dishSpinner;
    TextView dishPrice, dishTotalPrice, possibleDishes;

    @NotEmpty
    @Min(1)
    EditText dishQuantity;

    Button addDish;
    RestulatorAPI apiInterface;
    DishType[] dishTypeData;
    Dish[] dishData;
    CheckIngredients[] checkIng;
    PossibleDishes check;
    List<DishType> dishTypeList = new ArrayList<>();
    List<Dish> dishList = new ArrayList<>();
    public static float totalPrice = 0;
    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish_to_order);

        dishTypeSpinner = findViewById(R.id.dishTypeSpinner);
        dishSpinner = findViewById(R.id.dishSpinner);
        dishQuantity = findViewById(R.id.enterQuantity);
        dishPrice = findViewById(R.id.originalPrice);
        dishTotalPrice = findViewById(R.id.calculatedPrice);
        possibleDishes = findViewById(R.id.approxDishes);
        addDish = findViewById(R.id.addDish);
        validator = new Validator(this);
        validator.setValidationListener(this);

        insertDishTypeData();
        calculateTotalPrice();

        addDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
    }


    public void insertDishTypeData() {
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<DishType>> call = apiInterface.getdishType();

        call.enqueue(new Callback<ApiResponse<DishType>>() {
            @Override
            public void onResponse(Call<ApiResponse<DishType>> call, Response<ApiResponse<DishType>> response) {
                if(response.body() != null ? response.body().getStatus() : false) {
                    dishTypeData = response.body().getData();

                    Log.i("Success dishType", new Gson().toJson(dishTypeData));

                    for(DishType dishType: dishTypeData) {
                        String type = dishType.getType();
                        int type_id = dishType.getType_id();

                        DishType dish = new DishType(type, type_id);
                        dishTypeList.add(dish);
                    }

                    Log.i("Success dishType list", dishTypeList.toString());

                    ArrayAdapter<DishType> adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_spinner_item, dishTypeList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    dishTypeSpinner.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<DishType>> call, Throwable t) {

            }
        });

        dishTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DishType dishType = dishTypeList.get(i);
                int type_id = dishType.getType_id();
                String type = dishType.getType();
//                Toast.makeText(getApplicationContext(), "Selected DishType "+ type +"\n" + "Id is " + type_id , Toast.LENGTH_LONG).show();
                dishList.clear();
                dishSpinner.setAdapter(null);
                insertDishData(type_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    public void insertDishData(int type_id) {
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<Dish>> call = apiInterface.getDish(type_id);

        call.enqueue(new Callback<ApiResponse<Dish>>() {
            @Override
            public void onResponse(Call<ApiResponse<Dish>> call, Response<ApiResponse<Dish>> response) {
                if(response.body() != null ? response.body().getStatus() : false) {
                    dishData = response.body().getData();

                    Log.i("Success dishType", new Gson().toJson(dishData));

                    for(Dish dishes: dishData) {
                        String dishName = dishes.getName();
                        int dishId = dishes.getId();
                        float dishPrice = dishes.getPrice();
                        String dishDescription = dishes.getDescription();

                        Dish dish = new Dish(dishId,dishName, dishDescription,dishPrice);
                        dishList.add(dish);
                    }

                    Log.i("Success dish list", dishList.toString());

                    ArrayAdapter<DishType> adapter = new ArrayAdapter(getApplicationContext(),
                            android.R.layout.simple_spinner_item, dishList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    dishSpinner.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Dish>> call, Throwable t) {

            }
        });

        dishSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Dish dish = dishList.get(i);
                String dish_id = dish.getName();
                float price = dish.getPrice();
                String strPrice = String.valueOf(price);

//                Toast.makeText(getApplicationContext(), "Selected DishId "+ dish_id +"\n" + "Price is " + price , Toast.LENGTH_LONG).show();

                dishPrice.setText(strPrice);
                dishQuantity.setText("");
                dishTotalPrice.setText("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void calculateTotalPrice() {
        dishQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                Toast.makeText(DishActivity.this, "You entered dish Qauntity", Toast.LENGTH_SHORT).show();
                CharSequence d = dishPrice.getText();

                float price= Float.valueOf(d.toString());

                if(dishQuantity.getText().length() != 0 ) {
                    String q = dishQuantity.getText().toString();

                    int quantity = Integer.parseInt(q);

                    totalPrice = price * quantity;

//                    Toast.makeText(DishActivity.this, "The total price is " + totalPrice, Toast.LENGTH_SHORT).show();

                    String total_price = String.valueOf(totalPrice);

                    dishTotalPrice.setText(total_price);

                    Dish dish = (Dish) dishSpinner.getSelectedItem();
                    int dishId = dish.getId();

                    checkIngredients(dishId, quantity);

                }
                else {
//                    Toast.makeText(DishActivity.this, "No input provided!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void checkIngredients(int dishId, int quantity) {
        final CheckIngredients checkIngredients = new CheckIngredients(quantity);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<PossibleDishes>> call = apiInterface.checkIngredients(dishId,checkIngredients);


        call.enqueue(new Callback<ApiResponse<PossibleDishes>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<PossibleDishes>> call, @NonNull Response<ApiResponse<PossibleDishes>> response) {

                // Checking api response status.
                if(response.body() != null ? response.body().getStatus() : false){
                    check = response.body().getData()[0];

                    Double possibeDish = check.getPossible();
                    Log.i("Success check is ", possibeDish.toString());

                    int floorPossible = (int) Math.floor(possibeDish);

                    possibleDishes.setText(String.valueOf(floorPossible));

//                    Toast.makeText(getApplicationContext(), "Possible "+ possibeDish,Toast.LENGTH_LONG).show();


                }else{

                    Toast.makeText(getApplicationContext(), "Possible are hello world",Toast.LENGTH_LONG).show();

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
                final int order_id = getIntent().getIntExtra("order_id",0);
                Toast.makeText(this, "Order_id is " + order_id, Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Total price " + totalPrice, Toast.LENGTH_LONG).show();


                Dish dish = (Dish) dishSpinner.getSelectedItem();
                int dish_id = dish.getId();
                
                final AddDishInOrder addDishInOrder = new AddDishInOrder(order_id,quantity,dish_id,totalPrice);
                apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
                Call<ApiResponse<MySqlResult>> call = apiInterface.addDishInOrder(addDishInOrder);
                
                call.enqueue(new Callback<ApiResponse<MySqlResult>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<MySqlResult>> call, Response<ApiResponse<MySqlResult>> response) {
                        if(response.body() != null ? response.body().getStatus() : false){
                            Toast.makeText(getApplicationContext(), "Order Placed Successful!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), UnpaidOrders.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Unsuccessful",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<MySqlResult>> call, Throwable t) {
                        Toast.makeText(AddDishToOrder.this, "Chal nikal!!", Toast.LENGTH_SHORT).show();

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