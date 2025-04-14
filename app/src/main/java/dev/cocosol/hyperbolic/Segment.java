/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

/**
 * Represents a geodesic segment in the hyperbolic plane.
 * <p>
 * A geodesic segment is defined by two points on a geodesic. The segment belongs to the geodesic,
 * and it checks that both the start and end points lie on the corresponding geodesic.
 */
public class Segment {

    /**
     * The first point of the segment.
     */
    private final Point start;

    /**
     * The second point of the segment.
     */
    private final Point end;

    /**
     * Creates a geodesic segment from two points.
     *
     * @param start the first point of the segment
     * @param end the second point of the segment
     * @throws IllegalArgumentException if the two points are identical
     */
    public Segment(Point start, Point end) {
        if (start.equals(end)) {
            throw new IllegalArgumentException("Points must be different");
        }
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the length of the segment.
     * <p>
     * The length is calculated as the hyperbolic distance between the start and end points of the segment.
     *
     * @return the length of the segment
     */
    public double length() {
        return Distance.hyperbolicDistance(start, end);
    }

    /**
     * Checks if a given point is contained within the segment.
     * <p>
     * A point is considered to be on the segment if it lies on the geodesic and its hyperbolic distance
     * from both the start and end points is less than or equal to the length of the segment.
     *
     * @param point the point to check
     * @return {@code true} if the point is contained in the segment, {@code false} otherwise
     */
    public boolean contains(Point point) {
        return (
            Geodesic.fromTwoPoints(this.start, this.end).isOnGeodesic(point) &&
            Distance.hyperbolicDistance(point, this.start) <= this.length() &&
            Distance.hyperbolicDistance(point, this.end) <= this.length()
        );
    }

    
    /**
     * Use in the intersect method ; more details on the link below
     * https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     */
    private boolean ccw(Point a, Point b, Point c) {
        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x);
    }

    /**
     * The point C is the origin of the ray and so of the Poincare Disk and
     * the point D is the end of the ray
     *
     * @param segment the segment
     * @return true if the segment intersects the ray in Euclidean space
    */
    public boolean intersect(Segment segment) {
        return (
            ccw(segment.start, this.start, this.end) != ccw(segment.end, this.start, this.end) && 
            ccw(segment.start, segment.end, this.start) != ccw(segment.start, segment.end, this.end)
        );
    }
}
