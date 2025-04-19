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
     * The four vertices that define the geometry of the chunk in counter-clockwise order.
     */
    public final List<Point> vertices;

    /**
     * The relative path from the origin using direction steps.
     */
    private final List<Direction> directions;

    /**
     * Constructs a chunk with a given direction path and corner points.
     *
     * @param directions the list of directions taken from the origin
     * @param points     the four corner points of the chunk
     */
    public Chunk(final List<Direction> directions, final Point[] points) {
        SimpleEntry<List<Direction>, Direction> entry = simplifyDirections(directions, Direction.FORWARD);
        this.directions = entry.getKey();
        this.holonomy = entry.getValue();

        Point topRight = points[0];
        Point topLeft = points[1];
        Point bottomLeft = points[2];
        Point bottomRight = points[3];

        this.vertices = new ArrayList<>();
        Collections.addAll(this.vertices, topRight, topLeft, bottomLeft, bottomRight);
    }

    /**
     * Returns the origin chunk of the tiling.
     *
     * @return the central chunk at the origin
     */
    public static Chunk origin() {
        double position = size();
        Point topRight = new Point(position, position);
        Point topLeft = new Point(-position, position);
        Point bottomLeft = new Point(-position, -position);
        Point bottomRight = new Point(position, -position);

        return new Chunk(List.of(), new Point[] {topRight, topLeft, bottomLeft, bottomRight});
    }

    /**
     * Computes the standard size of a chunk in the current tiling.
     *
     * @return the size as a double
     */
    private static double size() {
        double numerator = Math.tan(Math.PI / 2 - Math.PI / 5) - Math.tan(Math.PI / 4);
        double denominator = Math.tan(Math.PI / 2 - Math.PI / 5) + Math.tan(Math.PI / 4);
        return Math.sqrt(numerator / (denominator * 2));
    }

    /**
     * Simplifies a direction path recursively based on predefined rules.
     *
     * @param directions the original list of directions
     * @return a simplified list of directions
     */
    private static SimpleEntry<List<Direction>, Direction> simplifyDirections(final List<Direction> directions,
            final Direction holonomy) {
        SimpleEntry<List<Direction>, Direction> entry = applySimplifications(directions, holonomy);

        if (entry.getKey().equals(directions)) {
            return entry;
        }
        return simplifyDirections(entry.getKey(), entry.getValue());
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
        List<Direction> firstPass = new ArrayList<>();
        for (int i = 1; i < input.size(); i++) {
            Direction fst = input.get(i - 1);
            Direction cur = input.get(i);
            boolean last = i == input.size() - 1;

            if (cur == Direction.BACKWARD) {
                if (!last) {
                    Direction newDirection = switch (fst) {
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

        List<Direction> secondPass = new ArrayList<>();
        for (int i = 2; i < firstPass.size(); i++) {
            Direction fst = firstPass.get(i - 2);
            Direction snd = firstPass.get(i - 1);
            Direction cur = firstPass.get(i);

            boolean last = i == firstPass.size() - 1;

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
        List<Direction> thirdPass = new ArrayList<>();

        int i = 0;

        while (i < secondPass.size()) {
            if (i + 1 >= secondPass.size()) {
                thirdPass.add(secondPass.get(i));
                i++;
                continue;
            }

            Direction y = secondPass.get(i);
            Direction r1 = secondPass.get(i + 1);

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
                    int r2Index = currentScanIndex;

                    thirdPass.add(y.clockwise());

                    for (int k = 0; k < fCount; k++) {
                        thirdPass.add(Direction.LEFT);
                        thirdPass.add(Direction.FORWARD);
                    }
                    thirdPass.add(Direction.LEFT);

                    int xIndex = r2Index + 1;
                    if (xIndex < secondPass.size()) {
                        Direction x = secondPass.get(xIndex);
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
            Direction direction = this.directions.get(i);
            result += (int)(direction.ordinal() * Math.pow(4, i));
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
        Chunk nextChunk = getNeighbors(direction);
        int num1 = nextChunk.encode();
        int num2 = this.encode();

        int a = Math.min(num1, num2);
        int b = Math.max(num1, num2);

        long hash = seed;
        hash ^= 0x9E3779B97F4A7C15L;
        hash ^= ((long)a << 32) | (b & 0xFFFFFFFFL);
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
        Geodesic geodesic = Geodesic.fromTwoPoints(getPointFromDirection(direction)[0],
                getPointFromDirection(direction)[1]);
        Point[] newPoint = switch (direction) {
        case FORWARD -> new Point[] {vertices.get(3), vertices.get(2), vertices.get(1), vertices.get(0)};
        case BACKWARD -> new Point[] {vertices.get(1), vertices.get(0), vertices.get(3), vertices.get(2)};
        case LEFT -> new Point[] {vertices.get(0), vertices.get(3), vertices.get(2), vertices.get(1)};
        case RIGHT -> new Point[] {vertices.get(2), vertices.get(1), vertices.get(0), vertices.get(3)};
        };
        for (int i = 0; i < 4; i++) {
            Reflexion reflexion = new Reflexion(geodesic);
            newPoint[i] = reflexion.apply(newPoint[i]);
        }

        List<Direction> newDirections = new ArrayList<>(directions);
        newDirections.add(holonomy.add(direction));
        return new Chunk(newDirections, newPoint);
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
        int index = switch (direction) {
        case FORWARD -> 0;
        case LEFT -> 1;
        case BACKWARD -> 2;
        case RIGHT -> 3;
        };
        return new Point[] {vertices.get(index), vertices.get((index + 1) % 4)};
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
            if (vertices.get(i).equals(a) && vertices.get((i + 1) % 4).equals(b)) {
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
        for (final Direction direction : directions) {
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
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Chunk other = (Chunk)obj;
        return this.directions.equals(other.directions);
    }

    /**
     * Returns the hash code of this chunk.
     * The hash code is determined by the hash code of the chunk's direction path.
     * @return the hash code of this chunk
     */
    @Override
    public int hashCode() {
        return directions.hashCode();
    }
}
