/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package src.hyperbolic;

/// A geodesic segment
public class Segment {
    /// The first point of the segment
    public Point start;

    /// The second point of the segment
    public Point end;

    /// The geodesic of the segment
    public Geodesic geodesic;

    /// Constructor of a geodesic segment
    public Segment(double a, double b, Point start, Point end) {
        this.geodesic = new Geodesic(a, b);
        this.start = start;
        this.end = end;

        if (!geodesic.is_on_geodesic(start) || !geodesic.is_on_geodesic(end)) {
            throw new IllegalArgumentException("The points must be on the geodesic");
        }
    }

    /// Returns the length of the segment
    public double length() {
        return Distance.hyperbolic_distance(start, end);
    }

    /// A constructor of a geodesic segment from two points
    public static Segment from_two_points(Point u, Point v) {
        if (u.equals(v)) {
            throw new IllegalArgumentException("Points must be different");
        }
        Geodesic geodesic = Geodesic.from_two_points(u, v);
        return new Segment(geodesic.a, geodesic.b, u, v);
    }

    /// Returns whether a point is contained in the segment
    public boolean contains(Point point) {
        return (
                geodesic.is_on_geodesic(point) &&
                Distance.hyperbolic_distance(point, start) <= this.length() &&
                Distance.hyperbolic_distance(point, end) <= this.length()
        );
    }
}
