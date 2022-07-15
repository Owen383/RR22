package org.firstinspires.ftc.teamcode.Hardware;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.hardwareMap;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.Utilities.Derivative;

public class MotorExCustom {
    private DcMotor motor1, motor2;
    private int currentPosition = 0;
    private double velocity = 0;
    private Derivative positionDerivative = new Derivative(3);
    private double ticksPerRev = 537.7;

    public MotorExCustom(DcMotor motor1, DcMotor motor2){
        this.motor1 = motor1;
        this.motor2 = motor2;
    }

    public MotorExCustom(DcMotor motor){
        motor1 = motor;
    }

    public MotorExCustom(String motor1ID, boolean reverse1, String motor2ID, boolean reverse2){
        motor1 = hardwareMap.get(DcMotor.class, motor1ID);
        motor2 = hardwareMap.get(DcMotor.class, motor2ID);

        if(reverse1) motor1.setDirection(DcMotorSimple.Direction.REVERSE);
        else motor1.setDirection(DcMotorSimple.Direction.FORWARD);
        if(reverse2) motor2.setDirection(DcMotorSimple.Direction.REVERSE);
        else motor2.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public MotorExCustom(String motorID, boolean reverse){
        motor1 = hardwareMap.get(DcMotor.class, motorID);
        if(reverse) setDirectionReverse();
        else setDirectionForward();
    }

    public MotorExCustom(String motor1ID, String motor2ID, boolean reversePair) {
        motor1 = hardwareMap.get(DcMotor.class, motor1ID);
        motor2 = hardwareMap.get(DcMotor.class, motor2ID);

        if(reversePair) setDirectionReverse();
        else setDirectionForward();
    }

    public MotorExCustom(String motor1ID, String motor2ID) {
        motor1 = hardwareMap.get(DcMotor.class, motor1ID);
        motor2 = hardwareMap.get(DcMotor.class, motor2ID);
        setDirectionForward();
    }

    public MotorExCustom(String motorID){
        motor1 = hardwareMap.get(DcMotor.class, motorID);
        setDirectionForward();
    }

    public void update(){
        if(motor2 != null) currentPosition = (motor1.getCurrentPosition() + motor2.getCurrentPosition()) / (2);
        else currentPosition = motor1.getCurrentPosition();
        positionDerivative.update(System.currentTimeMillis() / 60000.0, (double)currentPosition / ticksPerRev);
        velocity = positionDerivative.getDerivative();
    }

    public void setTicksPerRev(double ticksPerRev){
        this.ticksPerRev = ticksPerRev;
    }





    public void setPower(double power){
        motor1.setPower(power);
        if(motor2 != null) motor2.setPower(power);
    }

    public int getCurrentPosition(){
        return currentPosition;
    }

    public double getVelocity(){
        return velocity;
    }

    // DIRECTION //

    public void setDirection(DcMotorSimple.Direction direction){
        motor1.setDirection(direction);
        if(motor2 != null) motor2.setDirection(direction);
    }

    public void setDirectionForward() { setDirection(DcMotorSimple.Direction.FORWARD); }

    public void setDirectionReverse() { setDirection(DcMotorSimple.Direction.REVERSE); }

    // MODE //

    public void setMode(DcMotor.RunMode mode){
        motor1.setMode(mode);
        if(motor2 != null) motor2.setMode(mode);
    }

    public void runWithEncoders(){ setMode(DcMotor.RunMode.RUN_USING_ENCODER); }

    public void runWithoutEncoders(){ setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); }

    public void stopAndResetEncodes(){ setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); }

    public void runToPosition(){ setMode(DcMotor.RunMode.RUN_TO_POSITION); }

    public MotorConfigurationType getMotorType(){
        return motor1.getMotorType();
    }

    public void setMotorType(MotorConfigurationType motorConfigurationType){
        motor1.setMotorType(motorConfigurationType);
    }

    // ZERO POWER BEHAVIOR //

    public void setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior zeroPowerBehaviour){
        motor1.setZeroPowerBehavior(zeroPowerBehaviour);
        if(motor2 != null) motor2.setZeroPowerBehavior(zeroPowerBehaviour);
    }

    public void zeroPowerBrake(){ setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.BRAKE); }

    public void zeroPowerFloat(){ setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior.FLOAT); }

    public DcMotor getMotor1() {
        return motor1;
    }

    public DcMotor getMotor2() {
        return motor2;
    }
}