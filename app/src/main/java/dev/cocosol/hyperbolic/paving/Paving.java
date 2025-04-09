/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic.paving;

import dev.cocosol.Complex;
import dev.cocosol.hyperbolic.Point;
import dev.cocosol.hyperbolic.transformation.Rotation;
import dev.cocosol.hyperbolic.transformation.Translation;

import java.util.ArrayList;
import java.util.List;

/// A class for the paving
public class Paving {
    /// The main  chunk in the center of the disk
    public Chunk centerChunk = Chunk.ORIGIN();

    /// Apply a movement in the world with a given angle
    public void applyMovement(double angle) {
        // The speed of the movement
        double SPEED = 0.01;
        Complex newCenter = Complex.exponent(SPEED, angle);
        Translation translation = new Translation(Point.fromComplex(newCenter));

        // Apply the translation
        for (int i = 0; i < 4; i++) {
            Point p = translation.apply(centerChunk.vertices.get(i));
            centerChunk.vertices.get(i).x = p.x;
            centerChunk.vertices.get(i).y = p.y;
        }

        // Check if we need to change the chunk
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

    /// Apply a rotation to the world with a given angle
    public void applyRotation(double angle) {
        Rotation rotation = new Rotation(angle, Point.ORIGIN);
        for (int i = 0; i < 4; i++) {
            Point p = rotation.apply(centerChunk.vertices.get(i));
            centerChunk.vertices.get(i).x = p.x;
            centerChunk.vertices.get(i).y = p.y;
        }
    }

    /// Get all the neighbors of the chunk with the given depth
    public List<Chunk> getAllNeighbors(int n) {
        if (n == 0) {
            return new ArrayList<>(List.of(centerChunk));
        }
        List<Chunk> neighbors = getAllNeighbors(n-1);
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