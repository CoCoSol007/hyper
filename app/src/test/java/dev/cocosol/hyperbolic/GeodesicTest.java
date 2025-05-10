/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dev.cocosol.Point;

public class GeodesicTest {

    @Test
    public void testConstructorParameters() {
        Geodesic g = new Geodesic(1.0, -2.0);
        assertEquals(1.0, g.a, 0.000001);
        assertEquals(-2.0, g.b, 0.000001);
        assertFalse(g.diameter);
    }

    @Test
    public void testFromTwoPointsDiameterCase() {
        Point u = new Point(0.5, 0.5);
        Point v = new Point(1.0, 1.0); // Aligned → determinant near 0
        Geodesic g = Geodesic.fromTwoPoints(u, v);
        assertTrue(g.diameter);
        assertNotEquals(0.0, g.a);
        assertNotEquals(0.0, g.b);
    }

    @Test
    public void testFromTwoPointsGeneralCase() {
        Point u = new Point(0.2, 0.3);
        Point v = new Point(-0.4, 0.5);
        Geodesic g = Geodesic.fromTwoPoints(u, v);
        assertFalse(g.diameter);
        assertNotEquals(0.0, g.a);
        assertNotEquals(0.0, g.b);
    }

    @Test
    public void testIsOnGeodesicForDiameter() {
        Geodesic g = new Geodesic(1.0, -1.0);
        g.diameter = true;
        Point onLine = new Point(0.5, 0.5); // 1*0.5 + (-1)*0.5 = 0
        assertTrue(g.isOnGeodesic(onLine));
    }

    @Test
    public void testIsOnGeodesicForCircle() {
        Point u = new Point(0.2, 0.3);
        Point v = new Point(-0.3, 0.4);
        Geodesic g = Geodesic.fromTwoPoints(u, v);

        assertTrue(g.isOnGeodesic(u));
        assertTrue(g.isOnGeodesic(v));
        assertFalse(g.isOnGeodesic(new Point(-0.9, 0.9)));
    }

    @Test
    public void testGetEuclideanCenterForCircle() {
        Geodesic g = new Geodesic(4.0, -6.0);
        g.diameter = false;
        Point center = g.getEuclideanCenter();
        assertNotNull(center);
        assertEquals(-2.0, center.x, 0.000001);
        assertEquals(3.0, center.y, 0.000001);
    }

    @Test
    public void testGetEuclideanCenterForDiameter() {
        Geodesic g = new Geodesic(2.0, 1.0);
        g.diameter = true;
        assertNull(g.getEuclideanCenter());
    }

    @Test
    public void testGetEuclideanRadiusForCircle() {
        Geodesic g = new Geodesic(4.0, 4.0);
        g.diameter = false;
        double expected = Math.sqrt(16 + 16 - 4) / 2; // sqrt(28)/2
        assertEquals(expected, g.getEuclideanRadius(), 1e-9);
    }

    @Test
    public void testGetEuclideanRadiusForDiameter() {
        Geodesic g = new Geodesic(3.0, 4.0);
        g.diameter = true;
        assertEquals(-1.0, g.getEuclideanRadius(), 0.000001);
    }

    @Test
    public void testGetIdealPointsDiameter() {
        Geodesic g = new Geodesic(1.0, 1.0);
        g.diameter = true;
        Point[] points = g.getIdealPoints();
        assertNotNull(points);
        assertEquals(2, points.length);
        assertNotEquals(points[0], points[1]);
    }

    @Test
    public void testGetIdealPointsGeneralGeodesic() {
        Point u = new Point(0.3, 0.2);
        Point v = new Point(-0.2, 0.3);
        Geodesic g = Geodesic.fromTwoPoints(u, v);
        Point[] points = g.getIdealPoints();
        assertNotNull(points);
        assertEquals(2, points.length);
    }
}
