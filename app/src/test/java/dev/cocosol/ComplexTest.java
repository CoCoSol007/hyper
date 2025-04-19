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

public class ComplexTest {

    @Test
    public void testConstants() {
        assertEquals(1.0, Complex.ONE.re(), 1e-10);
        assertEquals(0.0, Complex.ONE.im(), 1e-10);

        assertEquals(0.0, Complex.ZERO.re(), 1e-10);
        assertEquals(0.0, Complex.ZERO.im(), 1e-10);

        assertEquals(0.0, Complex.I.re(), 1e-10);
        assertEquals(1.0, Complex.I.im(), 1e-10);

        assertEquals(-1.0, Complex.MINUS_ONE.re(), 1e-10);
        assertEquals(0.0, Complex.MINUS_ONE.im(), 1e-10);

        assertEquals(0.0, Complex.MINUS_I.re(), 1e-10);
        assertEquals(-1.0, Complex.MINUS_I.im(), 1e-10);
    }

    @Test
    public void testToString() {
        assertEquals("3.0 + 4.0i", new Complex(3, 4).toString());
        assertEquals("3.0 - 4.0i", new Complex(3, -4).toString());
        assertEquals("4.0i", new Complex(0, 4).toString());
        assertEquals("3.0", new Complex(3, 0).toString());
    }

    @Test
    public void testPlus() {
        Complex a = new Complex(1, 2);
        Complex b = new Complex(3, 4);
        Complex result = a.plus(b);
        assertTrue(new Complex(4, 6).equals(result));
    }

    @Test
    public void testMinus() {
        Complex a = new Complex(5, 6);
        Complex b = new Complex(3, 4);
        Complex result = a.minus(b);
        assertTrue(new Complex(2, 2).equals(result));
    }

    @Test
    public void testTimes() {
        Complex a = new Complex(1, 2);
        Complex b = new Complex(3, 4);
        Complex result = a.times(b);
        assertTrue(new Complex(-5, 10).equals(result));
    }

    @Test
    public void testScale() {
        Complex a = new Complex(1, -1);
        Complex result = a.scale(2.0);
        assertTrue(new Complex(2, -2).equals(result));
    }

    @Test
    public void testConjugate() {
        Complex a = new Complex(2, 3);
        assertTrue(new Complex(2, -3).equals(a.conjugate()));
    }

    @Test
    public void testReciprocal() {
        Complex a = new Complex(2, 1);
        Complex reciprocal = a.reciprocal();
        Complex expected = new Complex(2.0 / 5.0, -1.0 / 5.0);
        assertTrue(expected.equals(reciprocal));
    }

    @Test
    public void testDivides() {
        Complex a = new Complex(4, 2);
        Complex b = new Complex(1, -1);
        Complex result = a.divides(b);
        Complex expected = a.times(b.reciprocal());
        assertTrue(result.equals(expected));
    }

    @Test
    public void testModule() {
        Complex a = new Complex(3, 4);
        assertEquals(5.0, a.module(), 1e-10);
    }

    @Test
    public void testExponent() {
        Complex result = Complex.exponent(1.0, Math.PI); // Should be close to -1 + 0i
        assertEquals(-1.0, result.re(), 1e-10);
        assertEquals(0.0, result.im(), 1e-10);
    }

    @Test
    public void testEquals() {
        Complex a = new Complex(1.0, 1.0);
        Complex b = new Complex(1.0, 1.0);
        Complex c = new Complex(1.0, -1.0);
        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
    }
}
