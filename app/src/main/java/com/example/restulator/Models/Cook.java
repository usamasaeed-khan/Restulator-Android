package com.example.restulator.Models;

public class Cook {
    private int id;
    private String name;


    public Cook(int id,String name) {
        this.id = id;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

