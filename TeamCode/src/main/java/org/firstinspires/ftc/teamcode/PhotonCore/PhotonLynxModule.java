package org.firstinspires.ftc.teamcode.PhotonCore;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxUnsupportedCommandException;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;

import org.firstinspires.ftc.teamcode.PhotonCore.Commands.LynxStandardCommandV2;

public class PhotonLynxModule extends LynxModule {

    public PhotonLynxModule(LynxUsbDevice lynxUsbDevice, int moduleAddress, boolean isParent, boolean isUserModule) {
        super(lynxUsbDevice, moduleAddress, isParent, isUserModule);
    }

    @Override
    public void sendCommand(LynxMessage command) throws InterruptedException, LynxUnsupportedCommandException {
        if(!PhotonCore.instance.enabled.get() || command instanceof LynxStandardCommandV2){
            super.sendCommand(command);
            return;
        }
        if(command instanceof LynxCommand){
            if(PhotonCore.getCacheResponse((LynxCommand)command) != null){
                ((LynxCommand) command).onResponseReceived(PhotonCore.getCacheResponse((LynxCommand)command));
                return;
            }
            if(PhotonCore.shouldAckImmediately((LynxCommand)command)){
                boolean success = PhotonCore.registerSend((LynxCommand) command);
                if(success) {
                    ((LynxCommand) command).onAckReceived(new LynxAck(command.getModule(), false));
                }else {
                    super.sendCommand(command);
                    ((LynxCommand) command).onAckReceived(new LynxAck(command.getModule(), false));
                }
                return;
            }
        }
        super.sendCommand(command);
    }

    public void normalSend(LynxMessage command) throws LynxUnsupportedCommandException, InterruptedException {
        super.sendCommand(command);
    }

    @Override
    public void acquireNetworkTransmissionLock(LynxMessage message) throws InterruptedException {
        if(!PhotonCore.instance.enabled.get() || message instanceof LynxStandardCommandV2){
            super.acquireNetworkTransmissionLock(message);
            return;
        }
        if(message instanceof LynxCommand){
            if(PhotonCore.getCacheResponse((LynxCommand)message) != null){
                return;
            }
            if(PhotonCore.shouldAckImmediately((LynxCommand) message)){
                return;
            }
        }
        super.acquireNetworkTransmissionLock(message);
    }

    @Override
    public void releaseNetworkTransmissionLock(LynxMessage message) throws InterruptedException {
        if(!PhotonCore.instance.enabled.get() || message instanceof LynxStandardCommandV2){
            super.releaseNetworkTransmissionLock(message);
            return;
        }
        if(message instanceof LynxCommand){
            if(PhotonCore.getCacheResponse((LynxCommand)message) != null){
                return;
            }
            if (PhotonCore.shouldAckImmediately((LynxCommand) message)) {
                return;
            }
        }
        super.releaseNetworkTransmissionLock(message);
    }

    public void forceReleaseNetworkTransmissionLock(LynxMessage message) throws InterruptedException {
        super.releaseNetworkTransmissionLock(message);
    }

    public void forceAcquireNetworkTransmissionLock(LynxMessage message) throws InterruptedException {
        super.acquireNetworkTransmissionLock(message);
    }
}
