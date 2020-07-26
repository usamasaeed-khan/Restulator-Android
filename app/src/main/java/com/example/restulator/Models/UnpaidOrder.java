package com.example.restulator.Models;

import java.io.Serializable;

public class UnpaidOrder implements Serializable {
    private int cook_id;
    private String cook_name;
    private int waiter_id;
    private String waiter_name;
    private String customer_name;
    private int id;
    private String order_status;
    private String order_time;
    private String complete_time;
    private int bill;

    public UnpaidOrder(int cook_id, String cook_name, int waiter_id, String waiter_name, String customer_name, int id, String order_status, String order_time, String complete_time, int bill) {
        this.cook_id = cook_id;
        this.cook_name = cook_name;
        this.waiter_id = waiter_id;
        this.waiter_name = waiter_name;
        this.customer_name = customer_name;
        this.id = id;
        this.order_status = order_status;
        this.order_time = order_time;
        this.complete_time = complete_time;
        this.bill = bill;
    }

    public int getCook_id() {
        return cook_id;
    }

    public void setCook_id(int cook_id) {
        this.cook_id = cook_id;
    }

    public String getCook_name() {
        return cook_name;
    }

    public void setCook_name(String cook_name) {
        this.cook_name = cook_name;
    }

    public int getWaiter_id() {
        return waiter_id;
    }

    public void setWaiter_id(int waiter_id) {
        this.waiter_id = waiter_id;
    }

    public String getWaiter_name() {
        return waiter_name;
    }

    public void setWaiter_name(String waiter_name) {
        this.waiter_name = waiter_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }
}
