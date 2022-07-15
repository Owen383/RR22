package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.RRDrive.RRMecanumDrivetrain;
import org.firstinspires.ftc.teamcode.RRUtil.PoseStorage;

//@Disabled
@Autonomous(name="Iterative Auto", group="Autonomous")
public class IterativeAuto extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    RRMecanumDrivetrain drivetrain;

    public void init() {
        setOpMode(this);

        drivetrain = new RRMecanumDrivetrain();
        drivetrain.setPoseEstimate(new Pose2d(0,0,0));
        trajectoryBuild();

        multTelemetry.addData("Status", "Initialized");
        multTelemetry.update();
    }


    @Override
    public void init_loop() {
        update();

    }


    @Override
    public void start() {
        update();
        runtime.reset();
        multTelemetry.addData("Status", "Started");
        multTelemetry.update();
    }


    @Override
    public void loop() {
        update();

    }

    public void trajectoryBuild(){
        Trajectory trajectory1 = null;
    }




    //PUT ALL UPDATE METHODS HERE

    private void update(){
        drivetrain.update();
        Pose2d poseEstimate = drivetrain.getPoseEstimate();
        PoseStorage.currentPose = poseEstimate;
    }

    //Telemetry to be displayed during init_loop()

    private void initTelemetry(){
        multTelemetry.addData("Status", "InitLoop");
        multTelemetry.update();
    }

    //Telemetry to be displayed during loop()

    private void loopTelemetry(){
        multTelemetry.addData("Status", "TeleOp Running");
        multTelemetry.update();
    }
}