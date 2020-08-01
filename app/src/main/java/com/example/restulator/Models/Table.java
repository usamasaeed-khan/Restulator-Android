package com.example.restulator.Models;

import androidx.annotation.NonNull;

// Model to get information of all tables.
public class Table {

    public Table(int id, int capacity, String status) {
        this.id = id;
        this.capacity = capacity;
        this.status = status;
    }

    @Override
    public String toString() {
        return ""+id;
    }

    public Table(int id) {
        this.id = id;
    }

    public enum TableStatus{
        ACTIVE,
        IN_ACTIVE
    }
    private int id;
    private int capacity;
    private String status;

    public String getStatus() {
        return status;
    }

    public int getId(){
        return id;
    }

    public int getCapacity(){
        return capacity;
    }

    // To caste string to enum for better readability and less hard coding.
    public TableStatus getStatusEnum() {
        return TableStatus.valueOf(status.replaceAll("-", "_").toUpperCase());
    }

}
