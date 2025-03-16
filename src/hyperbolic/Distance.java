package src.hyperbolic;

public class Distance {
    /// This method returns the hyperbolic distance between a point and the center
    static public double hyperbolic_distance_to_center(Point point){
        double euclidean_dist = Distance.euclidean_distance_to_center(point);
        double euclidean_dist_squared =  Math.pow(euclidean_dist, 2);
        return HyperbolicMath.acosh(1 + 2 * (euclidean_dist_squared / (1- euclidean_dist_squared)));
    }

    /// This method returns the Euclidean distance between two points
    /// It basically returns the length of the vector between the two points
    static public double euclidean_distance(Point point1, Point point2) {
        return Math.sqrt((point1.x - point2.x)*(point1.x - point2.x) + (point1.y - point2.y)*(point1.y - point2.y));
    }

    /// This method returns the Euclidean distance to the center
    /// It basically returns the length of the vector between the point and the center
    static public double euclidean_distance_to_center(Point point) {
        return Math.sqrt(point.x*point.x + point.y*point.y);
    }

    /// This method returns the hyperbolic distance between two points
    static public double hyperbolic_distance(Point point1, Point point2){
        // ToDo: Better explanation of the formula and renaming of variables
        double euclidean_dist = Distance.euclidean_distance_to_center(point1.subtraction(point2));
        double euc_dist1 = Distance.euclidean_distance_to_center(point1);
        double euc_dist2 = Distance.euclidean_distance_to_center(point2);

        double numerator = Math.pow(euclidean_dist, 2);
        double denominator = (1 - Math.pow(euc_dist1, 2)) * (1 - Math.pow(euc_dist2, 2));

        return HyperbolicMath.acosh(1 + 2 * (numerator / denominator));
    }
}
