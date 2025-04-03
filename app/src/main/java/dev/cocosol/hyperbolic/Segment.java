/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package dev.cocosol.hyperbolic;

/// A geodesic segment
public class Segment {
    /// The first point of the segment
    private final Point start;

    /// The second point of the segment
    private final Point end;

    /// The geodesic of the segment
    private final Geodesic geodesic;

    /// Constructor of a geodesic segment
    public Segment(double a, double b, Point start, Point end) {
        this.geodesic = new Geodesic(a, b);
        this.start = start;
        this.end = end;

        if (!geodesic.isOnGeodesic(start) || !geodesic.isOnGeodesic(end)) {
            throw new IllegalArgumentException("The points must be on the geodesic");
        }
    }

    /// A constructor of a geodesic segment from two points
    public static Segment fromTwoPoints(Point u, Point v) {
        if (u.equals(v)) {
            throw new IllegalArgumentException("Points must be different");
        }
        Geodesic geodesic = Geodesic.fromTwoPoints(u, v);
        return new Segment(geodesic.a, geodesic.b, u, v);
    }

    /// Returns the length of the segment
    public double length() {
        return Distance.hyperbolicDistance(start, end);
    }

    /// Returns whether a point is contained in the segment
    public boolean contains(Point point) {
        return (
                geodesic.isOnGeodesic(point) &&
                        Distance.hyperbolicDistance(point, start) <= this.length() &&
                        Distance.hyperbolicDistance(point, end) <= this.length()
        );
    }
}