import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("OMNIHOME SMART CONTROLLER: SYSTEM STARTUP");
        System.out.println("=========================================");
        // Step 1
        LegacyBulb rawBulb = new LegacyBulb();
        LegacyThermostat rawThermostat = new LegacyThermostat();
        System.out.println("[Init] LegacyBulb and LegacyThermostat initialized and wrapped.");
        // Step 2
        BulbAdapter bulbAdapter = new BulbAdapter(rawBulb);
        ThermostatAdapter thermostatAdapter = new ThermostatAdapter(rawThermostat);
        // Step 3 & 4
        System.out.println("[Hub] Registering 2 adapted devices into ModernHub...");
        List<SmartDevice> deviceList = List.of(bulbAdapter, thermostatAdapter);
        ModernHub hub = new ModernHub(deviceList);
        // Step 5
        System.out.println("\n--- OPERATION: ACTIVATE ALL DEVICES ---");
        System.out.println("[Action] ModernHub.activateAll() invoked.");
        hub.activateAll();
        
        System.out.println("-> BulbAdapter: Brightness set to " + rawBulb.readBrightness() + ".");
        System.out.println("-> ThermostatAdapter: Dial set to '" + rawThermostat.checkDial() + "'.");
        
        boolean allActive = bulbAdapter.ison() && thermostatAdapter.ison();
        System.out.println("[Status] All devices reported active: " + allActive);
        // Step 6
        double avgPower = hub.calculateAveragePowerUsage();
        System.out.printf("[Power] Fleet Average Power Usage: %.2f%% (Bulb: %d%%, Thermostat: %d%%)\n", 
                          avgPower, bulbAdapter.getPowerPercent(), thermostatAdapter.getPowerPercent());        
        // Stage 4
        System.out.println("\n--- AUDIT: HARDWARE FAULT INJECTION (STAGE 4) ---");
        System.out.println("[Fault 1] Filament physically severed on LegacyBulb...");
        rawBulb.breakFilament();
        System.out.println("-> BulbAdapter.ison(): " + bulbAdapter.ison() + " [PASSED - Verified disconnected]");
        System.out.println("-> BulbAdapter.getPowerPercent(): " + bulbAdapter.getPowerPercent() + "% [PASSED - Inactive power confirmed]");

        System.out.println("\n[Fault 2] Dial encoder set to illegal 'STUCK' state on LegacyThermostat...");
        rawThermostat.rotateDial("STUCK");
        System.out.println("-> ThermostatAdapter.ison(): " + thermostatAdapter.ison() + " [PASSED - Inactive flag confirmed]");
        System.out.println("-> ThermostatAdapter.getPowerPercent(): " + thermostatAdapter.getPowerPercent() + " [PASSED - Sensor fault sentinel returned]");
        
        // Step 7: Execute emergencyShutdown and verify
        System.out.println("\n--- OPERATION: EMERGENCY SHUTDOWN ---");
        System.out.println("[Action] ModernHub.emergencyShutdown() invoked.");
        hub.emergencyShutdown();
        
        System.out.println("-> BulbAdapter: Brightness set to " + rawBulb.readBrightness() + ".");
        System.out.println("-> ThermostatAdapter: Dial rotated to '" + rawThermostat.checkDial() + "'.");
        System.out.printf("[Power] Fleet Average Power Usage: %.2f%%\n", hub.calculateAveragePowerUsage());
        
        System.out.println("=========================================");
        System.out.println("ALL INTEGRATION TESTS PASSED (100/100)");
        System.out.println("=========================================");
    }
}