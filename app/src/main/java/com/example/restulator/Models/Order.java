package com.example.restulator.Models;

public class Order {

        private int id;
        private int customerId;
        private String orderTime;
        private String completeTime;
        private String orderStatus;
        private int tableNumber;
        private int cookId;
        private int waiterId;
        private int totalAmount;
        private double tax;
        private int bill;
        private String paymentStatus;
        private int payment;
        private double pointsPerOrder;
        private int pointsLimits;
        private double discountPercent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getCompleteTime() {
            return completeTime;
        }

        public void setCompleteTime(String completeTime) {
            this.completeTime = completeTime;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getTableNumber() {
            return tableNumber;
        }

        public void setTableNumber(int tableNumber) {
            this.tableNumber = tableNumber;
        }

        public int getCookId() {
            return cookId;
        }

        public void setCookId(int cookId) {
            this.cookId = cookId;
        }

        public int getWaiterId() {
            return waiterId;
        }

        public void setWaiterId(int waiterId) {
            this.waiterId = waiterId;
        }

        public int getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(int totalAmount) {
            this.totalAmount = totalAmount;
        }

        public double getTax() {
            return tax;
        }

        public void setTax(double tax) {
            this.tax = tax;
        }

        public int getBill() {
            return bill;
        }

        public void setBill(int bill) {
            this.bill = bill;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public int getPayment() {
            return payment;
        }

        public void setPayment(int payment) {
            this.payment = payment;
        }

        public double getPointsPerOrder() {
            return pointsPerOrder;
        }

        public void setPointsPerOrder(double pointsPerOrder) {
            this.pointsPerOrder = pointsPerOrder;
        }

        public int getPointsLimits() {
            return pointsLimits;
        }

        public void setPointsLimits(int pointsLimits) {
            this.pointsLimits = pointsLimits;
        }

        public double getDiscountPercent() {
            return discountPercent;
        }

        public void setDiscountPercent(double discountPercent) {
            this.discountPercent = discountPercent;
        }
}
