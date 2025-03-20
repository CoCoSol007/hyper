/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package src.hyperbolic;

/// A simple hyperbolic circle
///
/// In the Poincaré disk model a hyperbolic circle is a Euclidean circle with a different radius and center
public class Circle {
    /// The radius of the hyperbolic circle
    double radius;

    /// The center of the hyperbolic circle
    Point center;

    /// Constructor of a hyperbolic circle
    public Circle(Point center, double radius) {
        this.radius = radius;
        this.center = center;
    }

    /// This method returns the Euclidean radius of the hyperbolic circle
    public double get_euclidean_radius() {
        double euclidean_radius = Math.tanh(radius/2);
        double dist = Distance.euclidean_distance_to_center(center);
        return ((1-dist*dist)*euclidean_radius)/(1-dist*dist* Math.pow(euclidean_radius,2));
    }

    /// This method returns the Euclidean center of the hyperbolic circle
    public Point get_euclidean_center() {
        double euclidean_radius = Math.tanh(radius/2);
        double dist = Distance.euclidean_distance_to_center(center);
        return center.mul((1- Math.pow(euclidean_radius,2))/(1-dist*dist* Math.pow(euclidean_radius,2)));
    }

    /// A constructor of a hyperbolic circle from a Euclidean circle
    public static Circle from_euclidean_circle(Point center, double r) {
        double d = Distance.euclidean_distance_to_center(center);
        if (d + r >= 1) {
            throw new IllegalArgumentException("The circle is not contained in the hyperbolic disk");
        }
        // If the center of circle is the center of the hyperbolic disk the conversion is trivial
        if (d == 0) {
            double hyperbolic_radius = 2 * HyperbolicMath.atanh(r);
            return new Circle(center, hyperbolic_radius);
        }

        // We start by finding the value of α that satisfies the equation f(α) = 0
        // ToDo: Better explanation
        double lower = 0;
        double upper = 1;
        double alpha = 0;
        int iteration = 0;
        while (iteration < 100) {
            alpha = (lower + upper) / 2;
            double k = computeK(alpha, d);
            double fAlpha = ((1 - k * k * d * d) * alpha) / (1 - k * k * d * d * alpha * alpha) - r;
            if (Math.abs(fAlpha) < 1e-8) {
                break;
            }
            if (fAlpha > 0) {
                upper = alpha;
            } else {
                lower = alpha;
            }
            iteration++;
        }

        // Now we can compute the hyperbolic circle
        double hyperbolic_radius = 2 * HyperbolicMath.atanh(alpha);
        double k = computeK(alpha, d);
        Point hyperbolic_center = center.mul(k);
        return new Circle(hyperbolic_center, hyperbolic_radius);
    }

    private static double computeK(double alpha, double d) {
        return (- (1 - alpha * alpha) + Math.sqrt((1 - alpha * alpha) * (1 - alpha * alpha) + 4 * d * d * alpha * alpha))
                / (2 * d * d * alpha * alpha);
    }

    /// This method returns whether a point is contained in the hyperbolic circle
    public boolean contains(Point point) {
        return Distance.hyperbolic_distance(point, center) <= radius;
    }
}
