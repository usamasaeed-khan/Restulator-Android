package com.example.restulator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restulator.Models.ApiResponse;
import com.example.restulator.Models.MySqlResult;
import com.example.restulator.Models.PaymentOrder;
import com.example.restulator.Models.PaymentUpdate;
import com.example.restulator.Models.Review;
import com.example.restulator.Models.UnpaidOrder;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;


public class OrderPayment extends BaseActivity implements Validator.ValidationListener{

    class ReviewValidator implements Validator.ValidationListener{

        @NotEmpty
        private EditText review;
        private AlertDialog dialogue;

        private Validator validator;

        public ReviewValidator(EditText review,AlertDialog dialogue) {
            this.review = review;
            this.dialogue = dialogue;
            validator = new Validator(this);
            validator.setValidationListener(this);
        }

        public void validate() {
            validator.validate();
        }

        @Override
        public void onValidationSucceeded() {
            reviewObj = new Review(orderId,review.getText().toString(),(int) ratingBar.getRating());
            SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
            String accessToken = pref.getString("ACCESS_TOKEN", null);

            apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);


            Call<ApiResponse<MySqlResult>> postReviewCall = apiInterface.postReview(reviewObj,accessToken);
            postReviewCall.enqueue(new Callback<ApiResponse<MySqlResult>>() {
                @Override
                public void onResponse(Call<ApiResponse<MySqlResult>> call, Response<ApiResponse<MySqlResult>> response) {
                    if(response.body() != null ? response.body().getStatus() : false)
                        Toast.makeText(getApplicationContext(), "Review Added Successfully", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(), "Unable to add review", Toast.LENGTH_LONG).show();

                    dialogue.dismiss();

                }

                @Override
                public void onFailure(Call<ApiResponse<MySqlResult>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {
            Toast.makeText(getApplicationContext(), "Please enter review!", Toast.LENGTH_LONG).show();
            for (ValidationError error : errors) {
                View view = error.getView();
                String message = error.getCollatedErrorMessage(getApplicationContext());
                // Display error messages
                if (view instanceof EditText) {
                    ((EditText) view).setError(message);
                } else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }

        }
    }
    private ReviewValidator reviewValidator;
    PaymentOrder[] paymentOrders;
    RestulatorAPI apiInterface;
    Review reviewObj;
    TextView totalPriceTextview, taxTextview, billTextview, changeTextview;
    Button reviewButton;
    EditText reviewText;
    RatingBar ratingBar;
    private Validator validator_payment;
    @NotEmpty
    EditText paymentEdittext;

    PaymentUpdate paymentUpdate;

    Integer orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_payment);

        validator_payment = new Validator(this);
        validator_payment.setValidationListener(this);



        Intent intentFromUnpaidOrderDetail = getIntent();
        orderId = intentFromUnpaidOrderDetail.getExtras().getInt("OrderId");

        totalPriceTextview = findViewById(R.id.TotalPriceData);
        taxTextview = findViewById(R.id.TaxData);
        billTextview = findViewById(R.id.BillData);
        paymentEdittext = findViewById(R.id.PaymentBox);
        changeTextview = findViewById(R.id.ChangeData);
        reviewButton = findViewById(R.id.ReviewAddButton);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);
        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);
        Call<ApiResponse<PaymentOrder>> call = apiInterface.getOrderPayments(orderId,accessToken);
        call.enqueue(new Callback<ApiResponse<PaymentOrder>>() {
            @Override
            public void onResponse(Call<ApiResponse<PaymentOrder>> call, Response<ApiResponse<PaymentOrder>> response) {
                if(response.body() != null ? response.body().getStatus() : false){
                    paymentOrders = response.body().getData();
                    totalPriceTextview.setText(String.valueOf(paymentOrders[0].getTotal_amount()));
                    taxTextview.setText(String.valueOf(paymentOrders[0].getTax()));
                    billTextview.setText(String.valueOf(paymentOrders[0].getBill()));
                    

                    paymentEdittext.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if(!String.valueOf(s).matches("")){
                                float payment = Float.parseFloat(String.valueOf(s));
                                paymentUpdate = new PaymentUpdate(payment);
                                if(payment - paymentOrders[0].getBill() > 0){
                                    changeTextview.setText(String.valueOf(payment - paymentOrders[0].getBill()));
                                }
//                                else{
//                                    paymentEdittext.setError("Enter greater amount");
//                                }
                            }
                            else{
                                changeTextview.setText(String.valueOf(0));
                            }

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PaymentOrder>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }

    public void makePayment(View view) {
        validator_payment.validate();


    }

    @Override
    public void onValidationSucceeded() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SharedData", 0);
        String accessToken = pref.getString("ACCESS_TOKEN", null);

        apiInterface = RetrofitInstance.getRetrofitInstance().create(RestulatorAPI.class);

        if(paymentUpdate.getPayment() >= paymentOrders[0].getBill()){
            Call<ApiResponse<MySqlResult>> paymentCall = apiInterface.updatePayment(orderId,paymentUpdate,accessToken);
            paymentCall.enqueue(new Callback<ApiResponse<MySqlResult>>() {
                @Override
                public void onResponse(Call<ApiResponse<MySqlResult>> call, Response<ApiResponse<MySqlResult>> response) {
                    if(response.body() != null ? response.body().getStatus() : false){
                        Toast.makeText(getApplicationContext(), "Payment Successful!",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), UnpaidOrders.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Payment Unsuccessful",Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<MySqlResult>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            paymentEdittext.setError("Insufficient payment! ");
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

    public void addReview(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.review_add_layout, null);

        ratingBar = dialogLayout.findViewById(R.id.ratingBar);
        reviewText = dialogLayout.findViewById(R.id.reviewText);
        final AlertDialog builderSingle = new AlertDialog.Builder(this)
                .setView(dialogLayout)
                .setPositiveButton("Submit",null)
                .setNegativeButton("Cancel",null)
                .create();
        reviewValidator = new ReviewValidator( reviewText,builderSingle);

        builderSingle.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) { reviewValidator.validate();    }
                });

            }
        });



        builderSingle.show();

    }
}