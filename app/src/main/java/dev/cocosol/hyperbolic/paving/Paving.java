/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic.paving;

import java.util.ArrayList;
import java.util.List;

import dev.cocosol.Complex;
import dev.cocosol.hyperbolic.Point;
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
     */
    public Chunk centerChunk = Chunk.ORIGIN();

    /**
     * Applies a translational movement in the hyperbolic plane,
     * based on the given angle. The movement simulates a small step
     * in the specified direction.
     *
     * @param angle the angle (in radians) indicating the direction of movement
     */
    public void applyMovement(double angle) {
        double SPEED = 0.01;
        Complex newCenter = Complex.exponent(SPEED, angle);
        Translation translation = new Translation(Point.fromComplex(newCenter));

        for (int i = 0; i < 4; i++) {
            Point p = translation.apply(centerChunk.vertices.get(i));
            centerChunk.vertices.get(i).x = p.x;
            centerChunk.vertices.get(i).y = p.y;
        }

        Direction directionOfChange = null;

        for (Direction direction : Direction.values()) {
            Point[] points = centerChunk.getPointFromDirection(direction.anticlockwise());
            Point vector = points[0].minus(points[1]);
            if (points[0].dot(vector) <  0) {
                directionOfChange = direction;
                break;
            }
        }

        if (directionOfChange == null) {
            return;
        }

        centerChunk = centerChunk.getNeighbors(directionOfChange);
        System.out.println(centerChunk);
    }

    /**
     * Applies a rotation to the current paving centered on the origin,
     * rotating all vertices of the central chunk by a given angle.
     *
     * @param angle the angle (in radians) to rotate
     */
    public void applyRotation(double angle) {
        Rotation rotation = new Rotation(angle);
        for (int i = 0; i < 4; i++) {
            Point p = rotation.apply(centerChunk.vertices.get(i));
            centerChunk.vertices.get(i).x = p.x;
            centerChunk.vertices.get(i).y = p.y;
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
    public List<Chunk> getAllNeighbors(int n) {
        if (n == 0) {
            return new ArrayList<>(List.of(centerChunk));
        }
        List<Chunk> neighbors = getAllNeighbors(n - 1);
        for (Chunk chunk : new ArrayList<>(neighbors)) {
            for (Direction direction : Direction.values()) {
                Chunk newChunk = chunk.getNeighbors(direction);
                if (!neighbors.contains(newChunk)) {
                    neighbors.add(newChunk);
                }
            }
        }
        return neighbors;
    }
}