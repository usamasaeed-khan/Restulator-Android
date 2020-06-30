package com.example.restulator.Models;

// Model to get information of all tables.
public class Table {

    public enum TableStatus{
        ACTIVE,
        IN_ACTIVE
    }
    private int id;
    private int capacity;
    private String status;
    private TableStatus statusEnum;

    public void setStatus(String status) {
        this.status = status;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    // To caste string to enum for better readability and less hard coding.
    public TableStatus getStatusEnum() {
        return TableStatus.valueOf(status.replaceAll("-", "_").toUpperCase());
    }

    public int getId(){
        return id;
    }

    public int getCapacity(){
        return capacity;
    }

}
