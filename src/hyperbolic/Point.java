package src.hyperbolic;

/// A simple point
public class Point {
    public double x;
    public double y;

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

    public Point mul(double scalar) {
        return new Point(this.x * scalar, this.y * scalar);
    }
}
