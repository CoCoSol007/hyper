/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic.caster;

import dev.cocosol.hyperbolic.Point;
import dev.cocosol.hyperbolic.paving.Paving;


public class Caster {
    // FOV in radians
    public static final double FOV = 2;

    public Paving paving;

    public int width;
    
    public int height;

    public int seed;

    public Caster(Paving paving, int width, int height, int seed) {
        this.paving = paving;
        this.width = width;
        this.height = height;
        this.seed = seed;
    }

    public Point[] castRay() {
        Point[] intersectionPoints = new Point[this.width];
        for (int i = 0; i < this.width; i++) {
            Ray ray = new Ray(FOV * (0.5 - (i / (double) (this.width))) + Math.PI / 2, seed);
            intersectionPoints[i] = ray.throwRay(paving.centerChunk);
        }
        return intersectionPoints;
    }

    public Point[] endPointsRay() {
        Point[] endPoints = new Point[this.width];
        for (int i = 0; i < this.width; i++) {
            Ray ray = new Ray(FOV * (0.5 - (i / (double) (this.width))) + Math.PI / 2, seed);
            endPoints[i] = ray.end;
        }
        return endPoints;
    }
}