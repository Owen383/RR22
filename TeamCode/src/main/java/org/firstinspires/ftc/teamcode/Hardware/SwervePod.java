package org.firstinspires.ftc.teamcode.Hardware;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensors.AnalogEncoder;
import org.firstinspires.ftc.teamcode.Utilities.MathUtils;
import org.firstinspires.ftc.teamcode.Utilities.PIDController;
import org.firstinspires.ftc.teamcode.Utilities.Vector2d;

public class SwervePod {

    @Config
    public static class SwervePodDash{
        public static double motorSpeedP = 0,   motorSpeedI = 0,   motorSpeedD = 0;
        public static double podAngleP = 0.0125, podAngleI = 0,     podAngleD = 0.0006;
    }

    public final MotorExCustom motor1, motor2;
    private AnalogEncoder MA3Encoder;
    private double targetAngle = 90, targetPower = 0, targetVelocityRatio = 0, measuredVelocityRatio = 0;
    private Vector2d positionVec;
    private final PIDController podAnglePID = new PIDController(), motorSpeedPID = new PIDController();

    public SwervePod(String motor1ID, String motor2ID, String encoderID, Vector2d positionVec){
        motor1 = new MotorExCustom(motor1ID,false);
        motor2 = new MotorExCustom(motor2ID, true);
        motor1.runWithoutEncoders(); motor2.runWithoutEncoders();
        MA3Encoder = new AnalogEncoder(0, encoderID);
        this.positionVec = positionVec;
    }

    public void update(){
        motor1.update(); motor2.update();
        //motorSpeedPID.setConstants(SwervePodDash.motorSpeedP, SwervePodDash.motorSpeedI, SwervePodDash.motorSpeedD);
        //motorSpeedPID.update(targetVelocityRatio - measuredVelocityRatio);
        podAnglePID.setConstants(SwervePodDash.podAngleP, SwervePodDash.podAngleI, SwervePodDash.podAngleD);
        podAnglePID.update(MathUtils.closestTarget(targetAngle, getWrappedAngle(), 180)  - getWrappedAngle());
        MA3Encoder.update();
    }


    public void goToTarget(Vector2d targetVector){
        if(abs(getWrappedAngle() - MathUtils.closestTarget(targetAngle, getWrappedAngle(), 360)) > 90) targetPower = -targetVector.getMagnitude();
        else targetPower = targetVector.getMagnitude();
        if(targetVector.getMagnitude() > .1) targetAngle = targetVector.getAngleDouble();
        double rotationInput = (targetVector.getMagnitude() != 0 ) ? podAnglePID.getCorrection() : podAnglePID.getCorrection() * (targetVector.getMagnitude());
        powerInput(rotationInput + targetPower, rotationInput - targetPower);
    }

    public void powerInput(double motor1Power, double motor2Power){
        motor1.setPower(motor1Power);
        motor2.setPower(motor2Power);
    }

    public double getWrappedAngle(){
        return -MA3Encoder.getWrappedAngle();
    }

    public double getAbsoluteAngle(){
        return -MA3Encoder.getAbsoluteAngle();
    }

    public Vector2d vec(){
        return positionVec.clone();
    }

}
