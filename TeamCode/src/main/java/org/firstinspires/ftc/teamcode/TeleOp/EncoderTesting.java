package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.updateControllers;

import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BetterSensors.Sensors.AnalogEncoder;

//@Disabled
@TeleOp(name="obametry", group="Iterative Opmode")
public class EncoderTesting extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    public MotorEx encoder;
    public AnalogEncoder analogEncoder;

    public static final double WHEEL_DIAMETER = 2.0;
    // if needed, one can add a gearing term here
    public static final double TICKS_PER_REV = 8192;
    public static final double DISTANCE_PER_PULSE = Math.PI * WHEEL_DIAMETER / TICKS_PER_REV;


    @Override
    public void init() {
        setOpMode(this);

        encoder = new MotorEx(this.hardwareMap, "obamatry");
        encoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        analogEncoder = new AnalogEncoder(0, "analog");

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

        loopTelemetry();

    }




    //PUT ALL UPDATE METHODS HERE

    private void update(){
        updateControllers();
        analogEncoder.update();
    }

    //Telemetry to be displayed during init_loop()

    private void initTelemetry(){
        multTelemetry.addData("Status", "InitLoop");
        multTelemetry.update();
    }

    //Telemetry to be displayed during loop()

    private void loopTelemetry(){
        multTelemetry.addData("obamatry", encoder.getDistance());
        multTelemetry.addData("analog", analogEncoder.getAbsoluteAngle());
        multTelemetry.addData("Status", "TeleOp Running");
        multTelemetry.update();
    }
}