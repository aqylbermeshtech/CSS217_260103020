**Student ID:** 260103020  
**K-Value (Calibration):** 0  

##  Architectural Reflection
The `ModernHub` is pretty strict - it only accepts a list of `SmartDevice` objects. When I tried passing the `LegacyBulb` and `LegacyThermostat` straight into the hub, the Java compiler threw a type mismatch error because those older classes don't implement the required interface. 

To fix this without actually touching the original vendor code (which was against the lab rules), I used the Object Adapter pattern. I created new adapter classes that implement `SmartDevice` and wrapped the legacy devices inside them. This acted like a bridge, seamlessly translating the old hardware methods into the format the modern hub expects.

## Adapter Implementations & Math
*   **BulbAdapter:** This class wraps the `LegacyBulb`. To calculate the power percentage, I used the required formula: `floor((rawBrightness * 100) / 255) + 0` (since my ID ends in 0, the K-value is 0). I also added checks to make sure it returns exactly 0 when the raw brightness is 0, and capped the maximum output at 100% so the hub doesn't receive invalid data.
*   **ThermostatAdapter:** This one wraps the `LegacyThermostat`. Since the old thermostat uses string states, I mapped them to numbers for the hub: "IDLE" becomes 0%, "LOW" is 33%, "MEDIUM" is 66%, and "MAX" is 100%. I also made the `turnon()` method idempotent, meaning it only sets the dial to "LOW" if the device is currently idle. If it's already running, it just leaves it alone.

I added some safety checks to both adapters so the main system won't crash if the hardware bugs out.
*   **Broken Filament:** If the bulb's filament snaps, it might still have a brightness value stuck in its memory. I set up the adapter to always check if the bulb actually has power first. If it doesn't, it safely overrides the memory and reports 0% power and an "off" state.
*   **Corrupted Thermostat Dial:** The legacy thermostat dial can sometimes return weird, corrupted strings (like "STUCK") or even just return null. Instead of letting the app crash with a `NullPointerException`, my adapter catches these weird states and safely returns a -1 error code for power and a false active state.
