package com.example.restulator.Models;

public class Review {
    int order_id;
    String review;
    int rating;

    public Review(int order_id, String review, int rating) {
        this.order_id = order_id;
        this.review = review;
        this.rating = rating;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
