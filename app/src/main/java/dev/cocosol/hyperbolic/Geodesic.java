/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package dev.cocosol.hyperbolic;

/// A simple geodesic
///
/// In the Poincaré disk model a geodesic can be described by a circle orthogonal to the unit circle.
/// So it can be described by an equation x² + y² + ax + by + 1 = 0.
public class Geodesic {
    /// One parameter of the geodesic
    public double a;

    /// One parameter of the geodesic
    public double b;

    /// True if the geodesic is a diameter
    /// If so the geodesic is defined by the equation ax+by = 0
    private boolean diameter = false;

    /// Constructor of the geodesic
    public Geodesic(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /// A constructor of a geodesic from two points
    ///
    /// ## Math
    /// It works by solving the system of equations x² + y² + ax + by + 1 = 0
    /// with (x,y) = u and (x,y) = v
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

    /// A constructor of a geodesic from a point and a tangent
    ///
    /// ## Math
    /// It works by solving the system of equations :
    /// x² + y² + ax + by + 1 = 0 (with (x,y) = u)
    /// (t1,t2)⋅(x,y) = 0 (where (t1,t2) is the tangent and (x,y) a segment of the circle formed by the geodesic)
    public static Geodesic fromPointAndTangent(Point u, double t1, double t2) {
        if (t1 != 0 || t2 != 0) {
            throw new IllegalArgumentException("Vector tangent must be not null");
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

    /// A constructor of a geodesic from a point and an angle
    ///
    /// ## Math
    /// It works by calling fromPointAndTangent.
    /// It creates a tangent vector by this way : (cos(angle), sin(angle))
    public static Geodesic fromPointAndAngle(Point u, double angle) {
        return fromPointAndTangent(u, Math.cos(angle), Math.sin(angle));
    }

    public boolean isOnGeodesic(Point point) {
        if (this.diameter) {
            return (this.a * point.x + this.b * point.y == 0);
        }
        // Due to imprecision of floating point numbers we need to use a tolerance : a = b <=> |a - b| < 0.000001
        // TODO: Check if this tolerance is coherent
        return (point.x * point.x + point.y * point.y + this.a * point.x + this.b * point.y + 1 < 0.000001);

    }

    /// This method returns the center of the Euclidean circle that represents the geodesic
    ///
    /// Returns null if the geodesic is a diameter
    public Point getEuclideanCenter() {
        if (this.diameter) {
            return null;
        }
        return new Point(-this.a / 2, -this.b / 2);
    }

    /// This method returns the Euclidean radius of the Euclidean circle that represents the geodesic
    ///
    /// Returns -1 if the geodesic is a diameter
    public double getEuclideanRadius() {
        if (this.diameter) {
            return -1;
        }
        return Math.sqrt(this.a * this.a + this.b * this.b - 4) / 2;
    }

    /// This method returns the ideal points of the geodesic
    /// The ideal points are the intersection of the geodesic and the Euclidean unit circle
    ///
    /// Returns null if the geodesic is a diameter
    public Point[] getIdealPoints() {
        if (this.diameter) {

            // Solve the quadratic equation to get the ideal points
            // Delta is always positive

            double delta = 4 * ((a * a) / (b * b)) + 4;
            double x1 = Math.sqrt(delta) / ((a * a) / (b * b) + 1);
            double y1 = (a * x1) / b;
            return new Point[]{new Point(x1, y1), new Point(-x1, -y1)};

        }

        // Solve the quadratic equation to get the ideal points
        // Delta is negative or equal to 0 if and only if the geodesic is not defined

        double delta = 16 * a * a - 4 * (b * b + a * a) * (4 - b * b);

        // First ideal point
        double x1 = (-4 * a - Math.sqrt(delta)) / ((a * a + b * b) * 2);
        double y1 = -(2 + a * x1) / b;

        // Second ideal point
        double x2 = (-4 * a + Math.sqrt(delta)) / ((a * a + b * b) * 2);
        double y2 = -(2 + a * x2) / b;

        return new Point[]{new Point(x1, y1), new Point(x2, y2)};
    }
}