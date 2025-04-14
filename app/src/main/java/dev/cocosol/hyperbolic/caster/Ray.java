package dev.cocosol.hyperbolic.caster;

import dev.cocosol.hyperbolic.Geodesic;
import dev.cocosol.hyperbolic.Point;
import dev.cocosol.hyperbolic.Segment;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;

public class Ray {
    private static final int STEPS = 6;

    /* The end of the ray in Euclidean space, it must be on the unit circle*/
    public Point end;

    public int seed;


    public Ray(double angle, int seed) {
        this.end = new Point(Math.cos(angle), Math.sin(angle));
        this.seed = seed;
    }


    private boolean intersectSegment(Segment segment) {
        return (new Segment(new Point(0, 0), this.end)).intersect(segment);
    }


    /**
     * The Geodesic must intersect the ray
     */
    private double euclideanDistanceToGeodesic(Geodesic geodesic) {
        Point center = geodesic.getEuclideanCenter();
        double radius = geodesic.getEuclideanRadius();

        /* Resolves a polynomial where the roots (t) are the euclidean distance to the center
         * in the polynomial equation of the form a*t^2 + b*t + c = 0, a is always 1
         * Furthermore we need to take the solution that is less than 1 (else where the hyperbolic distance is infinite)
         * So the root that is taken is the smaller
        */

        double b = -2 * (this.end.x * center.x + this.end.y * center.y);
        double c = center.x * center.x + center.y * center.y - radius * radius;

        double det = b * b - 4 * c;
        if (det < 0) {
            // No interserction
            throw new RuntimeException("No intersection with geodesic found");
        }
        // Choose the smaller root (we have b < 0)
        return (-b - Math.sqrt(det)) / 2;
    }

    /**
     * Look at the function distanceToGeodesic(Geodesic)
     * @param geodesic the geodesic to look at
     * @return the point in the unit circle (in Euclidean space) that is the intersection between the ray and the geodesic
     */
    private Point intersectionToGeodesic(Geodesic geodesic) {
        double t = euclideanDistanceToGeodesic(geodesic);
        return new Point(t * this.end.x, t * this.end.y);
    }


    private Point propagate(Chunk chunk, int step) {
        if (step == 0) {            
            return this.end;
        }

        for (Direction direction : Direction.values()) {
            Point[] points = chunk.getPointFromDirection(direction);
            
            if (!intersectSegment(new Segment(points[0], points[1]))) {
                continue;
            }
            
            if (chunk.getHash(seed, direction)) { // There is a wall
                return intersectionToGeodesic(Geodesic.fromTwoPoints(points[0], points[1]));
            }
            // There is no wall
            return propagate(chunk.getNeighbors(direction), step - 1);
        }

        throw new RuntimeException("No intersection found");
    }


    /** 
     * @param centerChunk the central chunk of the tiling
     */
    public Point throwRay(Chunk centerChunk) {
        return propagate(centerChunk, STEPS);
    }
}
