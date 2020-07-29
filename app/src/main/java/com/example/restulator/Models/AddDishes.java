package com.example.restulator.Models;

public class AddDishes {
    private int dish_id;
    private int dish_quantity;

    public int getDish_id() {
        return dish_id;
    }

//    public AddDishes(int dish_id, int dish_quantity) {
//        this.dish_id = dish_id;
//        this.dish_quantity = dish_quantity;
//    }

    public void setDish_id(int dish_id) {
        this.dish_id = dish_id;
    }

    public void setDish_quantity(int dish_quantity) {
        this.dish_quantity = dish_quantity;
    }

    public int getDish_quantity() {
        return dish_quantity;
    }
}
