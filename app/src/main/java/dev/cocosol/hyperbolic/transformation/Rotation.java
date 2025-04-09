/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import dev.cocosol.Complex;
import dev.cocosol.hyperbolic.Point;

/**
 * Represents a rotation transformation in the hyperbolic plane.
 * <p>
 * The rotation is defined by an angle (in radians) and a center point.
 * It supports both origin-centered and arbitrary-center rotations using Möbius transformations.
 */
public class Rotation {

    /** The angle of rotation in radians. */
    public double theta;

    /** The center point around which the rotation is applied. */
    public Point center;

    /**
     * Constructs a new {@code Rotation} with the specified angle and center.
     *
     * @param theta  the angle of rotation in radians
     * @param center the center of rotation
     */
    public Rotation(double theta, Point center) {
        this.theta = theta;
        this.center = center;
    }

    /**
     * Applies this rotation to a given point.
     * <p>
     * If the rotation center is the origin, a simplified formula is used.
     * Otherwise, the function uses a Möbius transformation to compute the rotated point.
     *
     * @param point the point to rotate
     * @return the rotated point
     */
    public Point apply(Point point) {
        Complex z = point.toComplex();
        Complex p = center.toComplex();
        Complex e = Complex.exponent(1, theta);

        // Special case: rotation around the origin
        if (center == Point.ORIGIN) {
            return Point.fromComplex(e.times(z));
        }

        // General case: Möbius transformation for rotation around an arbitrary center
        Complex a = z.minus(p);
        Complex b = Complex.ONE.minus(p.conjugate().times(z));
        Complex num = e.times(a).plus(p.times(b));
        Complex den = Complex.ONE.minus(p.conjugate().times(z)).plus(e.times(p.conjugate().times(a)));

        return Point.fromComplex(num.divides(den));
    }
}
