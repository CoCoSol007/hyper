/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dev.cocosol.Point;

public class TranslationTest {

    @Test
    public void testInitialization() {
        Point p = new Point(0.3, 0.4);
        Translation t = new Translation(p);
        assertNotNull(t);
    }

    @Test
    public void testTranslationMapsOriginToItself() {
        Translation t = new Translation(new Point(0, 0));
        Point p = new Point(0.5, -0.5);
        Point translated = t.apply(p);

        assertEquals(p.x, translated.x, 1e-9);
        assertEquals(p.y, translated.y, 1e-9);
    }

    @Test
    public void testPointMappedToOrigin() {
        Point a = new Point(0.3, 0.4);
        Translation t = new Translation(a);
        Point result = t.apply(a);

        assertEquals(0.0, result.x, 1e-9);
        assertEquals(0.0, result.y, 1e-9);
    }

    @Test
    public void testTranslationPreservesInsideDisk() {
        Point a = new Point(0.2, 0.2);
        Point p = new Point(0.4, 0.1);
        Translation t = new Translation(a);
        Point result = t.apply(p);

        double rSquared = result.x * result.x + result.y * result.y;
        assertTrue(rSquared < 1.0);
    }

    @Test
    public void testInversibilityLikeBehavior() {
        Point a = new Point(0.2, 0.3);
        Point p = new Point(-0.1, 0.1);

        Translation t = new Translation(p);
        Point result = t.apply(a);

        Translation tInverse = new Translation(p.mul(-1));
        Point resultInverse = tInverse.apply(result);

        assertTrue(resultInverse.equals(a));
    }

    @Test
    public void testTranslationOfPointAtOrigin() {
        Point a = new Point(0.3, 0.4);
        Point origin = new Point(0.0, 0.0);
        Translation t = new Translation(a);
        Point result = t.apply(origin);

        // Result should be the inverse of the transformation (roughly -a)
        assertEquals(-a.x, result.x, 1e-6);
        assertEquals(-a.y, result.y, 1e-6);
    }
}
