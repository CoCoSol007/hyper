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

    public Point[] castRayIntersectionPoint() {
        Point[] distances = new Point[this.width];
        for (int i = 0; i < this.width; i++) {
            Ray ray = new Ray(FOV * ((i / (double) (this.width)) - 0.5) + Math.PI / 2, seed);
            distances[i] = ray.throwRayIntersectionPoint(paving.centerChunk);
        }
        return distances;
    }

    public double[] castRay() {
        double[] distances = new double[this.width];
        for (int i = 0; i < this.width; i++) {
            Ray ray = new Ray(FOV * ((i / (double) (this.width)) - 0.5), seed);
            distances[i] = ray.throwRay(paving.centerChunk);
        }
        return distances;
    }
}