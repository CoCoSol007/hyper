/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic.paving;

/**
 * Represents a direction in the hyperbolic paving.
 * <p>
 * The four possible directions are {@code LEFT}, {@code RIGHT},
 * {@code FORWARD}, and {@code BACKWARD}.
 * These are typically used to navigate across tiles or apply geometric
 * transformations.
 */
public enum Direction {

    /** The leftward direction. */
    LEFT,

    /** The rightward direction. */
    RIGHT,

    /** The forward direction. */
    FORWARD,

    /** The backward direction. */
    BACKWARD;

    /**
     * Returns the opposite of this direction.
     * <ul>
     * <li>{@code LEFT} ↔ {@code RIGHT}</li>
     * <li>{@code FORWARD} ↔ {@code BACKWARD}</li>
     * </ul>
     *
     * @return the opposite direction
     */
    public Direction opposite() {
        return switch (this) {
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case FORWARD -> BACKWARD;
            case BACKWARD -> FORWARD;
        };
    }

    /**
     * Returns the direction obtained by rotating 90 degrees clockwise from this
     * one.
     *
     * @return the direction after a clockwise rotation
     */
    public Direction clockwise() {
        return switch (this) {
            case LEFT -> FORWARD;
            case RIGHT -> BACKWARD;
            case FORWARD -> RIGHT;
            case BACKWARD -> LEFT;
        };
    }

    /**
     * Returns the direction obtained by rotating 90 degrees counterclockwise from
     * this one.
     *
     * @return the direction after a counterclockwise rotation
     */
    public Direction anticlockwise() {
        return switch (this) {
            case LEFT -> BACKWARD;
            case RIGHT -> FORWARD;
            case FORWARD -> LEFT;
            case BACKWARD -> RIGHT;
        };
    }

    /**
     * Returns the direction obtained by adding the specified direction to this one.
     *
     * @param direction the direction to add
     * @return the resulting direction
     */
    public Direction add(final Direction direction) {
        return switch (direction) {
            case LEFT -> anticlockwise();
            case RIGHT -> clockwise();
            case FORWARD -> this;
            case BACKWARD -> opposite();
        };
    }

    /**
     * Returns a single character string representation of this direction.
     * <ul>
     * <li>{@code LEFT} = {@code "L"}</li>
     * <li>{@code RIGHT} = {@code "R"}</li>
     * <li>{@code FORWARD} = {@code "F"}</li>
     * <li>{@code BACKWARD} = {@code "B"}</li>
     * </ul>
     *
     * @return a single character string representation of this direction
     */
    @Override
    public String toString() {
        return switch (this) {
            case LEFT -> "L";
            case RIGHT -> "R";
            case FORWARD -> "F";
            case BACKWARD -> "B";
        };
    }
}
