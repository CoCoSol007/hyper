package src;

/// A geodesic segment
public class GeodesicSegment {
    /// The first point of the segment
    public Point start;

    /// The second point of the segment
    public Point end;

    /// The geodesic of the segment
    public Geodesic geodesic;

    /// Constructor of a geodesic segment
    public GeodesicSegment(double a, double b, Point start, Point end) {
        this.geodesic = new Geodesic(a, b);
        this.start = start;
        this.end = end;

        if (!geodesic.is_on_geodesic(start) || !geodesic.is_on_geodesic(end)) {
            throw new IllegalArgumentException("The points must be on the geodesic");
        }
    }

    /// Returns the length of the segment
    public double length() {
        return Distance.hyperbolic_distance(start, end);
    }

    /// A constructor of a geodesic segment from two points
    public static GeodesicSegment from_two_points(Point u, Point v) {
        if (u.equals(v)) {
            throw new IllegalArgumentException("Points must be different");
        }
        Geodesic geodesic = Geodesic.from_two_points(u, v);
        return new GeodesicSegment(geodesic.a, geodesic.b, u, v);
    }

    /// Returns whether a point is contained in the segment
    public boolean contains(Point point) {
        return (
                geodesic.is_on_geodesic(point) &&
                Distance.hyperbolic_distance(point, start) <= this.length() &&
                Distance.hyperbolic_distance(point, end) <= this.length()
        );
    }
}
