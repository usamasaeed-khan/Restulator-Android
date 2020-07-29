package com.example.restulator.Models;

public class Waiter {
    private int id;
    private String email;
    private String role;
    private String token;


    public Waiter(int id,String email, String role,String token) {
        this.email = email;
        this.role = role;
        this.id = id;
        this.token = token;
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
    public String getToken() {
        return token;
    }
}
