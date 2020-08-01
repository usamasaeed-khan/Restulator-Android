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

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.CheckIngredients;
import com.example.restulator.Models.Dish;
import com.example.restulator.Models.DishType;
import com.example.restulator.Models.Order;
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

public class DishActivity extends AppCompatActivity implements Validator.ValidationListener {

    Spinner dishTypeSpinner, dishSpinner;
    TextView dishPrice, dishTotalPrice, possibleDishes;

    @NotEmpty
    @Min(1)
    EditText dishQuantity;

    Button addOrder,addDishes;
    RestulatorAPI apiInterface;
    DishType[] dishTypeData;
    Dish[] dishData;
    CheckIngredients[] checkIng;
    PossibleDishes check;
    List<DishType> dishTypeList = new ArrayList<>();
    List<Dish> dishList = new ArrayList<>();
    public static int[][] dishes;
    public List<HashMap<Integer, Integer>> addDish = new ArrayList<HashMap<Integer, Integer>>();
    private Validator validator;
    public static float totalPrice = 0;
    public static float totalAmount;
    ArrayList<Float> priceList = new ArrayList<Float>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);
        validator = new Validator(this);
        validator.setValidationListener(this);

        final int waiterId = getIntent().getIntExtra("waiterId",0);
        final int cookId = getIntent().getIntExtra("cookId",0);
        final int customerId = getIntent().getIntExtra("customerId",0);
        final int tableId = getIntent().getIntExtra("table_id",0);
        final String orderTime = getIntent().getStringExtra("orderTime");
        final String completeTime = getIntent().getStringExtra("completeTime");
        final String orderStatus = getIntent().getStringExtra("orderStatus");

        dishTypeSpinner = findViewById(R.id.dishTypeSpinner);
        dishSpinner = findViewById(R.id.dishSpinner);
        dishQuantity = findViewById(R.id.enterQuantity);
        dishPrice = findViewById(R.id.originalPrice);
        dishTotalPrice = findViewById(R.id.calculatedPrice);
        addOrder = findViewById(R.id.addOrder);
        possibleDishes = findViewById(R.id.approxDishes);
        addDishes = findViewById(R.id.addDishes);


        insertDishTypeData();
        calculateTotalPrice();

        addDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Toast.makeText(DishActivity.this, "Total Amount is " + totalAmount, Toast.LENGTH_SHORT).show();

                Order order = new Order(customerId,orderTime,completeTime,orderStatus,tableId,cookId,waiterId,totalAmount,dishes);
                apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
                Call<ApiResponse<Order>> call = apiInterface.placeOrder(order);

                call.enqueue(new Callback<ApiResponse<Order>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Order>> call, Response<ApiResponse<Order>> response) {
                        Toast.makeText(DishActivity.this, "Your Order has been placed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DishActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<Order>> call, Throwable t) {
                    }
                });
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

                    for(Dish dishes: dishData) {
                        String dishName = dishes.getName();
                        int dishId = dishes.getId();
                        float dishPrice = dishes.getPrice();
                        String dishDescription = dishes.getDescription();

                        Dish dish = new Dish(dishId,dishName, dishDescription,dishPrice);
                        dishList.add(dish);
                    }
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
                CharSequence d = dishPrice.getText();

                float price= Float.valueOf(d.toString());

                if(dishQuantity.getText().length() != 0 ) {
                    String q = dishQuantity.getText().toString();

                    int quantity = Integer.parseInt(q);

                    totalPrice = price * quantity;

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
                    int floorPossible = (int) Math.floor(possibeDish);

                    possibleDishes.setText(String.valueOf(floorPossible));

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
                Toast.makeText(this, "We got it right!", Toast.LENGTH_LONG).show();

                Dish dish = (Dish) dishSpinner.getSelectedItem();
                int dish_id = dish.getId();
                HashMap<Integer, Integer> hmap = new HashMap<Integer, Integer>();

                hmap.put(dish_id,quantity);

                    addDish.add(hmap);

                    Object[] arr = hmap.entrySet().toArray();

                HashMap<Integer, Integer> finalHmap = new HashMap<Integer, Integer>();

                for(int i=0; i< addDish.size(); i++) {
                        Integer[] keys = new Integer[addDish.get(i).size()];
                        Integer[] values = new Integer[addDish.get(i).size()];
                        int index = 0;
                        for (Map.Entry<Integer, Integer> mapEntry : addDish.get(i).entrySet()) {
                            keys[index] = mapEntry.getKey();
                            values[index] = mapEntry.getValue();

                            if(finalHmap.containsKey(keys[index])) {
                                int prevValue = finalHmap.get(keys[index]);
                                int total_dishes = prevValue + values[index];
                                if(total_dishes <= possible) {
                                    finalHmap.put(keys[index],prevValue+ values[index]);
                                    Toast.makeText(this, "The total dishes are "+ total_dishes, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(this, "Remaining Dishes Are "+ (possible - total_dishes), Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(this, "The total dishes are "+ total_dishes, Toast.LENGTH_SHORT).show();
                                    Toast.makeText(this, "Number Of Dishes is Greater than possible!", Toast.LENGTH_SHORT).show();
                                    dishQuantity.setError("Number of Dishes exceeds the possible Dishes");
                                }
                            }
                            else {
                                Toast.makeText(this, "First Time!!", Toast.LENGTH_SHORT).show();
                                finalHmap.put(keys[index],values[index]);

                            }

                                index++;
                        }
//                    priceList.add(totalPrice);
//                    Log.i("Price list is ", priceList.toString());
//                    totalAmount = calculateTotalAmount(priceList);
//                    Log.i("totalAmount is ", String.valueOf(totalAmount));
//                    Log.i("First Time Final Hmap ", String.valueOf(finalHmap));
//

                }
                Object[] arr2 = finalHmap.entrySet().toArray();
                dishes = new int[finalHmap.size()][2];

                int index = 0;
                for (Map.Entry<Integer, Integer> mapEntry : finalHmap.entrySet()) {
                    dishes[index] = new int[] {mapEntry.getKey(), mapEntry.getValue()};
                    index++;
                }

                for (int i = 0; i < dishes.length; i++)
                    System.out.println("final Arr "+Arrays.toString(dishes[i]));
                
                Log.i("cheetos", addDish.toString());
//                System.out.println("cheetah"+ Arrays.toString(addDish.get(0)));
                System.out.println("Add Dish: "+ addDish);
                System.out.println("EntrySet: "+ Arrays.toString(arr));
                System.out.println("FinalHmap: "+ Arrays.toString(arr2));
                Log.i("totalAmount is ", String.valueOf(totalAmount));

                Toast.makeText(this, "addDIsh list is  " + addDish+ "\n" + "dishes are "+ dishes, Toast.LENGTH_LONG).show();

//                priceList.add(totalPrice);
//                Log.i("Price list is ", priceList.toString());
//                totalAmount = calculateTotalAmount(priceList);
//                Log.i("totalAmount is ", String.valueOf(totalAmount));

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

    private float calculateTotalAmount(ArrayList<Float> totalPrice) {
        float sum = 0;
        for (float i: totalPrice) {
            sum += i;
        }
        return sum;
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
