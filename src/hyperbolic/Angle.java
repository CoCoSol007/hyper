package src.hyperbolic;

public class Angle {
    public static double from_two_geodesics(Geodesic a, Geodesic b) {
        Point[] ideal_points_a = a.get_ideal_points();
        Point[] ideal_points_b = b.get_ideal_points();
        Point u = ideal_points_a[0];
        Point v = ideal_points_a[1];
        Point s = ideal_points_b[0];
        Point t = ideal_points_b[1];

        double P =u.minus(v).dot(s.minus(t)) + u.dot(t) * v.dot(s) - u.dot(s) * v.dot(t);
        double Q = Math.pow(1-u.dot(v), 2);
        double R = Math.pow(1-s.dot(t), 2);
        return Math.acos(P / Math.sqrt(Q * R));
    }
}
