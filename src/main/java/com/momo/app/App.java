package com.momo.app;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DataStore store = new DataStore(); // stays alive throughout the program

        System.out.println("Welcome to Bill Payment System!");
        System.out.println("Available commands:");
        System.out.println("CASHIN <amount>");
        System.out.println("LISTBILLS");
        System.out.println("PAY <billId>");
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
                    try {
                        int amount = Integer.parseInt(parts[1]);
                        store.cashIn(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount.");
                    }
                    break;

                case "LISTBILLS":
                    store.listBills();
                    break;

                case "PAY":
                    if (parts.length < 2) {
                        System.out.println("Usage: PAY <billId>");
                        break;
                    }
                    try {
                        int billId = Integer.parseInt(parts[1]);
                        store.payBill(billId);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid bill id.");
                    }
                    break;

                case "LISTPAYMENTS":
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