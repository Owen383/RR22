package org.firstinspires.ftc.teamcode.PhotonCore.Caching;

import com.qualcomm.hardware.lynx.LynxModule;

import org.firstinspires.ftc.teamcode.PhotonCore.CacheIntent;
import org.firstinspires.ftc.teamcode.PhotonCore.Commands.LynxStandardCommandV2;
import org.firstinspires.ftc.teamcode.PhotonCore.Commands.V2.LynxGetADCCommand;

public class MotorCurrentCacheIntent extends CacheIntent {
    private LynxModule module;
    private LynxStandardCommandV2 commandV2;

    public MotorCurrentCacheIntent(long runDelayMs, LynxModule module, int motorPort) {
        super(runDelayMs);
        this.module = module;
        this.commandV2 = new LynxGetADCCommand(module, LynxGetADCCommand.Channel.motorCurrent(motorPort), LynxGetADCCommand.Mode.ENGINEERING);
    }

    @Override
    protected LynxStandardCommandV2 getCommand() {
        return commandV2;
    }
}
