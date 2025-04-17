/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

/**
 * Represents a geodesic segment in the euclidean plane.
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
    public Segment(final Point start, final Point end) {
        if (start.equals(end)) {
            throw new IllegalArgumentException("Points must be different");
        }
        this.start = start;
        this.end = end;
    }

    /**
     * Use in the intersect method ; more details on the link below
     * https://bryceboe.com/2006/10/23/line-segment-intersection-algorithm/
     */
    private boolean ccw(final Point a, final Point b, final Point c) {
        return (c.y - a.y) * (b.x - a.x) > (b.y - a.y) * (c.x - a.x);
    }

    /**
     * The point C is the origin of the ray and so of the Poincare Disk and
     * the point D is the end of the ray
     *
     * @param segment the segment
     * @return true if the segment intersects the ray in Euclidean space
     */
    public boolean intersect(final Segment segment) {
        return 
            ccw(segment.start, this.start, this.end) != ccw(segment.end, this.start, this.end) 
            && 
            ccw(segment.start, segment.end, this.start) != ccw(segment.start, segment.end, this.end);
    }
}
