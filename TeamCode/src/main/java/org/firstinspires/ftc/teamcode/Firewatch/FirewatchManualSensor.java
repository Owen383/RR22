package org.firstinspires.ftc.teamcode.Firewatch;

import java.util.function.DoubleSupplier;

public class FirewatchManualSensor {

    private final DoubleSupplier sensorDataSupplier;
    private final String ID;

    public FirewatchManualSensor(DoubleSupplier sensorDataSupplier, String ID) {
        this.sensorDataSupplier = sensorDataSupplier;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public Double getData(){
        return sensorDataSupplier.getAsDouble();
    }
}
