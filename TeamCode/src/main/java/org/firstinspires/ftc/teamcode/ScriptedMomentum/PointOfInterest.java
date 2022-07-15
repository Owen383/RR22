package org.firstinspires.ftc.teamcode.ScriptedMomentum;

public class PointOfInterest {

    private double x;
    private double y;

    private double decisiveRadius;
    private Modes.Mode activatedMode;

    private double decisiveWeightedAngle;
    private Modes.Mode routeActivatedMode;

    private double shiftedX;
    private double shiftedY;
    private double weightedAngle;
    private double distance;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getDecisiveRadius() {
        return decisiveRadius;
    }

    public Modes.Mode getActivatedMode() {
        return activatedMode;
    }

    public double getDecisiveWeightedAngle() {
        return decisiveWeightedAngle;
    }

    public Modes.Mode getRouteActivatedMode() {
        return routeActivatedMode;
    }

    public double getShiftedX() {
        return shiftedX;
    }

    public double getShiftedY() {
        return shiftedY;
    }

    public double getWeightedAngle() {
        return weightedAngle;
    }

    public double getDistance() {
        return distance;
    }

    public void setShiftedX(double shiftedX) {
        this.shiftedX = shiftedX;
    }

    public void setShiftedY(double shiftedY) {
        this.shiftedY = shiftedY;
    }

    public void setWeightedAngle(double weightedAngle) {
        this.weightedAngle = weightedAngle;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public PointOfInterest(double x, double y, double decisiveRadius, Modes.Mode activatedModes, double decisiveWeightedAngle, Modes.Mode routeActivatedModes) {
        this.x = x;
        this.y = y;
        this.decisiveRadius = decisiveRadius;
        this.activatedMode = activatedModes;
        this.decisiveWeightedAngle = decisiveWeightedAngle;
        this.routeActivatedMode = routeActivatedModes;
        this.shiftedX = 0;
        this.shiftedY = 0;
        this.weightedAngle = 0;
        this.distance = 0;
    }
}
