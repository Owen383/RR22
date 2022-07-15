package org.firstinspires.ftc.teamcode.Hardware;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.BetterSensors.SensorInterpreters.GyroExtensions;
import org.firstinspires.ftc.teamcode.BetterSensors.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.Utilities.MathUtils;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.Vector2d;

public class SwerveDrivetrain {

    @Config
    public static class DrivetrainDash{
        public static double headingP = 0.014,   headingI = 0,   headingD = 0.0018;
        public static double momentOfInertia = 0.0015, angularVelocityThreshold = 100;
    }

    private final SwervePod leftPod, rightPod;
    public final Gyro gyro;
    public final GyroExtensions gyroExtensions;
    public double targetAngle = 0;
    private final PIDController headingPID = new PIDController();
    //public ThreeWheelOdometry odometry;


    public SwerveDrivetrain(){
        leftPod = new SwervePod("rightback", "rightfront", "rightencoder", new Vector2d(90));
        rightPod = new SwervePod("leftfront", "leftback", "leftencoder", new Vector2d(-90));
        //odometry = new ThreeWheelOdometry(leftPod.motor1::getCurrentPosition, leftPod.motor2::getCurrentPosition, rightPod.motor1::getCurrentPosition);
        gyro = new Gyro(0, "imu");
        gyroExtensions = new GyroExtensions(gyro);
        gyro.update(); gyro.reset();
    }

    public void update(){
        rightPod.update(); leftPod.update(); gyro.update();
        gyroExtensions.setMomentOfInertia(DrivetrainDash.momentOfInertia);
        headingPID.update(gyroExtensions.closestTarget(targetAngle) - gyro.angle());
        headingPID.setConstants(DrivetrainDash.headingP, DrivetrainDash.headingI, DrivetrainDash.headingD);
    }



    //TELEOP CODE

    public void driveFullControl(Vector2d transVec, double rotation, double power, boolean KETurns){
        Vector2d transVecFC = transVec.rotateBy(gyro.angle());
        podInput(transVecFC.scaleBy(power), KETurns ? KETurns(rotation) * power : rotation * power);
    }

    public void driveFullControl(Vector2d transVec, double rotation, double power){
        driveFullControl(transVec, rotation, power, true);
    }

    public void driveAngleLock(Vector2d transVec, double angle, double power){
        targetAngle = angle;
        Vector2d transVecFC = transVec.rotateBy(gyro.angle());
        podInput(transVecFC.scaleBy(power), headingPID.getCorrection() * power);
    }

    public double KETurns(double input){
        if(input!= 0 || gyro.angularVelocity() > DrivetrainDash.angularVelocityThreshold) {
            targetAngle = gyro.angle() + gyroExtensions.angularKE();
            return input;
        }else{
            return headingPID.getCorrection();
        }
    }

    public void podInput(Vector2d transVec, double rotMag) {
        Vector2d targetVecR = transVec.add(rightPod.vec().scaleTo(rotMag).rotateBy(-90));
        Vector2d targetVecL = transVec.add(leftPod.vec().scaleTo(rotMag).rotateBy(-90));
        double maxPower = MathUtils.absMax(targetVecL.getMagnitude(), targetVecR.getMagnitude());
        if(maxPower > 1) { targetVecL = targetVecL.scaleBy(1 / maxPower); targetVecR = targetVecR.scaleBy(1 / maxPower); }
        else if(maxPower < .05) { targetVecL = targetVecL.scaleTo(0); targetVecR = targetVecR.scaleTo(0); }
        rightPod.goToTarget(targetVecR); leftPod.goToTarget(targetVecL);
    }


    //STATE MACHINE

    public void teleState(Vector2d translation, double rotation, double power){
        switch(currentDriveState){
            case FULL_CONTROL:
                driveFullControl(translation, rotation, power);
                break;
            case NORTH:
                driveAngleLock(translation, gyroExtensions.closestTarget(0), power);
                break;
            case EAST:
                driveAngleLock(translation, gyroExtensions.closestTarget(270), power);
                break;
            case SOUTH:
                driveAngleLock(translation, gyroExtensions.closestTarget(180), power);
                break;
            case WEST:
                driveAngleLock(translation, gyroExtensions.closestTarget(90), power);
                break;
        }
        if(abs(rotation) > 0.05) newState(DriveState.FULL_CONTROL);
    }

    public void cardinalControls(boolean north, boolean east, boolean south, boolean west){
        if(north) newState(DriveState.NORTH);
        if(east) newState(DriveState.EAST);
        if(west) newState(DriveState.WEST);
        if(south) newState(DriveState.SOUTH);
    }

    private enum DriveState {
        FULL_CONTROL, NORTH, EAST, SOUTH, WEST
    }

    public static DriveState currentDriveState = DriveState.FULL_CONTROL;

    private static void newState(DriveState newState) {
        if(currentDriveState != newState) currentDriveState = newState;
    }

}
