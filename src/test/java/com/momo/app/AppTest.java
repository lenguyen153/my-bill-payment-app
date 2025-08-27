package com.momo.app;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BillPaymentSystemTest {

    private DataStore store;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        store = new DataStore();
        System.setOut(new PrintStream(outContent)); // Capture console output
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut); // Restore console output
    }

    // --- CASH_IN Tests (3 Tests) ---
    @Test
    @DisplayName("1. CASH_IN with a positive amount should increase balance")
    void cashIn_withPositiveAmount_shouldIncreaseBalance() {
        store.cashIn(500000);
        assertTrue(outContent.toString().contains("Your available balance: 500000"));
    }

    @Test
    @DisplayName("2. CASH_IN with zero amount should not change balance")
    void cashIn_withZeroAmount_shouldNotChangeBalance() {
        store.cashIn(0);
        assertTrue(outContent.toString().contains("Your available balance: 0"));
    }

    @Test
    @DisplayName("3. CASH_IN multiple times should accumulate balance")
    void cashIn_multipleTimes_shouldAccumulateBalance() {
        store.cashIn(100000);
        store.cashIn(200000);
        assertTrue(outContent.toString().endsWith("Your available balance: 300000\n"));
    }

    // --- PAY Tests (11 Tests) ---
    @Test
    @DisplayName("4. PAY for a single unscheduled bill with sufficient funds should succeed")
    void payBills_singleValidBillSufficientFunds_shouldSucceed() {
        store.cashIn(300000);
        store.payBills(List.of(1)); // Bill 1 costs 200000
        String output = outContent.toString();
        assertTrue(output.contains("Payment has been completed for 1 bill(s)."));
        assertTrue(output.contains("Your current balance: 100000"));
    }

    @Test
    @DisplayName("5. PAY for multiple bills with sufficient funds should succeed")
    void payBills_multipleValidBillsSufficientFunds_shouldSucceed() {
        store.cashIn(500000);
        store.payBills(List.of(1, 2)); // Total cost 375000
        String output = outContent.toString();
        assertTrue(output.contains("Payment has been completed for 2 bill(s)."));
        assertTrue(output.contains("Your current balance: 125000"));
    }

    @Test
    @DisplayName("6. PAY when balance is insufficient should fail")
    void payBills_insufficientFunds_shouldFail() {
        store.cashIn(100000);
        store.payBills(List.of(1)); // Bill 1 costs 200000
        String output = outContent.toString();
        assertTrue(output.contains("Sorry! Not enough funds to proceed with payment."));
    }

    @Test
    @DisplayName("7. PAY with a non-existent bill ID should fail atomically")
    void payBills_withNonExistentBillId_shouldFailAtomically() {
        store.cashIn(500000);
        store.payBills(List.of(1, 99)); // Bill 99 doesn't exist
        String output = outContent.toString();
        assertTrue(output.contains("Error: Bill with id 99 not found."));
        assertTrue(output.contains("Transaction failed due to one or more errors. No bills were paid."));
    }

    @Test
    @DisplayName("8. PAY with an already paid bill ID should fail atomically")
    void payBills_withAlreadyPaidBillId_shouldFailAtomically() {
        store.cashIn(500000);
        store.payBills(List.of(1)); // Pay bill 1 successfully
        outContent.reset();
        store.payBills(List.of(1, 2)); // Attempt to pay bill 1 again
        String output = outContent.toString();
        assertTrue(output.contains("Error: Bill with id 1 is already paid."));
        assertTrue(output.contains("Transaction failed due to one or more errors. No bills were paid."));
    }
    
    @Test
    @DisplayName("9. PAY with duplicate bill IDs should only process payment once")
    void payBills_withDuplicateBillIds_shouldProcessOnce() {
        store.cashIn(300000);
        store.payBills(List.of(1, 1, 1));
        String output = outContent.toString();
        assertTrue(output.contains("Payment has been completed for 1 bill(s)."));
        assertTrue(output.contains("Your current balance: 100000"));
    }

    @Test
    @DisplayName("10. PAY with an empty list of IDs should show usage message")
    void payBills_withEmptyIdList_shouldShowUsage() {
        store.payBills(Collections.emptyList());
        assertTrue(outContent.toString().contains("Usage: PAY <billId1> <billId2> ..."));
    }

    @Test
    @DisplayName("11. PAY for an unscheduled bill should create a new PROCESSED payment")
    void payBills_forUnscheduledBill_shouldCreateNewProcessedPayment() {
        store.cashIn(300000);
        store.payBills(List.of(1));
        outContent.reset();
        store.listPayments();
        String output = outContent.toString();
        assertTrue(output.contains("1. 200000 " + LocalDate.now() + " PROCESSED 1"));
    }

    @Test
    @DisplayName("12. PAY for a scheduled bill should update PENDING payment to PROCESSED")
    void payBills_forScheduledBill_shouldUpdatePendingToProcessed() {
        // 1. Schedule the bill first
        store.scheduleBill(2, "20/10/2020");
        outContent.reset(); // Clear output from scheduling

        // 2. Cash in and pay the bill
        store.cashIn(200000);
        store.payBills(List.of(2));
        outContent.reset();

        // 3. Verify the payment list
        store.listPayments();
        String output = outContent.toString();
        
        // Check that the original payment (ID 1) is now PROCESSED
        assertTrue(output.contains("1. 175000 " + LocalDate.now() + " PROCESSED 2"));
        // Check that a duplicate payment was NOT created
        assertFalse(output.contains("2. 175000"));
    }

    @Test
    @DisplayName("13. PAY should correctly change the bill's state to PAID")
    void payBills_shouldChangeBillStateToPaid() {
        store.cashIn(300000);
        store.payBills(List.of(1));
        outContent.reset();
        store.listBills();
        assertTrue(outContent.toString().contains("1. ELECTRIC 200000 2020-10-25 PAID EVN HCMC"));
    }

    // --- LIST & SEARCH Tests (7 Tests) ---
    @Test
    @DisplayName("14. LIST_BILL should display all initial bills")
    void listBills_shouldDisplayAllBills() {
        store.listBills();
        String output = outContent.toString();
        assertTrue(output.contains("1. ELECTRIC"));
        assertTrue(output.contains("2. WATER"));
        assertTrue(output.contains("3. INTERNET"));
    }

    @Test
    @DisplayName("15. LIST_PAYMENTS when no payments exist should show empty message")
    void listPayments_whenEmpty_shouldShowEmptyMessage() {
        store.listPayments();
        assertTrue(outContent.toString().contains("No payments yet."));
    }

    @Test
    @DisplayName("16. DUE_DATE should not list paid bills")
    void listUnpaidBills_shouldNotListPaidBills() {
        store.cashIn(300000);
        store.payBills(List.of(1));
        outContent.reset();
        store.listUnpaidBills();
        String output = outContent.toString();
        assertFalse(output.contains("1. ELECTRIC"));
        assertTrue(output.contains("2. WATER"));
    }

    @Test
    @DisplayName("17. DUE_DATE initially should list all bills")
    void listUnpaidBills_initially_shouldListAllBills() {
        store.listUnpaidBills();
        String output = outContent.toString();
        assertTrue(output.contains("1. ELECTRIC"));
        assertTrue(output.contains("2. WATER"));
        assertTrue(output.contains("3. INTERNET"));
    }

    @Test
    @DisplayName("18. SEARCH_BILL_BY_PROVIDER with existing provider should return bills")
    void listBillbyProvider_withExistingProvider_shouldReturnBills() {
        store.listBillbyProvider("VNPT");
        String output = outContent.toString();
        assertTrue(output.contains("3. INTERNET 800000 2020-11-30 NOT_PAID VNPT"));
        assertFalse(output.contains("ELECTRIC"));
    }

    @Test
    @DisplayName("19. SEARCH_BILL_BY_PROVIDER with non-existent provider should return nothing")
    void listBillbyProvider_withNonExistentProvider_shouldReturnNothing() {
        store.listBillbyProvider("NONEXISTENT");
        assertEquals("Bill No. Type Amount Due Date State PROVIDER\n", outContent.toString());
    }

    @Test
    @DisplayName("20. LIST_PAYMENTS should show all processed payments correctly")
    void listPayments_withMultiplePayments_shouldListAll() {
        store.cashIn(1000000);
        store.payBills(List.of(1, 2));
        outContent.reset();
        store.listPayments();
        String output = outContent.toString();
        assertTrue(output.contains("PROCESSED 1"));
        assertTrue(output.contains("PROCESSED 2"));
    }

    // --- SCHEDULE_BILL Tests (7 Tests) ---
    @Test
    @DisplayName("21. SCHEDULE_BILL with valid ID and date should succeed")
    void scheduleBill_withValidIdAndDate_shouldSucceed() {
        store.scheduleBill(1, "20/10/2020");
        assertTrue(outContent.toString().contains("Payment for bill 1 is scheduled on 20/10/2020"));
    }

    @Test
    @DisplayName("22. SCHEDULE_BILL should create a PENDING payment record")
    void scheduleBill_shouldCreatePendingPayment() {
        store.scheduleBill(1, "20/10/2020");
        outContent.reset();
        store.listPayments();
        String output = outContent.toString();
        assertTrue(output.contains(" PENDING 1"));
    }

    @Test
    @DisplayName("23. SCHEDULE_BILL with non-existent bill ID should fail")
    void scheduleBill_withNonExistentId_shouldFail() {
        store.scheduleBill(99, "20/10/2020");
        assertTrue(outContent.toString().contains("Error: Bill with id 99 not found."));
    }

    @Test
    @DisplayName("24. SCHEDULE_BILL for an already paid bill should fail")
    void scheduleBill_forPaidBill_shouldFail() {
        store.cashIn(300000);
        store.payBills(List.of(1));
        outContent.reset();
        store.scheduleBill(1, "20/10/2020");
        // NOTE: This test will fail until the bug in scheduleBill is fixed.
        // It should check for "PAID", not "PROCESSED".
        assertTrue(outContent.toString().contains("Error: Bill with id 1 is already paid."));
    }

    @Test
    @DisplayName("25. SCHEDULE_BILL for an already scheduled bill should fail")
    void scheduleBill_forAlreadyScheduledBill_shouldFail() {
        store.scheduleBill(1, "20/10/2020"); // First schedule
        outContent.reset();
        store.scheduleBill(1, "21/10/2020"); // Second attempt
        assertTrue(outContent.toString().contains("Error: A payment for bill id 1 is already scheduled."));
    }

    @Test
    @DisplayName("26. SCHEDULE_BILL with invalid date format should fail")
    void scheduleBill_withInvalidDateFormat_shouldFail() {
        store.scheduleBill(1, "20-10-2020");
        assertTrue(outContent.toString().contains("Error: Invalid date format. Please use DD/MM/YYYY."));
    }
    
    @Test
    @DisplayName("27. SCHEDULE_BILL with date after due date should fail")
    void scheduleBill_withDateAfterDueDate_shouldFail() {
        store.scheduleBill(1, "26/10/2020"); // Bill 1 is due 2020-10-25
        assertTrue(outContent.toString().contains("Error: Scheduled date (2020-10-26) cannot be after the due date (2020-10-25)."));
    }

    // --- Bill & Payment Class Logic Tests (3 Tests) ---
    @Test
    @DisplayName("28. Bill constructor should correctly initialize fields")
    void billConstructor_shouldInitializeFields() {
        LocalDate dueDate = LocalDate.of(2025, 9, 15);
        Bill bill = new Bill(10, "TEST", 5000, dueDate, "TEST_PROVIDER");
        assertEquals(10, bill.getId());
        assertEquals("NOT_PAID", bill.getState());
        assertNull(bill.getScheduledDate());
    }

    @Test
    @DisplayName("29. Payment setters should correctly update state and date")
    void paymentSetters_shouldUpdateStateAndDate() {
        Payment payment = new Payment(1, 1000, null, "PENDING", 1);
        LocalDate now = LocalDate.now();
        
        payment.setState("PROCESSED");
        payment.setPaymentDate(now);
        
        assertEquals("PROCESSED", payment.getState());
        assertEquals(now, payment.getPaymentDate());
    }

    @Test
    @DisplayName("30. Bill's setScheduledDate with a late date should fail")
    void setScheduledDate_whenDateIsAfterDueDate_shouldFail() {
        Bill bill = new Bill(1, "TEST", 1000, LocalDate.of(2025, 9, 15), "TEST_PROVIDER");
        boolean result = bill.setScheduledDate(LocalDate.of(2025, 9, 16));
        assertFalse(result);
        assertNull(bill.getScheduledDate());
    }
}