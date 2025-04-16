/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import dev.cocosol.Complex;
import dev.cocosol.Point;

/**
 * Represents a translation transformation in the hyperbolic plane.
 * <p>
 * In the disk model, a translation is defined by a single point that the transformation maps to the origin.
 * For example, the translation {@code f} defined by the point {@code A}, maps {@code A} to the origin ({@code f(A) = (0,0)}).
 */
public class Translation {

    /** The point that is mapped to the origin under this translation. */
    private final Point origin;

    /**
     * Constructs a new {@code Translation} transformation defined by the given point.
     *
     * @param point the point that will be mapped to the origin
     */
    public Translation(Point point) {
        origin = point;
    }

    /**
     * Applies this translation to a given point.
     * <p>
     * The translation is implemented using a Möbius transformation that maps the given point
     * to a new position in the hyperbolic plane.
     *
     * @param point the point to translate
     * @return the translated point
     */
    public Point apply(Point point) {
        Complex a = origin.toComplex();
        Complex z = point.toComplex();
        return Point.fromComplex(
                z.minus(a).divides(Complex.ONE.minus(a.conjugate().times(z)))
        );
    }
}
