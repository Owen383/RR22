package org.firstinspires.ftc.teamcode.PhotonCore.Commands;

import com.qualcomm.hardware.lynx.commands.LynxMessage;

public interface ResponseCallback {
    void call(LynxMessage response);
}
