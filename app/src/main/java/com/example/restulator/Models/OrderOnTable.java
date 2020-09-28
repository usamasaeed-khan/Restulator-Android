package com.example.restulator.Models;

public class OrderOnTable {
    private String cook_name;
    private String customer_name;
    private int id;
    private String order_status;
    private String order_time;
    private String complete_time;
    private String payment_status;
    private float bill;

    public String getCook_name() {
        return cook_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public int getId() {
        return id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public String getOrder_time() {
        return order_time;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public float getBill() {
        return bill;
    }

    public String getPayment_status(){
        return payment_status;
    }



    public OrderOnTable(String cook_name, String customer_name, int id, String order_status, String payment_status, String order_time, String complete_time, float bill) {
        this.cook_name = cook_name;
        this.customer_name = customer_name;
        this.id = id;
        this.payment_status = payment_status;
        this.order_status = order_status;
        this.order_time = order_time;
        this.complete_time = complete_time;
        this.bill = bill;
    }
}
