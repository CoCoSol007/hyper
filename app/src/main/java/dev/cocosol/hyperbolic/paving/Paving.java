/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.paving;

import java.util.ArrayList;
import java.util.List;

import dev.cocosol.Complex;
import dev.cocosol.Point;
import dev.cocosol.hyperbolic.transformation.Rotation;
import dev.cocosol.hyperbolic.transformation.Translation;

/**
 * Represents a paving structure in the hyperbolic disk.
 * This class allows movements and rotations within the tiling,
 * and manages access to neighboring chunks.
 */
public class Paving {

    /**
     * The central chunk located at the origin of the disk.
     * This is the “true” central tile of the paving.
     */
    public Chunk centerChunk = Chunk.origin();

    /**
     * Applies a translational movement in the hyperbolic plane,
     * based on the given angle. The movement simulates a small step
     * in the specified direction.
     *
     * @param angle the angle (in radians) indicating the direction of movement
     */
    public void applyMovement(final double angle, final double speed) {
        final Complex newCenter = Complex.exponent(speed, angle);
        final Translation translation = new Translation(Point.fromComplex(newCenter));

        this.centerChunk.translate(translation);

        // Check if we are in the current chunk
        while (true) {
            final Point[] exitingEdge = this.findExitEdge();
            if (exitingEdge == null) {
                break;
            }
            final Direction dir = this.centerChunk.getDirectionFromPoints(exitingEdge[0], exitingEdge[1]);
            this.centerChunk = this.centerChunk.getNeighbors(dir);
        }
    }

    /**
     * Finds the exit edge of the current chunk based on the orientation of the
     * vertices.
     * 
     * @return an array of two points representing the exit edge, or null if no exit
     *         edge is found
     */
    public Point[] findExitEdge() {
        final Point[] quad = this.centerChunk.vertices.toArray(new Point[0]);

        for (int i = 0; i < 4; i++) {
            final Point p1 = quad[i];
            final Point p2 = quad[(i + 1) % 4];

            final double orientInside = this.centerChunk.getCenter().orientation(p1, p2);
            final double orientOutside = Point.ORIGIN.orientation(p1, p2);

            if (orientInside * orientOutside < 0) {
                return new Point[] { p1, p2 };
            }
        }
        return null;
    }

    /**
     * Applies a rotation to the current paving centered on the origin,
     * rotating all vertices of the central chunk by a given angle.
     *
     * @param angle the angle (in radians) to rotate
     */
    public void applyRotation(final double angle) {
        final Rotation rotation = new Rotation(angle);
        // Apply the rotation transformation to each vertex of the center and
        // approximate chunks.
        for (int i = 0; i < 4; i++) {
            final Point p = rotation.apply(this.centerChunk.vertices.get(i));
            this.centerChunk.vertices.get(i).x = p.x;
            this.centerChunk.vertices.get(i).y = p.y;
        }
    }

    /**
     * Returns a list of all chunks within a specified neighbor depth
     * from the central chunk. The depth defines how many "layers"
     * of neighboring chunks are retrieved.
     *
     * @param n the depth of neighbor retrieval; 0 returns only the center chunk
     * @return a list of all unique neighboring chunks up to the given depth
     */
    public List<Chunk> getAllNeighbors(final int n) {
        if (n == 0) {
            // Base case: only the approximate center is needed.
            return new ArrayList<>(List.of(this.centerChunk));
        }
        final List<Chunk> neighbors = this.getAllNeighbors(n - 1);
        for (final Chunk chunk : new ArrayList<>(neighbors)) {
            for (final Direction direction : Direction.values()) {
                final Chunk newChunk = chunk.getNeighbors(direction);
                if (!neighbors.contains(newChunk)) {
                    neighbors.add(newChunk);
                }
            }
        }
        return neighbors;
    }
}
