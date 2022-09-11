package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.driver;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.operator;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.updateControllers;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.Intake;
import org.firstinspires.ftc.teamcode.Hardware.Slides;
import org.firstinspires.ftc.teamcode.Hardware.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.Utilities.Vector2d;

//@Disabled
@TeleOp(name="FUNNY TeleOp", group="Iterative Opmode")
public class PowerPlayTeleOp extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    double offset = 0;
    String offsetTelemetry = "UP";

    MecanumDrivetrain drivetrain;
    Intake intake;
    Slides slides;

    @Override
    public void init() {
        setOpMode(this);

        drivetrain = new MecanumDrivetrain();
        slides = new Slides();
        intake = new Intake();

        multTelemetry.addData("Status", "Initialized");
        multTelemetry.update();
    }


    @Override
    public void init_loop() {
        update();
        drivetrain.driveFullControl(new Vector2d(0,0), driver.leftStick.X(), .5, false);
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
        drivetrain.gyro.reset(offset);
        drivetrain.targetAngle = offset;
        runtime.reset();
        multTelemetry.addData("Status", "Started");
        multTelemetry.update();
    }


    @Override
    public void loop() {
        update();

        intake.stateMachine(operator.RT.press(), operator.LT.press());
        slides.stateMachine(operator.triangle.tap(), driver.LB.tap() || operator.square.tap());

        drivetrain.teleState(driver.rightStick.vec(), driver.leftStick.X(), driver.RT.range(.5, 1));


        if(Intake.rumble){ driver.rumble(); operator.rumble(); }
        loopTelemetry();

    }




    //PUT ALL UPDATE METHODS HERE

    private void update(){
        updateControllers();
        drivetrain.update();
        intake.update();
        slides.update();
    }

    //Telemetry to be displayed during init_loop()

    private void initTelemetry(){
        multTelemetry.addData("Status", "InitLoop");
        multTelemetry.addData("offset", offsetTelemetry);
        multTelemetry.addData("Z", drivetrain.gyro.yaw());
        multTelemetry.addData("Y", drivetrain.gyro.pitch());
        multTelemetry.addData("X", drivetrain.gyro.roll());
        multTelemetry.update();
    }

    //Telemetry to be displayed during loop()

    private void loopTelemetry(){
        multTelemetry.addData("slide extension", Slides.slideMotor.getCurrentPosition());
        multTelemetry.addData("Status", "TeleOp Running");
        multTelemetry.update();
    }
}