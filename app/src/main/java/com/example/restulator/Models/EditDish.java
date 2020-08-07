package com.example.restulator.Models;

public class EditDish {
    private int order_id;
    private int dish_id;
    private int dish_quantity;
    private float total_amount;

    public EditDish(int order_id, int dish_id, int dish_quantity, float total_amount) {
        this.order_id = order_id;
        this.dish_id = dish_id;
        this.dish_quantity = dish_quantity;
        this.total_amount = total_amount;
    }

    public EditDish(int order_id, int dish_id, float total_amount) {
        this.order_id = order_id;
        this.dish_id = dish_id;
        this.total_amount = total_amount;
    }
}
