package com.example.restulator.Models;

public class DishType {
    private int type_id;
    private String type;

    public DishType(String type, int type_id ) {
        this.type = type;
        this.type_id = type_id;
    }

    @Override
    public String toString() {
        return type;
    }

    public int getType_id() {
        return type_id;
    }

    public String getType() {
        return type;
    }

}
