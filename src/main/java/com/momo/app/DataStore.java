package com.momo.app;

import java.util.*;
import java.time.LocalDate;

public class DataStore {
    private int balance;
    private List<Bill> bills;
    private List<Payment> payments;

    public DataStore() {
        this.balance = 0;
        this.bills = new ArrayList<>();
        this.payments = new ArrayList<>();

        // Some sample dât
        bills.add(new Bill(1, "ELECTRIC", 200000, LocalDate.of(2020, 10, 25), "EVN HCMC"));
        bills.add(new Bill(2, "WATER", 175000, LocalDate.of(2020, 10, 30), "SAVACO HCMC"));
        bills.add(new Bill(3, "INTERNET", 800000, LocalDate.of(2020, 11, 30), "VNPT"));
    }

    // Cash in money
    public void cashIn(int amount) {
        balance += amount;
        System.out.println("Your available balance: " + balance);
    }

    // List all bills
    public void listBills() {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    // Pay a single bill
    public void payBill(int billId) {
        System.out.println("Current balance before payment: " + balance);
    
        Bill bill = findBill(billId);
        if (bill == null) {
            System.out.println("Sorry! Not found a bill with such id");
            return;
        }
    
        if (bill.getState().equals("PAID")) {
            System.out.println("Bill already paid.");
            return;
        }
    
        if (balance < bill.getAmount()) {
            System.out.println("Current balance: " + balance);
            System.out.println("Sorry! Not enough funds to proceed with payment.");
            return;
        }
    
        // Deduct balance and mark bill as paid
        balance -= bill.getAmount();
        bill.markPaid();
    
        // Create payment record
        Payment payment = new Payment(
            payments.size() + 1,
            bill.getAmount(),
            bill.getDueDate(),
            "PROCESSED",
            bill.getId()
        );
        payments.add(payment);
    
        System.out.println("Payment has been completed for Bill with id " + billId + ".");
        System.out.println("Your current balance after payment: " + balance);
    }

    // List all payments
    public void listPayments() {
        if (payments.isEmpty()) {
            System.out.println("No payments yet.");
            return;
        }
        System.out.println("No. Amount Payment Date State Bill Id");
        for (Payment p : payments) {
            System.out.println(p);
        }
    }

    // Helper to find bill
    private Bill findBill(int id) {
        for (Bill b : bills) {
            if (b.getId() == id) return b;
        }
        return null;
    }
}

