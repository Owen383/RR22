package org.firstinspires.ftc.teamcode.Firewatch;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class FirewatchMotor {

    private final DcMotor rawMotor;
    private final DcMotorEx motorEx;
    private final String ID;
    private boolean isInError = false;

    public FirewatchMotor(DcMotor dcMotor, String ID){
        rawMotor = dcMotor;
        motorEx = (DcMotorEx)rawMotor;
        this.ID = ID;
    }

    public double getPower(){
        return rawMotor.getPower();
    }

    public double getMilliamps(){
        return motorEx.getCurrent(CurrentUnit.MILLIAMPS);
    }

    public boolean isInError(){
        isInError = rawMotor.getPower() != 0 ? getMilliamps() < 4 && getMilliamps() > -1: isInError;
        return isInError;
    }

    public void resetErrorState(){
        isInError = false;
    }

    public String getID(){
        return ID;
    }

    public DcMotorEx getMotor(){
        return motorEx;
    }

}
