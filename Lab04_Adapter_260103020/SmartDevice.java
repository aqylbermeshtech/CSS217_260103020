public interface SmartDevice {
    void turnon();
    void turnoff();
    boolean ison();
    int getPowerPercent(); 
}