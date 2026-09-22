package Lab04_Adapter_260103020;

public class BulbAdapter implements SmartDevice {
    private final LegacyBulb bulb;

    public BulbAdapter(LegacyBulb bulb) {
        if (bulb == null) {
            throw new IllegalArgumentException("LegacyBulb cannot be null");
        }
        this.bulb = bulb;
    }

    @Override
    public void turnon() {
        bulb.setBrightness(255);
    }

    @Override
    public void turnoff() {
        bulb.setBrightness(0);
    }

    @Override
    public boolean ison() {
        return bulb.hasPower();
    }

    @Override
    public int getPowerPercent() {
        if (!bulb.hasPower()) {
            return 0;
        }
        return (int) Math.round((bulb.readBrightness() / 255.0) * 100);
    }
}
