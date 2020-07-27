package com.example.restulator.Models;

public class PaymentOrder {
    int dish_id;
    String dish_name;
    float price;
    int type_id;
    String type;
    float dish_quantity;
    int order_id;
    String order_time;
    float bill;
    float total_amount;
    float tax;

    public PaymentOrder() {
    }

    public PaymentOrder(int dish_id, String dish_name, float price, int type_id, String type, float dish_quantity, int order_id, String order_time, float bill, float total_amount, float tax) {
        this.dish_id = dish_id;
        this.dish_name = dish_name;
        this.price = price;
        this.type_id = type_id;
        this.type = type;
        this.dish_quantity = dish_quantity;
        this.order_id = order_id;
        this.order_time = order_time;
        this.bill = bill;
        this.total_amount = total_amount;
        this.tax = tax;
    }

    public int getDish_id() {
        return dish_id;
    }

    public void setDish_id(int dish_id) {
        this.dish_id = dish_id;
    }

    public String getDish_name() {
        return dish_name;
    }

    public void setDish_name(String dish_name) {
        this.dish_name = dish_name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getDish_quantity() {
        return dish_quantity;
    }

    public void setDish_quantity(float dish_quantity) {
        this.dish_quantity = dish_quantity;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public float getBill() {
        return bill;
    }

    public void setBill(float bill) {
        this.bill = bill;
    }

    public float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }
}
