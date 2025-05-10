/**
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import dev.cocosol.Point;
import dev.cocosol.hyperbolic.Geodesic;

/**
 * Implements the reflection transformation in hyperbolic geometry.
 */
public class Reflexion {

    final Geodesic geodesic;

    /**
     * Constructor for creating a Reflexion object with a given geodesic.
     * 
     * @param geodesic the geodesic with respect to which the inversion is performed
     */
    public Reflexion(final Geodesic geodesic) {
        this.geodesic = geodesic;
    }

    /**
     * Computes the inverse of a point with respect to a given geodesic in the
     * hyperbolic plane.
     * <p>
     * This operation reflects a point across a geodesic, which is a straight line
     * or arc in the hyperbolic plane.
     * 
     * @param point the point to be inverted
     * @return the inverted point
     * @throws IllegalArgumentException if the point lies at the center of the
     *                                  geodesic
     */
    public Point apply(final Point point) {
        double R = geodesic.getEuclideanRadius();
        Point c = geodesic.getEuclideanCenter();

        if (this.geodesic.diameter) {
            double a = geodesic.a;
            double b = geodesic.b;
            double xa = point.x;
            double ya = point.y;

            double dot = a * xa + b * ya;
            double denom = a * a + b * b;

            double x1 = xa - 2 * a * dot / denom;
            double y1 = ya - 2 * b * dot / denom;

            return new Point(x1, y1);
        }

        // For general geodesics
        double dx = point.x - c.x;
        double dy = point.y - c.y;
        double OM2 = dx * dx + dy * dy;

        if (OM2 == 0) {
            throw new IllegalArgumentException("The point must not be the center of the geodesic");
        }

        double xPrime = c.x + (R * R * dx) / OM2;
        double yPrime = c.y + (R * R * dy) / OM2;

        return new Point(xPrime, yPrime);
    }

}
