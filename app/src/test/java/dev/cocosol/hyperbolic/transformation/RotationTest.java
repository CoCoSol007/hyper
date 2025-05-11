/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import org.junit.Assert;
import org.junit.Test;

import dev.cocosol.Point;

public class RotationTest {

    @Test
    public void testRotationInitialization() {
        final Rotation rotation = new Rotation(Math.PI / 2);
        Assert.assertEquals(Math.PI / 2, rotation.theta, 1e-9);
    }

    @Test
    public void testRotationByZero() {
        final Rotation rotation = new Rotation(0);
        final Point p = new Point(0.5, 0.5);
        final Point rotated = rotation.apply(p);

        Assert.assertEquals(p.x, rotated.x, 1e-9);
        Assert.assertEquals(p.y, rotated.y, 1e-9);
    }

    @Test
    public void testRotationByPiOver2() {
        final Rotation rotation = new Rotation(Math.PI / 2);
        final Point p = new Point(1.0, 0.0);
        final Point rotated = rotation.apply(p);

        Assert.assertEquals(0.0, rotated.x, 1e-9);
        Assert.assertEquals(1.0, rotated.y, 1e-9);
    }

    @Test
    public void testRotationByPi() {
        final Rotation rotation = new Rotation(Math.PI);
        final Point p = new Point(0.5, 0.5);
        final Point rotated = rotation.apply(p);

        Assert.assertEquals(-0.5, rotated.x, 1e-9);
        Assert.assertEquals(-0.5, rotated.y, 1e-9);
    }

    @Test
    public void testRotationFullCircle() {
        final Rotation rotation = new Rotation(2 * Math.PI);
        final Point p = new Point(-0.3, 0.4);
        final Point rotated = rotation.apply(p);

        Assert.assertEquals(p.x, rotated.x, 1e-9);
        Assert.assertEquals(p.y, rotated.y, 1e-9);
    }

    @Test
    public void testRotationPreservesDistanceToOrigin() {
        final Rotation rotation = new Rotation(Math.PI / 3);
        final Point p = new Point(0.4, 0.3);
        final Point rotated = rotation.apply(p);

        final double originalDistSquared = p.x * p.x + p.y * p.y;
        final double rotatedDistSquared = rotated.x * rotated.x + rotated.y * rotated.y;

        Assert.assertEquals(originalDistSquared, rotatedDistSquared, 1e-9);
    }
}
