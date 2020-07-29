package com.example.restulator.Models;

import androidx.annotation.NonNull;

public class Dish {
    private int id;
    private String name;
    private String description;
    private float price;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    @Override
    public String toString() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public Dish(int id, String name, String description, float price) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.price = price;
    }

}
