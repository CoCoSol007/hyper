/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import dev.cocosol.Complex;
import dev.cocosol.Point;

/**
 * Represents a rotation transformation in the hyperbolic plane.
 * <p>
 * The rotation is defined by an angle (in radians).
 * It supports origin-centered transformations.
 */
public class Rotation {

    /**
     * The angle of rotation in radians.
     */
    public double theta;

    /**
     * Constructs a new {@code Rotation} with the specified angle.
     *
     * @param theta the angle of rotation in radians
     */
    public Rotation(final double theta) {
        this.theta = theta;
    }

    /**
     * Applies this rotation to a given point.
     * <p>
     * The rotation center is the origin, a simplified formula is used.
     *
     * @param point the point to rotate
     * @return the rotated point
     */
    public Point apply(final Point point) {
        Complex z = point.toComplex();
        Complex e = Complex.exponent(1, theta);
        return Point.fromComplex(e.times(z));
    }
}
