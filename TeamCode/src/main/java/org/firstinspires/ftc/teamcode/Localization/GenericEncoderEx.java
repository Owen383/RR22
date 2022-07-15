package org.firstinspires.ftc.teamcode.Localization;

import java.util.function.IntSupplier;

public class GenericEncoderEx {
    private final IntSupplier rawTicks;
    private double distancePerPulse = 1.0;

    public GenericEncoderEx(IntSupplier rawTicks) {
        this.rawTicks = rawTicks;
    }

    public int getCurrentTicks() {
        return rawTicks.getAsInt();
    }

    public double getDistance(){
        return rawTicks.getAsInt() * distancePerPulse;
    }

    public void setDistancePerPulse(double distancePerPulse){
        this.distancePerPulse = distancePerPulse;
    }
}
