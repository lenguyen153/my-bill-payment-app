package com.momo.app;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Main test class containing nested test classes for Bill, Payment, and DataStore.
 */
@DisplayName("Bill Payment Application Tests")
class BillPaymentAppTest {

    //================================================================================
    // Tests for Bill.java
    //================================================================================
    @Nested
    @DisplayName("Bill Class Tests")
    class BillTest {
        private Bill bill;
        private final LocalDate dueDate = LocalDate.of(2025, 8, 28);

        @BeforeEach
        void setUp() {
            bill = new Bill(1, "ELECTRIC", 150000, dueDate, "EVN");
        }

        @Test
        @DisplayName("1. Constructor should initialize bill with correct values and NOT_PAID state")
        void testBillConstructor_ShouldInitializeCorrectly() {
            assertAll("Bill properties",
                () -> assertEquals(1, bill.getId()),
                () -> assertEquals("ELECTRIC", bill.getType()),
                () -> assertEquals(150000, bill.getAmount()),
                () -> assertEquals(dueDate, bill.getDueDate()),
                () -> assertEquals("EVN", bill.getProvider()),
                () -> assertEquals("NOT_PAID", bill.getState(), "Initial state should be NOT_PAID")
            );
        }

        @Test
        @DisplayName("2. markPaid should change the bill state to PAID")
        void testMarkPaid_ShouldChangeStateToPaid() {
            assertEquals("NOT_PAID", bill.getState()); // Verify initial state
            bill.markPaid();
            assertEquals("PAID", bill.getState(), "State should be updated to PAID");
        }

        @Test
        @DisplayName("3. Getters should return the correct values")
        void testGetters_ShouldReturnCorrectValues() {
            assertEquals(1, bill.getId());
            assertEquals("ELECTRIC", bill.getType());
            assertEquals(150000, bill.getAmount());
            assertEquals(dueDate, bill.getDueDate());
            assertEquals("EVN", bill.getProvider());
        }

        @Test
        @DisplayName("4. toString should return a correctly formatted string")
        void testToString_ShouldReturnFormattedString() {
            String expected = "1. ELECTRIC 150000 2025-08-28 NOT_PAID EVN";
            assertEquals(expected, bill.toString());
        }
    }

    //================================================================================
    // Tests for Payment.java
    //================================================================================
    @Nested
    @DisplayName("Payment Class Tests")
    class PaymentTest {
        private final LocalDate paymentDate = LocalDate.of(2025, 8, 28);

        @Test
        @DisplayName("5. Constructor should initialize payment with correct values")
        void testPaymentConstructor_ShouldInitializeCorrectly() {
            Payment payment = new Payment(1, 200000, paymentDate, "PROCESSED", 101);
            assertAll("Payment properties",
                () -> assertEquals(1, payment.getId()),
                () -> assertEquals(200000, payment.getAmount()),
                () -> assertEquals(paymentDate, payment.getPaymentDate()),
                () -> assertEquals("PROCESSED", payment.getState()),
                () -> assertEquals(101, payment.getBillId())
            );
        }
        
        @Test
        @DisplayName("6. toString should return a correctly formatted string")
        void testToString_ShouldReturnFormattedString() {
            Payment payment = new Payment(1, 200000, paymentDate, "PROCESSED", 101);
            String expected = "1. 200000 2025-08-28 PROCESSED 101";
            assertEquals(expected, payment.toString());
        }
    }

    //================================================================================
    // Tests for DataStore.java
    //================================================================================
    @Nested
    @DisplayName("DataStore Class Tests")
    class DataStoreTest {
        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private final PrintStream originalOut = System.out;
        private DataStore store;

        // Redirect System.out to capture console output before each test
        @BeforeEach
        void setUp() {
            store = new DataStore();
            System.setOut(new PrintStream(outContent));
        }

        // Restore System.out after each test
        @AfterEach
        void restoreStreams() {
            System.setOut(originalOut);
        }

        private String getOutput() {
            return outContent.toString().trim().replace("\r\n", "\n");
        }

        @Test
        @DisplayName("7. Constructor should initialize with a zero balance")
        void testDataStoreConstructor_ShouldInitializeBalanceToZero() {
            store.cashIn(0);
            assertTrue(getOutput().contains("Your available balance: 0"), "Initial balance should be 0");
        }

        @Test
        @DisplayName("8. Constructor should initialize with three default bills")
        void testDataStoreConstructor_ShouldInitializeWithDefaultBills() {
            store.listBills();
            String output = getOutput();
            assertAll("Default bills check",
                () -> assertTrue(output.contains("1. ELECTRIC")),
                () -> assertTrue(output.contains("2. WATER")),
                () -> assertTrue(output.contains("3. INTERNET"))
            );
        }

