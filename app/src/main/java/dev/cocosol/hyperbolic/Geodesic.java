/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic;

/**
 * Represents a geodesic in the Poincaré disk model of hyperbolic geometry.
 * <p>
 * A geodesic in this model is represented by a circle orthogonal to the unit circle,
 * and it can be described by the equation {@code x² + y² + ax + by + 1 = 0}.
 * Additionally, geodesics can also represent diameters of the unit disk.
 */
public class Geodesic {
    
    /**
     * One parameter of the geodesic equation.
     */
    public double a;

    /**
     * One parameter of the geodesic equation.
     */
    public double b;

    /**
     * True if the geodesic is a diameter. 
     * If true, the geodesic is defined by the equation {@code ax + by = 0}.
     */
    boolean diameter = false;

    /**
     * Constructor for creating a geodesic from the parameters {@code a} and {@code b}.
     *
     * @param a the first parameter of the geodesic equation
     * @param b the second parameter of the geodesic equation
     */
    public Geodesic(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Constructs a geodesic from two points in the hyperbolic plane.
     * <p>
     * The geodesic is determined by solving the system of equations for the points {@code u} and {@code v}.
     *
     * @param u the first point
     * @param v the second point
     * @return the resulting geodesic defined by the points {@code u} and {@code v}
     */
    public static Geodesic fromTwoPoints(Point u, Point v) {
        double det = (u.x * v.y) - (u.y * v.x);

        if (Math.abs(det) < 0.000001) {
            // The geodesic is a diameter
            Geodesic geodesic = new Geodesic(u.y - v.y, v.x - u.x);
            geodesic.diameter = true;
            return geodesic;
        }

        double A = -1 - (u.x * u.x) - (u.y * u.y);
        double B = -1 - (v.x * v.x) - (v.y * v.y);

        return new Geodesic((A * v.y - B * u.y) / det, (B * u.x - A * v.x) / det);
    }

    /**
     * Constructs a geodesic from a point and a tangent vector.
     * <p>
     * The geodesic is determined by solving the system of equations involving the point {@code u}
     * and the tangent vector {@code (t1, t2)}.
     *
     * @param u the point that the geodesic passes through
     * @param t1 the first component of the tangent vector
     * @param t2 the second component of the tangent vector
     * @return the resulting geodesic defined by the point and tangent vector
     * @throws IllegalArgumentException if the tangent vector is null (i.e., {@code t1 == 0} and {@code t2 == 0})
     */
    public static Geodesic fromPointAndTangent(Point u, double t1, double t2) {
        if (t1 == 0 && t2 == 0) {
            throw new IllegalArgumentException("Vector tangent must not be null");
        }

        double det = t1 * u.y - t2 * u.x;
        if (det == 0) {
            // The geodesic is a diameter
            Geodesic geodesic = new Geodesic(-t2, t1);
            geodesic.diameter = true;
            return geodesic;
        }

        double x = -2 * (t1 * u.x + t2 * u.y) * u.y + (u.x * u.x + u.y * u.y + 1) * t2;
        double y = 2 * (t1 * u.x + t2 * u.y) * u.x - (u.x * u.x + u.y * u.y + 1) * t1;

        return new Geodesic(x / det, y / det);
    }

    /**
     * Constructs a geodesic from a point and an angle.
     * <p>
     * This method calls {@link #fromPointAndTangent(Point, double, double)} with a tangent vector
     * corresponding to the given angle.
     *
     * @param u the point that the geodesic passes through
     * @param angle the angle used to create the tangent vector (in radians)
     * @return the resulting geodesic defined by the point and angle
     */
    public static Geodesic fromPointAndAngle(Point u, double angle) {
        return fromPointAndTangent(u, Math.cos(angle), Math.sin(angle));
    }

    /**
     * Determines if a given point lies on the geodesic.
     * <p>
     * This method checks if the point satisfies the equation of the geodesic.
     *
     * @param point the point to check
     * @return {@code true} if the point lies on the geodesic, {@code false} otherwise
     */
    public boolean isOnGeodesic(Point point) {
        if (this.diameter) {
            return (this.a * point.x + this.b * point.y == 0);
        }
        // Due to imprecision of floating point numbers, we use a tolerance for comparison
        return (point.x * point.x + point.y * point.y + this.a * point.x + this.b * point.y + 1 < 0.000001);
    }

    /**
     * Returns the center of the Euclidean circle that represents the geodesic.
     * <p>
     * Returns {@code null} if the geodesic is a diameter, as a diameter has no well-defined center.
     *
     * @return the center of the Euclidean circle or {@code null} if the geodesic is a diameter
     */
    public Point getEuclideanCenter() {
        if (this.diameter) {
            return null;
        }
        return new Point(-this.a / 2, -this.b / 2);
    }

    /**
     * Returns the Euclidean radius of the Euclidean circle that represents the geodesic.
     * <p>
     * Returns {@code -1} if the geodesic is a diameter, as a diameter has no well-defined radius.
     *
     * @return the Euclidean radius or {@code -1} if the geodesic is a diameter
     */
    public double getEuclideanRadius() {
        if (this.diameter) {
            return -1;
        }
        return Math.sqrt(this.a * this.a + this.b * this.b - 4) / 2;
    }

    /**
     * Returns the ideal points of the geodesic.
     * <p>
     * The ideal points are the intersection of the geodesic with the Euclidean unit circle.
     * If the geodesic is a diameter, it returns the two points of intersection.
     *
     * @return an array of two {@link Point}s representing the ideal points, or {@code null} if the geodesic is a diameter
     */
    public Point[] getIdealPoints() {
        if (this.diameter) {
            // Solve the quadratic equation to get the ideal points for a diameter
            double delta = 4 * ((a * a) / (b * b)) + 4;
            double x1 = Math.sqrt(delta) / ((a * a) / (b * b) + 1);
            double y1 = (a * x1) / b;
            return new Point[]{new Point(x1, y1), new Point(-x1, -y1)};
        }

        // Solve the quadratic equation to get the ideal points for a non-diameter geodesic
        double delta = 16 * a * a - 4 * (b * b + a * a) * (4 - b * b);

        double x1 = (-4 * a - Math.sqrt(delta)) / ((a * a + b * b) * 2);
        double y1 = -(2 + a * x1) / b;

        double x2 = (-4 * a + Math.sqrt(delta)) / ((a * a + b * b) * 2);
        double y2 = -(2 + a * x2) / b;

        return new Point[]{new Point(x1, y1), new Point(x2, y2)};
    }
}
