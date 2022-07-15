package org.firstinspires.ftc.teamcode.BetterSensors.Sensors;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;

import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensor;

public class MotorEncoder extends Sensor <Double>{

    String deviceName;
    MotorEx encoder;

    public MotorEncoder(int pingFrequency, String hardwareID){
        super(pingFrequency, hardwareID);
    }

    @Override
    protected void sensorInit(String hardwareID) {
        deviceName = hardwareMap.get(DcMotor.class, hardwareID).getDeviceName() + "-encoder";
        encoder = new MotorEx(hardwareMap, hardwareID);
        encoder.resetEncoder();
    }

    @Override
    protected Double pingSensor() {
        return encoder.getDistance();
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public String getDeviceName() {
        return deviceName;
    }


    protected void setDistancePerPulse(double distancePerPulse){
        encoder.setDistancePerPulse(distancePerPulse);
    }

    public double getDistance(){
        return readingCache;
    }

}
