package src.hyperbolic.paving;

/// A direction
public enum Direction {
    /// The left direction
    LEFT,

    /// The right direction
    RIGHT,

    /// The forward direction
    FORWARD,

    /// The backward direction
    BACKWARD;

    /// Get the opposite direction
    public Direction opposite() {
        return switch (this) {
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case FORWARD -> BACKWARD;
            case BACKWARD -> FORWARD;
        };
    }

    /// Get the 90 degrees clockwise direction
    public Direction clockwise() {
        return switch (this) {
            case LEFT -> FORWARD;
            case RIGHT -> BACKWARD;
            case FORWARD -> RIGHT;
            case BACKWARD -> LEFT;
        };
    }

    /// Get the 90 degrees anticlockwise direction
    public Direction anticlockwise() {
        return switch (this) {
            case LEFT -> BACKWARD;
            case RIGHT -> FORWARD;
            case FORWARD -> LEFT;
            case BACKWARD -> RIGHT;
        };
    }
}