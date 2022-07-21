package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.apache.commons.math3.stat.descriptive.moment.VectorialCovariance;
import org.firstinspires.ftc.teamcode.RRDrive.RRMecanumDrivetrain;

//@Disabled
@Autonomous(name = "RRAuto", group="Autonomous")
public class LinearAuto extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    RRMecanumDrivetrain drivetrain;
    Trajectory splineTest, backToStart;
    Pose2d startPose = new Pose2d(60, 20, 0);

    public void initialize() {
        setOpMode(this);


        drivetrain = new RRMecanumDrivetrain();
        drivetrain.setPoseEstimate(startPose);
        multTelemetry.addData("Status", "Initialized");
        multTelemetry.update();

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void runOpMode() throws InterruptedException {

        initialize();
        waitForStart();

        if(isStopRequested()) return;

        buildTrajectories();

        drivetrain.followTrajectory(splineTest);
        //drivetrain.followTrajectory(backToStart);
    }

    private void buildTrajectories(){
        splineTest = drivetrain.trajectoryBuilder(startPose, Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-15, 35, Math.toRadians(-90)), Math.toRadians(140))
                .splineToConstantHeading(new Vector2d(-50, 40), Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(-50, 10), Math.toRadians(-90))
                .build();

        backToStart = drivetrain.trajectoryBuilder(splineTest.end(), Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(-45, 0, Math.toRadians(90)), Math.toRadians(180))
                .build();




    }
}