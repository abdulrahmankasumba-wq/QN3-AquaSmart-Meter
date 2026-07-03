package com.aquasmart;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SmartMeter class using JUnit 5 (Jupiter).
 * This class validates the core business rules and state transitions of the prepaid smart meter.
 */
public class SmartMeterTest {

    /**
     * Test Case 1: Token Loading Reopens Valve.
     * Verifies that loading a token on a meter with a closed valve (due to credit exhaustion)
     * correctly updates the balance and opens the valve.
     */
    @Test
    public void shouldReopenValveWhenTokenIsLoaded() {
        // 1. Arrange: Create a meter and exhaust the credit to close the valve
        // Start with UGX 100 (enough for 2 litres at UGX 50/litre)
        SmartMeter meter = new SmartMeter("KIS-TEST-101", 100.0);
        
        // Consume 3 litres (costs UGX 150), which exceeds available UGX 100.
        // This will fail, reset balance to 0, and automatically close the valve.
        boolean consumptionResult = meter.recordConsumption(3.0);
        
        // Assert preconditions: make sure the valve is closed and balance is 0
        assertFalse(consumptionResult, "Precondition: Consumption should have failed due to insufficient credit.");
        assertFalse(meter.isValveOpen(), "Precondition: Valve should be closed after credit exhaustion.");
        assertEquals(0.0, meter.getCreditBalance(), "Precondition: Credit balance should be reset to 0.");

        // 2. Act: Load a new token of UGX 1000
        double updatedBalance = meter.loadToken(1000.0);

        // 3. Assert: Verify the balance increased and the valve reopened
        assertEquals(1000.0, updatedBalance, "The returned balance should be exactly the loaded amount.");
        assertEquals(1000.0, meter.getCreditBalance(), "The stored credit balance should be exactly the loaded amount.");
        assertTrue(meter.isValveOpen(), "The valve should reopen automatically after loading a fresh token.");
    }

    /**
     * Test Case 2: Insufficient Credit Closes Valve.
     * Verifies that attempting consumption that exceeds the current credit balance
     * does not deduct any credit, returns false, sets credit to 0, and closes the valve.
     */
    @Test
    public void shouldCloseValveAndResetBalanceWhenCreditIsInsufficient() {
        // 1. Arrange: Create a meter with limited credit
        // UGX 100 balance can purchase exactly 2 litres (UGX 50 * 2 = UGX 100)
        SmartMeter meter = new SmartMeter("KIS-TEST-102", 100.0);

        // Verify initial preconditions: valve is open, balance is UGX 100
        assertTrue(meter.isValveOpen(), "Precondition: Valve should start as open.");
        assertEquals(100.0, meter.getCreditBalance(), "Precondition: Credit balance should start at UGX 100.");

        // 2. Act: Attempt to consume 2.5 litres (Costs: 2.5 * 50 = UGX 125, which exceeds UGX 100 credit)
        boolean consumptionResult = meter.recordConsumption(2.5);

        // 3. Assert: Verify method returns false, credit balance becomes 0, and valve closes
        assertFalse(consumptionResult, "The consumption should fail because UGX 125 cost exceeds UGX 100 credit.");
        assertEquals(0.0, meter.getCreditBalance(), "No money should be partially deducted; credit balance should reset to 0.");
        assertFalse(meter.isValveOpen(), "The valve should be automatically closed to stop further water dispensing.");
    }
}
