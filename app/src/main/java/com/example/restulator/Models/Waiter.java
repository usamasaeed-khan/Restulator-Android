package com.example.restulator.Models;

public class Waiter {
    private int id;
    private String email;
    private String role;


    public Waiter(int id,String email, String role) {
        this.email = email;
        this.role = role;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public int getId() {
        return id;
    }
}
