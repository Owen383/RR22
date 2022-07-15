package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.updateControllers;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Localization.ThreeWheelOdometry;

@Disabled
@TeleOp(name="OdoExampleOp", group="Iterative Opmode")
public class OdoExampleOp extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private ThreeWheelOdometry odometry;
    private DcMotor leftEncoder;
    private DcMotor rightEncoder;
    private DcMotor horizontalEncoder;

    public FtcDashboard dashboard = FtcDashboard.getInstance(); //so we can output pos to dashboard



    @Override
    public void init() {
        setOpMode(this);

        leftEncoder = hardwareMap.get(DcMotor.class, "left");
        rightEncoder = hardwareMap.get(DcMotor.class, "right");
        horizontalEncoder = hardwareMap.get(DcMotor.class, "horizontal");

        odometry = new ThreeWheelOdometry(
                leftEncoder::getCurrentPosition,
                rightEncoder::getCurrentPosition,
                horizontalEncoder::getCurrentPosition
        );

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

        odometry.start();  //NEED TO DO THIS!!!!

        runtime.reset();
        multTelemetry.addData("Status", "Started");
        multTelemetry.update();
    }


    @Override
    public void loop() {
        update();
        //No need to call odo update here, it will automatically continue looping
        multTelemetry.addData("x", ThreeWheelOdometry.position.getX());
        multTelemetry.addData("y", ThreeWheelOdometry.position.getY());

        //this assumes we start in the center of the field, but good enough for now! output position to dashboard telemetry
        TelemetryPacket packet = new TelemetryPacket();
        packet.fieldOverlay()
                .setStrokeWidth(1)
                .setFill("black").fillCircle(ThreeWheelOdometry.position.getX() * 39.37, ThreeWheelOdometry.position.getY() * 39.37, 2);
        dashboard.sendTelemetryPacket(packet);

    }

    @Override
    public void stop(){

        odometry.endLoop();  //ONCE AGAIN, SUPER IMPORTANT!!!!!!

    }




    //PUT ALL UPDATE METHODS HERE

    private void update(){
        updateControllers();
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