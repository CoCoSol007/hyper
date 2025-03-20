/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package src.hyperbolic;

public class HyperbolicMath {
    /// The argument cosines hyperbolic function
    public static double acosh(double x) {
        return Math.log(x + Math.sqrt(x*x - 1));
    }

    /// The argument sine hyperbolic function
    public static double asinh(double x) {
        return Math.log(x + Math.sqrt(x*x + 1));
    }

    /// The argument tangent hyperbolic function
    public static double atanh(double x) {
        return 0.5 * Math.log((1+x)/(1-x));
    }
}
