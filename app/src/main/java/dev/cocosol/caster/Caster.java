/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.caster;

import dev.cocosol.hyperbolic.paving.Paving;

/**
 * The Caster class handles the ray casting for the Poincaré disk visualization.
 * It creates rays evenly distributed within the Field of View (FOV) and computes
 * their intersection points with the hyperbolic paving.
 */
public class Caster {
    // Field Of View (in radians) for ray casting.
    public static final double FOV = 1.5;

    // Represents the hyperbolic paving (tiled space).
    public Paving paving;

    // Screen dimensions for rendering (number of rays corresponds to screen width).
    public int screenWidth;
    public int screenHeight;

    public Sun sun;
    public double cameraHeight;
    public double wallHeight;

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
    public Caster(Paving paving, int screenWidth, int screenHeight, Sun sun, double wallHeight, double cameraHeight, int wallSeed) {
        this.paving = paving;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.sun = sun;
        this.wallHeight = wallHeight;
        this.cameraHeight = cameraHeight;
        this.wallSeed = wallSeed;
    }

    /**
     * Casts rays from the center of the disk and computes their intersection points
     * with the paving boundaries.
     *
     * @return an array of intersection points in Euclidean space (on the unit disk).
     */
    public CasterResult castRay() {
        CasterResult result = new CasterResult(screenWidth);
        for (int i = 0; i < this.screenWidth; i++) {
            // Compute the angle of the current ray.
            // The rays are distributed across the FOV, with a half FOV offset and an added PI/2 rotation.
            double angle = Caster.FOV * (0.5 - (i / (double) this.screenWidth)) + Math.PI / 2;
            VerticalRays verticalRays = new VerticalRays(
                angle, 
                this.wallSeed, 
                this.cameraHeight, 
                smallerVerticalAngle(), 
                this.screenHeight / 2, 
                this.sun,
                wallHeight,
                this.paving.centerChunk
            );
            // The ray is thrown from the central chunk of the paving
            result.intersectionPoints[i] = verticalRays.throwRays();

            // Can be done because the method throwRays has been called and so shadowsIntensities is not empty.
            result.shadowsIntensity[i] = verticalRays.shadowsIntensities;
        }
        return result;
    }

    public double smallerVerticalAngle() {
        double verticalFOV = Caster.FOV * ((double) this.screenHeight / (double) this.screenWidth);
        return (Math.PI * 0.5) - (verticalFOV * 0.5);
    }
}
