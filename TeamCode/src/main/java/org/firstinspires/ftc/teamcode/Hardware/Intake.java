package org.firstinspires.ftc.teamcode.Hardware;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensors.ColorSensor;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;

public class Intake {

    private CRServo intakeLeft, intakeRight;

    private static ColorSensor intakeColorSensor;
    public static boolean rumble;
    public static double intakePower;

    @Config
    public static class IntakeDash{
        public static double COLOR_SENSOR_THRESHOLD = 70;
    }

    public Intake(){
        intakeLeft = hardwareMap.get(CRServo.class, "intakeleft");
        intakeRight = hardwareMap.get(CRServo.class, "intakeright");
        intakeLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        intakeColorSensor = new ColorSensor(0, "color1");
    }

    public void stateMachine(boolean intakeOn, boolean intakeReverse){
        rumble = false;
        intakeOff();
        if(Slides.slideMotor.getCurrentPosition() < Slides.SlidesDash.slideIntakeThreshold){
            if(intakeOn) intakeOn();
            else if(intakeReverse) intakeReverse();
        }
        if(isConeIntaken()) rumble = true;
    }

    public void update(){
        intakeColorSensor.update();
        if(Slides.currentSlideState == Slides.SlideState.HOME && Slides.slideMotor.getCurrentPosition() < Slides.SlidesDash.slideIntakeThreshold) intakeColorSensor.resumeSensor();
        else intakeColorSensor.pauseSensor();
    }

    public void setPower(double power){
        intakeLeft.setPower(power); intakeRight.setPower(power); intakePower = power;
    }


    public void intakeOn(){ setPower(1); }

    public void intakeOff(){ setPower(0); }

    public void intakeReverse(){ setPower(-1); }

    public static boolean isConeIntaken(){
        multTelemetry.addData("color1", intakeColorSensor.getDistanceCM());
        return intakeColorSensor.getDistanceCM() < IntakeDash.COLOR_SENSOR_THRESHOLD && intakePower != 0;
    }

}
