package org.firstinspires.ftc.teamcode.Hardware;

import static java.lang.Math.abs;
import static java.lang.Math.max;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.BetterSensors.SensorInterpreters.GyroExtensions;
import org.firstinspires.ftc.teamcode.BetterSensors.Sensors.Gyro;
import org.firstinspires.ftc.teamcode.RRDrive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.Vector2d;

public class MecanumDrivetrain {

    private MotorExCustom drivefl, drivefr, drivebl, drivebr;

    @Config
    public static class DrivetrainDash{
        public static double headingP = 0.014,   headingI = 0,   headingD = 0.0018;
        public static double momentOfInertia = 0.0015, angularVelocityThreshold = 100;
    }

    public final Gyro gyro;
    public final GyroExtensions gyroExtensions;
    public double targetAngle = 0;
    private final PIDController headingPID = new PIDController(DrivetrainDash.headingP, DrivetrainDash.headingI, DrivetrainDash.headingD, 0);
    //public StandardTrackingWheelLocalizer localizer = new StandardTrackingWheelLocalizer();

    public MecanumDrivetrain(){
        drivefl = new MotorExCustom("drivefl", true);
        drivefr = new MotorExCustom("drivefr");
        drivebl = new MotorExCustom("drivebl", true);
        drivebr = new MotorExCustom("drivebr");

        gyro = new Gyro(0, "imu");
        gyroExtensions = new GyroExtensions(gyro);
        gyro.update(); gyro.reset();
    }

    public void update(){
        drivefl.update(); drivefr.update(); drivebl.update(); drivebr.update(); gyro.update();
        gyroExtensions.setMomentOfInertia(DrivetrainDash.momentOfInertia);
        //localizer.update();
        headingPID.update(gyroExtensions.closestTarget(targetAngle) - gyro.angle());
    }

    public void driveFullControl(Vector2d transVec, double rotation, double power, boolean KETurns){
        Vector2d transVecFC = transVec.rotateBy(gyro.angle());
        setMotorPower(transVecFC.getY(), transVecFC.getX(), KETurns ? KETurns(rotation) : rotation, power);
    }

    public void driveFullControl(Vector2d transVec, double rotation, double power){
        driveFullControl(transVec, rotation, power, true);
    }

    public void driveAngleLock(Vector2d transVec, double angle, double power){
        targetAngle = angle;
        Vector2d transVecFC = transVec.rotateBy(gyro.angle());
        setMotorPower(transVecFC.getY(), transVecFC.getX(),headingPID.getCorrection(), power);
    }

    public double KETurns(double input){
        if(input!= 0 || gyro.angularVelocity() > SwerveDrivetrain.DrivetrainDash.angularVelocityThreshold) {
            targetAngle = gyro.angle() + gyroExtensions.angularKE();
            return input;
        }else{
            return headingPID.getCorrection();
        }
    }

    public void setMotorPower(double drive, double strafe, double turn, double power){
        drive = Range.clip(-drive, -1, 1);
        strafe = Range.clip(strafe, -1, 1);
        turn = Range.clip(turn, -1, 1);
        power = Range.clip(power, 0.05 , 1);

        double flPower = (drive - strafe - turn) * power;
        double frPower = (drive + strafe + turn) * power;
        double blPower = (drive + strafe - turn) * power;
        double brPower = (drive - strafe + turn) * power;

        double maxPower = abs(max(max(abs(flPower), abs(frPower)), max(abs(blPower), abs(brPower))));
        if(maxPower > 1) { frPower /= maxPower; flPower /= maxPower; blPower /= maxPower; brPower /= maxPower; }
        else if(maxPower < .05 && maxPower > -.05) { flPower = 0; frPower = 0; blPower = 0; brPower = 0; }

        drivefl.setPower(flPower); drivefr.setPower(frPower); drivebl.setPower(blPower); drivebr.setPower(brPower);;
    }

    public void teleState(Vector2d translation, double rotation, double power){
        switch(currentDriveState){
            case FULL_CONTROL:
                driveFullControl(translation, rotation, power, false);
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
