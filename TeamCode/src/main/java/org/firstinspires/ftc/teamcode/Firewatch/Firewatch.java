package org.firstinspires.ftc.teamcode.Firewatch;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.driver;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensor;
import org.firstinspires.ftc.teamcode.Firewatch.revextensions2.ExpansionHubEx;
import org.firstinspires.ftc.teamcode.Hardware.MotorExCustom;
import org.firstinspires.ftc.teamcode.Utilities.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

public class Firewatch {

    private static Color NORMAL_LED = new Color(0, 255, 0);
    private static Color ERROR_LED = new Color(255, 0, 0);
    private static Color CHECKED_LED = new Color(0, 221, 255);


    private static ArrayList<ExpansionHubEx> registeredHubs = new ArrayList<ExpansionHubEx>();

    private static ArrayList<FirewatchMotor> registeredMotors = new ArrayList<FirewatchMotor>();
    private static ArrayList<FirewatchSensor> registeredSensors = new ArrayList<FirewatchSensor>();
    private static ArrayList<FirewatchManualSensor> registeredManualSensors = new ArrayList<FirewatchManualSensor>();

    private static boolean errorState = false;
    private static boolean sensorsErrorState = false;
    private static boolean motorsErrorState = false;
    private static boolean hubsErrorState = false;



    private static int sensorIndex = 0;
    private static boolean sensorsChecked = false;

    public static void registerHubs(){
        List<LynxModule> hubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub: hubs) {
            registeredHubs.add(new ExpansionHubEx(hub));
        }
    }

    public static void register(ExpansionHubEx hub){
        registeredHubs.add(hub);
    }

    public static void register(DcMotor motor, String ID){
        registeredMotors.add(new FirewatchMotor(motor, ID));
    }

    public static void register(DcMotor motor){
        registeredMotors.add(new FirewatchMotor(motor, motor.getDeviceName()));
    }

    public static void register(MotorExCustom motorUnit, String ID){
        register(motorUnit.getMotor1(), ID + ": motor1");
        register(motorUnit.getMotor2(), ID + ": motor2");
    }

    public static void register(MotorExCustom motorUnit){
        register(motorUnit.getMotor1(), motorUnit.getMotor1().getDeviceName());
        register(motorUnit.getMotor2(), motorUnit.getMotor2().getDeviceName());
    }

    public static void register(Sensor sensor, String ID){
        registeredSensors.add(new FirewatchSensor(sensor, ID));
    }

    public static void register(Sensor sensor){
        registeredSensors.add(new FirewatchSensor(sensor, sensor.getDeviceName()));
    }

    @Deprecated
    public static void register(DoubleSupplier sensorDataChecked, String ID){
        registeredManualSensors.add(new FirewatchManualSensor(sensorDataChecked, ID));
    }

    public static void readErrorStateOnLEDs(){
        errorState = sensorsErrorState || motorsErrorState || hubsErrorState;
        multTelemetry.addData("errorState", errorState);
        for (ExpansionHubEx hub: registeredHubs) {
            if(errorState){
                hub.setLedColor(ERROR_LED);
            }else{
                hub.setLedColor(CHECKED_LED);
            }
        }
    }

    public static void checkMotorIntegrity(){
        motorsErrorState = false;
        for (FirewatchMotor motor: registeredMotors) {
            if(motor.isInError()){
                motorsErrorState = true;
                multTelemetry.addData("!!!!MOTOR DISCONNECTION DETECTED ON: ", motor.getID());
            }
        }
    }

    public static void checkSensorIntegrity(){
        sensorsErrorState = false;
        for (FirewatchSensor sensor: registeredSensors) {
            if(!sensor.isConnected()){
                sensorsErrorState = true;
                multTelemetry.addData("!!!!SENSOR DISCONNECTION DETECTED ON: ", sensor.getID());
            }
        }
    }

    public static void checkHubIntegrity(){
        hubsErrorState = false;
        for (ExpansionHubEx hub: registeredHubs) {
            if(hub.isModuleOverTemp()){
                hubsErrorState = true;
                multTelemetry.addData("!!!!HUB OVERHEAT ON HUB: ", hub.getDeviceName());
            }
            for (int i = 0; i < 4; i++) {
                if(hub.isMotorBridgeOverTemp(i)){
                    hubsErrorState = true;
                    multTelemetry.addLine("!!!!HUB MOTOR OVERHEAT ON HUB " + hub.getDeviceName() + " at port:" + i);
                }
            }
        }
    }

    @Deprecated
    public static void checkSensorIntegrityManual(){
        if(sensorIndex >= registeredSensors.size()){
            FirewatchManualSensor sensor = registeredManualSensors.get(sensorIndex);
            multTelemetry.addLine("Ensure that " + sensor.getID() + " sensor is working:");
            multTelemetry.addLine("Press the circle button when the sensor has been checked");
            multTelemetry.addData("Current Sensor Reading:", sensor.getData());
            if(driver.circle.tap()){
                sensorIndex++;
            }
        }else{
            multTelemetry.addLine("Sensor check complete.");
        }

    }
    
    public static void resetErrorState(){
        for (FirewatchMotor motor: registeredMotors) {
            motor.resetErrorState();
        }
        errorState = false;
        sensorIndex = 0;
    }

    public static void reset(){
        for (ExpansionHubEx hub: registeredHubs) {
            hub.setLedColor(NORMAL_LED);
        }
        errorState = false;
        registeredMotors.clear();
        registeredSensors.clear();
        sensorIndex = 0;
    }

}
