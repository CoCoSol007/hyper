/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dev.cocosol.Point;
import dev.cocosol.hyperbolic.Geodesic;

public class ReflexionTest {

    @Test
    public void testReflexionConstruction() {
        Point p1 = new Point(0.2, 0.0);
        Point p2 = new Point(-0.2, 0.0);
        Geodesic g = Geodesic.fromTwoPoints(p1, p2);
        Reflexion r = new Reflexion(g);
        assertNotNull(r);
    }

    @Test
    public void testDiameterReflexion() {
        // Reflection across x-axis (diameter geodesic)
        Geodesic g = Geodesic.fromTwoPoints(new Point(-0.5, 0), new Point(0.5, 0));
        Reflexion r = new Reflexion(g);

        Point original = new Point(0.3, 0.4);
        Point reflected = r.apply(original);

        assertEquals(original.x, reflected.x, 1e-6);
        assertEquals(-original.y, reflected.y, 1e-6);
    }

    @Test
    public void testCircleReflexionSymmetry() {
        Point p1 = new Point(0.2, 0.2);
        Point p2 = new Point(-0.2, 0.3);
        Geodesic g = Geodesic.fromTwoPoints(p1, p2);
        Reflexion r = new Reflexion(g);

        Point original = new Point(0.3, 0.1);
        Point reflected = r.apply(original);
        Point reflectedBack = r.apply(reflected);

        // Applying reflection twice should yield original point
        assertEquals(original.x, reflectedBack.x, 1e-6);
        assertEquals(original.y, reflectedBack.y, 1e-6);
    }

    @Test
    public void testReflectionThrowsOnGeodesicCenter() {
        Point p1 = new Point(0.3, 0.3);
        Point p2 = new Point(-0.3, 0.3);
        Geodesic g = Geodesic.fromTwoPoints(p1, p2);
        Reflexion r = new Reflexion(g);

        Point center = g.getEuclideanCenter();
        assertNotNull(center);

        assertThrows(IllegalArgumentException.class, () -> r.apply(center));
    }

    @Test
    public void testReflectedPointStaysInDisk() {
        Point a = new Point(0.1, 0.5);
        Point b = new Point(-0.5, 0.1);
        Geodesic g = Geodesic.fromTwoPoints(a, b);
        Reflexion r = new Reflexion(g);

        Point original = new Point(0.3, 0.2);
        Point reflected = r.apply(original);

        double radiusSquared = reflected.x * reflected.x + reflected.y * reflected.y;
        assertTrue(radiusSquared < 1.0);
    }
}
