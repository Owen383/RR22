package org.firstinspires.ftc.teamcode.Utilities;

import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.Alliance.BLUE;
import static org.firstinspires.ftc.teamcode.Utilities.OpModeUtils.Alliance.RED;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BetterSensors.SensorFusion.Enums.AutoType;
import org.firstinspires.ftc.teamcode.Hardware.Controller;

public class OpModeUtils {

    public static HardwareMap hardwareMap;
    public static OpMode opMode;

    public static Telemetry telemetry;
    public static FtcDashboard dashboard = FtcDashboard.getInstance();
    public static Telemetry dashboardTelemetry = dashboard.getTelemetry();
    public static MultipleTelemetry multTelemetry;

    public static Controller driver, operator;

    public static Alliance currentAlliance = BLUE;
    public static AutoType autoType = AutoType.FORWARD;

    public static void setBlueAlliance(){ currentAlliance = BLUE; }

    public static void setRedAlliance(){ currentAlliance = RED; }


    private static boolean isLinearOpMode;

    // Only use if it is in fact a LinearOpMode
    public static LinearOpMode linearOpMode = null;

    /**
     * Sets the OpMode
     * @param opMode
     */
    public static void setOpMode(OpMode opMode) {
        OpModeUtils.opMode = opMode;
        hardwareMap = opMode.hardwareMap;
        telemetry = opMode.telemetry;
        telemetry.setMsTransmissionInterval(5);
        multTelemetry = new MultipleTelemetry(telemetry, dashboardTelemetry);

        driver = new Controller(opMode.gamepad1);
        operator = new Controller(opMode.gamepad2);

        isLinearOpMode = (opMode instanceof LinearOpMode);
        if (isLinearOpMode) {
            linearOpMode = (LinearOpMode) opMode;
        }
    }

    public static void updateControllers(){
        driver.update(); operator.update();
    }


    public static boolean isActive(){
        if (isLinearOpMode) return linearOpMode.opModeIsActive();
        return true;
    }

    /**
     * I'm lazy
     * @param o
     */
    public static void print(Object o){
        System.out.println(o);
    }


    public enum Alliance{ BLUE, RED }

}