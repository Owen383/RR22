package org.firstinspires.ftc.teamcode.TeleOp;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.multTelemetry;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.setOpMode;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.updateControllers;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@Disabled
@TeleOp(name="DEFAULTIterative TeleOp", group="Iterative Opmode")
public class Default18438TeleOp extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        setOpMode(this);

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