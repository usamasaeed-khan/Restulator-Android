package com.example.restulator.Models;

import androidx.annotation.NonNull;

public class WaiterData {
    private int id;
    private String name;

    public WaiterData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

}
