package com.example.restulator.Models;

import java.util.ArrayList;
import java.util.List;

// This model is used to map the api responses
// to get the relevant data.
public class ApiResponse<T> {
    private Boolean status;
    private T[] data;
    private String message;


    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public T[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setData(T[] data) {
        this.data = data;
    }

}
