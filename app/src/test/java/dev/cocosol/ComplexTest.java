/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import org.junit.Assert;
import org.junit.Test;

public class ComplexTest {

    @Test
    public void testConstants() {
        Assert.assertEquals(1.0, Complex.ONE.re(), 1e-10);
        Assert.assertEquals(0.0, Complex.ONE.im(), 1e-10);

        Assert.assertEquals(0.0, Complex.ZERO.re(), 1e-10);
        Assert.assertEquals(0.0, Complex.ZERO.im(), 1e-10);

        Assert.assertEquals(0.0, Complex.I.re(), 1e-10);
        Assert.assertEquals(1.0, Complex.I.im(), 1e-10);

        Assert.assertEquals(-1.0, Complex.MINUS_ONE.re(), 1e-10);
        Assert.assertEquals(0.0, Complex.MINUS_ONE.im(), 1e-10);

        Assert.assertEquals(0.0, Complex.MINUS_I.re(), 1e-10);
        Assert.assertEquals(-1.0, Complex.MINUS_I.im(), 1e-10);
    }

    @Test
    public void testToString() {
        Assert.assertEquals("3.0 + 4.0i", new Complex(3, 4).toString());
        Assert.assertEquals("3.0 - 4.0i", new Complex(3, -4).toString());
        Assert.assertEquals("4.0i", new Complex(0, 4).toString());
        Assert.assertEquals("3.0", new Complex(3, 0).toString());
    }

    @Test
    public void testPlus() {
        final Complex a = new Complex(1, 2);
        final Complex b = new Complex(3, 4);
        final Complex result = a.plus(b);
        Assert.assertTrue(new Complex(4, 6).equals(result));
    }

    @Test
    public void testMinus() {
        final Complex a = new Complex(5, 6);
        final Complex b = new Complex(3, 4);
        final Complex result = a.minus(b);
        Assert.assertTrue(new Complex(2, 2).equals(result));
    }

    @Test
    public void testTimes() {
        final Complex a = new Complex(1, 2);
        final Complex b = new Complex(3, 4);
        final Complex result = a.times(b);
        Assert.assertTrue(new Complex(-5, 10).equals(result));
    }

    @Test
    public void testScale() {
        final Complex a = new Complex(1, -1);
        final Complex result = a.scale(2.0);
        Assert.assertTrue(new Complex(2, -2).equals(result));
    }

    @Test
    public void testConjugate() {
        final Complex a = new Complex(2, 3);
        Assert.assertTrue(new Complex(2, -3).equals(a.conjugate()));
    }

    @Test
    public void testReciprocal() {
        final Complex a = new Complex(2, 1);
        final Complex reciprocal = a.reciprocal();
        final Complex expected = new Complex(2.0 / 5.0, -1.0 / 5.0);
        Assert.assertTrue(expected.equals(reciprocal));
    }

    @Test
    public void testDivides() {
        final Complex a = new Complex(4, 2);
        final Complex b = new Complex(1, -1);
        final Complex result = a.divides(b);
        final Complex expected = a.times(b.reciprocal());
        Assert.assertTrue(result.equals(expected));
    }

    @Test
    public void testModule() {
        final Complex a = new Complex(3, 4);
        Assert.assertEquals(5.0, a.module(), 1e-10);
    }

    @Test
    public void testExponent() {
        final Complex result = Complex.exponent(1.0, Math.PI); // Should be close to -1 + 0i
        Assert.assertEquals(-1.0, result.re(), 1e-10);
        Assert.assertEquals(0.0, result.im(), 1e-10);
    }

    @Test
    public void testEquals() {
        final Complex a = new Complex(1.0, 1.0);
        final Complex b = new Complex(1.0, 1.0);
        final Complex c = new Complex(1.0, -1.0);
        Assert.assertTrue(a.equals(b));
        Assert.assertFalse(a.equals(c));
    }
}
