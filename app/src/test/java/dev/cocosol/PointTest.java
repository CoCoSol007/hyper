/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import org.junit.Assert;
import org.junit.Test;

public class PointTest {

    @Test
    public void testOrigin() {
        Assert.assertEquals(0.0, Point.ORIGIN.x, 1e-10);
        Assert.assertEquals(0.0, Point.ORIGIN.y, 1e-10);
    }

    @Test
    public void testToString() {
        final Point p = new Point(1.23456, -7.891);
        Assert.assertEquals("( 1.235, -7.891 )", p.toString());
    }

    @Test
    public void testFromComplex() {
        final Complex z = new Complex(2.5, -3.5);
        final Point p = Point.fromComplex(z);
        Assert.assertEquals(2.5, p.x, 1e-10);
        Assert.assertEquals(-3.5, p.y, 1e-10);
    }

    @Test
    public void testToComplex() {
        final Point p = new Point(3.0, 4.0);
        final Complex z = p.toComplex();
        Assert.assertTrue(new Complex(3.0, 4.0).equals(z));
    }

    @Test
    public void testPlus() {
        final Point a = new Point(1.0, 2.0);
        final Point b = new Point(3.0, 4.0);
        final Point result = a.plus(b);
        Assert.assertEquals(new Point(4.0, 6.0), result);
    }

    @Test
    public void testMinus() {
        final Point a = new Point(5.0, 6.0);
        final Point b = new Point(3.0, 2.0);
        final Point result = a.minus(b);
        Assert.assertEquals(new Point(2.0, 4.0), result);
    }

    @Test
    public void testMul() {
        final Point p = new Point(2.0, -3.0);
        final Point result = p.mul(2.5);
        Assert.assertEquals(new Point(5.0, -7.5), result);
    }

    @Test
    public void testDot() {
        final Point a = new Point(1.0, 2.0);
        final Point b = new Point(3.0, 4.0);
        final double dot = a.dot(b); // 1*3 + 2*4 = 11
        Assert.assertEquals(11.0, dot, 1e-10);
    }

    @Test
    public void testEquals() {
        final Point a = new Point(1.0000001, 2.0000001);
        final Point b = new Point(1.0000002, 2.0000002);
        Assert.assertTrue(a.equals(b));
    }

    @Test
    public void testNotEquals() {
        final Point a = new Point(1.0, 2.0);
        final Point b = new Point(1.0001, 2.0001);
        Assert.assertFalse(a.equals(b));
    }

    @Test
    public void testHashCodeConsistency() {
        final Point a = new Point(3.0, 4.0);
        final Point b = new Point(3.0, 4.0);
        Assert.assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testOrientation() {
        final Point a = new Point(0, 0);
        final Point b = new Point(1, 0);
        final Point c = new Point(0, 1);
        final double orientation = c.orientation(a, b); // > 0 => c is to the left of ab
        Assert.assertTrue(orientation > 0);

        final Point d = new Point(1, -1);
        Assert.assertTrue(d.orientation(a, b) < 0); // to the right
    }
}
