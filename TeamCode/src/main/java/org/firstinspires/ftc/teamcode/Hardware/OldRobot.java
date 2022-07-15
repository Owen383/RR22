package org.firstinspires.ftc.teamcode.Hardware;/*

package org.firstinspires.ftc.teamcode.Hardware;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utilities.PID;
import org.firstinspires.ftc.teamcode.utilities.RingBufferOwen;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.floorMod;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static org.firstinspires.ftc.teamcode.HardwareClasses.SensorClasses.Vision.VisionUtils.PowerShot.PS_CLOSE;
import static org.firstinspires.ftc.teamcode.HardwareClasses.SensorClasses.Vision.VisionUtils.PowerShot.PS_FAR;
import static org.firstinspires.ftc.teamcode.HardwareClasses.SensorClasses.Vision.VisionUtils.PowerShot.PS_MID;
import static org.firstinspires.ftc.teamcode.utilities.MathUtils.pow;
import static org.firstinspires.ftc.teamcode.utilities.Utils.hardwareMap;

public class OldRobot {

    public static MotorEx frontLeft, frontRight, backLeft, backRight;

    private static final PID telePID = new PID(.02, 0, .004, 8);
    private static final PID autoPID = new PID(.02, 0, .003, 8);
    private static final PID visionPID = new PID(0.06, 0, .007, 10);

    private static final double TICKS_PER_ROTATION = 537.7;

    public static DriveState currentDriveState;

    public static double drive = 0, strafe = 0, turn = 0, power = 1;
    public static double targetAngle;
    public static double releaseAngle = 0;
    public static double adjRateOfChange = 0;

    private static double startAngle = 0;
    public static double currentInches = 0;
    public static boolean isStrafeComplete = true, isTurnComplete = true;

    private static int powerShotState = 3;
    private static double adjustmentAngle = 0;
    private static double towerAimAngle = 0;
    private static double PSAngle = 0;

    private static double maxRPM;


    public static void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontleft");
        frontRight = hardwareMap.get(DcMotor.class, "frontright");
        backLeft = hardwareMap.get(DcMotor.class, "backleft");
        backRight = hardwareMap.get(DcMotor.class, "backright");

        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        currentDriveState = DriveState.FULL_CONTROL;
    }


    public static void resetGyro(double offsetAngle){
        Sensors.gyro.update();
        Sensors.gyro.setDatum(Sensors.gyro.IMUAngle() + offsetAngle);
        Sensors.gyro.update();
        targetAngle = Sensors.gyro.rawAngle();
        releaseAngle = Sensors.gyro.rawAngle();
        adjRateOfChange = 0;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double closestTarget(double targetAngle){
        double simpleTargetDelta = floorMod(Math.round(((360 - targetAngle) + Sensors.gyro.rawAngle()) * 1e6), Math.round(360.000 * 1e6)) / 1e6;
        double alternateTargetDelta = -1 * (360 - simpleTargetDelta);
        return StrictMath.abs(simpleTargetDelta) <= StrictMath.abs(alternateTargetDelta) ? Sensors.gyro.rawAngle() - simpleTargetDelta : Sensors.gyro.rawAngle() - alternateTargetDelta;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double absClosestTarget(double targetAngle){
        if(Sensors.alliance == Sensors.Alliance.RED) return closestTarget(targetAngle + 180);
        else return closestTarget(targetAngle);
    }


    //SET POWER METHODS


    public static void setPower(double drive, double strafe, double turn, double power){
        OldRobot.drive = Range.clip(drive, -1, 1);
        OldRobot.strafe = Range.clip(strafe, -1, 1);
        OldRobot.turn = Range.clip(turn, -1, 1);
        OldRobot.power = Range.clip(power, 0.05 , 1);

        double flPower = (OldRobot.drive - OldRobot.strafe - OldRobot.turn) * OldRobot.power;
        double frPower = (OldRobot.drive + OldRobot.strafe + OldRobot.turn) * OldRobot.power;
        double blPower = (OldRobot.drive + OldRobot.strafe - OldRobot.turn) * OldRobot.power;
        double brPower = (OldRobot.drive - OldRobot.strafe + OldRobot.turn) * OldRobot.power;

        double maxPower = abs(max(max(abs(flPower), abs(frPower)), max(abs(blPower), abs(brPower))));
        if(maxPower > 1) { frPower /= maxPower; flPower /= maxPower; blPower /= maxPower; brPower /= maxPower; }
        else if(maxPower < .05 && maxPower > -.05) { flPower = 0; frPower = 0; blPower = 0; brPower = 0; }

        setMotorPower(flPower, frPower, blPower, brPower);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setPowerTele(double drive, double strafe, double turn, double power){
        double inputTurn;
        runWithoutEncoders();

        if(turn!= 0) {
            inputTurn = turn;
            releaseAngle = Sensors.gyro.rawAngle();
            adjRateOfChange = pow(Sensors.gyro.rateOfChange(), 2);
        }else if(adjRateOfChange > 1000){
            releaseAngle = Sensors.gyro.rawAngle();
            adjRateOfChange = pow(Sensors.gyro.rateOfChange(), 2);
            inputTurn = 0;
        }else{
            targetAngle = releaseAngle + .5 * .0035 * adjRateOfChange;
            inputTurn = telePID.update(closestTarget(targetAngle) - Sensors.gyro.rawAngle());
        }
        setPower(drive, strafe, inputTurn, power);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setPowerAuto(double drive, double strafe, double targetAngle, double power){
        OldRobot.targetAngle = targetAngle;
        releaseAngle = Sensors.gyro.rawAngle();
        turn = autoPID.update(OldRobot.targetAngle - Sensors.gyro.rawAngle());
        setPower(drive, strafe, turn, power);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void setPowerAuto(double drive, double strafe, double targetAngle){
        setPowerAuto(drive, strafe, targetAngle,1.0);
    }


    public double KETurns(double input){
        if(input!= 0 || gyro.angularVelocity() > SwerveDrivetrain.DrivetrainDash.angularVelocityThreshold) {
            targetAngle = gyro.angle() + gyroExtensions.angularKE();
            return input;
        }else{
            return headingPID.getCorrection();
        }
    }


    //TELEOP METHODS


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void driveState(double drive, double strafe, double turn, double power){
        switch ( currentDriveState){

            case FULL_CONTROL:
                setPowerTele(drive, strafe, turn, power);
                break;

            case NORTH:
                if(abs(turn) > 0.05) { newState(DriveState.FULL_CONTROL); break; }
                setPowerAuto(drive, strafe, closestTarget(0), power);
                break;

            case EAST:
                if(abs(turn) > 0.05) { newState(DriveState.FULL_CONTROL); break; }
                setPowerAuto(drive, strafe, closestTarget(270), power);
                break;

            case SOUTH:
                if(abs(turn) > 0.05) { newState(DriveState.FULL_CONTROL); break; }
                setPowerAuto(drive, strafe, closestTarget(180), power);
                break;

            case WEST:
                if(abs(turn) > 0.05) { newState(DriveState.FULL_CONTROL); break; }
                setPowerAuto(drive, strafe, closestTarget(90), power);
                break;


        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    //if the boolean is true, it sets the targetAngle for the PID system to the closest coterminal angle to the input's respective angle (North 0, East 270, South 180, West 90) with priority being in that order
    public static void cardinalState(boolean north, boolean east, boolean south, boolean west){
        if(north) newState(DriveState.NORTH);
        if(east) newState(DriveState.EAST);
        if(west) newState(DriveState.WEST);
        if(south) newState(DriveState.SOUTH);
    }


    public static void setAdjustmentAngle(double adjustmentAngle){ OldRobot.adjustmentAngle = adjustmentAngle; }

    public static double getAdjustmentAngle(){ return adjustmentAngle; }

    //MOTOR METHODS


    public static void setMotorPower(double flPower, double frPower, double blPower, double brPower){
        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(blPower);
        backRight.setPower(brPower);
    }

    public static void runWithEncoders(){
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public static void runWithoutEncoders(){
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public static void stopAndResetEncodes(){
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public static void resetWithoutEncoders(){
        stopAndResetEncodes();
        runWithoutEncoders();
    }

    public static void resetWithEncoders(){
        stopAndResetEncodes();
        runWithEncoders();
    }


    //STATE MACHINE STUFF


    private static void newState(DriveState newState) {
        currentDriveState = newState;
        runWithoutEncoders();
    }

    private enum DriveState {
        FULL_CONTROL, NORTH, EAST, SOUTH, WEST, ADJUSTMENT, HIGH_GOAL_AIM, MID_GOAL_AIM, POWER_SHOT_AIM
    }

}

*/
