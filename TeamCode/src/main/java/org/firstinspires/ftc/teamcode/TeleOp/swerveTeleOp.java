package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.driver;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.updateControllers;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.RRDrive.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.RRUtil.PoseStorage;
import org.firstinspires.ftc.teamcode.Utilities.Vector2d;

//@Disabled
@TeleOp(name="swerve TeleOp", group="Iterative Opmode")
public class swerveTeleOp extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    double offset = 0;
    String offsetTelemetry = "UP";

    MecanumDrivetrain mecanumDrivetrain;

    StandardTrackingWheelLocalizer myLocalizer;
    Pose2d robotPose;



    @Override
    public void init() {
        setOpMode(this);

        PoseStorage.currentPose = new Pose2d(0,0);
        myLocalizer = new StandardTrackingWheelLocalizer();
        myLocalizer.setPoseEstimate(new Pose2d(0,0,0));

        mecanumDrivetrain = new MecanumDrivetrain();
        multTelemetry.update();
    }


    @Override
    public void init_loop() {
        update();
        mecanumDrivetrain.driveFullControl(new Vector2d(0,0), driver.leftStick.X(), .5, false);
        if(driver.up.tap()){
            offsetTelemetry = "UP"; offset = 0;
        }else if(driver.right.tap()){
            offsetTelemetry = "RIGHT"; offset = 90;
        }else if(driver.down.tap()){
            offsetTelemetry = "DOWN"; offset = 180;
        }else if(driver.left.tap()){
            offsetTelemetry = "LEFT"; offset = -90;
        }
        initTelemetry();
    }


    @Override
    public void start() {
        update();
        mecanumDrivetrain.gyro.reset(offset);
        mecanumDrivetrain.targetAngle = offset;
        //swerveDrivetrain.odometry.start();
        runtime.reset();
        multTelemetry.addData("Status", "Started");
        multTelemetry.update();
    }


    @Override
    public void loop() {
        update();
        //mecanumDrivetrain.teleState(driver.rightStick.vec(), driver.leftStick.X(), driver.RT.range(.4, 1));
        //mecanumDrivetrain.cardinalControls(driver.up.tap(), driver.right.tap(), driver.down.tap(), driver.left.tap());
        loopTelemetry();
    }

    public void stop(){
    }




    //PUT ALL UPDATE METHODS HERE

    private void update(){
        updateControllers();
        myLocalizer.update();
        robotPose = myLocalizer.getPoseEstimate();
        mecanumDrivetrain.update();
    }

    //Telemetry to be displayed during init_loop()

    private void initTelemetry(){
        multTelemetry.addData("Status", "InitLoop");
        multTelemetry.addData("offset", offsetTelemetry);
        multTelemetry.update();
    }

    //Telemetry to be displayed during loop()

    private void loopTelemetry(){
        multTelemetry.addData("Status", "TeleOp Running");
        multTelemetry.addData("left", myLocalizer.leftEncoder.getCurrentPosition());
        multTelemetry.addData("right", myLocalizer.rightEncoder.getCurrentPosition());
        multTelemetry.update();
    }
}