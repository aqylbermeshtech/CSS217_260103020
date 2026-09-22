public class BulbAdapter implements SmartDevice {
    private final LegacyBulb bulb;
    private final int K = 0; 

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
        if (!bulb.hasPower()) {
            return false;
        }
        return bulb.readBrightness() > 0;
    }

    @Override
    public int getPowerPercent() {
        if (!bulb.hasPower()) {
            return 0;
        }
        
        int rawBrightness = bulb.readBrightness();

        if (rawBrightness == 0) {
            return 0;
        }
        int rawPercent = (int) Math.floor((rawBrightness * 100.0) / 255.0);
        int calibratedPercent = rawPercent + K;

        if (calibratedPercent > 100) {
            return 100;
        }
        
        return calibratedPercent;
    }
}