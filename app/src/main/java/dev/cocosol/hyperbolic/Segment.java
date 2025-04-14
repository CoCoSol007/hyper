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
     * The geodesic of the segment, representing the curve that the segment lies on.
     */
    private final Geodesic geodesic;

    /**
     * Constructor for creating a geodesic segment.
     * <p>
     * This constructor initializes the segment with a geodesic defined by two parameters and two points.
     * It checks whether the start and end points lie on the geodesic.
     *
     * @param a the first parameter of the geodesic equation
     * @param b the second parameter of the geodesic equation
     * @param start the starting point of the segment
     * @param end the ending point of the segment
     * @throws IllegalArgumentException if the points do not lie on the geodesic
     */
    public Segment(double a, double b, Point start, Point end) {
        this.geodesic = new Geodesic(a, b);
        this.start = start;
        this.end = end;

        if (!geodesic.isOnGeodesic(start) || !geodesic.isOnGeodesic(end)) {
            throw new IllegalArgumentException("The points must be on the geodesic");
        }
    }

    /**
     * Creates a geodesic segment from two points.
     * <p>
     * This constructor generates a geodesic based on the two provided points, and creates a segment 
     * connecting them.
     *
     * @param u the first point of the segment
     * @param v the second point of the segment
     * @return a new {@code Segment} object representing the geodesic segment
     * @throws IllegalArgumentException if the two points are identical
     */
    public static Segment fromTwoPoints(Point u, Point v) {
        if (u.equals(v)) {
            throw new IllegalArgumentException("Points must be different");
        }
        Geodesic geodesic = Geodesic.fromTwoPoints(u, v);
        return new Segment(geodesic.a, geodesic.b, u, v);
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
                geodesic.isOnGeodesic(point) &&
                        Distance.hyperbolicDistance(point, start) <= this.length() &&
                        Distance.hyperbolicDistance(point, end) <= this.length()
        );
    }
}
