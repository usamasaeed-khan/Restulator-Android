package com.example.restulator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.User;
import com.example.restulator.Models.Waiter;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener{
    RestulatorAPI apiInterface;
    Waiter obj;
    private Validator validator;
    @NotEmpty
    EditText emailEditText;
    @NotEmpty
    EditText pwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        validator = new Validator(this);
        validator.setValidationListener(this);

        emailEditText = findViewById(R.id.loginEmail);
        pwdEditText = findViewById(R.id.loginPass);

        if(isLoggedIn()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    public void login(View view) {

        validator.validate();

//        if(!email.matches("") || !password.matches("")){
//        }
//        else{
//            Toast.makeText(getApplicationContext(), "Please Enter Email and Password!",Toast.LENGTH_LONG).show();
//        }

    }

    private Boolean isLoggedIn() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        int timeDiff = getLoginTimeDifference(pref);
        return getLoginToken(pref) != null && timeDiff == 0;

    }

    private String getLoginToken(SharedPreferences pref){
        return pref.getString("ACCESS_TOKEN", null);
    }


    private int getLoginTimeDifference(SharedPreferences pref){
        Date loginTime = null;
        long differenceInMs;

        try {
            String storedLoginTime = pref.getString("LOGIN_AT", null);
            if (storedLoginTime == null)
                return -1;
            loginTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US).parse(storedLoginTime);
            differenceInMs = Calendar.getInstance().getTime().getTime() - (loginTime != null ? loginTime.getTime() : 0);
        }
        catch (ParseException e){
            differenceInMs = 0;
        }

        // convert difference in hours.
        return (int) ((differenceInMs / (1000*60*60)) % 24);
    }

    @Override
    public void onValidationSucceeded() {
        String email = emailEditText.getText().toString();
        String password = pwdEditText.getText().toString();
        User userObj = new User(email,password);

        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);

        // Initialize call to the api method getTables()
        Call<ApiResponse<Waiter>> call = apiInterface.waiterLogin(userObj);
        call.enqueue(new Callback<ApiResponse<Waiter>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Waiter>> call, @NonNull Response<ApiResponse<Waiter>> response) {

                // Checking api response status.
                if(response.body() != null ? response.body().getStatus() : false){
                    obj = response.body().getData()[0];

                    if(obj.getRole().equals("waiter") || obj.getRole().equals("Waiter") ){
                        Toast.makeText(getApplicationContext(), "Login Successful "  ,Toast.LENGTH_LONG).show();



                        String pattern = "MM/dd/yyyy HH:mm:ss";

                        // Create an instance of SimpleDateFormat used for formatting
                        // the string representation of date according to the chosen pattern
                        DateFormat df = new SimpleDateFormat(pattern);

                        // Get the today date using Calendar object.
                        Date today = Calendar.getInstance().getTime();
                        // Using DateFormat format method we can create a string
                        // representation of a date with the defined format.
                        String todayAsString = df.format(today);


                        //Toast.makeText(getApplicationContext(), "Token Rec: "+obj.getToken(), Toast.LENGTH_LONG).show();

                        // Initializing Shared Preferences obj.

                        // 1st argument is the file name and 2nd arg is the access mode, 0 is for private mode to be
                        // accessed only by the application.
                        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("ACCESS_TOKEN", obj.getToken());
                        editor.putString("LOGIN_AT", todayAsString);
                        editor.apply(); // commit and save changes


                        //Toast.makeText(getApplicationContext(), "Token Rec: "+obj.getToken(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                    else{

                        Toast.makeText(getApplicationContext(), "Login Unsuccessful! Please Enter Registered Waiter Email and Password!" ,Toast.LENGTH_LONG).show();
                    }



                }else{
                    Toast.makeText(getApplicationContext(), "Login Unsuccessful ",Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Waiter>> call,@NonNull Throwable t) {

//                    If incomplete, toast the error message.
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.INVISIBLE);
            }
        });

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