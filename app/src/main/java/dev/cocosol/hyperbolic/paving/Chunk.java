/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.paving;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.cocosol.Point;
import dev.cocosol.hyperbolic.Geodesic;
import dev.cocosol.hyperbolic.transformation.Reflexion;
import dev.cocosol.hyperbolic.transformation.Translation;

/**
 * Represents a single tile (chunk) in the hyperbolic tiling.
 * Each chunk maintains a list of directions representing its relative position,
 * its global path through the tiling, and its four corner vertices.
 */
public class Chunk {

    /**
     * The direction of the holonomy of the chunk.
     */
    public final Direction holonomy;

    /**
     * The four vertices that define the geometry of the chunk in counter-clockwise
     * order.
     */
    public final List<Point> vertices;

    /**
     * The relative path from the origin using direction steps.
     */
    private final List<Direction> directions;

    /**
     * Constructs a chunk with a given direction path, an holonomy and corner points.
     *
     * @param directions the list of directions taken from the origin, need to be simplified
     * @param holonomy   the direction of the holonomy
     * @param points     the four corner points of the chunk
     */
    // private Chunk(final List<Direction> directions, final Point[] points) {
    private Chunk(final List<Direction> directions, final Direction holonomy, final Point[] points) {
        this.directions = directions;
        this.holonomy = holonomy;
        
        final Point topRight = points[0];
        final Point topLeft = points[1];
        final Point bottomLeft = points[2];
        final Point bottomRight = points[3];
        
        this.vertices = new ArrayList<>();
        Collections.addAll(this.vertices, topRight, topLeft, bottomLeft, bottomRight);
    }
    
    /**
     * The same as the constructor, but does simplify the directions and thus takes more performance.
     *
     * @param directions the list of directions taken from the origin, they do not need to be simplified
     * @param points     the four corner points of the chunk
     */
    public static Chunk newChunk(final List<Direction> directions, final Point[] points) {
        final SimpleEntry<List<Direction>, Direction> entry = Chunk.simplifyDirections(directions, Direction.FORWARD);
        return new Chunk(entry.getKey(), entry.getValue(), points);
    }

    /**
     * Returns the origin chunk of the tiling.
     *
     * @return the central chunk at the origin
     */
    public static Chunk origin() {
        final double position = Chunk.size();
        final Point topRight = new Point(position, position);
        final Point topLeft = new Point(-position, position);
        final Point bottomLeft = new Point(-position, -position);
        final Point bottomRight = new Point(position, -position);

        return newChunk(List.of(), new Point[] { topRight, topLeft, bottomLeft, bottomRight });
    }

    /**
     * Computes the standard size of a chunk in the current tiling.
     *
     * @return the size as a double
     */
    private static double size() {
        final double numerator = Math.tan(Math.PI / 2 - Math.PI / 5) - Math.tan(Math.PI / 4);
        final double denominator = Math.tan(Math.PI / 2 - Math.PI / 5) + Math.tan(Math.PI / 4);
        return Math.sqrt(numerator / (denominator * 2));
    }

    /**
     * Applies a translation to the chunk's vertices.
     *
     * @param translation the translation to apply
     */
    public void translate(Translation translation) {
        // Apply the translation to each vertex of the centerChunk
        for (int i = 0; i < 4; i++) {
            // Update the vertex positions for the center chunk.
            Point p = translation.apply(this.vertices.get(i));
            this.vertices.get(i).x = p.x;
            this.vertices.get(i).y = p.y;
        }
    }

    /**
     * Return a copy of the chunk.
     *
     * @return a copy of the chunk
     */
    public Chunk CopyChunk() {
        Point[] new_vertices = new Point[4];
        for (int i = 0; i < 4; i++) {
            new_vertices[i] = new Point(vertices.get(i).x, vertices.get(i).y);
        }
        return new Chunk(this.directions, this.holonomy, new_vertices);
    }

    /**
     * Simplifies a direction path recursively based on predefined rules.
     *
     * @param directions the original list of directions
     * @return a simplified list of directions
     */
    private static SimpleEntry<List<Direction>, Direction> simplifyDirections(final List<Direction> directions,
            final Direction holonomy) {
        final SimpleEntry<List<Direction>, Direction> entry = Chunk.applySimplifications(directions, holonomy);

        if (entry.getKey().equals(directions)) {
            return entry;
        }
        return Chunk.simplifyDirections(entry.getKey(), entry.getValue());
    }

