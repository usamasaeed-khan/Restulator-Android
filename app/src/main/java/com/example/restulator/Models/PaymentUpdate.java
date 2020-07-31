package com.example.restulator.Models;

public class PaymentUpdate {
    Float payment;

    public PaymentUpdate(float payment) {
        this.payment = payment;
    }

    public Float getPayment() {
        return payment;
    }

    public void setPayment(float payment) {
        this.payment = payment;
    }
}
