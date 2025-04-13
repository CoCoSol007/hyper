package dev.cocosol.hyperbolic.caster;

import dev.cocosol.hyperbolic.Distance;
import dev.cocosol.hyperbolic.Geodesic;
import dev.cocosol.hyperbolic.Point;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;

public class Ray {
    private static final int STEPS = 6;

    /* The end of the ray in Euclidean space, in fact, it does not mater if it is out of the Poincare Disk*/
    public Point end;

    public int seed;

    public Ray(double angle, int seed) {
        this.end = new Point(Math.cos(angle), -Math.sin(angle));
        this.seed = seed;
    }

    /*Code from  https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/  */
    private boolean ccw(Point a, Point b, Point c) {
        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x);
    }
    /**
     * The point C is the origin of the ray and so of the Poincare Disk and
     * the point D is the end of the ray
     *
     * @param a one of the vertices of the segment
     * @param b the other vertex
     * @return true if the segment intersects the ray in Euclidean space
    */
    private boolean intersectSegment(Point a, Point b) {
        Point C = new Point(0, 0);
        return ccw(a, C, end) != ccw(b, C, end) && ccw(a, b, C) != ccw(a, b, end);
    }

    /**
     * The Geodesic must intersect the ray
     */
    private double distanceToGeodesic(Geodesic geodesic) {
        double A, B, C, det, t;
        Point center = geodesic.getEuclideanCenter();
        double radius = geodesic.getEuclideanRadius();

        A = this.end.x * this.end.x + this.end.y * this.end.y;
        B = -2 * (this.end.x * center.x + this.end.y * center.y);
        C = center.x * center.x + center.y * center.y - radius * radius;

        det = B * B - 4 * A * C;
        if (det < 0) {
            // No interserction
            throw new RuntimeException("No intersection with geodesic found");
            // return Double.POSITIVE_INFINITY;
        }
        // One or two intersections, so choose the closest one
        t = (-B - Math.sqrt(det)) / (2 * A);
        return Distance.hyperbolicDistanceToCenter(new Point(t * this.end.x, t * this.end.y));
    }

    private Point intersectionToGeodesic(Geodesic geodesic) {
        double A, B, C, det, t;
        Point center = geodesic.getEuclideanCenter();
        double radius = geodesic.getEuclideanRadius();

        A = this.end.x * this.end.x + this.end.y * this.end.y;
        B = -2 * (this.end.x * center.x + this.end.y * center.y);
        C = center.x * center.x + center.y * center.y - radius * radius;

        det = B * B - 4 * A * C;
        if (det < 0) {
            // No interserction
            throw new RuntimeException("No intersection with geodesic found");
            // return Double.POSITIVE_INFINITY;
        }
        // One or two intersections, so choose the closest one
        t = (-B - Math.sqrt(det)) / (2 * A);
        return new Point(t * this.end.x, t * this.end.y);
    }


    private double propagate(Chunk chunk, int step) {
        if (step == 0) {
            return Double.POSITIVE_INFINITY;
        }
        for (Direction direction : Direction.values()) {
            Point[] points = chunk.getPointFromDirection(direction);
            if (intersectSegment(points[0], points[1])) {
                if (chunk.getHash(seed, direction)) {
                    return distanceToGeodesic(Geodesic.fromTwoPoints(points[0], points[1]));
                }
                return propagate(chunk.getNeighbors(direction), step - 1);
            }
        }
        throw new RuntimeException("No intersection found");
    }


    private Point propagateIntersectionPoint(Chunk chunk, int step) {
        if (step == 0) {
            // return Double.POSITIVE_INFINITY;
            return this.end;
        }
        for (Direction direction : Direction.values()) {
            Point[] points = chunk.getPointFromDirection(direction);
            if (intersectSegment(points[0], points[1])) {
                if (chunk.getHash(seed, direction)) {
                    return intersectionToGeodesic(Geodesic.fromTwoPoints(points[0], points[1]));
                }
                return propagateIntersectionPoint(chunk.getNeighbors(direction), step - 1);
            }
        }
        throw new RuntimeException("No intersection found");
    }



    /** 
     * @param centerChunk the central chunk of the tiling
     */
    public double throwRay(Chunk centerChunk) {
        return propagate(centerChunk, STEPS);
    }

    public Point throwRayIntersectionPoint(Chunk centerChunk) {
        return propagateIntersectionPoint(centerChunk, STEPS);
    }
}