    /**
     * Applies transformation rules to simplify the direction path.
     *
     * @param input the original direction list
     * @return a partially simplified direction list
     */
    private static SimpleEntry<List<Direction>, Direction> applySimplifications(final List<Direction> input,
            final Direction oldHolonomy) {

        Direction holonomy = oldHolonomy;
        if (input.size() < 2) {
            return new SimpleEntry<>(input, holonomy);
        }

        // Rule 1: Undo BACKWARD operations
        final List<Direction> firstPass = new ArrayList<>();
        for (int i = 1; i < input.size(); i++) {
            final Direction fst = input.get(i - 1);
            final Direction cur = input.get(i);
            final boolean last = i == input.size() - 1;

            if (cur == Direction.BACKWARD) {
                if (!last) {
                    final Direction newDirection = switch (fst) {
                        case LEFT -> input.get(i + 1).clockwise();
                        case RIGHT -> input.get(i + 1).anticlockwise();
                        case FORWARD -> input.get(i + 1).opposite();
                        case BACKWARD -> input.get(i + 1);
                    };
                    firstPass.add(newDirection);
                    firstPass.addAll(input.subList(i + 2, input.size()));
                } else {
                    holonomy = holonomy.add(fst.opposite());
                }
                break;
            }
            firstPass.add(fst);
            if (last) {
                firstPass.add(cur);
            }
        }

        // Rule 2: Merge repeated directions
        if (firstPass.size() < 3) {
            return new SimpleEntry<>(firstPass, holonomy);
        }

        final List<Direction> secondPass = new ArrayList<>();
        for (int i = 2; i < firstPass.size(); i++) {
            final Direction fst = firstPass.get(i - 2);
            final Direction snd = firstPass.get(i - 1);
            final Direction cur = firstPass.get(i);

            final boolean last = i == firstPass.size() - 1;

            if (snd == cur && cur == Direction.RIGHT) {
                secondPass.addAll(List.of(fst.clockwise(), Direction.LEFT));
                if (!last) {
                    secondPass.add(firstPass.get(i + 1).clockwise());
                    secondPass.addAll(firstPass.subList(i + 2, firstPass.size()));
                } else {
                    holonomy = holonomy.clockwise();
                }
                break;
            }

            if (snd == cur && cur == Direction.LEFT) {
                secondPass.addAll(List.of(fst.anticlockwise(), Direction.RIGHT));
                if (!last) {
                    secondPass.add(firstPass.get(i + 1).anticlockwise());
                    secondPass.addAll(firstPass.subList(i + 2, firstPass.size()));
                } else {
                    holonomy = holonomy.anticlockwise();
                }
                break;
            }

            secondPass.add(fst);
            if (last) {
                secondPass.add(snd);
                secondPass.add(cur);
            }
        }

        // --- Pass 3: Apply LF rule ---
        // Ryle: Y + R (F*n) R + X -> Y.clockwise + (LF*n) L + X.clockwise
        if (secondPass.size() < 4) {
            return new SimpleEntry<>(secondPass, holonomy);
        }
        final List<Direction> thirdPass = new ArrayList<>();

        int i = 0;

        while (i < secondPass.size()) {
            if (i + 1 >= secondPass.size()) {
                thirdPass.add(secondPass.get(i));
                i++;
                continue;
            }

            final Direction y = secondPass.get(i);
            final Direction r1 = secondPass.get(i + 1);

            if (r1 == Direction.RIGHT) {
                int fCount = 0;
                int currentScanIndex = i + 2;

                while (currentScanIndex < secondPass.size()
                        && secondPass.get(currentScanIndex) == Direction.FORWARD) {
                    fCount++;
                    currentScanIndex++;
                }

                if (fCount > 0
                        && currentScanIndex < secondPass.size()
                        && secondPass.get(currentScanIndex) == Direction.RIGHT) {
                    final int r2Index = currentScanIndex;

                    thirdPass.add(y.clockwise());

                    for (int k = 0; k < fCount; k++) {
                        thirdPass.add(Direction.LEFT);
                        thirdPass.add(Direction.FORWARD);
                    }
                    thirdPass.add(Direction.LEFT);

                    final int xIndex = r2Index + 1;
                    if (xIndex < secondPass.size()) {
                        final Direction x = secondPass.get(xIndex);
                        thirdPass.add(x.clockwise());
                        thirdPass.addAll(secondPass.subList(xIndex + 1, secondPass.size()));
                    } else {
                        holonomy = holonomy.clockwise();
                    }

                    break;

                } else {
                    thirdPass.add(y);
                    i++;
                }

            } else {
                thirdPass.add(y);
                i++;
            }
        }

        return new SimpleEntry<>(thirdPass, holonomy);
    }

