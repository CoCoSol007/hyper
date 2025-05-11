/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import org.junit.Assert;
import org.junit.Test;

import dev.cocosol.Point;
import dev.cocosol.hyperbolic.Geodesic;

public class ReflexionTest {

    @Test
    public void testReflexionConstruction() {
        final Point p1 = new Point(0.2, 0.0);
        final Point p2 = new Point(-0.2, 0.0);
        final Geodesic g = Geodesic.fromTwoPoints(p1, p2);
        final Reflexion r = new Reflexion(g);
        Assert.assertNotNull(r);
    }

    @Test
    public void testDiameterReflexion() {
        // Reflection across x-axis (diameter geodesic)
        final Geodesic g = Geodesic.fromTwoPoints(new Point(-0.5, 0), new Point(0.5, 0));
        final Reflexion r = new Reflexion(g);

        final Point original = new Point(0.3, 0.4);
        final Point reflected = r.apply(original);

        Assert.assertEquals(original.x, reflected.x, 1e-6);
        Assert.assertEquals(-original.y, reflected.y, 1e-6);
    }

    @Test
    public void testCircleReflexionSymmetry() {
        final Point p1 = new Point(0.2, 0.2);
        final Point p2 = new Point(-0.2, 0.3);
        final Geodesic g = Geodesic.fromTwoPoints(p1, p2);
        final Reflexion r = new Reflexion(g);

        final Point original = new Point(0.3, 0.1);
        final Point reflected = r.apply(original);
        final Point reflectedBack = r.apply(reflected);

        // Applying reflection twice should yield original point
        Assert.assertEquals(original.x, reflectedBack.x, 1e-6);
        Assert.assertEquals(original.y, reflectedBack.y, 1e-6);
    }

    @Test
    public void testReflectionThrowsOnGeodesicCenter() {
        final Point p1 = new Point(0.3, 0.3);
        final Point p2 = new Point(-0.3, 0.3);
        final Geodesic g = Geodesic.fromTwoPoints(p1, p2);
        final Reflexion r = new Reflexion(g);

        final Point center = g.getEuclideanCenter();
        Assert.assertNotNull(center);

        Assert.assertThrows(IllegalArgumentException.class, () -> r.apply(center));
    }

    @Test
    public void testReflectedPointStaysInDisk() {
        final Point a = new Point(0.1, 0.5);
        final Point b = new Point(-0.5, 0.1);
        final Geodesic g = Geodesic.fromTwoPoints(a, b);
        final Reflexion r = new Reflexion(g);

        final Point original = new Point(0.3, 0.2);
        final Point reflected = r.apply(original);

        final double radiusSquared = reflected.x * reflected.x + reflected.y * reflected.y;
        Assert.assertTrue(radiusSquared < 1.0);
    }
}
