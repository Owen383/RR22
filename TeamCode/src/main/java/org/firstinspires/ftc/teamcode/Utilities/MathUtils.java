package org.firstinspires.ftc.teamcode.Utilities;

import static java.lang.Math.abs;
import static java.lang.Math.floorMod;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;

public class MathUtils {

    public static double deadZone(double number, double deadZone){

        return number < deadZone && number > -deadZone ? 0 : number;

    }

    public static double sigmoid(double rawNumber, double range, double outputRange){
        return (outputRange / (1 + Math.pow(range, -rawNumber))) - outputRange / 2;
    }

    public static double degsToRads(Double theta){
        return theta * (Math.PI / 180);
    }
    
    public static double radsToDegs(Double theta){ return theta * (180 / Math.PI); }
    
    
    public static double degSin(double theta){ return Math.sin(degsToRads(theta)); }
    
    public static double degCos(double theta){ return Math.cos(degsToRads(theta)); }
    
    public static double degTan(double theta){ return Math.tan(degsToRads(theta)); }
    
    public static double degASin(double sin){ return radsToDegs(Math.asin(sin)); }
    
    public static double degATan(double opposite, double adjacent){ return radsToDegs(Math.atan2(opposite, adjacent)); }


    public static double mod(double value, int base){
        return (value % base + base) % base;
    }
    
    public static double pow(double value, double exponent) {
        if(value == 0) return 0;
        else return Math.pow(abs(value), exponent) * (value / abs(value));
    }

    public static double absMax(double value1, double value2){
        return Math.max(abs(value1), abs(value2));
    }

    //TODO  I need to do a lot more documentation on my code as I write it -  so I can do a better job of maintenance
    //TODO Make these actually have input for accuracy like a normal function
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double floorModDouble(double dividend, double divisor){
        return floorMod(Math.round(dividend * 1e6), Math.round(divisor * 1e6)) / 1e6;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double floorModDouble(double dividend, int divisor){
        return floorMod(Math.round(dividend * 1e6), divisor) / 1e6;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static double floorModDouble(int dividend, double divisor){
        return floorMod(dividend, Math.round(divisor * 1e6)) / 1e6;

    }

    //calculates distance between two points
    public static double distance2D(double x1, double y1, double x2, double y2){
        return abs(Math.hypot(x2-x1, y2-y1));
    }


    public static double angleFromOrigin(double X, double Y){
        return ((270 - (Math.atan2(0 - Y, 0 - X)) * 180 / Math.PI) % 360);
    }

    /**
     * x1 and y1 should be for the current location, x2 and y2 for the target location
     * THIS IS FOR THE AUTO AND WONT WORK WITH TRADITIONAL COORDINATE SYSTEMS
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double angleBetweenPoints(double x1, double y1, double x2, double y2){
        double xShifted = x2 - x1;
        double yShifted = y2 - y1;
        return angleFromOrigin(xShifted, yShifted) + 180;
    }

    /*public static Point shift(Point p, double shiftAngle){
        double rawX = p.x;
        double rawY = p.y;
        double x = (rawX * Math.cos(Math.toRadians(shiftAngle))) - (rawY * Math.sin(Math.toRadians(shiftAngle)));
        double y = (rawX * Math.sin(Math.toRadians(shiftAngle))) + (rawY * Math.cos(Math.toRadians(shiftAngle)));
        return new Point(x, y);
    }*/

    public static boolean withinRange(double value, double min, double max){
        return (value >= min) && (value <= max);
    }

    public static double linearConversion(double value, double oldMin, double oldMax, double newMin, double newMax){
        double oldRange = (oldMax - oldMin);
        double newRange = (newMax - newMin);
        return (((value - oldMin) * newRange) / oldRange) + newMin;
    }

    public static double middle(double num1, double num2){
        double avg = (num1 + num2) / 2;
        return avg;
    }

    public static double median(ArrayList<Double> numArray){
        numArray.sort(Comparator.naturalOrder());
        double median;
        if(numArray.size() % 2 == 0){
            median = (numArray.get(numArray.size() / 2) + numArray.get(numArray.size() / 2 - 1)/ 2);
        }else{
            median = numArray.get(numArray.size() / 2);
        }
        return median;
    }

    public static double closestTarget(double targetAngle, double currentAngle, double mod){
        double simpleTargetDelta = StrictMath.floorMod(Math.round(((mod - targetAngle) + currentAngle) * 1e6), Math.round((mod + 0.000) * 1e6)) / 1e6;
        double alternateTargetDelta = -1 * (mod - simpleTargetDelta);
        return StrictMath.abs(simpleTargetDelta) <= StrictMath.abs(alternateTargetDelta) ? currentAngle - simpleTargetDelta : currentAngle - alternateTargetDelta;
    }


}
