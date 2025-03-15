package src;

/// A simple geodesic
///
/// In the Poincaré disk model a geodesic can be described by a circle orthogonal to the unit circle.
/// So it can be described by an equation x² + y² + ax + by + 1 = 0.
///
/// TODO: This geodesic do not handle diameters yet.
public class Geodesic {
    /// One parameter of the geodesic
    double a;

    /// One parameter of the geodesic
    double b;

    /// Constructor of the geodesic
    public Geodesic(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /// A constructor of a geodesic from two points
    ///
    /// ## Math
    /// It works by solving the system of equations x² + y² + ax + by + 1 = 0
    /// with (x,y) = u and (x,y) = v
    public static Geodesic from_two_points(Point u, Point v) {
        double A = -1 - (u.x * u.x) - (u.y * u.y);
        double B = -1 - (v.x * v.x) - (v.y * v.y);

        double det = (u.x * v.y) - (u.y * v.x);

        if (det == 0) {
            throw new IllegalArgumentException("u and v must be linearly independent");
        }

        return new Geodesic((A * v.y - B * u.y) / det, (B * u.x - A * v.x) / det);
    }

    /// A constructor of a geodesic from a point and a tangent
    ///
    /// ## Math
    /// It works by solving the system of equations :
    /// x² + y² + ax + by + 1 = 0 (with (x,y) = u)
    /// (t1,t2)⋅(x,y) = 0 (where (t1,t2) is the tangent and (x,y) a segment of the circle formed by the geodesic)
    public static Geodesic from_point_and_tangent(Point u, double t1, double t2) {
        double det = t1 * u.y - t2 * u.x;
        if (det == 0) {
            throw new IllegalArgumentException("t1 and t2 must be linearly independent");
        }

        double x = -2*(t1 * u.x + t2 * u.y) * u.y + (u.x*u.x + u.y*u.y +1)*t2;
        double y = 2*(t1 * u.x + t2 * u.y) * u.x - (u.x*u.x + u.y*u.y +1)*t1;

        return new Geodesic(x/det, y/det);
    }

    /// A constructor of a geodesic from a point and an angle
    ///
    /// ## Math
    /// It works by calling from_point_and_tangent.
    /// It creates a tangent vector by this way : (cos(angle), sin(angle))
    public static Geodesic from_point_and_angle(Point u, double angle) {
        return from_point_and_tangent(u, Math.cos(angle), Math.sin(angle));
    }

    public boolean is_on_geodesic(Point point) {
        // Due to imprecision of floating point numbers we need to use a tolerance
        // TODO: Check if this tolerance is coherent
        return (point.x*point.x + point.y*point.y + this.a * point.x + this.b * point.y + 1 < Constants.TOLERANCE);

    }
}
