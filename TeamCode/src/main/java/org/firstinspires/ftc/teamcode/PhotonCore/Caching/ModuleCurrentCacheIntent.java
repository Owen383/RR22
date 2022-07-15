package org.firstinspires.ftc.teamcode.PhotonCore.Caching;

import com.qualcomm.hardware.lynx.LynxModule;

import org.firstinspires.ftc.teamcode.PhotonCore.CacheIntent;
import org.firstinspires.ftc.teamcode.PhotonCore.Commands.LynxStandardCommandV2;
import org.firstinspires.ftc.teamcode.PhotonCore.Commands.V2.LynxGetADCCommand;

public class ModuleCurrentCacheIntent extends CacheIntent {
    private LynxModule module;
    private LynxStandardCommandV2 commandV2;

    public ModuleCurrentCacheIntent(long runDelayMs, LynxModule module) {
        super(runDelayMs);
        this.module = module;
        this.commandV2 = new LynxGetADCCommand(module, LynxGetADCCommand.Channel.BATTERY_CURRENT, LynxGetADCCommand.Mode.ENGINEERING);
    }

    @Override
    protected LynxStandardCommandV2 getCommand() {
        return commandV2;
    }
}