        @Test
        @DisplayName("9. cashIn should increase the balance correctly")
        void testCashIn_ShouldIncreaseBalance() {
            store.cashIn(500000);
            assertTrue(getOutput().contains("Your available balance: 500000"));
        }
        
        @Test
        @DisplayName("10. cashIn with a negative amount should decrease balance")
        void testCashIn_WithNegativeAmount_ShouldDecreaseBalance() {
            store.cashIn(-50000);
            assertTrue(getOutput().contains("Your available balance: -50000"));
        }

        @Test
        @DisplayName("11. listPayments should show 'No payments yet' message when empty")
        void testListPayments_WhenNoPayments_ShouldShowEmptyMessage() {
            store.listPayments();
            assertTrue(getOutput().contains("No payments yet."));
        }

        @Test
        @DisplayName("12. payBills should fail for a non-existent bill ID")
        void testpayBills_Failure_BillNotFound() {
            store.payBills(999);
            assertTrue(getOutput().contains("Sorry! Not found a bill with such id"));
        }

        @Test
        @DisplayName("13. payBills should fail due to insufficient funds")
        void testpayBills_Failure_InsufficientFunds() {
            store.cashIn(100000); // Bill 1 costs 200000
            store.payBills(1);
            assertTrue(getOutput().contains("Sorry! Not enough funds to proceed with payment."));
        }

        @Test
        @DisplayName("14. payBills should succeed with sufficient funds")
        void testpayBills_Success() {
            store.cashIn(1000000);
            store.payBills(1); // Pay bill 1 (amount 200000)
            String output = getOutput();
            
            assertAll("Successful payment verification",
                () -> assertTrue(output.contains("Payment has been completed for Bill with id 1")),
                () -> assertTrue(output.contains("Your current balance after payment: 800000"))
            );
        }

        @Test
        @DisplayName("15. payBills should fail if the bill is already paid")
        void testpayBills_Failure_BillAlreadyPaid() {
            store.cashIn(500000);
            store.payBills(1); // First payment, successful
            outContent.reset(); // Clear the output stream
            store.payBills(1); // Second attempt to pay
            
            assertTrue(getOutput().contains("Bill already paid."));
        }

        @Test
        @DisplayName("16. After payment, bill state should be PAID")
        void testBillState_AfterSuccessfulPayment() {
            store.cashIn(500000);
            store.payBills(2); // Pay bill 2
            outContent.reset();
            store.listBills();
            
            // Check that Bill 2 is now PAID
            assertTrue(getOutput().contains("2. WATER 175000 2020-10-30 PAID SAVACO HCMC"));
        }

        @Test
        @DisplayName("17. After payment, a new payment record should be created")
        void testListPayments_AfterSuccessfulPayment() {
            store.cashIn(1000000); // Enough money
            store.payBills(3); 
            outContent.reset();
            store.listPayments();
            String output = getOutput();
            
            assertAll("Payment record verification",
                () -> assertTrue(output.contains("1. 800000 2020-11-30 PROCESSED 3")),
                () -> assertFalse(output.contains("No payments yet."))
            );
        }

        @Test
        @DisplayName("18. payBills with exact funds should succeed and balance becomes zero")
        void testpayBills_WithExactFunds_ShouldSucceedAndBalanceBecomesZero() {
            store.cashIn(200000); // Exact amount for bill 1
            store.payBills(1);
            
            assertTrue(getOutput().contains("Your current balance after payment: 0"));
        }

        @Test
        @DisplayName("19. Paying one bill should not affect the state of other bills")
        void testpayBills_DoesNotAffectOtherBills() {
            store.cashIn(1000000);
            store.payBills(1); // Pay bill 1
            outContent.reset();
            store.listBills();
            String output = getOutput();

            assertAll("Verify state of other bills",
                () -> assertTrue(output.contains("1. ELECTRIC 200000 2020-10-25 PAID EVN HCMC")),
                () -> assertTrue(output.contains("2. WATER 175000 2020-10-30 NOT_PAID SAVACO HCMC")),
                () -> assertTrue(output.contains("3. INTERNET 800000 2020-11-30 NOT_PAID VNPT"))
            );
        }

        @Test
        @DisplayName("20. Multiple payments should create multiple records in listPayments")
        void testListPayments_AfterMultiplePayments() {
            store.cashIn(1000000);
            store.payBills(1);
            store.payBills(2);
            outContent.reset();
            store.listPayments();
            String output = getOutput();

            assertAll("Verify multiple payment records",
                () -> assertTrue(output.contains("1. 200000 2020-10-25 PROCESSED 1")),
                () -> assertTrue(output.contains("2. 175000 2020-10-30 PROCESSED 2"))
            );
        }
    }
}