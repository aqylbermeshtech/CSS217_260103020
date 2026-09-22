package Lab04_Adapter_260103020;
public class ThermostatAdapter implements SmartDevice {
    private final LegacyThermostat thermostat;
    public ThermostatAdapter(LegacyThermostat thermostat) {
        if (thermostat == null) {
            throw new IllegalArgumentException("LegacyThermostat cannot be null");
        }
        this.thermostat = thermostat;
    }

    @Override
    public void turnon() {
        String state = thermostat.checkDial();
        if ("IDLE".equals(state)) {
            thermostat.rotateDial("LOW");
        }
    }

    @Override
    public void turnoff() {
        thermostat.rotateDial("IDLE");
    }

    @Override
    public boolean ison() {
        String state = thermostat.checkDial();
        if (state == null) {
            return false;
        }
        switch (state) {
            case "LOW":
            case "MEDIUM":
            case "MAX":
                return true;
            case "IDLE":
                return false;
            default:
                return false;
        }
    }
    @Override
    public int getPowerPercent() {
        String state = thermostat.checkDial();
        if (state == null) {
            return -1;
        }
        switch (state) {
            case "IDLE":
                return 0;
            case "LOW":
                return 33;
            case "MEDIUM":
                return 66;
            case "MAX":
                return 100;
            default:
                return -1;
        }
    }
}