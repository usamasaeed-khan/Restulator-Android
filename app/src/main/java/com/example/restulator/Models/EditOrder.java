package com.example.restulator.Models;

public class EditOrder {
    private int order_id;
    private String order_status;
    private int cook_id;
    private int waiter_id;
    private int table_number;

    public EditOrder(int order_id, String order_status, int cook_id, int waiter_id, int table_number) {
        this.order_id = order_id;
        this.order_status = order_status;
        this.cook_id = cook_id;
        this.waiter_id = waiter_id;
        this.table_number = table_number;
    }
}
