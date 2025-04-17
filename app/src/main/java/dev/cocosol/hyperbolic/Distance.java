/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

import dev.cocosol.Point;

/**
 * Provides methods for calculating distances in the hyperbolic plane.
 * <p>
 * The class includes methods for computing both Euclidean and hyperbolic distances,
 * including distances between points and from points to the center of the hyperbolic disk.
 */
public class Distance {

    /**
     * Returns the hyperbolic distance between a point and the origin (center of the hyperbolic disk).
     * <p>
     * This method uses the Euclidean distance to the center and applies the formula for
     * hyperbolic distance in the Poincaré disk model.
     *
     * @param point the point for which the hyperbolic distance to the origin is computed
     * @return the hyperbolic distance between the point and the center
     */
    public static double hyperbolicDistanceToCenter(final Point point) {
        double euclideanDistSquared = point.x * point.x + point.y * point.y;
        double intermediate = 1 + 2 * (euclideanDistSquared / (1 - euclideanDistSquared));
        if (intermediate < 1) {
            return Double.POSITIVE_INFINITY;
        }
        return HyperbolicMath.acosh(intermediate);
    }

    /**
     * Returns the Euclidean distance between two points.
     * <p>
     * This method computes the length of the vector between the two points in the
     * Euclidean plane.
     *
     * @param point1 the first point
     * @param point2 the second point
     * @return the Euclidean distance between the two points
     */
    public static double euclideanDistance(final Point point1, final Point point2) {
        return Math.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
    }

    /**
     * Returns the Euclidean distance between a point and the origin (center of the
     * hyperbolic disk).
     * <p>
     * This method computes the length of the vector from the point to the origin in
     * the Euclidean plane.
     *
     * @param point the point for which the Euclidean distance to the center is
     *              computed
     * @return the Euclidean distance between the point and the center
     */
    public static double euclideanDistanceToCenter(final Point point) {
        return Math.sqrt(point.x * point.x + point.y * point.y);
    }

    /**
     * Returns the hyperbolic distance between two points in the hyperbolic plane.
     * <p>
     * This method uses the Euclidean distance between the points and applies the
     * formula for
     * hyperbolic distance in the Poincaré disk model.
     *
     * @param point1 the first point
     * @param point2 the second point
     * @return the hyperbolic distance between the two points
     */
    public static double hyperbolicDistance(final Point point1, final Point point2) {
        double euclideanDist = Distance.euclideanDistanceToCenter(point1.minus(point2));
        double eucDist1 = Distance.euclideanDistanceToCenter(point1);
        double eucDist2 = Distance.euclideanDistanceToCenter(point2);

        double numerator = Math.pow(euclideanDist, 2);
        double denominator = (1 - Math.pow(eucDist1, 2)) * (1 - Math.pow(eucDist2, 2));

        return HyperbolicMath.acosh(1 + 2 * (numerator / denominator));
    }
}
