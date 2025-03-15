package src;

/// A simple point
public class Point {
    double x;
    double y;

    /// Constructor for a point
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /// Subtraction of two points
    public Point subtraction(Point other) {
        return new Point(this.x - other.x,this.y - other.y);
    }

    /// Addition of two points
    public Point addition(Point other) {
        return new Point(this.x + other.x,this.y + other.y);
    }
}
