package org.firstinspires.ftc.teamcode.Utilities;

public class Vector2d {

    //Vector constants
    final static Vector2d FORWARD = new Vector2d(0, 1),
            BACKWARD = new Vector2d(0, -1),
            LEFT = new Vector2d(-1, 0),
            RIGHT = new Vector2d(1, 0),
            ZERO = new Vector2d(0, 0);

    private double x;
    private double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
        this.fixFloatingPointErrors();
    }

    //makes a unit vector with a certain angle
    public Vector2d(Angle angle, double magnitude) {
        this.x = angle.cos() * magnitude;
        this.y = angle.sin() * magnitude;
        this.fixFloatingPointErrors();
    }

    public Vector2d(Angle angle){
        this(angle, 1);
    }

    public Vector2d(double angle){
        this(new Angle(angle, Angle.Direction.UP), 1);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX (double x) { this.x = x; }

    public void setY (double y) { this.y = y; }

    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public void fixFloatingPointErrors() {
        if (Math.abs(this.x) < 1e-5) {
            this.x = 0;
        }
        if (Math.abs(this.y) < 1e-5) {
            this.y = 0;
        }
    }

    //returns Angle object
    public Angle getAngle() {
        return new Angle(MathUtils.degATan(y, x));
    }

    //returns numerical value for angle in specified type
    public double getAngleDouble() {
        return MathUtils.degATan(y, x);
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.getX(), y + other.getY());
    }

    public Vector2d scaleBy(double scale) {
        return new Vector2d(getX() * scale, getY() * scale);
    }

    public Vector2d getUnitVector() {
        return scaleTo(1);
    }

    //returns a Vector2d in the same direction with magnitude of "target"
    public Vector2d scaleTo(double target) {
        if (getMagnitude() == 0) return ZERO; //avoid dividing by zero
        return scaleBy(target / getMagnitude());
    }

    //returns Vector2d rotated by ang degrees
    public Vector2d rotateBy(double angle) {
        return new Vector2d(x * MathUtils.degCos(angle) - y * MathUtils.degSin(angle), x * MathUtils.degSin(angle) + y * MathUtils.degCos(angle));
    }

    //returns Vector2d with the same magnitude as this but at the same angle as an Angle object
    public Vector2d rotateTo (Angle ang) {
        return new Vector2d(ang).scaleBy(this.getMagnitude());
    }

    //dot product
    public double dot(Vector2d other) {
        return getX() * other.getX() + getY() * other.getY();
    }

    //returns Vector2d reflected into 1st quadrant
    public Vector2d abs() {
        return new Vector2d(Math.abs(x), Math.abs(y));
    }

    //flips the signs of both components
    public Vector2d reflect () {
        return new Vector2d(-x, -y);
    }

    //projection of current vector onto v
    public Vector2d projection (Vector2d v) {
        return v.scaleBy(dot(v)/(Math.pow(v.getMagnitude(), 2))); // u dot v over mag(v)^2 times v
    }

    //normalizes a group of vectors so that they maintain the same relative magnitudes and ...
    // the vector of largest magnitude now has a magnitude equal to limit
    public static Vector2d[] batchNormalize(double limit, Vector2d... vecs) {
        double maxMag = 0;
        for (Vector2d v : vecs) {
            if (v.getMagnitude() > maxMag) {
                maxMag = v.getMagnitude();
            }
        }
        if (limit >= maxMag) {
            return vecs;
        }
        Vector2d[] normed = new Vector2d[vecs.length];
        for (int i = 0; i < vecs.length; i++) {
            normed[i] = vecs[i].scaleBy(limit / maxMag);
        }
        return normed;
    }


    public Vector2d clone() {
        return new Vector2d(x,y);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Vector2d)) {
            return false;
        }
        Vector2d other = (Vector2d) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }
}

