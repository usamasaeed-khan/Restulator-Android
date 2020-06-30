package com.example.restulator.Models;

import java.util.List;

// This model is used to map the api responses
// to get the relevant data.
public class ApiResponse {
    public Boolean status;
    public List<Table> data;
    public String message;


    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public List<Table> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(List<Table> data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
