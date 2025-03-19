package src.hyperbolic.transformation;

import src.Complex;
import src.hyperbolic.Point;

/// A class for translations in the hyperbolic plane
public class Translation{
    /// In the disk model, a translation is defined by a single point that the transformation maps to the origin
    /// For example the translation f defined by the point A, maps A to the origin ( f(A) = (0,0) )
    private static Point origin;

    /// Constructor
    public Translation(Point point) {
        origin = point;
    }

    /// Applies the translation with the given point
    public Point apply(Point point) {
        Complex a = origin.to_complex();
        Complex z = point.to_complex();
        return Point.from_complex(
                z.times(a).divides(Complex.ONE.minus(a.conjugate().times(z)))
        );
    }
}
