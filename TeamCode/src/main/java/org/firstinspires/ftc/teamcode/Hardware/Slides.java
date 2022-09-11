package org.firstinspires.ftc.teamcode.Hardware;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;

import android.transition.Slide;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensors.ColorSensor;
import org.firstinspires.ftc.teamcode.Utilities.MathUtils;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;

public class Slides {

    public static MotorExCustom slideMotor;
    private Servo arm, claw;

    private final PIDController slidePID = new PIDController(SlidesDash.slidesP, SlidesDash.slidesI, SlidesDash.slidesD, (int) SlidesDash.slidesILength);

    private double slideInputPower = 0;

    @Config
    public static class SlidesDash{
        public static double ARM_INTAKE = .97,   ARM_SCORE = 0.3;
        public static double CLAW_OPEN = 0.85, CLAW_CLOSED = 1;
        public static double slidesP = 0.006,   slidesI = 0.0003,   slidesD = 0.0008,  slidesILength = 20, slidesISum = 0.2, slidesF = 0.1;
        public static double accelerationConstantSlides = .01;
        public static double scorePosition = 750, homePosition = -15, middlePosition = 175, slideIntakeThreshold = 25;
    }

    public Slides(){
        slideMotor = new MotorExCustom("slides");
        arm = hardwareMap.get(Servo.class, "arm");
        claw = hardwareMap.get(Servo.class, "claw");
        slideMotor.stopAndResetEncodes(); slideMotor.runWithoutEncoders();
        currentSlideState = SlideState.HOME; previousSlideState = SlideState.HOME;
    }

    public void update(){
        slideMotor.update();
        slidePID.setMaxIntSum(SlidesDash.slidesISum);
    }

    public void stateMachine(boolean extend, boolean drop){
        switch (currentSlideState){
            case HOME:
                slidesHome();
                if(slideMotor.getCurrentPosition() < SlidesDash.slideIntakeThreshold){
                    if(Intake.intakePower == 0) clawClosed();
                    else clawOpen();
                }else clawClosed();
                armIntake();
                if(extend) newState(SlideState.SCORE);
                break;

            case SCORE:
                slidesScore();
                if(stateTime.seconds() > .2) armScore();
                else armIntake();
                clawClosed();
                if(extend) newState(SlideState.RETRACT);
                if(drop) newState(SlideState.DROP);
                break;

            case DROP:
                if(stateTime.seconds() < .3) { clawOpen(); armScore(); }
                else if(stateTime.seconds() < .6) { clawClosed(); armIntake(); }
                else newState(SlideState.HOME);
                break;

            case RETRACT:
                if(stateTime.seconds() < .3) { clawClosed(); armIntake(); }
                else newState(SlideState.HOME);
                break;
        }


    }

    public void setSlidePosition(double position, double maxPower){
        position = Range.clip(position,-15, 1550);
        slidePID.setFComponent(SlidesDash.slidesF);
        double slidePower = Range.clip(MathUtils.pow(slidePID.update(position - slideMotor.getCurrentPosition()), 1), -maxPower, maxPower);
        if(Math.abs(slidePower) > Math.abs(slideInputPower) + .05){
            if(slidePower > 0) slideInputPower += Sensors.loopTime * SlidesDash.accelerationConstantSlides;
            else slideInputPower -= Sensors.loopTime * SlidesDash.accelerationConstantSlides;
        }
        slideInputPower = slidePower;
        slideMotor.setPower(slideInputPower);
    }

    public void slidesScore(){
        setSlidePosition(SlidesDash.scorePosition, 1);
    }

    public void slidesMiddle(){
        setSlidePosition(SlidesDash.middlePosition, 1);
    }

    public void slidesHome(){
        setSlidePosition(SlidesDash.homePosition, 1);
    }

    public void tuneFComponent(double power){
        slideMotor.setPower(power + SlidesDash.slidesF);
    }

    public void armScore(){
        arm.setPosition(SlidesDash.ARM_SCORE);
    }

    public void armIntake(){
        arm.setPosition(SlidesDash.ARM_INTAKE);
    }

    public void clawOpen(){
        claw.setPosition(SlidesDash.CLAW_OPEN);
    }

    public void clawClosed(){
        claw.setPosition(SlidesDash.CLAW_CLOSED);
    }


    public enum SlideState {
        SCORE, DROP, RETRACT, HOME
    }

    public static SlideState currentSlideState, previousSlideState;

    private static ElapsedTime stateTime = new ElapsedTime();

    private static void newState(SlideState newState) {
        if(currentSlideState != newState){
            previousSlideState = currentSlideState;
            currentSlideState = newState;
            stateTime.reset();
        }
    }
}
