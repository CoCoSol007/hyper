/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package src.hyperbolic;

import src.Complex;

/// A simple point
public class Point {
    public double x;
    public double y;

    public static Point ORIGIN = new Point(0,0);

    /// Constructor for a point
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /// Subtraction of two points
    public Point minus(Point other) {
        return Point.from_complex(this.to_complex().minus(other.to_complex()));
    }

    /// Addition of two points
    public Point plus(Point other) {
        return Point.from_complex(this.to_complex().plus(other.to_complex()));
    }

    /// Multiplication of a point by a real number
    public Point mul(double alpha) {
        return Point.from_complex(this.to_complex().scale(alpha));
    }

    public double dot(Point other) {
        return this.x * other.x + this.y * other.y;
    }

    /// Conversion of a point to a complex number
    public Complex to_complex() {
        return new Complex(this.x, this.y);
    }

    /// Conversion of a complex number to a point
    public static Point from_complex(Complex z) {
        return new Point(z.re(), z.im());
    }
}
