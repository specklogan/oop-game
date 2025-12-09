package org.gooseapple.core.math;

/**
 * Vector2 Helper class which makes things easier to work with
 */
public class Vector2 implements Cloneable {
    private double x;
    private double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public void add(Vector2 v) {
        this.x += v.x;
        this.y += v.y;
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    public Vector2 multiply(double scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 normalize() {
        double length = length();

        if (length == 0) {
            return new Vector2(0,0);
        } else {
            return new Vector2(x/length, y/length);
        }
    }

    public void zero() {
        this.x = 0.0;
        this.y = 0.0;
    }

    @Override
    public String toString() {
        return "X:" + this.x + " | Y:" + this.y;
    }

    @Override
    public Vector2 clone() {
        try {
            Vector2 clone = (Vector2) super.clone();

            clone.x = this.x;
            clone.y = this.y;

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
