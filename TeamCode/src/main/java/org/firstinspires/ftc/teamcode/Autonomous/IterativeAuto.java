package org.firstinspires.ftc.teamcode.Autonomous;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
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
    Trajectory spline1, spline2, spline3;
    RRMecanumDrivetrain drivetrain;

    Pose2d startPose = new Pose2d(0, 0, 0);

    public void init() {
        setOpMode(this);

        drivetrain = new RRMecanumDrivetrain();
        drivetrain.setPoseEstimate(startPose);
        trajectoryBuild();

    }


    @Override
    public void init_loop() {
        initTelemetry();
        update();
    }


    @Override
    public void start() {
        update();
        runtime.reset();
        newState(AutoState.IDLE);
        drivetrain.followTrajectory(spline1);
    }


    @Override
    public void loop() {
        update();
        switch(currentAutoState){
            case SPLINE1:

                if(!drivetrain.isBusy()){
                    newState(AutoState.SPLINE2);
                    drivetrain.followTrajectory(spline2);
                }
                break;

            case SPLINE2:

                if(!drivetrain.isBusy()){
                    newState(AutoState.SPLINE3);
                    drivetrain.followTrajectory(spline3);
                }
                break;

            case SPLINE3:
                if(!drivetrain.isBusy()){
                    newState(AutoState.IDLE);
                }
                break;

            case IDLE:
                break;
        }

        loopTelemetry();
    }

    public void trajectoryBuild(){
        spline1 = drivetrain.trajectoryBuilder(startPose)
                .splineToConstantHeading(new Vector2d(30, 25), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-30, 25), Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(0, 0), Math.toRadians(0))
                .build();

        spline2 = drivetrain.trajectoryBuilder(spline1.end())
                .splineToConstantHeading(new Vector2d(-30, -25), Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(30, -25), Math.toRadians(-90))
                .splineToConstantHeading(new Vector2d(0, 0), Math.toRadians(180))
                .build();

        spline3 = drivetrain.trajectoryBuilder(spline2.end())
                .splineToConstantHeading(new Vector2d(30, -25), Math.toRadians(0))
                .splineToConstantHeading(new Vector2d(30, 25), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(0, 0), Math.toRadians(90))
                .build();
    }

    //PUT ALL UPDATE METHODS HERE

    private void update(){
        drivetrain.update();
        Pose2d poseEstimate = drivetrain.getPoseEstimate();
        PoseStorage.currentPose = poseEstimate;
    }



    AutoState currentAutoState = AutoState.IDLE;

    enum AutoState{
        SPLINE1,
        SPLINE2,
        SPLINE3,
        IDLE
    }

    public void newState(AutoState newState){
        if(newState!= currentAutoState){
            currentAutoState = newState;
        }
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
