package com.momo.app;
import java.time.LocalDate;

public class Bill {
    private int id;
    private String type;       // ELECTRIC, WATER, INTERNET 
    private int amount;        // amount of the bill
    private LocalDate dueDate; // due date of the bill
    private String state;      // NOT_PAID, PAID
    private String provider;   // EVN, SAVACO, VNPT, ...
    private LocalDate scheduledDate; // Can be null

    public Bill(int id, String type, int amount, LocalDate dueDate, String provider) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dueDate = dueDate;
        this.provider = provider;
        this.state = "NOT_PAID";
        this.scheduledDate = null; // Initially null
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

    public LocalDate getScheduledDate() { 
        return scheduledDate; 
    }

    public boolean setScheduledDate(LocalDate newScheduledDate) {
        if (newScheduledDate.isAfter(this.dueDate)) {
            System.out.println("Error: Scheduled date (" + newScheduledDate + ") cannot be after the due date (" + this.dueDate + ").");
            return false;
        }
        this.scheduledDate = newScheduledDate;
        return true;
    }

    // Mark bill as PAID
    public void markPaid() {
        this.state = "PAID";
    }
    

    @Override
    public String toString() {
        return id + ". " + type + " " + amount + " " + dueDate + " " + state + " " + provider;
    }
}