package org.firstinspires.ftc.teamcode.ScriptedMomentum;

public class Modes {

    public static Mode activeMode = Mode.EMPTY;

    public enum Mode{

        //system reserved modes
        DEFAULT,
        EMPTY,

        //custom modes
        INTAKE_ON,
        AUTO_AIM,
        HOLD_ROTATION

    }

}
