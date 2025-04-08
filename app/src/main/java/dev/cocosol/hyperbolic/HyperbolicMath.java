/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic;

public class HyperbolicMath {
    /// The argument cosines hyperbolic function
    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x * x - 1));
    }

    /// The argument sine hyperbolic function
    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(x * x + 1));
    }

    /// The argument tangent hyperbolic function
    public static double atanh(double x) {
        return 0.5 * Math.log((1 + x) / (1 - x));
    }

    public static Point inverseWithRespectToGeodesic(Point point, Geodesic geodesic) {
        double R = geodesic.getEuclideanRadius();
        Point c = geodesic.getEuclideanCenter();

        if (geodesic.diameter) {
            double a = geodesic.a;
            double b = geodesic.b;
            double xa = point.x;
            double ya = point.y;
            double x1 = ((b * b - a * a) * xa - 2 * a * b * ya) / (a * a + b * b);
            double y1 = ((a * a - b * b) * ya + 2 * b * b * xa) / (a * a + b * b);
            return new Point(x1, y1);
        }
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