package com.example.restulator.Models;

public class Dish {

    private int orderId;
    private int dishId;
    private int dishQuantity;
    private String dishCreatedAt;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getDishQuantity() {
        return dishQuantity;
    }

    public void setDishQuantity(int dishQuantity) {
        this.dishQuantity = dishQuantity;
    }

    public String getDishCreatedAt() {
        return dishCreatedAt;
    }

    public void setDishCreatedAt(String dishCreatedAt) {
        this.dishCreatedAt = dishCreatedAt;
    }

}
