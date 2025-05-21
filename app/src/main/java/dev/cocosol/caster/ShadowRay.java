/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.caster;

import dev.cocosol.Point;
import dev.cocosol.hyperbolic.Distance;
import dev.cocosol.hyperbolic.paving.Chunk;


public class ShadowRay extends Ray {
    // Maximum number of recursive propagation steps.
    private final static int STEP = 3;

    private final static double SMOOTH_SHADOW_RANGE = 0.01;

    private final static double EPSILON = 0.01;

    private final double wallHeight;

    private final Sun sun;

    private final Chunk starterChunk;

    public ShadowRay(double wallHeight, Sun sun, int wallSeed, Chunk starterChunk) {
        super(sun.horizontalAngle, wallSeed);
        this.wallHeight = wallHeight;
        this.sun = sun;
        this.starterChunk = starterChunk;
    }


    /**
     * @return a number between 0 and 1. 0 is no shadow, 1 is full shadow
     */
    public double shadowIntensity() {
        double maxDistance = (wallHeight + SMOOTH_SHADOW_RANGE) / Math.tan(this.sun.verticalAngle) + EPSILON;
        Point point = super.propagate(this.starterChunk, STEP, maxDistance);
        if (point == null) {
            return 0.0;
        }
        double distance = Distance.hyperbolicDistanceToCenter(point);
        double height = Math.tan(this.sun.verticalAngle) * distance;
        if (height > wallHeight + SMOOTH_SHADOW_RANGE) return 0.0;
        if (height < wallHeight - SMOOTH_SHADOW_RANGE) return 1.0;
        return 1.0 - (height - (wallHeight - SMOOTH_SHADOW_RANGE)) / (SMOOTH_SHADOW_RANGE * 2);
    }
}
