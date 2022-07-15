package org.firstinspires.ftc.teamcode.BetterSensors.Sensors;


import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensor;
import org.firstinspires.ftc.teamcode.Utilities.AngleWrapper;
import org.firstinspires.ftc.teamcode.Utilities.MathUtils;
import org.firstinspires.ftc.teamcode.Utilities.OpModeUtils;

public class AnalogEncoder extends Sensor<Double> {

    private AnalogInput encoder;
    private double absoluteAngle, wrappedAngle;
    private AngleWrapper angleWrapper;

    public AnalogEncoder(int pingFrequency, String hardwareID) {
        super(pingFrequency, hardwareID);
    }

    @Override
    protected void sensorInit(String hardwareID) {
        angleWrapper = new AngleWrapper();
        encoder = OpModeUtils.hardwareMap.get(AnalogInput.class, hardwareID);
    }

    @Override
    protected Double pingSensor() {
        absoluteAngle = MathUtils.linearConversion(encoder.getVoltage() * 72, 1.3, 348, 0, 360);
        wrappedAngle = angleWrapper.wrapAngle(absoluteAngle);
        return encoder.getVoltage();
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    public double getAbsoluteAngle(){
        return absoluteAngle;
    }

    public double getWrappedAngle(){
        return wrappedAngle;
    }
}
