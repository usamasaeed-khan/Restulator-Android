package com.example.restulator.Models;

public class PossibleDishes {
    private double possible;
    private int required;
    private int quantity;

    public PossibleDishes(double possible, int required, int quantity) {
        this.possible = possible;
        this.required = required;
        this.quantity = quantity;
    }

    public double getPossible() {
        return possible;
    }

    public int getRequired() {
        return required;
    }

    public float getQuantity() {
        return quantity;
    }
}
