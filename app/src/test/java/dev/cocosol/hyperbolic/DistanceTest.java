/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dev.cocosol.Point;

public class DistanceTest {

    @Test
    public void testEuclideanDistance() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(4, 6);
        double expected = Math.sqrt(9 + 16); // 5
        assertEquals(expected, Distance.euclideanDistance(p1, p2), 1e-9);
    }

    @Test
    public void testEuclideanDistanceToCenter() {
        Point p = new Point(3, 4);
        assertEquals(5.0, Distance.euclideanDistanceToCenter(p), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceToCenterAtOrigin() {
        Point p = new Point(0, 0);
        assertEquals(0.0, Distance.hyperbolicDistanceToCenter(p), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceToCenterInsideDisk() {
        Point p = new Point(0.3, 0.4); // euclideanDistance^2 = 0.25
        double expected = HyperbolicMath.acosh(1 + 2 * (0.25 / (1 - 0.25))); // acosh(1 + 0.666...)
        assertEquals(expected, Distance.hyperbolicDistanceToCenter(p), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceToCenterNearBoundary() {
        Point p = new Point(0.99, 0); // very close to boundary
        double result = Distance.hyperbolicDistanceToCenter(p);
        assertTrue(result > 4.5); // should be large
    }

    @Test
    public void testHyperbolicDistanceBetweenSamePoints() {
        Point p = new Point(0.1, 0.1);
        assertEquals(0.0, Distance.hyperbolicDistance(p, p), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceBetweenTwoPoints() {
        Point p1 = new Point(0.3, 0.0);
        Point p2 = new Point(-0.3, 0.0);

        double eucDist = Distance.euclideanDistanceToCenter(p1.minus(p2));
        double d1 = Distance.euclideanDistanceToCenter(p1);
        double d2 = Distance.euclideanDistanceToCenter(p2);
        double expected = HyperbolicMath.acosh(1 + 2 * (Math.pow(eucDist, 2) / ((1 - d1 * d1) * (1 - d2 * d2))));

        assertEquals(expected, Distance.hyperbolicDistance(p1, p2), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceFromOriginToEdge() {
        Point p = new Point(0.9999999, 0);
        double result = Distance.hyperbolicDistanceToCenter(p);
        assertTrue(Double.isFinite(result));
        assertTrue(result > 8.0); // practically very large
    }

    @Test
    public void testHyperbolicDistanceErrorHandlingEdgeCase() {
        // simulate a point outside the disk
        Point p = new Point(2, 0);
        double result = Distance.hyperbolicDistanceToCenter(p);
        assertEquals(Double.POSITIVE_INFINITY, result, 1e-9);
    }
}
