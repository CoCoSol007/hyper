/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol.hyperbolic.paving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.cocosol.hyperbolic.Geodesic;
import dev.cocosol.hyperbolic.HyperbolicMath;
import dev.cocosol.hyperbolic.Point;

/// A chunk is a simple case of the tiling
public class Chunk {
    /// The way we represent the position of the chunk
    private final List<Direction> directions;
    /// The way we save the holonomy of  the movement
    private final List<Direction> globalChunk;
    ///  All the points of the chunk
    public final List<Point> vertices;

    public Chunk(List<Direction> directions, Point[] points) {
        this.globalChunk = directions;
        this.directions = simplifyDirections(directions);

        Point topRight = points[0];
        Point topLeft = points[1];
        Point bottomLeft = points[2];
        Point bottomRight = points[3];

        this.vertices = new ArrayList<>();
        Collections.addAll(this.vertices, topRight, topLeft, bottomLeft, bottomRight);
    }

    /// The origin of the map
    public static Chunk ORIGIN() {
        double position = size();
        Point topRight = new Point(position, position);
        Point topLeft = new Point(-position, position);
        Point bottomLeft = new Point(-position, -position);
        Point bottomRight = new Point(position, -position);

        return new Chunk(List.of(), new Point[]{topRight, topLeft, bottomLeft, bottomRight});
    }

    ///  The size of the chunk with the current paving
    private static double size() {
        double numerator = Math.tan(Math.PI / 2 - Math.PI / 5) - Math.tan(Math.PI / 4);
        double denominator = Math.tan(Math.PI / 2 - Math.PI / 5) + Math.tan(Math.PI / 4);
        return Math.sqrt(numerator / (denominator * 2));
    }

    /// A function to simplify a list of directions recursively
    private static List<Direction> simplifyDirections(List<Direction> directions) {
        List<Direction> simplified = applySimplifications(directions);
        if (simplified.equals(directions)) {
            return simplified;
        }
        return simplifyDirections(simplified);
    }

    /// A function to apply a simplifications
    private static List<Direction> applySimplifications(List<Direction> input) {

        // Rule n°1: Undo backward
        if (input.size() < 2) {
            return input;
        }

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
                }
                break;
            }
            firstPass.add(fst);
            if (last) {
                firstPass.add(cur);
            }
        }


        // Rule n°2: Merge directions (for example L + R + R => F + L)
        // It can only be applied if there are at least 3 directions
        if (firstPass.size() < 3) {
            return firstPass;
        }

        List<Direction> secondPass = new ArrayList<>();
        for (int i = 2; i < firstPass.size(); i++) {
            Direction fst = firstPass.get(i - 2);
            Direction snd = firstPass.get(i - 1);
            Direction cur = firstPass.get(i);

            // Check if we are at the last element
            boolean last = i == firstPass.size() - 1;

            if (snd == cur && cur == Direction.RIGHT) {
                secondPass.addAll(List.of(new Direction[]{fst.clockwise(), Direction.LEFT}));
                if (!last) {
                    secondPass.add(firstPass.get(i + 1).clockwise());
                    secondPass.addAll(firstPass.subList(i + 2, firstPass.size()));
                }
                break;
            }

            if (snd == cur && cur == Direction.LEFT) {
                secondPass.addAll(List.of(new Direction[]{fst.anticlockwise(), Direction.RIGHT}));
                if (!last) {
                    secondPass.add(firstPass.get(i + 1).anticlockwise());
                    secondPass.addAll(firstPass.subList(i + 2, firstPass.size()));
                }
                break;
            }
            secondPass.add(fst);
            if (last) {
                secondPass.add(snd);
                secondPass.add(cur);
            }
        }
        return secondPass;
    }

    public String toString() {
        return this.directions.toString();
    }

    /// Encode the chunk
    public int encode() {
        int result = 0;
        for (int i = 0; i < this.directions.size(); i++) {
            Direction direction = this.directions.get(i);
            result += (int) (direction.ordinal() * Math.pow(4, i));
        }
        return result;
    }

    /// Get a pseudorandom boolean based on a seed, a chunk and a direction
    public boolean getHash(int seed, Direction direction) {
        Chunk nextChunk = getNeighbors(direction);
        int num1 = nextChunk.encode();
        int num2 = this.encode();

        int a = Math.min(num1, num2);
        int b = Math.max(num1, num2);

        long hash = seed;
        hash ^= 0x9E3779B97F4A7C15L;
        hash ^= ((long) a << 32) | (b & 0xFFFFFFFFL);
        hash = Long.rotateLeft(hash * 0xBF58476D1CE4E5B9L, 31);
        hash *= 0x94D049BB133111EBL;
        hash ^= (hash >>> 33);

        return (hash & 1) == 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Chunk other = (Chunk) obj;
        return this.directions.equals(other.directions);

    }

    /// Get the neighbors of the chunk in a given direction
    public Chunk getNeighbors(Direction direction) {
        Geodesic geodesic = Geodesic.fromTwoPoints(getPointFromDirection(direction)[0], getPointFromDirection(direction)[1]);
        Point[] newPoint = switch (direction) {
            case FORWARD -> new Point[]{vertices.get(3), vertices.get(2), vertices.get(1), vertices.get(0)};
            case BACKWARD -> new Point[]{vertices.get(1), vertices.get(0), vertices.get(3), vertices.get(2)};
            case LEFT -> new Point[]{vertices.get(0), vertices.get(3), vertices.get(2), vertices.get(1)};
            case RIGHT -> new Point[]{vertices.get(2), vertices.get(1), vertices.get(0), vertices.get(3)};
        };
        for (int i = 0; i < 4; i++) {
            newPoint[i] = HyperbolicMath.inverseWithRespectToGeodesic(newPoint[i], geodesic);
        }

        List<Direction> directions = new ArrayList<>(globalChunk);
        directions.add(direction);

        return new Chunk(directions, newPoint);
    }


    /// Get the point from a direction
    public Point[] getPointFromDirection(Direction direction) {
        int index = switch (direction) {
            case FORWARD -> 0;
            case LEFT -> 1;
            case BACKWARD -> 2;
            case RIGHT -> 3;
        };
        return new Point[] {vertices.get(index),vertices.get((index+1)%4)};
    }

    /// Get the direction from two points
    public Direction getDirectionFromPoints(Point a, Point b) {
        int index = -1;
        for (int i = 0; i < 4; i ++) {
            if (vertices.get(i).equals(a) && vertices.get((i+1)%4).equals(b)) {
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
}