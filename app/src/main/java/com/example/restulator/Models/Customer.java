package com.example.restulator.Models;

import androidx.annotation.NonNull;

public class Customer {
    private int id;
    private String name;
    private String email;
    private float points;

    public Customer(int id,String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public float getPoints() {
        return points;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
