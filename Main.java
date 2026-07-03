package com.aquasmart;


public class Main {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       KIS AQUASMART SMART METER DEMO             ");
        System.out.println("==================================================");

        // 1. Create a SmartMeter object
        // Meter ID: KIS-SMART-702, Opening Credit: UGX 1,000 (enough for 20 litres)
        System.out.println("\n--- Step 1: Initializing Smart Meter ---");
        SmartMeter meter = new SmartMeter("KIS-SMART-702", 1000.0);
        
        // 2. Print initial state
        printMeterState(meter);

        // 3. Record several water consumption events
        System.out.println("\n--- Step 2: Recording Normal Water Consumption ---");
        
        // Consumption 1: 5 Litres (Cost: 5 * 50 = UGX 250)
        simulateConsumption(meter, 5.0);
        
        // Consumption 2: 10 Litres (Cost: 10 * 50 = UGX 500)
        simulateConsumption(meter, 10.0);

        // 4. Demonstrate valve automatically closing when credit is exhausted
        System.out.println("\n--- Step 3: Demonstrating Credit Exhaustion & Valve Closure ---");
        
        // Attempt Consumption 3: 10 Litres (Cost: 10 * 50 = UGX 500)
        // Current balance is UGX 250. This should fail because credit is insufficient.
        // It will close the valve and reset the balance to 0.
        simulateConsumption(meter, 10.0);
        
        // Check state after exhaustion
        printMeterState(meter);

        // Attempt Consumption 4: Try to consume 2 litres while the valve is closed
        // This should fail immediately because the valve is already closed.
        System.out.println("\n--- Step 4: Attempting Consumption with Closed Valve ---");
        simulateConsumption(meter, 2.0);

        // 5. Load a new token
        System.out.println("\n--- Step 5: Loading a Fresh Token ---");
        double loadedAmount = 1500.0; // UGX 1,500
        System.out.println("Loading token of UGX " + loadedAmount + " using mobile money...");
        double newBalance = meter.loadToken(loadedAmount);
        System.out.println("Token loaded successfully! New Balance: UGX " + newBalance);

        // 6. Demonstrate that the valve has reopened and record new consumption
        System.out.println("\n--- Step 6: Verifying Valve Reopened & Dispensing Water ---");
        printMeterState(meter); // Valve should be open now

        // Consumption 5: 20 Litres (Cost: 20 * 50 = UGX 1000)
        simulateConsumption(meter, 20.0);

        // 7. Print the final meter state
        System.out.println("\n--- Step 7: Final Meter State ---");
        printMeterState(meter);
        
        System.out.println("==================================================");
        System.out.println("           DEMONSTRATION COMPLETED                ");
        System.out.println("==================================================");
    }

    /**
     * Helper method to simulate consumption and print readable output.
     * 
     * @param meter  The smart meter instance.
     * @param litres The number of litres to consume.
     */
    private static void simulateConsumption(SmartMeter meter, double litres) {
        System.out.printf("Attempting to consume: %.2f Litres (Cost: UGX %.2f)%n", litres, (litres * 50.0));
        boolean success = meter.recordConsumption(litres);
        if (success) {
            System.out.printf(">> SUCCESS: Dispensed %.2f Litres. Remaining Balance: UGX %.2f%n", litres, meter.getCreditBalance());
        } else {
            System.out.println(">> FAILED: Water could not be dispensed.");
        }
        System.out.println("--------------------------------------------------");
    }

    /**
     * Helper method to print the complete current state of the meter.
     * 
     * @param meter The smart meter instance.
     */
    private static void printMeterState(SmartMeter meter) {
        System.out.println("[METER STATE STATUS REPORT]");
        System.out.println("  * Meter ID       : " + meter.getMeterId());
        System.out.println("  * Credit Balance : UGX " + meter.getCreditBalance());
        System.out.println("  * Valve Status   : " + (meter.isValveOpen() ? "OPEN (Flowing)" : "CLOSED (Blocked)"));
        System.out.println("--------------------------------------------------");
    }
}
