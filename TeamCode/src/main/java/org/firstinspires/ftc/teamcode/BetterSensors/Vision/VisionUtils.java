package org.firstinspires.ftc.teamcode.BetterSensors.Vision;/*
package org.firstinspires.ftc.teamcode.BetterSensors.Vision;

import org.firstinspires.ftc.teamcode.Utilities.VisionDash;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.Utilities.VisionDash.dilate_const;
import static org.firstinspires.ftc.teamcode.Utilities.VisionDash.erode_const;
import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.min;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.GaussianBlur;
import static org.opencv.imgproc.Imgproc.RETR_TREE;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.contourArea;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.dilate;
import static org.opencv.imgproc.Imgproc.erode;
import static org.opencv.imgproc.Imgproc.findContours;

public class VisionUtils {

    public static double IMG_WIDTH = 432;
    public static double IMG_HEIGHT = 240;
    public static final double X_FOV = 72;
    public static final double Y_FOV = 43;
    public static final double CAMERA_HEIGHT = 0.19;
    public static final double BACK_CAMERA_HEIGHT = 0.19;

    // Note: All measurements in CM

    public static enum RECT_OPTION {
        AREA, WIDTH, HEIGHT, X, Y
    }
    public static enum AXES {
        X, Y
    }

    //TODO: Replace DuckDetector with this function, one the weirdness with Thresh has been troubleshooted (line on top and left of non-submatted image)
    */
/**
     * Designed to replace most existing non-specialized vision code. Takes in an image, converts it to the right colorspace, modifies it, thresholds it, and returns bounding rectangles, sorted by area. Uses VisionDash for blur, erosion, and dilation constants.
     * @param input
     * @param colorSpace
     * @param minThresh
     * @param maxThresh
     * @return
     *//*

    public static List<Rect> simpleDetect(Mat input, int colorSpace, Scalar minThresh, Scalar maxThresh){

        //Variable init
        Mat modified = new Mat();
        Mat thresholded = new Mat();
        Mat hierarchy = new Mat();

        //Color conversion to desired colorSpace
        cvtColor(input, modified, colorSpace);
        //Blur
        GaussianBlur(modified, modified, new Size(VisionDash.blur, VisionDash.blur), 0);

        //Thresholding
        inRange(modified, minThresh, maxThresh, thresholded);

        //Erode and dilate
        erode(thresholded, thresholded, new Mat(erode_const, erode_const, CV_8U));
        dilate(thresholded, thresholded, new Mat(dilate_const, dilate_const, CV_8U));

        //Get contours
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        findContours(thresholded, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE);

        //Retrieve rects
        List<Rect> rects = new ArrayList<>();
        for (int i=0; i < contours.size(); i++){
            Rect rect = boundingRect(contours.get(i));
            rects.add(rect);
        }

        //Check that rectangles have been found, return null if none for better error handling
        if(rects.size() < 1){
            return null;
        }

        //Sort rectangles and return sorted list
        rects = sortRectsByMaxOption(rects.size(), RECT_OPTION.AREA, rects);
        return rects;

    }

    */
/**
     * Designed to replace most existing non-specialized vision code. Takes in an image, converts it to the right colorspace, modifies it, thresholds it, and returns bounding rectangles, sorted by area. Uses VisionDash for blur, erosion, and dilation constants. This version uses numbers, not scalars, to determine thresholds.
     * @param input
     * @param colorSpace
     * @param min1
     * @param min2
     * @param min3
     * @param max1
     * @param max2
     * @param max3
     * @return
     *//*

    public static List<Rect> simpleDetect(Mat input, int colorSpace, int min1, int min2, int min3, int max1, int max2, int max3){

        final Scalar minThresh = new Scalar(min1, min2, min3);
        final Scalar maxTresh = new Scalar(max1, max2, max3);

        return simpleDetect(input, colorSpace, minThresh, maxTresh);
    }

    public static double pixels2Degrees(double pixels, AXES axe) {
        return (axe == AXES.X) ? pixels * (X_FOV / IMG_WIDTH) : pixels * (Y_FOV / IMG_HEIGHT);
    }

    */
/**
     * Sorts n number of rectangles by RECT_OPTION in ascending order
     * @param n
     * @param option
     * @param rects
     * @return
     *//*

    public static List<Rect> sortRectsByMinOption(int n, RECT_OPTION option, List<Rect> rects){
        List<Rect> sorted_rects = new ArrayList<>();
        for (int j=0; j < n; j++){
            int beta_index = getMinIndex(rects, option);
            sorted_rects.add(rects.get(beta_index));

            rects.remove(beta_index);
            if (rects.size() == 0) break;
        }
        return sorted_rects;
    }

    */