    /**
     * Encodes the chunk's position into an integer value.
     *
     * @return the encoded integer
     */
    public int encode() {
        int result = 0;
        for (int i = 0; i < this.directions.size(); i++) {
            final Direction direction = this.directions.get(i);
            result += (int) (direction.ordinal() * Math.pow(4, i));
        }
        return result;
    }

    /**
     * Computes a pseudo-random boolean value based on a given seed, current chunk,
     * and direction.
     *
     * @param seed      a random seed
     * @param direction the direction to compute from
     * @return a pseudo-random boolean value
     */
    public boolean getHash(final int seed, final Direction direction) {
        final Chunk nextChunk = this.getNeighbors(direction);
        final int num1 = nextChunk.encode();
        final int num2 = this.encode();

        final int a = Math.min(num1, num2);
        final int b = Math.max(num1, num2);

        long hash = seed;
        hash ^= 0x9E3779B97F4A7C15L;
        hash ^= ((long) a << 32) | (b & 0xFFFFFFFFL);
        hash = Long.rotateLeft(hash * 0xBF58476D1CE4E5B9L, 31);
        hash *= 0x94D049BB133111EBL;
        hash ^= hash >>> 33;

        return (hash & 1) == 1;
    }

    /**
     * Returns the neighboring chunk in the specified direction.
     *
     * @param direction the direction to retrieve the neighbor from
     * @return the neighboring chunk
     */
    public Chunk getNeighbors(final Direction direction) {
        final Geodesic geodesic = Geodesic.fromTwoPoints(this.getPointFromDirection(direction)[0],
                this.getPointFromDirection(direction)[1]);
        final Point[] newPoint = switch (direction) {
            case FORWARD ->
                new Point[] { this.vertices.get(3), this.vertices.get(2), this.vertices.get(1), this.vertices.get(0) };
            case BACKWARD ->
                new Point[] { this.vertices.get(1), this.vertices.get(0), this.vertices.get(3), this.vertices.get(2) };
            case LEFT ->
                new Point[] { this.vertices.get(0), this.vertices.get(3), this.vertices.get(2), this.vertices.get(1) };
            case RIGHT ->
                new Point[] { this.vertices.get(2), this.vertices.get(1), this.vertices.get(0), this.vertices.get(3) };
        };
        for (int i = 0; i < 4; i++) {
            final Reflexion reflexion = new Reflexion(geodesic);
            newPoint[i] = reflexion.apply(newPoint[i]);
        }

        final List<Direction> newDirections = new ArrayList<>(this.directions);
        newDirections.add(this.holonomy.add(direction));
        return newChunk(newDirections, newPoint);
    }

    public Point getCenter() {
        Point sum = new Point(0, 0);
        for (final Point p : this.vertices) {
            sum = sum.plus(p);
        }
        return sum.mul(0.25);
    }

    /**
     * Returns the two points (edge) associated with a given direction.
     *
     * @param direction the direction to query
     * @return an array of two points corresponding to that edge
     */
    public Point[] getPointFromDirection(final Direction direction) {
        final int index = switch (direction) {
            case FORWARD -> 0;
            case LEFT -> 1;
            case BACKWARD -> 2;
            case RIGHT -> 3;
        };
        return new Point[] { this.vertices.get(index), this.vertices.get((index + 1) % 4) };
    }

    /**
     * Determines the direction that corresponds to the given two consecutive
     * points.
     *
     * @param a the first point
     * @param b the second point
     * @return the direction from point a to point b, or null if no match is found
     */
    public Direction getDirectionFromPoints(final Point a, final Point b) {
        int index = -1;
        for (int i = 0; i < 4; i++) {
            if (this.vertices.get(i).equals(a) && this.vertices.get((i + 1) % 4).equals(b)) {
                index = i;
            }
        }
        if (index == -1) {
            return null;
        }
        return switch (index) {
            case 0 -> Direction.FORWARD;
            case 1 -> Direction.LEFT;
            case 2 -> Direction.BACKWARD;
            case 3 -> Direction.RIGHT;
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
    }

    /**
     * Returns a string representation of the chunk's direction path.
     *
     * @return a string showing the chunk's direction history
     */
    @Override
    public String toString() {
        String text = "";
        for (final Direction direction : this.directions) {
            text += direction;
        }
        return text;
    }

    /**
     * Checks whether this chunk is equal to another object.
     * Two chunks are equal if they share the same direction path.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        final Chunk other = (Chunk) obj;
        return this.directions.equals(other.directions);
    }

    /**
     * Returns the hash code of this chunk.
     * The hash code is determined by the hash code of the chunk's direction path.
     * 
     * @return the hash code of this chunk
     */
    @Override
    public int hashCode() {
        return this.directions.hashCode();
    }
}
