/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.caster;

import java.util.ArrayList;
import java.util.List;

import dev.cocosol.Point;
import dev.cocosol.Segment;
import dev.cocosol.hyperbolic.Distance;
import dev.cocosol.hyperbolic.Geodesic;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;
import dev.cocosol.hyperbolic.transformation.Translation;

public class VerticalRays extends Ray {
    // Maximum number of recursive propagation steps.
    private static final int STEPS = 6;

    private static final float MAX_DISTANCE = 200;

    private final double cameraHeight;
    private final double minimalAngle;
    private final int rayNumber;

    private final double wallHeight;
    private final Sun sun;

    private final Chunk centerChunk;

    private int lastRayDone;

    public final List<Double> shadowsIntensities;

    public VerticalRays(
        double angle, 
        int wallSeed, 
        double cameraHeight, 
        double smallerVerticalAngle, 
        int numberOfDescendingRays, 
        Sun sun, 
        double wallHeight,
        Chunk centerChunk
    ) {
        super(angle, wallSeed);
        this.cameraHeight = cameraHeight;
        this.minimalAngle = smallerVerticalAngle;
        this.rayNumber = numberOfDescendingRays;
        this.sun = sun;
        this.wallHeight = wallHeight;
        this.lastRayDone = 0;
        this.shadowsIntensities = new ArrayList<>();
        this.centerChunk = centerChunk;
    }

    private double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }


    // TODO: avoid duplicate with the propagate method in Ray
    @Override
    public Point propagate(Chunk chunk, int remainingSteps, double maxDistance) {
        if (remainingSteps == 0) {
            // Maximum steps reached, return the current end of the ray.
            return super.end;
        }
        
        // Iterate over all possible directions from the chunk.
        for (Direction direction : Direction.values()) {
            // Get the two boundary points of the chunk in the given direction.
            Point[] boundaryPoints = chunk.getPointFromDirection(direction);
            
            // If the ray does not intersect this segment, continue with the next.
            if (!super.intersectSegment(new Segment(boundaryPoints[0], boundaryPoints[1]))) {
                continue;
            }
            
            Point intersection = super.intersectionToGeodesic(Geodesic.fromTwoPoints(boundaryPoints[0], boundaryPoints[1]));
            double distToIntersection = Distance.hyperbolicDistanceToCenter(intersection);

            if (distToIntersection >= maxDistance && maxDistance > 0) {
                return null;
            }
            
            for (int i = this.lastRayDone; i < this.rayNumber; i++) {
                double angle = lerp(this.minimalAngle, Math.PI / 2, (double) i / this.rayNumber);
                double distance = this.cameraHeight * Math.tan(angle);

                if (distance > distToIntersection) {
                    break;
                }
                this.lastRayDone = i + 1;

                
                Point startPoint = end.mul(Distance.hyperbolicToEuclideanDistanceToCenter(distance));
                Translation translation = new Translation(startPoint);
                
                Chunk chunkCopy = chunk.CopyChunk();
                chunkCopy.translate(translation);

                ShadowRay shadowRay = new ShadowRay(this.wallHeight, this.sun, this.wallSeed, chunkCopy);
                double intensity = shadowRay.shadowIntensity();
                this.shadowsIntensities.add(intensity);
            }

            // Check if a wall exists in this direction using the chunk's hash and the wallSeed.
            if (chunk.getHash(wallSeed, direction)) {
                // If there is a wall, return the intersection point with the geodesic representing that wall.
                return intersection;
            }
            // Otherwise, propagate the ray into the neighboring chunk recursively.
            return propagate(chunk.getNeighbors(direction), remainingSteps - 1, maxDistance);
        }

        // If no segment intersection was found, its abnormal, return the origin.
        return new Point(0, 0);
    }

    public Point throwRays() {
        return propagate(this.centerChunk, STEPS, MAX_DISTANCE);
    }
}
