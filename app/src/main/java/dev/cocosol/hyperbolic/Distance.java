/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic;

public class Distance {
    /// This method returns the hyperbolic distance between a point and the center
    public static double hyperbolicDistanceToCenter(Point point) {
        double euclideanDist = Distance.euclideanDistanceToCenter(point);
        double euclideanDistSquared = Math.pow(euclideanDist, 2);
        return HyperbolicMath.acosh(1 + 2 * (euclideanDistSquared / (1 - euclideanDistSquared)));
    }

    /// This method returns the Euclidean distance between two points
    /// It basically returns the length of the vector between the two points
    public static double euclideanDistance(Point point1, Point point2) {
        return Math.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
    }

    /// This method returns the Euclidean distance to the center
    /// It basically returns the length of the vector between the point and the center
    public static double euclideanDistanceToCenter(Point point) {
        return Math.sqrt(point.x * point.x + point.y * point.y);
    }

    /// This method returns the hyperbolic distance between two points
    public static double hyperbolicDistance(Point point1, Point point2) {
        // TODO: Better explanation of the formula and renaming of variables
        double euclideanDist = Distance.euclideanDistanceToCenter(point1.minus(point2));
        double eucDist1 = Distance.euclideanDistanceToCenter(point1);
        double eucDist2 = Distance.euclideanDistanceToCenter(point2);

        double numerator = Math.pow(euclideanDist, 2);
        double denominator = (1 - Math.pow(eucDist1, 2)) * (1 - Math.pow(eucDist2, 2));

        return HyperbolicMath.acosh(1 + 2 * (numerator / denominator));
    }
}