package com.example.restulator.Models;

public class PaymentUpdate {
    float payment;

    public PaymentUpdate(float payment) {
        this.payment = payment;
    }

    public float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }
}
