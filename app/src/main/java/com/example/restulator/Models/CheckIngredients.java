package com.example.restulator.Models;

import androidx.annotation.NonNull;

public class CheckIngredients {
    private int quantity;

    public CheckIngredients(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

}
