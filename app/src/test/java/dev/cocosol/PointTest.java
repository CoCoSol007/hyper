/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PointTest {

    @Test
    public void testOrigin() {
        assertEquals(0.0, Point.ORIGIN.x, 1e-10);
        assertEquals(0.0, Point.ORIGIN.y, 1e-10);
    }

    @Test
    public void testToString() {
        Point p = new Point(1.23456, -7.891);
        assertEquals("( 1.235, -7.891 )", p.toString());
    }

    @Test
    public void testFromComplex() {
        Complex z = new Complex(2.5, -3.5);
        Point p = Point.fromComplex(z);
        assertEquals(2.5, p.x, 1e-10);
        assertEquals(-3.5, p.y, 1e-10);
    }

    @Test
    public void testToComplex() {
        Point p = new Point(3.0, 4.0);
        Complex z = p.toComplex();
        assertTrue(new Complex(3.0, 4.0).equals(z));
    }

    @Test
    public void testPlus() {
        Point a = new Point(1.0, 2.0);
        Point b = new Point(3.0, 4.0);
        Point result = a.plus(b);
        assertEquals(new Point(4.0, 6.0), result);
    }

    @Test
    public void testMinus() {
        Point a = new Point(5.0, 6.0);
        Point b = new Point(3.0, 2.0);
        Point result = a.minus(b);
        assertEquals(new Point(2.0, 4.0), result);
    }

    @Test
    public void testMul() {
        Point p = new Point(2.0, -3.0);
        Point result = p.mul(2.5);
        assertEquals(new Point(5.0, -7.5), result);
    }

    @Test
    public void testDot() {
        Point a = new Point(1.0, 2.0);
        Point b = new Point(3.0, 4.0);
        double dot = a.dot(b); // 1*3 + 2*4 = 11
        assertEquals(11.0, dot, 1e-10);
    }

    @Test
    public void testEquals() {
        Point a = new Point(1.0000001, 2.0000001);
        Point b = new Point(1.0000002, 2.0000002);
        assertTrue(a.equals(b));
    }

    @Test
    public void testNotEquals() {
        Point a = new Point(1.0, 2.0);
        Point b = new Point(1.0001, 2.0001);
        assertFalse(a.equals(b));
    }

    @Test
    public void testHashCodeConsistency() {
        Point a = new Point(3.0, 4.0);
        Point b = new Point(3.0, 4.0);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void testOrientation() {
        Point a = new Point(0, 0);
        Point b = new Point(1, 0);
        Point c = new Point(0, 1);
        double orientation = c.orientation(a, b); // > 0 => c is to the left of ab
        assertTrue(orientation > 0);

        Point d = new Point(1, -1);
        assertTrue(d.orientation(a, b) < 0); // to the right
    }
}
