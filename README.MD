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
