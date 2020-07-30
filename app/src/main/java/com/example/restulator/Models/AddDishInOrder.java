package com.example.restulator.Models;

public class AddDishInOrder {
    private int order_id;
    private int dish_quantity;
    private int dish_id;
    private float total_amount;


    public AddDishInOrder(int order_id, int dish_quantity, int dish_id, float total_amount) {
        this.order_id = order_id;
        this.dish_quantity = dish_quantity;
        this.dish_id = dish_id;
        this.total_amount = total_amount;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getDish_quantity() {
        return dish_quantity;
    }

    public int getDish_id() {
        return dish_id;
    }

    public float getTotal_amount() {
        return total_amount;
    }
}
