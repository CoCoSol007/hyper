/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import dev.cocosol.Complex;
import dev.cocosol.hyperbolic.Point;

/// A class for translations in the hyperbolic plane
public class Translation {
    /// In the disk model, a translation is defined by a single point that the transformation maps to the origin
    /// For example the translation f defined by the point A, maps A to the origin ( f(A) = (0,0) )
    private final Point origin;

    /// Constructor
    public Translation(Point point) {
        origin = point;
    }

    /// Applies the translation with the given point
    public Point apply(Point point) {
        Complex a = origin.toComplex();
        Complex z = point.toComplex();
        return Point.fromComplex(
                z.times(a).divides(Complex.ONE.minus(a.conjugate().times(z)))
        );
    }
}