package org.firstinspires.ftc.teamcode.ScriptedMomentum;

import static org.firstinspires.ftc.teamcode.Utilities.MathUtils.distance2D;

import org.firstinspires.ftc.teamcode.Utilities.MathUtils;

public class ScriptedMomentum {

    public static boolean pointActive = false;
    public static boolean pointRouting = false;
    public static PointOfInterest currentPOI;

    public static PointOfInterest[] POIs = {

            new PointOfInterest(5, 3, 50, Modes.Mode.DEFAULT, 6, Modes.Mode.EMPTY),
            new PointOfInterest(10, 3, 50, Modes.Mode.DEFAULT, 6, Modes.Mode.EMPTY),
            new PointOfInterest(5, 7, 50, Modes.Mode.DEFAULT, 6, Modes.Mode.EMPTY)

    };

    public static void init(){

    }

    public static void update(double thumbAngle, double thumbDistance){
        double x = 5; //determined by measuring robot pos. accurate every time i tried it from the same pos. //RobotState.robotPose.getX();
        double y = 5; //RobotState.robotPose.getY();

        //shift POIs
        for (int i = 0; i < POIs.length; i++) {
            POIs[i].setShiftedX(POIs[i].getX() - x);
            POIs[i].setShiftedY(POIs[i].getY() - y);
        }

        //weighted distance and distance calculation
        for (int i = 0; i < POIs.length; i++) {
            POIs[i].setDistance(Math.abs(distance2D(x, y, POIs[i].getX(), POIs[i].getY())));

            POIs[i].setWeightedAngle(
                    MathUtils.angleFromOrigin(POIs[i].getShiftedX(), POIs[i].getShiftedY())
                    / (POIs[i].getDistance() * .05)
            );
        }

        //sorting
        int lowestWeightedAngleIndex = 0;
        int lowestDistanceIndex = 0;
        for (int i = 0; i < POIs.length; i++) {
            if(POIs[i].getWeightedAngle() < POIs[lowestWeightedAngleIndex].getWeightedAngle()){
                lowestWeightedAngleIndex = i;
            }
            if(POIs[i].getDistance() < POIs[lowestDistanceIndex].getDistance()){
                lowestDistanceIndex = i;
            }
        }

        //checking radius
        if(POIs[lowestDistanceIndex].getDistance() <= POIs[lowestDistanceIndex].getDecisiveRadius() && thumbDistance < .02){
            pointActive = true;
            pointRouting = false;
            currentPOI = POIs[lowestDistanceIndex];
            Modes.activeMode = currentPOI.getActivatedMode();
        }else{
            currentPOI = POIs[lowestWeightedAngleIndex];
            pointActive = false;
            if(currentPOI.getWeightedAngle() <= currentPOI.getDecisiveWeightedAngle()){
                pointRouting = true;
                Modes.activeMode = currentPOI.getRouteActivatedMode();
            }else{
                pointRouting = false;
            }
        }

    }
}
