/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

import org.junit.Assert;
import org.junit.Test;

import dev.cocosol.Point;

public class GeodesicTest {

    @Test
    public void testConstructorParameters() {
        final Geodesic g = new Geodesic(1.0, -2.0);
        Assert.assertEquals(1.0, g.a, 0.000001);
        Assert.assertEquals(-2.0, g.b, 0.000001);
        Assert.assertFalse(g.diameter);
    }

    @Test
    public void testFromTwoPointsDiameterCase() {
        final Point u = new Point(0.5, 0.5);
        final Point v = new Point(1.0, 1.0); // Aligned → determinant near 0
        final Geodesic g = Geodesic.fromTwoPoints(u, v);
        Assert.assertTrue(g.diameter);
        Assert.assertNotEquals(0.0, g.a);
        Assert.assertNotEquals(0.0, g.b);
    }

    @Test
    public void testFromTwoPointsGeneralCase() {
        final Point u = new Point(0.2, 0.3);
        final Point v = new Point(-0.4, 0.5);
        final Geodesic g = Geodesic.fromTwoPoints(u, v);
        Assert.assertFalse(g.diameter);
        Assert.assertNotEquals(0.0, g.a);
        Assert.assertNotEquals(0.0, g.b);
    }

    @Test
    public void testIsOnGeodesicForDiameter() {
        final Geodesic g = new Geodesic(1.0, -1.0);
        g.diameter = true;
        final Point onLine = new Point(0.5, 0.5); // 1*0.5 + (-1)*0.5 = 0
        Assert.assertTrue(g.isOnGeodesic(onLine));
    }

    @Test
    public void testIsOnGeodesicForCircle() {
        final Point u = new Point(0.2, 0.3);
        final Point v = new Point(-0.3, 0.4);
        final Geodesic g = Geodesic.fromTwoPoints(u, v);

        Assert.assertTrue(g.isOnGeodesic(u));
        Assert.assertTrue(g.isOnGeodesic(v));
        Assert.assertFalse(g.isOnGeodesic(new Point(-0.9, 0.9)));
    }

    @Test
    public void testGetEuclideanCenterForCircle() {
        final Geodesic g = new Geodesic(4.0, -6.0);
        g.diameter = false;
        final Point center = g.getEuclideanCenter();
        Assert.assertNotNull(center);
        Assert.assertEquals(-2.0, center.x, 0.000001);
        Assert.assertEquals(3.0, center.y, 0.000001);
    }

    @Test
    public void testGetEuclideanCenterForDiameter() {
        final Geodesic g = new Geodesic(2.0, 1.0);
        g.diameter = true;
        Assert.assertNull(g.getEuclideanCenter());
    }

    @Test
    public void testGetEuclideanRadiusForCircle() {
        final Geodesic g = new Geodesic(4.0, 4.0);
        g.diameter = false;
        final double expected = Math.sqrt(16 + 16 - 4) / 2; // sqrt(28)/2
        Assert.assertEquals(expected, g.getEuclideanRadius(), 1e-9);
    }

    @Test
    public void testGetEuclideanRadiusForDiameter() {
        final Geodesic g = new Geodesic(3.0, 4.0);
        g.diameter = true;
        Assert.assertEquals(-1.0, g.getEuclideanRadius(), 0.000001);
    }

    @Test
    public void testGetIdealPointsDiameter() {
        final Geodesic g = new Geodesic(1.0, 1.0);
        g.diameter = true;
        final Point[] points = g.getIdealPoints();
        Assert.assertNotNull(points);
        Assert.assertEquals(2, points.length);
        Assert.assertNotEquals(points[0], points[1]);
    }

    @Test
    public void testGetIdealPointsGeneralGeodesic() {
        final Point u = new Point(0.3, 0.2);
        final Point v = new Point(-0.2, 0.3);
        final Geodesic g = Geodesic.fromTwoPoints(u, v);
        final Point[] points = g.getIdealPoints();
        Assert.assertNotNull(points);
        Assert.assertEquals(2, points.length);
    }
}
