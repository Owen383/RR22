package org.firstinspires.ftc.teamcode.PhotonCore.Testing;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.BetterSensors.Sensors.ColorSensor;
import org.firstinspires.ftc.teamcode.PhotonCore.Caching.BulkReadCacheIntent;
import org.firstinspires.ftc.teamcode.PhotonCore.Caching.MotorCurrentCacheIntent;
import org.firstinspires.ftc.teamcode.PhotonCore.PhotonCore;
import org.firstinspires.ftc.teamcode.Utilities.OpModeUtils;

@TeleOp
public class PhotonExample extends LinearOpMode {

    double power = 0;
    long start = System.currentTimeMillis();
    long prev = System.currentTimeMillis();
    double sign = 1;
    DcMotorEx motor;
    DcMotor motor1;
    ColorSensor color1;
    ColorSensor color2;
    ColorSensor color3;

    @Override
    public void runOpMode() throws InterruptedException {
        OpModeUtils.setOpMode(this);
        PhotonCore.addCacheIntent(new BulkReadCacheIntent(5, PhotonCore.CONTROL_HUB)); //Cache bulk data every 5 ms
        PhotonCore.addCacheIntent(new MotorCurrentCacheIntent(30, PhotonCore.CONTROL_HUB, 0)); //Cache motor 0 current every 30 ms
        LynxModule module = hardwareMap.getAll(LynxModule.class).get(0);
        motor = (DcMotorEx) hardwareMap.dcMotor.get("motor1");
        motor1 = hardwareMap.dcMotor.get("motor2");
        color1 = new ColorSensor(0, "color1");
        color2 = new ColorSensor(0, "color2");
        color3 = new ColorSensor(0, "color3");


        while(!isStarted()){
            PhotonCore.disable();
            stressTestCode();
        }
        PhotonCore.enable(); //Enable PhotonCore
        power = 0;
        start = System.currentTimeMillis();
        sign = 1;
        while(opModeIsActive()){
            stressTestCode();
        }
    }

    private void stressTestCode() {
        long now = System.currentTimeMillis();

        double dt = (now - start) / 2000.0;

        power += dt * sign;

        motor.setPower(power);
        motor1.setPower(power);

        color1.update();
        color2.update();
        color3.update();

        start = now;

        if(Math.abs(power) > 0.2){
            sign = -sign;
        }

        long msStart = System.currentTimeMillis();
        double current = motor.getCurrent(CurrentUnit.MILLIAMPS);
        long msEnd = System.currentTimeMillis();

        telemetry.addData("dt", dt * 2000);
        telemetry.addData("Power", current);
        telemetry.addData("loopTime", now - prev);
        prev = now;
        telemetry.update();
    }
}
