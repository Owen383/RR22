package org.firstinspires.ftc.teamcode.Firewatch;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensor;

public class FirewatchSensor {

    private final Sensor sensor;
    private final String ID;

    public FirewatchSensor(Sensor sensor, String ID) {
        this.sensor = sensor;
        this.ID = ID;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public boolean isConnected(){
        return sensor.isConnected();
    }

    public String getID() {
        return ID;
    }
}
