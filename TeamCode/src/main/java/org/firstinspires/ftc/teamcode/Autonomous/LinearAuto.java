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

import org.firstinspires.ftc.teamcode.RRDrive.RRMecanumDrivetrain;

//@Disabled
@Autonomous(name = "RRAuto", group="Autonomous")
public class LinearAuto extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    RRMecanumDrivetrain drivetrain;
     Trajectory splineTest, strafe;

    public void initialize() {
        setOpMode(this);


        drivetrain = new RRMecanumDrivetrain();
        drivetrain.setPoseEstimate(new Pose2d(0,0,0));
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
    }

    private void buildTrajectories(){
        splineTest = drivetrain.trajectoryBuilder(new Pose2d(0,0, 0))
                .splineToConstantHeading(new Vector2d(30, 25), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-30, 25), Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(0, 0), Math.toRadians(0))
                .build();

        strafe = drivetrain.trajectoryBuilder(new Pose2d(20, 0, 0))
                .strafeLeft(30)
                .build();




    }
}


