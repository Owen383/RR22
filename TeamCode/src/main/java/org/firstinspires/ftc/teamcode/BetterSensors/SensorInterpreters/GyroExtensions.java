package org.firstinspires.ftc.teamcode.BetterSensors.SensorInterpreters;

import static org.firstinspires.ftc.teamcode.Utilities.MathUtils.pow;
import static org.firstinspires.ftc.teamcode.Utilities.MathUtils.withinRange;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.MathUtils;

public class GyroExtensions {
    double momentOfInertia = 0;

    private final Gyro gyro;

    public GyroExtensions (Gyro gyro){
        this.gyro = gyro;
    }

    public boolean isAngleWithinMargin(double targetAngle, double margin){
        double coterminalTarget = closestTarget(targetAngle);
        return withinRange(gyro.angle(), coterminalTarget - margin, coterminalTarget + margin);
    }
    
    @RequiresApi(api = Build.VERSION_CODES.N)
    public double closestTarget(double targetAngle){
        return MathUtils.closestTarget(targetAngle, gyro.angle(), 360);
    }


    public void setMomentOfInertia(double moi){
        momentOfInertia = moi;
    }

    public double angularKE() {
        return pow(gyro.angularVelocity(), 2) * .5 * momentOfInertia;
    }
    
}
