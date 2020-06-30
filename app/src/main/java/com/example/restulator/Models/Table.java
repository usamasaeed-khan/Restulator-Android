package com.example.restulator.Models;

// Model to get information of all tables.
public class Table {
    public enum TableStatus{
        ACTIVE,
        IN_ACTIVE
    }
    private int id;
    private int capacity;
    private TableStatus status;

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public TableStatus getStatus() {
        return status;
    }

    public int getId(){
        return id;
    }

    public int getCapacity(){
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setId(int id) {
        this.id = id;
    }
}
