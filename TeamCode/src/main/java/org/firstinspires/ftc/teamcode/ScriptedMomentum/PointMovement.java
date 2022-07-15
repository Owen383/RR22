package org.firstinspires.ftc.teamcode.ScriptedMomentum;/*
package org.firstinspires.ftc.teamcode.ScriptedMomentum;


import org.firstinspires.ftc.teamcode.Utilities.MathUtils;
import org.opencv.core.Point;

public class PointMovement {

    public static Point steerToPoint(double targetX, double targetY, double currentX, double currentY){



        Point retPoint = new Point(0, (MathUtils.distance2D(targetX, targetY, currentX, currentY) * .01));

        double calcX = targetX - currentX;
        double calcY = targetY - currentY;
        double steeringAngle = MathUtils.angleFromOrigin(calcX, calcY);

        retPoint = MathUtils.shift(retPoint, steeringAngle);

        Point origin = new Point(0, 0);

        return (MathUtils.distance2D(targetX, targetY, currentX, currentY) * .01) < .1 ? origin : retPoint;

    }

}
*/
