package com.example.restulator.Models;

public class Order {
        private int customer_id;
        private String order_time;
        private String complete_time;
        private String order_status;
        private int table_number;
        private int cook_id;
        private int waiter_id;
        private float total_amount;
        private int[][] dishes;

    public Order(int customer_id, String order_time, String complete_time, String order_status, int table_number, int cook_id, int waiter_id, float total_amount, int[][] dishes) {

        this.customer_id = customer_id;
        this.order_time = order_time;
        this.complete_time = complete_time;
        this.order_status = order_status;
        this.table_number = table_number;
        this.cook_id = cook_id;
        this.waiter_id = waiter_id;
        this.total_amount = total_amount;
        this.dishes = dishes;
    }



        public int getCustomerId() {
            return customer_id;
        }

        public void setCustomerId(int customerId) {
            this.customer_id = customer_id;
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

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }

        public int getTableNumber() {
            return table_number;
        }

        public void setTableNumber(int tableNumber) {
            this.table_number = table_number;
        }

        public int getCook_id() {
            return cook_id;
        }

        public void setCook_id(int cook_id) {
            this.cook_id = cook_id;
        }

        public int getWaiter_id() {
            return waiter_id;
        }

        public void setWaiter_id(int waiter_id) {
            this.waiter_id = waiter_id;
        }

        public float getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(int total_amount) {
            this.total_amount = total_amount;
        }
}
