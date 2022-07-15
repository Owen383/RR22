package org.firstinspires.ftc.teamcode.Firewatch;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.TeleOp.Default18438TeleOp;

@Disabled
@TeleOp(name="SmokeTest", group="Iterative Opmode")
public class SmokeTest extends OpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    long cycleTime = System.currentTimeMillis();
    OpMode BaseOpMode = new Default18438TeleOp();
    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        BaseOpMode.init();
    }
    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
        BaseOpMode.init_loop();
    }
    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        BaseOpMode.start();
    }
    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        BaseOpMode.loop();
        Firewatch.checkMotorIntegrity();
        Firewatch.checkSensorIntegrity();
        Firewatch.checkHubIntegrity();
        Firewatch.readErrorStateOnLEDs();
    }
    @Override
    public void stop() {
        BaseOpMode.stop();
    }
}
