package src;

public class HyperbolicMath {
    /// The cosines hyperbolic function
    public static double cosh(double x) {
        return (Math.exp(x) + Math.exp(-x))/2;
    }

    /// The sine hyperbolic function
    public static double sinh(double x) {
        return (Math.exp(x) - Math.exp(-x))/2;
    }

    /// The tangent hyperbolic function
    public static double tanh(double x) {
        return (Math.exp(x) - Math.exp(-x))/(Math.exp(x) + Math.exp(-x));
    }

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
