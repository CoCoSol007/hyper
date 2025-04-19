/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.transformation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dev.cocosol.Point;

public class RotationTest {

    @Test
    public void testRotationInitialization() {
        Rotation rotation = new Rotation(Math.PI / 2);
        assertEquals(Math.PI / 2, rotation.theta, 1e-9);
    }

    @Test
    public void testRotationByZero() {
        Rotation rotation = new Rotation(0);
        Point p = new Point(0.5, 0.5);
        Point rotated = rotation.apply(p);

        assertEquals(p.x, rotated.x, 1e-9);
        assertEquals(p.y, rotated.y, 1e-9);
    }

    @Test
    public void testRotationByPiOver2() {
        Rotation rotation = new Rotation(Math.PI / 2);
        Point p = new Point(1.0, 0.0);
        Point rotated = rotation.apply(p);

        assertEquals(0.0, rotated.x, 1e-9);
        assertEquals(1.0, rotated.y, 1e-9);
    }

    @Test
    public void testRotationByPi() {
        Rotation rotation = new Rotation(Math.PI);
        Point p = new Point(0.5, 0.5);
        Point rotated = rotation.apply(p);

        assertEquals(-0.5, rotated.x, 1e-9);
        assertEquals(-0.5, rotated.y, 1e-9);
    }

    @Test
    public void testRotationFullCircle() {
        Rotation rotation = new Rotation(2 * Math.PI);
        Point p = new Point(-0.3, 0.4);
        Point rotated = rotation.apply(p);

        assertEquals(p.x, rotated.x, 1e-9);
        assertEquals(p.y, rotated.y, 1e-9);
    }

    @Test
    public void testRotationPreservesDistanceToOrigin() {
        Rotation rotation = new Rotation(Math.PI / 3);
        Point p = new Point(0.4, 0.3);
        Point rotated = rotation.apply(p);

        double originalDistSquared = p.x * p.x + p.y * p.y;
        double rotatedDistSquared = rotated.x * rotated.x + rotated.y * rotated.y;

        assertEquals(originalDistSquared, rotatedDistSquared, 1e-9);
    }
}
