/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

import org.junit.Assert;
import org.junit.Test;

import dev.cocosol.Point;

public class DistanceTest {

    @Test
    public void testEuclideanDistance() {
        final Point p1 = new Point(1, 2);
        final Point p2 = new Point(4, 6);
        final double expected = Math.sqrt(9 + 16); // 5
        Assert.assertEquals(expected, Distance.euclideanDistance(p1, p2), 1e-9);
    }

    @Test
    public void testEuclideanDistanceToCenter() {
        final Point p = new Point(3, 4);
        Assert.assertEquals(5.0, Distance.euclideanDistanceToCenter(p), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceToCenterAtOrigin() {
        final Point p = new Point(0, 0);
        Assert.assertEquals(0.0, Distance.hyperbolicDistanceToCenter(p), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceToCenterInsideDisk() {
        final Point p = new Point(0.3, 0.4); // euclideanDistance^2 = 0.25
        final double expected = HyperbolicMath.acosh(1 + 2 * (0.25 / (1 - 0.25))); // acosh(1 + 0.666...)
        Assert.assertEquals(expected, Distance.hyperbolicDistanceToCenter(p), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceToCenterNearBoundary() {
        final Point p = new Point(0.99, 0); // very close to boundary
        final double result = Distance.hyperbolicDistanceToCenter(p);
        Assert.assertTrue(result > 4.5); // should be large
    }

    @Test
    public void testHyperbolicDistanceBetweenSamePoints() {
        final Point p = new Point(0.1, 0.1);
        Assert.assertEquals(0.0, Distance.hyperbolicDistance(p, p), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceBetweenTwoPoints() {
        final Point p1 = new Point(0.3, 0.0);
        final Point p2 = new Point(-0.3, 0.0);

        final double eucDist = Distance.euclideanDistanceToCenter(p1.minus(p2));
        final double d1 = Distance.euclideanDistanceToCenter(p1);
        final double d2 = Distance.euclideanDistanceToCenter(p2);
        final double expected = HyperbolicMath.acosh(1 + 2 * (Math.pow(eucDist, 2) / ((1 - d1 * d1) * (1 - d2 * d2))));

        Assert.assertEquals(expected, Distance.hyperbolicDistance(p1, p2), 1e-9);
    }

    @Test
    public void testHyperbolicDistanceFromOriginToEdge() {
        final Point p = new Point(0.9999999, 0);
        final double result = Distance.hyperbolicDistanceToCenter(p);
        Assert.assertTrue(Double.isFinite(result));
        Assert.assertTrue(result > 8.0); // practically very large
    }

    @Test
    public void testHyperbolicDistanceErrorHandlingEdgeCase() {
        // simulate a point outside the disk
        final Point p = new Point(2, 0);
        final double result = Distance.hyperbolicDistanceToCenter(p);
        Assert.assertEquals(Double.POSITIVE_INFINITY, result, 1e-9);
    }
}
