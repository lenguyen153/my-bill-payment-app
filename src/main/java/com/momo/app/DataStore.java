package com.momo.app;

import java.util.*;
import java.time.LocalDate;
import java.util.stream.Collectors;

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

    // Pay MULTIPLE bills at once
    public void payBills(List<Integer> billIds) {
        if (billIds.isEmpty()) {
            System.out.println("Usage: PAY <billId1> <billId2> ...");
            return;
        }
        
        System.out.println("Your available balance: " + balance);

        List<Bill> billsToPay = new ArrayList<>();
        long totalAmount = 0;
        boolean hasErrors = false;

        // Validate all bills and calculate total amount
        for (int billId : billIds.stream().distinct().collect(Collectors.toList())) { // Use distinct IDs
            Bill bill = findBill(billId);
            if (bill == null) {
                System.out.println("Error: Bill with id " + billId + " not found.");
                hasErrors = true;
            } else if ("PAID".equals(bill.getState())) {
                System.out.println("Error: Bill with id " + billId + " is already paid.");
                hasErrors = true;
            } else {
                billsToPay.add(bill);
                totalAmount += bill.getAmount();
            }
        }

        if (hasErrors) {
            System.out.println("Transaction failed due to one or more errors. No bills were paid.");
            return;
        }

        //Check if balance is sufficient
        if (balance < totalAmount) {
            System.out.println("Sorry! Not enough funds to proceed with payment.");
            System.out.println("Total amount needed: " + totalAmount);
            return;
        }

        //Execute payment for all valid bills
        for (Bill bill : billsToPay) {
            balance -= bill.getAmount();
            bill.markPaid();
            Payment payment = new Payment(
                payments.size() + 1,
                bill.getAmount(),
                LocalDate.now(), // Use current date for payment
                "PROCESSED",
                bill.getId()
            );
            payments.add(payment);
        }

        System.out.println("Payment has been completed for " + billsToPay.size() + " bill(s).");
        System.out.println("Your current balance: " + balance);
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

    // List all payments
    public void listUnpaidBills() {
        System.out.println("Bill No. Type Amount Due Date State PROVIDER");
        for (Bill bill : bills) {
            if (bill.getState().equals("NOT_PAID")) {
                System.out.println(bill);
            }
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

