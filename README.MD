# Bill Payment Command-Line Application

A simple Java-based command-line application for managing and paying bills using an in-memory balance.

---

## Prerequisites

Before you begin, ensure you have the following installed on your system:
* **Java Development Kit (JDK)** - Version 11 or higher.

This project uses the Maven Wrapper, so you do **not** need to install Maven separately.

---

## 🚀 How to Run the Project

The included Maven Wrapper scripts (`mvnw` for Linux/macOS and `mvnw.cmd` for Windows) will handle downloading the correct Maven version and running the project.

1.  **Clone the Repository**
    ```sh
    git clone [https://github.com/lenguyen153/my-bill-payment-app.git](https://github.com/lenguyen153/my-bill-payment-app.git)
    cd my-bill-payment-app
    ```

2.  **Compile and Run the Application**
    Use the following Maven command to compile and run the project in your terminal.

    * On macOS or Linux:
        ```sh
        ./mvnw compile exec:java
        ```
    * On Windows:
        ```sh
        mvnw.cmd compile exec:java
        ```

    The application will start, and you will see the welcome message and available commands.

---

## ✅ How to Test the Project

To run the complete suite of unit tests, use the Maven Wrapper's `test` command.

1.  **Open a terminal** in the project's root directory.

2.  **Execute the test command**:

    * On macOS or Linux:
        ```sh
        ./mvnw test
        ```
    * On Windows:
        ```sh
        mvnw.cmd test
        ```
    Maven will compile the code and run all tests. You will see a `BUILD SUCCESS` message if all tests pass.

---

## 📝 Application Usage

Once the application is running, you can use the following commands:
```
Of course. Here is a complete README.md file for your project. This file explains what the project is, how to run it, and details all the available commands with an example usage flow.

You can save this content as a file named README.md in the root directory of your Java project.
Markdown

# Bill Payment System 🧾

A simple command-line application built in Java for managing and paying bills. This system allows users to manage a balance, view outstanding bills, schedule payments, and process payments directly from the console.

---

## ✨ Features

* **Manage Balance**: Add funds to your account with the `CASH_IN` command.
* **View Bills**: List all bills, see their status, or search for bills by a specific provider.
* **Pay Bills**: Pay one or more bills in a single transaction.
* **Schedule Payments**: Schedule a payment for a future date to avoid missing deadlines.
* **Payment History**: View a complete list of all `PROCESSED` and `PENDING` payments.

---

## 🚀 Getting Started

Follow these instructions to get the project compiled and running on your local machine.

### Prerequisites

* **Java Development Kit (JDK)** 11 or newer.
* **Apache Maven** to build the project.

### How to Run

1.  **Clone the repository or navigate to your project's root directory.**

2.  **Compile the project using Maven.** This will create a runnable `.jar` file in the `target/` directory.
    ```shell
    mvn clean install
    ```

3.  **Run the application from the command line.**
    ```shell
    java -jar target/app-1.0-SNAPSHOT.jar 
    ```
    *(Note: The name of the `.jar` file may vary based on your `pom.xml` configuration.)*

---

## ⌨️ Commands

The following commands are available once the application is running:
```
| Command                                  | Description                                                              | Example                          |
| :--------------------------------------- | :----------------------------------------------------------------------- | :------------------------------- |
| `CASH_IN <amount>`                       | Adds the specified amount to your available balance.                     | `CASH_IN 500000`                 |
| `LIST_BILL`                              | Displays all available bills and their current status.                   | `LIST_BILL`                      |
| `PAY <billId> <billId>...`               | Pays one or more bills by their IDs using your balance.                  | `PAY 1 3`                        |
| `SCHEDULE_BILL <billId> <dueDate>`       | Schedules a payment for an unpaid bill on a specific date (`dd/MM/yyyy`).  | `SCHEDULE_BILL 2 25/10/2020`     |
| `DUE_DATE`                               | Lists all bills that are currently unpaid.                               | `DUE_DATE`                       |
| `LIST_PAYMENTS`                          | Shows a history of all payments and their status (PROCESSED/PENDING).    | `LIST_PAYMENTS`                  |
| `SEARCH_BILL_BY_PROVIDER <provider>`     | Searches for and displays all bills from a specific provider.            | `SEARCH_BILL_BY_PROVIDER VNPT`   |
| `EXIT`                                   | Closes the application.                                                  | `EXIT`                           |
```
