/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

import dev.cocosol.Point;

/**
 * Represents a geodesic in the Poincaré disk model of hyperbolic geometry.
 * 
 * A geodesic in this model is represented by a circle orthogonal to the unit
 * circle,
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
    public boolean diameter;

    /**
     * Constructor for creating a geodesic from the parameters {@code a} and
     * {@code b}.
     *
     * @param a the first parameter of the geodesic equation
     * @param b the second parameter of the geodesic equation
     */
    public Geodesic(final double a, final double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Constructs a geodesic from two points in the hyperbolic plane.
     * 
     * The geodesic is determined by solving the system of equations for the points
     * {@code u} and {@code v}.
     *
     * @param u the first point
     * @param v the second point
     * @return the resulting geodesic defined by the points {@code u} and {@code v}
     */
    public static Geodesic fromTwoPoints(final Point u, final Point v) {
        final double det = (u.x * v.y) - (u.y * v.x);

        if (Math.abs(det) < 0.000001) {
            // The geodesic is a diameter
            final Geodesic geodesic = new Geodesic(u.y - v.y, v.x - u.x);
            geodesic.diameter = true;
            return geodesic;
        }

        final double A = -1 - (u.x * u.x) - (u.y * u.y);
        final double B = -1 - (v.x * v.x) - (v.y * v.y);

        return new Geodesic((A * v.y - B * u.y) / det, (B * u.x - A * v.x) / det);
    }

    /**
     * Determines if a given point lies on the geodesic.
     * 
     * This method checks if the point satisfies the equation of the geodesic.
     *
     * @param point the point to check
     * @return {@code true} if the point lies on the geodesic, {@code false}
     *         otherwise
     */
    public boolean isOnGeodesic(final Point point) {
        if (this.diameter) {
            return this.a * point.x + this.b * point.y == 0;
        }
        // Due to imprecision of floating point numbers, we use a tolerance for
        // comparison
        return Math.abs(point.x * point.x + point.y * point.y + this.a * point.x + this.b * point.y + 1) < 0.000001;
    }

    /**
     * Returns the center of the Euclidean circle that represents the geodesic.
     * 
     * Returns {@code null} if the geodesic is a diameter, as a diameter has no
     * well-defined center.
     *
     * @return the center of the Euclidean circle or {@code null} if the geodesic is
     *         a diameter
     */
    public Point getEuclideanCenter() {
        if (this.diameter) {
            return null;
        }
        return new Point(-this.a / 2, -this.b / 2);
    }

    /**
     * Returns the Euclidean radius of the Euclidean circle that represents the
     * geodesic.
     * 
     * Returns {@code -1} if the geodesic is a diameter, as a diameter has no
     * well-defined radius.
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
     * 
     * The ideal points are the intersection of the geodesic with the Euclidean unit
     * circle.
     * If the geodesic is a diameter, it returns the two points of intersection.
     *
     * @return an array of two {@link Point}s representing the ideal points, or
     *         {@code null} if the geodesic is a diameter
     */
    public Point[] getIdealPoints() {
        if (this.diameter) {
            // Solve the quadratic equation to get the ideal points for a diameter
            final double delta = 4 * ((this.a * this.a) / (this.b * this.b)) + 4;
            final double x1 = Math.sqrt(delta) / ((this.a * this.a) / (this.b * this.b) + 1);
            final double y1 = (this.a * x1) / this.b;
            return new Point[] { new Point(x1, y1), new Point(-x1, -y1) };
        }

        // Solve the quadratic equation to get the ideal points for a non-diameter
        // geodesic
        final double delta = 16 * this.a * this.a - 4 * (this.b * this.b + this.a * this.a) * (4 - this.b * this.b);

        final double x1 = (-4 * this.a - Math.sqrt(delta)) / ((this.a * this.a + this.b * this.b) * 2);
        final double y1 = -(2 + this.a * x1) / this.b;

        final double x2 = (-4 * this.a + Math.sqrt(delta)) / ((this.a * this.a + this.b * this.b) * 2);
        final double y2 = -(2 + this.a * x2) / this.b;

        return new Point[] { new Point(x1, y1), new Point(x2, y2) };
    }
}
