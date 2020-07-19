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

public class LoginActivity extends AppCompatActivity {
    RestulatorAPI apiInterface;
    Waiter obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(isLoggedIn()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    public void login(View view) {

        EditText emailEditText = findViewById(R.id.loginEmail);
        String email = emailEditText.getText().toString();

        EditText pwdEditText =findViewById(R.id.loginPass);
        String password =pwdEditText.getText().toString();

        if(!email.matches("") || !password.matches("")){
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


                             //Toast.makeText(getApplicationContext(), "Token Rec: "+obj.getToken(), Toast.LENGTH_LONG).show();

                             // Initializing Shared Preferences obj.

                             // 1st argument is the file name and 2nd arg is the access mode, 0 is for private mode to be
                             // accessed only by the application.
                             SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
                             SharedPreferences.Editor editor = pref.edit();
                             editor.putString("ACCESS_TOKEN", obj.getToken());
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

                    // If incomplete, toast the error message.
                    Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
//                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Please Enter Email and Password!",Toast.LENGTH_LONG).show();
        }
        // Creating retrofit instance to call the getTables() method.
    }

    private Boolean isLoggedIn(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        return pref.getString("ACCESS_TOKEN", null) != null;
    }
}