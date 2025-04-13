/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic;

import dev.cocosol.Complex;

/**
 * Represents a point in the hyperbolic plane.
 * <p>
 * A point is defined by its {@code x} and {@code y} coordinates. This class provides various operations
 * for manipulating points, including addition, subtraction, and multiplication by a scalar.
 */
public class Point {
    
    /**
     * The origin point (0, 0).
     */
    public static Point ORIGIN = new Point(0, 0);

    /**
     * The x-coordinate of the point.
     */
    public double x;

    /**
     * The y-coordinate of the point.
     */
    public double y;

    /**
     * Constructor for creating a point with given coordinates.
     *
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Converts a complex number to a point.
     * <p>
     * This method maps the real and imaginary parts of the complex number to the x and y coordinates of the point.
     *
     * @param z the complex number to convert
     * @return the corresponding point
     */
    public static Point fromComplex(Complex z) {
        return new Point(z.re(), z.im());
    }

    /**
     * Subtracts another point from this point.
     * <p>
     * This method computes the difference of the two points by converting them to complex numbers, 
     * subtracting them, and then converting the result back to a point.
     *
     * @param other the point to subtract
     * @return a new point representing the result of the subtraction
     */
    public Point minus(Point other) {
        return Point.fromComplex(this.toComplex().minus(other.toComplex()));
    }

    /**
     * Adds another point to this point.
     * <p>
     * This method computes the sum of the two points by converting them to complex numbers, 
     * adding them, and then converting the result back to a point.
     *
     * @param other the point to add
     * @return a new point representing the result of the addition
     */
    public Point plus(Point other) {
        return Point.fromComplex(this.toComplex().plus(other.toComplex()));
    }

    /**
     * Multiplies this point by a real scalar.
     * <p>
     * This method scales the point by the given scalar value. The operation is done in the complex plane,
     * where the point is first converted to a complex number, scaled, and then converted back to a point.
     *
     * @param alpha the scalar value to multiply the point by
     * @return a new point representing the result of the multiplication
     */
    public Point mul(double alpha) {
        return Point.fromComplex(this.toComplex().scale(alpha));
    }

    /**
     * Computes the dot product of this point and another point.
     * <p>
     * This method computes the Euclidean dot product, which is the sum of the products of the corresponding coordinates.
     *
     * @param other the other point to compute the dot product with
     * @return the dot product of the two points
     */
    public double dot(Point other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Converts this point to a complex number.
     * <p>
     * This method represents the point as a complex number where the x-coordinate is the real part 
     * and the y-coordinate is the imaginary part.
     *
     * @return the corresponding complex number
     */
    public Complex toComplex() {
        return new Complex(this.x, this.y);
    }

    /**
     * Returns a string representation of the point.
     * <p>
     * The string representation is formatted as {@code (x, y)}, where {@code x} and {@code y} are 
     * the coordinates of the point, rounded to three decimal places.
     *
     * @return a string representation of the point
     */
    @Override
    public String toString() {
        return String.format("( %.3f, %.3f )", x, y);
    }

    
/**
 * Compares this point to the specified object for equality.
 * <p>
 * Two points are considered equal if their {@code x} and {@code y} coordinates
 * are approximately equal within a tolerance of 0.000001.
 *
 * @param o the object to compare with
 * @return {@code true} if the specified object is a point with approximately
 * equal coordinates to this point, {@code false} otherwise
 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Math.abs(x - point.x) < 0.000001 && Math.abs(y - point.y) < 0.000001;
    }
}
