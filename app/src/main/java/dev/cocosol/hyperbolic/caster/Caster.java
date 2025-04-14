/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.caster;

import dev.cocosol.hyperbolic.Point;
import dev.cocosol.hyperbolic.paving.Paving;

/**
 * The Caster class handles the ray casting for the Poincaré disk visualization.
 * It creates rays evenly distributed within the Field of View (FOV) and computes
 * their intersection points with the hyperbolic paving.
 */
public class Caster {
    // Field Of View (in radians) for ray casting.
    public static final double FOV = 2;

    // Represents the hyperbolic paving (tiled space).
    public Paving paving;

    // Screen dimensions for rendering (number of rays corresponds to screen width).
    public int screenWidth;
    public int screenHeight;

    // A seed value used for wall determination in the paving.
    public int wallSeed;

    /**
     * Constructs a new Caster.
     *
     * @param paving      The hyperbolic paving structure.
     * @param screenWidth The width (number of rays) of the screen.
     * @param screenHeight The height of the screen.
     * @param wallSeed    A seed parameter for wall hashing.
     */
    public Caster(Paving paving, int screenWidth, int screenHeight, int wallSeed) {
        this.paving = paving;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.wallSeed = wallSeed;
    }

    /**
     * Casts rays from the center of the disk and computes their intersection points
     * with the paving boundaries.
     *
     * @return an array of intersection points in Euclidean space (on the unit disk).
     */
    public Point[] castRay() {
        Point[] intersectionPoints = new Point[this.screenWidth];
        for (int i = 0; i < this.screenWidth; i++) {
            // Compute the angle of the current ray.
            // The rays are distributed across the FOV, with a half FOV offset and an added PI/2 rotation.
            double angle = FOV * (0.5 - (i / (double) this.screenWidth)) + Math.PI / 2;
            Ray ray = new Ray(angle, wallSeed);
            // The ray is thrown from the central chunk of the paving
            intersectionPoints[i] = ray.throwRay(paving.centerChunk);
        }
        return intersectionPoints;
    }
}
