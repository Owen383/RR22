package org.firstinspires.ftc.teamcode.Localization;

import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.kinematics.HolonomicOdometry;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.MotorExCustom;
import org.firstinspires.ftc.teamcode.Utilities.LoopingThread;

import java.util.function.IntSupplier;

public class ThreeWheelOdometry extends LoopingThread {
    public static Pose2d position;

    private final HolonomicOdometry odometry;

    //TODO: THESE VALUES NEED TO BE TUNED, EXCEPT FOR LAST TWO VALUES!!!!!
    private static double TRACKWIDTH = 14.31;
    private static double CENTER_WHEEL_OFFSET = 0.477;
    private static double WHEEL_DIAMETER = 2.0;
    private static double TURNS_PER_REV = 1.0; //sounds weird but how many turns odo wheel goes per encoder rev
    private static double TICKS_PER_REV = 8192;

    private final GenericEncoderEx leftEncoder;
    private final GenericEncoderEx rightEncoder;
    private final GenericEncoderEx horizontalEncoder;

    private final boolean savePosition;

    public ThreeWheelOdometry(DcMotor leftEncoder, DcMotor rightEncoder, DcMotor horizontalEncoder){
        this(leftEncoder, rightEncoder, horizontalEncoder, false);
    }

    public ThreeWheelOdometry(DcMotor leftEncoder, DcMotor rightEncoder, DcMotor horizontalEncoder, boolean savePosition){
        this(leftEncoder::getCurrentPosition, rightEncoder::getCurrentPosition, horizontalEncoder::getCurrentPosition, savePosition);
    }

    public ThreeWheelOdometry(MotorExCustom leftEncoder, MotorExCustom rightEncoder, MotorExCustom horizontalEncoder){
        this(leftEncoder, rightEncoder, horizontalEncoder, false);
    }

    public ThreeWheelOdometry(MotorExCustom leftEncoder, MotorExCustom rightEncoder, MotorExCustom horizontalEncoder, boolean savePosition){
        this(leftEncoder::getCurrentPosition, rightEncoder::getCurrentPosition, horizontalEncoder::getCurrentPosition, savePosition);
    }

    public ThreeWheelOdometry(IntSupplier leftEncoder, IntSupplier rightEncoder, IntSupplier horizontalEncoder){
        this(leftEncoder, rightEncoder, horizontalEncoder, false);
    }

    public ThreeWheelOdometry(IntSupplier leftEncoder, IntSupplier rightEncoder, IntSupplier horizontalEncoder, boolean savePosition){
        this.leftEncoder = new GenericEncoderEx(leftEncoder);
        this.rightEncoder = new GenericEncoderEx(rightEncoder);
        this.horizontalEncoder = new GenericEncoderEx(horizontalEncoder);

        odometry = new HolonomicOdometry(
                this.leftEncoder::getDistance,
                this.rightEncoder::getDistance,
                this.horizontalEncoder::getDistance,
                TRACKWIDTH,
                CENTER_WHEEL_OFFSET
        );

        odometry.updatePose(position);

        this.savePosition = savePosition;
    }

    private void updateDistancePerPulse(){
        double DISTANCE_PER_PULSE = Math.PI * WHEEL_DIAMETER / (TICKS_PER_REV * TURNS_PER_REV);
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        horizontalEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void loop() {
        updateDistancePerPulse();
        odometry.updatePose();
        position = odometry.getPose();
    }

    @Override
    protected void end() {
        if(!savePosition){
            position = new Pose2d(0, 0, new Rotation2d(0));
        }
    }

    public void resetPosition(){
        position = new Pose2d(0, 0, new Rotation2d(0));
        odometry.updatePose(position);
    }
}
