package com.example.restulator.Models;

import java.util.ArrayList;
import java.util.List;

// This model is used to map the api responses
// to get the relevant data.
public class ApiResponse<T> {
    private Boolean status;
    private ArrayList<T> data;
    private String message;


    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
