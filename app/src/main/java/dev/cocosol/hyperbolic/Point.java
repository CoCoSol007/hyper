/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package dev.cocosol.hyperbolic;

import dev.cocosol.Complex;

/// A simple point
public class Point {
    public static Point ORIGIN = new Point(0, 0);
    public double x;
    public double y;

    /// Constructor for a point
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /// Conversion of a complex number to a point
    public static Point fromComplex(Complex z) {
        return new Point(z.re(), z.im());
    }

    /// Subtraction of two points
    public Point minus(Point other) {
        return Point.fromComplex(this.toComplex().minus(other.toComplex()));
    }

    /// Addition of two points
    public Point plus(Point other) {
        return Point.fromComplex(this.toComplex().plus(other.toComplex()));
    }

    /// Multiplication of a point by a real number
    public Point mul(double alpha) {
        return Point.fromComplex(this.toComplex().scale(alpha));
    }

    public double dot(Point other) {
        return this.x * other.x + this.y * other.y;
    }

    /// Conversion of a point to a complex number
    public Complex toComplex() {
        return new Complex(this.x, this.y);
    }

    @Override
    public String toString() {
        return String.format("( %.3f, %.3f )", x, y);
    }
}