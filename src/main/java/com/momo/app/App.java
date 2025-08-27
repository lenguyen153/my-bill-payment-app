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
        System.out.println("CASHIN <amount>");
        System.out.println("LIST_BILLS");
        System.out.println("PAY <billId> <billId> <billId> ...");
        System.out.println("LISTPAYMENTS");
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
                case "CASHIN":
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

                case "LIST_BILLS":
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

                case "LISTPAYMENTS":
                    store.listPayments();
                    break;
                
                case "SCHEDULE":
                    store.listPayments();
                    break;
                
                case "DUE_DATE":
                    store.listPayments();
                    break;

                case "SEARCH_BILL_BY_PRODVIDER":
                    store.listPayments();
                    break;

                default:
                    System.out.println("Unknown command.");
                    break;
            }
        }

        scanner.close();
    }
}