/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

/**
 * Provides mathematical functions for hyperbolic geometry operations.
 * <p>
 * This class includes implementations for hyperbolic trigonometric functions
 * (such as acosh, asinh, and atanh),
 * as well as methods for geometric operations like finding the inverse of a
 * point with respect to a geodesic.
 */
public class HyperbolicMath {

    /**
     * Computes the inverse hyperbolic cosine (acosh) of a given number.
     * <p>
     * This is the inverse function of the hyperbolic cosine function.
     *
     * @param x the value for which the inverse hyperbolic cosine is computed
     * @return the inverse hyperbolic cosine of {@code x}
     * @throws IllegalArgumentException if {@code x < 1}, as acosh is undefined for
     *                                  values less than 1
     */
    public static double acosh(final double x) {
        if (x < 1) {
            throw new IllegalArgumentException("acosh is only defined for x >= 1");
        }
        return Math.log(x + Math.sqrt(x * x - 1));
    }

    /**
     * Computes the inverse hyperbolic sine (asinh) of a given number.
     * <p>
     * This is the inverse function of the hyperbolic sine function.
     *
     * @param x the value for which the inverse hyperbolic sine is computed
     * @return the inverse hyperbolic sine of {@code x}
     */
    public static double asinh(final double x) {
        return Math.log(x + Math.sqrt(x * x + 1));
    }

    /**
     * Computes the inverse hyperbolic tangent (atanh) of a given number.
     * <p>
     * This is the inverse function of the hyperbolic tangent function.
     *
     * @param x the value for which the inverse hyperbolic tangent is computed
     * @return the inverse hyperbolic tangent of {@code x}
     * @throws IllegalArgumentException if {@code |x| >= 1}, as atanh is undefined
     *                                  for values greater than or equal to 1 in
     *                                  magnitude
     */
    public static double atanh(final double x) {
        if (Math.abs(x) >= 1) {
            throw new IllegalArgumentException("atanh is only defined for |x| < 1");
        }
        return 0.5 * Math.log((1 + x) / (1 - x));
    }
}
