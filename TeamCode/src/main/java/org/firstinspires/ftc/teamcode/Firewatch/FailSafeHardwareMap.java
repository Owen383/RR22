package org.firstinspires.ftc.teamcode.Firewatch;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

public class FailSafeHardwareMap {

    private final HardwareMap map;

    public FailSafeHardwareMap(HardwareMap map) {
        this.map = map;
    }

    public <T> T get(Class<? extends T> classOrInterface, String deviceName) {
        try{
            return map.get(classOrInterface, deviceName);
        }catch(IllegalArgumentException e){
            RobotLog.ee("HardwareMap failed safe", "No hardware device found with ID: " + deviceName);
            return null;
        }
    }
}
