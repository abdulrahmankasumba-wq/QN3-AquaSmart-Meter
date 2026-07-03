package com.aquasmart;

/**
 * The SmartMeter class represents a prepaid smart water meter.
 * It manages water credit balances, valve status, and records consumption
 * to support the "AquaSmart" water-metering system for Kalangala Infrastructure Services Ltd (KIS).
 * 
 * This class is designed to demonstrate key Object-Oriented Programming (OOP) principles
 * such as encapsulation, data hiding, and proper state management.
 */
public class SmartMeter {

    // ==========================================
    // Fields (Attributes)
    // ==========================================
    
    /**
     * Unique identifier for the smart water meter.
     */
    private String meterId;

    /**
     * Current credit balance in Ugandan Shillings (UGX).
     */
    private double creditBalance;

    /**
     * Represents the state of the meter's water valve.
     * True means open (water can flow), False means closed (water flow is blocked).
     */
    private boolean valveOpen;

    // ==========================================
    // Constructor
    // ==========================================

    /**
     * Constructs a new SmartMeter with a specified ID and opening credit balance.
     * The meter valve is automatically opened initially to allow immediate water flow.
     *
     * @param meterId               The unique identifier for the meter.
     * @param openingCreditBalance  The initial amount of credit (in UGX) loaded onto the meter.
     */
    public SmartMeter(String meterId, double openingCreditBalance) {
        this.meterId = meterId;
        this.creditBalance = openingCreditBalance;
        this.valveOpen = true; // Automatically open the valve upon initialization
    }

    // ==========================================
    // Methods (Business Logic)
    // ==========================================

    /**
     * Loads credit (water token) onto the smart meter.
     * If the valve was previously closed due to insufficient credit, loading a fresh token
     * will automatically reopen the valve.
     *
     * @param amount The token amount in UGX to add to the meter.
     * @return The updated credit balance after loading.
     */
    public double loadToken(double amount) {
        // Add the loaded token amount to the existing credit balance
        this.creditBalance += amount;

        // If the valve was previously closed (due to running out of credit), reopen it
        if (!this.valveOpen) {
            this.valveOpen = true;
        }

        // Return the updated balance
        return this.creditBalance;
    }

    /**
     * Records a water consumption event in litres.
     * Each litre of water consumed costs a flat rate of UGX 50.
     * The method manages the credit balance and controls the valve status accordingly:
     * - Returns false immediately if the valve is closed.
     * - If credit is sufficient: deducts cost, shuts valve if balance becomes zero or below, and returns true.
     * - If credit is insufficient: keeps credit untouched, closes the valve, resets balance to 0, and returns false.
     *
     * @param litres The amount of water consumed in litres.
     * @return true if consumption was successful (water was dispensed); false otherwise.
     */
    public boolean recordConsumption(double litres) {
        // 1. Each litre of water costs UGX 50
        double cost = litres * 50.0;

        // 2. If the valve is closed before dispensing, immediately reject the consumption
        if (!this.valveOpen) {
            System.out.println("[Meter " + meterId + "] Consumption failed: Valve is CLOSED.");
            return false;
        }

        // 3. Check if there is sufficient credit to cover the total cost
        if (this.creditBalance >= cost) {
            // Deduct the cost from the credit balance
            this.creditBalance -= cost;

            // If the balance drops to zero or below after deduction
            if (this.creditBalance <= 0) {
                this.creditBalance = 0.0; // Ensure credit balance is exactly 0 (no negative values)
                this.valveOpen = false;   // Automatically close the valve to stop water flow
                System.out.println("[Meter " + meterId + "] Warning: Credit exhausted. Valve closed.");
            }
            return true;
        } else {
            // 4. Insufficient credit case:
            // Do not deduct any money, shut the valve immediately, reset balance to 0
            this.valveOpen = false;
            this.creditBalance = 0.0;
            System.out.println("[Meter " + meterId + "] Insufficient credit. Valve shut immediately. Balance reset to 0.");
            return false;
        }
    }

    // ==========================================
    // Getters (Accessors)
    // ==========================================

    /**
     * Gets the unique meter ID.
     *
     * @return The meter's ID.
     */
    public String getMeterId() {
        return this.meterId;
    }

    /**
     * Gets the current credit balance.
     *
     * @return The credit balance in UGX.
     */
    public double getCreditBalance() {
        return this.creditBalance;
    }

    /**
     * Checks if the valve is currently open.
     *
     * @return true if open, false if closed.
     */
    public boolean isValveOpen() {
        return this.valveOpen;
    }

    // ==========================================
    // Setters (Mutators) - Encapsulation Note
    // ==========================================
    
    // NOTE: Setter methods for creditBalance and valveOpen are purposely omitted.
    // In a well-encapsulated object, the internal state of these fields should only 
    // be modified through formal operations/methods (loadToken and recordConsumption)
    // to maintain business rules and prevent unauthorized modification of balance/valve states.
}
