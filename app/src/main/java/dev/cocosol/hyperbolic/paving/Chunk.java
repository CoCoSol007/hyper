package dev.cocosol.hyperbolic.paving;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/// A chunk is a simple case of the tiling
public class Chunk {

    /// The origin of the map
    public static Chunk ORIGIN = new Chunk(List.of());
    /// The way we represent the position of the chunk
    private final List<Direction> directions;
    /// The way we save the holonomy of  the movement
    private final List<Direction> globalChunk;

    public Chunk(List<Direction> directions) {
        this.globalChunk = directions;
        this.directions = simplifyDirections(directions);
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

    /// Get the hash of the chunk with the given seed
    public boolean getHash(int seed) {
        int num = this.encode();
        long combinedSeed = (long) seed * 31 + num;
        int randomValue = new Random(combinedSeed).nextInt();
        return (randomValue & Integer.MAX_VALUE % 2) == 1;
    }

    /// Get the neighbors
    public Chunk[] neighbors() {
        Chunk[] chunks = new Chunk[4];
        for (int i = 0; i < 4; i++) {
            Direction toAdd = Direction.values()[i];
            List<Direction> newDirections = new ArrayList<>(this.directions);
            newDirections.add(toAdd);
            chunks[i] = new Chunk(newDirections);
        }
        return chunks;
    }

    /// Check if two chunks are the same place
    public boolean isTheSamePlace(Chunk chunk) {
        return this.directions.equals(chunk.directions);
    }

    /// Check if two position are the same place and is the same holonomy
    public boolean isTheSameHolonomy(Chunk other) {
        return this.isTheSamePlace(other) 
        && this.applyDirection(Direction.FORWARD).equals(other.applyDirection(Direction.FORWARD));
    }

    /// Apply a direction to the chunk
    public Chunk applyDirection(Direction direction) {
        List<Direction> newDirection = new ArrayList<Direction>();
        newDirection.addAll(this.globalChunk);
        newDirection.add(direction);
        return new Chunk(newDirection);
    }
}