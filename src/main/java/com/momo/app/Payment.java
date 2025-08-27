package com.momo.app;

import java.time.LocalDate;

public class Payment {
    private int id; // id of the payment
    private int amount; // amount of the payment
    private LocalDate paymentDate; // date the of the payment
    private String state;   // PROCESSED or PENDING
    private int billId;     // id of the bill

    public Payment(int id, int amount, LocalDate paymentDate, String state, int billId) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.state = state;
        this.billId = billId;
    }

    // Getters
    public int getId() {
        return id; 
    }

    public int getAmount() {
        return amount; 
    }

    public LocalDate getPaymentDate() {
        return paymentDate; 
    }

    public String getState() {
        return state; 
    }

    public int getBillId() {
        return billId; 
    }

    @Override
    public String toString() {
        return id + ". " + amount + " " + paymentDate + " " + state + " " + billId;
    }
}

