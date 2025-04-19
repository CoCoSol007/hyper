/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class HyperbolicMathTest {

    @Test
    public void testAcoshValid() {
        assertEquals(0.0, HyperbolicMath.acosh(1), 1e-9);
        assertEquals(Math.log(3 + Math.sqrt(8)), HyperbolicMath.acosh(3), 1e-9);
    }

    @Test
    public void testAcoshInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            HyperbolicMath.acosh(0.9);
        });
        assertEquals("acosh is only defined for x >= 1", exception.getMessage());
    }

    @Test
    public void testAsinh() {
        assertEquals(0.0, HyperbolicMath.asinh(0), 1e-9);
        assertEquals(Math.log(2 + Math.sqrt(5)), HyperbolicMath.asinh(2), 1e-9);
    }

    @Test
    public void testAtanhValid() {
        assertEquals(0.0, HyperbolicMath.atanh(0), 1e-9);
        assertEquals(0.5 * Math.log((1 + 0.5) / (1 - 0.5)), HyperbolicMath.atanh(0.5), 1e-9);
    }

    @Test
    public void testAtanhInvalid() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            HyperbolicMath.atanh(1.0);
        });
        assertEquals("atanh is only defined for |x| < 1", exception.getMessage());
    }
}
