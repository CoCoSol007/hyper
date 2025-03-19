package src.hyperbolic.transformation;

import src.Complex;
import src.hyperbolic.Point;

/// A class for rotations in the hyperbolic plane
public class Rotation {
    /// theta is the angle of rotation in radians
    double theta;

    /// center is the center of rotation
    Point center;

    /// Constructor
    public Rotation(double theta, Point center) {
        this.theta = theta;
        this.center = center;
    }

    /// Applies the rotation with the given point
    public Point apply(Point point) {
        Complex z = point.to_complex();
        Complex p = center.to_complex();
        Complex e = Complex.exponent(1, theta);

        // special case: rotation around the origin
        if (center == Point.ORIGIN) {
            return Point.from_complex(e.times(z));
        }

        // If the center is not at the origin, we first translate the space to map the center to the origin,
        // perform the rotation in the standard way,
        // and then apply the inverse translation to restore the original position.

        // a = z - p
        Complex a = z.minus(p);
        // b = 1 - conj(p)*z
        Complex b = Complex.ONE.minus(p.conjugate().times(z));
        // num = e*a + p*b
        Complex num = e.times(a).plus(p.times(b));
        // den = 1 - conj(p)*z + e*conj(p)*a
        Complex den = Complex.ONE.minus(p.conjugate().times(z)).plus(e.times(p.conjugate().times(a)));

        return Point.from_complex(num.divides(den));
    }
}