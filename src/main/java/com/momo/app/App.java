package com.momo.app;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataStore store = new DataStore(); // stays alive throughout the program

        System.out.println("Welcome to Bill Payment System!");
        System.out.println("Available commands:");
        System.out.println("CASH_IN <amount>");
        System.out.println("LIST_BILL");
        System.out.println("PAY <billId> <billId> <billId> ...");
        System.out.println("LIST_PAYMENTS");
        System.out.println("SEARCH_BILL_BY_PROVIDER <provider>");
        System.out.println("SCHEDULE_BILL <billId> <dueDate>");
        System.out.println("DUE_DATE");
        System.out.println("EXIT");

        while (true) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("EXIT")) {
                System.out.println("Goodbye!");
                break;
            }

            String[] parts = input.split(" ");
            String command = parts[0].toUpperCase();

            switch (command) {
                case "CASH_IN":
                    if (parts.length < 2) {
                        System.out.println("Usage: CASHIN <amount>");
                        break;
                    }
                    try { //FOOL PROOF EX: 99999999999999
                        int amount = Integer.parseInt(parts[1]);
                        store.cashIn(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount.");
                    }
                    break;

                case "LIST_BILL":
                    store.listBills();
                    break;

                case "PAY":
                    if (parts.length < 2) {
                        System.out.println("Usage: PAY <billId>");
                        break;
                    }
                    try {
                        List<Integer> billIds = new ArrayList<>();
                        for (int i = 1; i < parts.length; i++) {
                            billIds.add(Integer.parseInt(parts[i]));
                        }
                        store.payBills(billIds);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid bill ID. Please provide numbers only.");
                    }
                    break;

                case "LIST_PAYMENTS":
                    store.listPayments();
                    break;
                
                case "SCHEDULE_BILL":
                    if (parts.length < 3) {
                        System.out.println("Usage: SCHEDULE_BILL <billId> <dueDate>");
                        break;
                    }
                    store.scheduleBill(Integer.parseInt(parts[1]), parts[2]);
                    break;
                
                case "DUE_DATE":
                    store.listUnpaidBills();
                    break;

                case "SEARCH_BILL_BY_PROVIDER":
                if (parts.length < 2) {
                    System.out.println("Usage: SEARCH_BILL_BY_PROVIDER <provider>");
                    break;
                }
                    store.listBillbyProvider(parts[1]);
                    break;

                default:
                    System.out.println("Unknown command.");
                    break;
            }
        }

        scanner.close();
    }
}