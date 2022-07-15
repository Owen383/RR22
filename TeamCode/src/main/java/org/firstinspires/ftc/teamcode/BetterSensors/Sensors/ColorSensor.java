package org.firstinspires.ftc.teamcode.BetterSensors.Sensors;
import static org.firstinspires.ftc.teamcode.Utilities.MathUtils.withinRange;

import com.qualcomm.robotcore.hardware.HardwareDeviceHealth;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.BetterSensors.DataTypes.ColorAndDistReading;
import org.firstinspires.ftc.teamcode.BetterSensors.Drivers.ColorSensorV3;
import org.firstinspires.ftc.teamcode.BetterSensors.Sensor;
import org.firstinspires.ftc.teamcode.Utilities.OpModeUtils;

public class ColorSensor extends Sensor<ColorAndDistReading> {

    ColorSensorV3 colorSensor;

    public ColorSensor(int pingFrequency, String hardwareID){
        super(pingFrequency, hardwareID);
    }

    /**
     * This initializes the method. hardwareID specifies which color sensor is initialized.
     * @param hardwareID
     */
    @Override
    protected void sensorInit(String hardwareID) {
        colorSensor = OpModeUtils.hardwareMap.get(ColorSensorV3.class, hardwareID);
    }

    @Override
    protected ColorAndDistReading pingSensor() {
        colorSensor.updateColors();
        double distanceReading = colorSensor.getDistance(DistanceUnit.MM);
        return new ColorAndDistReading(colorSensor.alpha(), colorSensor.red(), colorSensor.green(), colorSensor.blue(), distanceReading);
    }

    @Override
    public boolean isConnected() {
        return (!withinRange(getDistanceCM(), 1523, 1525) || !(colorSensor.getDeviceClient().getHealthStatus() == HardwareDeviceHealth.HealthStatus.UNHEALTHY));
    }

    @Override
    public String getDeviceName() {
        return colorSensor.getDeviceClient().getUserConfiguredName();
    }

    public int getAlpha(){
        return readingCache.alpha();
    }

    public int getRed(){
        return readingCache.red();
    }

    public int getBlue(){
        return readingCache.blue();
    }

    public int getGreen(){
        return readingCache.green();
    }

    public double getDistanceMM(){
        return readingCache.distance();
    }

    public double getDistanceCM(){
        return readingCache.distance() * 10;
    }

}
