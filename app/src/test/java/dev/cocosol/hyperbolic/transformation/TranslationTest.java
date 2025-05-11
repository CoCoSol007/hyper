/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import org.junit.Assert;
import org.junit.Test;

import dev.cocosol.Point;

public class TranslationTest {

    @Test
    public void testInitialization() {
        final Point p = new Point(0.3, 0.4);
        final Translation t = new Translation(p);
        Assert.assertNotNull(t);
    }

    @Test
    public void testTranslationMapsOriginToItself() {
        final Translation t = new Translation(new Point(0, 0));
        final Point p = new Point(0.5, -0.5);
        final Point translated = t.apply(p);

        Assert.assertEquals(p.x, translated.x, 1e-9);
        Assert.assertEquals(p.y, translated.y, 1e-9);
    }

    @Test
    public void testPointMappedToOrigin() {
        final Point a = new Point(0.3, 0.4);
        final Translation t = new Translation(a);
        final Point result = t.apply(a);

        Assert.assertEquals(0.0, result.x, 1e-9);
        Assert.assertEquals(0.0, result.y, 1e-9);
    }

    @Test
    public void testTranslationPreservesInsideDisk() {
        final Point a = new Point(0.2, 0.2);
        final Point p = new Point(0.4, 0.1);
        final Translation t = new Translation(a);
        final Point result = t.apply(p);

        final double rSquared = result.x * result.x + result.y * result.y;
        Assert.assertTrue(rSquared < 1.0);
    }

    @Test
    public void testInversibilityLikeBehavior() {
        final Point a = new Point(0.2, 0.3);
        final Point p = new Point(-0.1, 0.1);

        final Translation t = new Translation(p);
        final Point result = t.apply(a);

        final Translation tInverse = new Translation(p.mul(-1));
        final Point resultInverse = tInverse.apply(result);

        Assert.assertTrue(resultInverse.equals(a));
    }

    @Test
    public void testTranslationOfPointAtOrigin() {
        final Point a = new Point(0.3, 0.4);
        final Point origin = new Point(0.0, 0.0);
        final Translation t = new Translation(a);
        final Point result = t.apply(origin);

        // Result should be the inverse of the transformation (roughly -a)
        Assert.assertEquals(-a.x, result.x, 1e-6);
        Assert.assertEquals(-a.y, result.y, 1e-6);
    }
}
