package com.momo.app;
import java.time.LocalDate;

public class Bill {
    private int id;
    private String type;       // ELECTRIC, WATER, INTERNET 
    private int amount;        // amount of the bill
    private LocalDate dueDate; // due date of the bill
    private String state;      // NOT_PAID, PAID
    private String provider;   // EVN, SAVACO, VNPT, ...

    public Bill(int id, String type, int amount, LocalDate dueDate, String provider) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.provider = provider;
        this.state = "NOT_PAID";
    }

    // Getters
    public int getId() { 
        return id; 
    }

    public String getType() { 
        return type; 
    }

    public int getAmount() {
         return amount; 
    }

    public LocalDate getDueDate() { 
        return dueDate; 
    }

    public String getState() { 
        return state; 
    }
    
    public String getProvider() { 
        return provider; 
    }

    // Mark bill as paid
    public void markPaid() {
        this.state = "PAID";
    }

    @Override
    public String toString() {
        return id + ". " + type + " " + amount + " " + dueDate + " " + state + " " + provider;
    }
}