/**
     * Sorts n number of rectangles by RECT_OPTION in descending order
     * @param n
     * @param option
     * @param rects
     * @return
     *//*

    public static List<Rect> sortRectsByMaxOption(int n, RECT_OPTION option, List<Rect> rects){
        List<Rect> sorted_rects = new ArrayList<>();
        for (int j=0; j < n; j++){
            int alpha_i = getMaxIndex(rects, option);
            sorted_rects.add(rects.get(alpha_i));

            rects.remove(alpha_i);
            if (rects.size() == 0) break;
        }
        return sorted_rects;
    }


    */
/**
     * Sorts contours by x-coordinate in ascending order
     * @param contours
     * @return
     *//*

    public static int findLeftMostContourIndex(List<MatOfPoint> contours){
        int index = 0;
        double minX = Integer.MAX_VALUE;
        for (int i=0; i < contours.size(); i++){
            MatOfPoint cnt = contours.get(i);
            double x = boundingRect(cnt).x;
            if (x < minX) {
                minX = x;
                index = i;
            }
        }
        return index;
    }

    */
/**
     * Sorts n number of contours by x-coordinate in ascending order
     * @param n
     * @param contours
     * @return
     *//*

    public static List<MatOfPoint> findNLeftMostContours(int n, List<MatOfPoint> contours){
        List<MatOfPoint> widest_contours = new ArrayList<>();
        for (int j=0; j < n; j++){
            int largest_index = findLeftMostContourIndex(contours);
            widest_contours.add(contours.get(largest_index));

            contours.remove(largest_index);
            if (contours.size() == 0) break;
        }

        for (MatOfPoint cnt : contours){
            cnt.release();
        }

        return widest_contours;
    }

    public static int findWidestContourIndex(List<MatOfPoint> contours){
        int index = 0;
        double maxWidth = 0;
        for (int i=0; i < contours.size(); i++){
            MatOfPoint cnt = contours.get(i);
            double width = boundingRect(cnt).width;
            if (width > maxWidth) {
                maxWidth = width;
                index = i;
            }
        }
        return index;
    }

    public static List<MatOfPoint> findNWidestContours(int n, List<MatOfPoint> contours){
        List<MatOfPoint> widest_contours = new ArrayList<>();
        for (int j=0; j < n; j++){
            int largest_index = findWidestContourIndex(contours);
            widest_contours.add(contours.get(largest_index));

            contours.remove(largest_index);
            if (contours.size() == 0) break;
        }

        for (MatOfPoint cnt : contours){
            cnt.release();
        }

        return widest_contours;
    }

    public static int findLargestContourIndex(List<MatOfPoint> contours) {
        int index = 0;
        double maxArea = 0;
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint cnt = contours.get(i);
            double area = contourArea(cnt);
            if (area > maxArea) {
                maxArea = area;
                index = i;
            }
        }
        return index;
    }

    public static List<MatOfPoint> findNLargestContours(int n, List<MatOfPoint> contours) {
        List<MatOfPoint> new_contours = new ArrayList<>();

        for (int j = 0; j < n; j++) {
            int largest_index = findLargestContourIndex(contours);
            new_contours.add(contours.get(largest_index));

            contours.remove(largest_index);
            if (contours.size() == 0) break;
        }
        for (MatOfPoint cnt : contours){
            cnt.release();
        }
        return new_contours;
    }



    */
/**
     * Searches for the index of the item who's RECT_OPTION is the greatest
     * @param rects
     * @param option
     * @return
     *//*

    public static int getMaxIndex(List<Rect> rects, RECT_OPTION option){
        int alpha_index = 0;
        double max = Integer.MIN_VALUE;
        double cur = 0;
        for (int i=0; i < rects.size(); i++){

            switch (option){
                case X:
                    cur = rects.get(i).x;
                    break;

                case Y:
                    cur = rects.get(i).y;
                    break;

                case WIDTH:
                    cur = rects.get(i).width;
                    break;

                case HEIGHT:
                    cur = rects.get(i).height;
                    break;

                case AREA:
                    cur = rects.get(i).width * rects.get(i).height;
                    break;

            }
            if (cur > max) {
                max = cur;
                alpha_index = i;
            }
        }
        return alpha_index;
    }

    */
/**
     * Searches for the index of the item who's RECT_OPTION is the least
     * @param rects
     * @param option
     * @return
     *//*

    public static int getMinIndex(List<Rect> rects, RECT_OPTION option){
        int beta_index = 0;
        double min = Integer.MAX_VALUE;
        double cur = 0;
        for (int i=0; i < rects.size(); i++){

            switch (option){
                case X:
                    cur = rects.get(i).x;
                    break;

                case Y:
                    cur = rects.get(i).y;
                    break;

                case WIDTH:
                    cur = rects.get(i).width;
                    break;

                case HEIGHT:
                    cur = rects.get(i).height;
                    break;

                case AREA:
                    cur = rects.get(i).width * rects.get(i).height;
                    break;
            }
            if (cur < min) {
                min = cur;
                beta_index = i;
            }
        }
        return beta_index;
    }

}
*/
