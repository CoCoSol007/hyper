/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.caster;

import dev.cocosol.Point;
import dev.cocosol.Segment;
import dev.cocosol.hyperbolic.Distance;
import dev.cocosol.hyperbolic.Geodesic;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;

/**
 * The Ray class represents a single ray in the Poincaré disk.
 * It computes its intersection with paving boundaries by propagating
 * through chunks until hitting a wall.
 */
public class Ray {
    /**
     * The unit vector representing the ray’s direction (its endpoint on the unit circle).
     * This is the direction of the ray.
     */
    public Point end;

    // A seed used to determine wall presence in chunks.
    public int wallSeed;

    /**
     * Constructs a ray with a given angle and seed.
     *
     * @param angle    the angle (in radians) determining the ray's direction.
     * @param wallSeed the seed used for determining the presence of walls.
     */
    public Ray(double angle, int wallSeed) {
        this.end = new Point(Math.cos(angle), Math.sin(angle));
        this.wallSeed = wallSeed;
    }

    /**
     * Checks if the ray (from the origin to its current end) intersects a given segment.
     *
     * @param segment the segment of the paving to check.
     * @return true if there is an intersection, false otherwise.
     */
    public boolean intersectSegment(Segment segment) {
        // Create a segment from the origin to the ray's endpoint and check for intersection.
        return (new Segment(new Point(0, 0), this.end)).intersect(segment);
    }

    /**
     * Computes the Euclidean distance from the origin along the ray to the point
     * where it intersects a given geodesic.
     *
     * <p>
     * The computation solves a quadratic equation:
     * t² + b*t + c = 0, where t corresponds to the scaling factor along the ray's
     * endpoint vector. We choose the smaller root (assuming b is negative) so that the
     * resulting point lies within the unit circle.
     * </p>
     *
     * @param geodesic the geodesic to compute intersection with.
     * @return the scaling factor (distance along the ray) for the intersection point.
     */
    public double euclideanDistanceToGeodesic(Geodesic geodesic) {
        Point center = geodesic.getEuclideanCenter();
        if (center == null) {
            return 0.0;
        }
        double radius = geodesic.getEuclideanRadius();

        // Coefficients for the quadratic equation: t^2 + b*t + c = 0
        double b = -2 * (this.end.x * center.x + this.end.y * center.y);
        double c = center.x * center.x + center.y * center.y - radius * radius;

        double discriminant = b * b - 4 * c;
        if (discriminant < 0) {
            // No intersection found; this should not happen if the geodesic is valid.
            throw new RuntimeException("No intersection with geodesic found");
        }
        // Return the smaller positive root so that the intersection is inside the unit circle.
        return (-b - Math.sqrt(discriminant)) / 2;
    }

    /**
     * Computes the point (in Euclidean coordinates on the unit disk) where this ray
     * intersects the provided geodesic.
     *
     * @param geodesic the geodesic to intersect.
     * @return the intersection point.
     */
    public Point intersectionToGeodesic(Geodesic geodesic) {
        double t = euclideanDistanceToGeodesic(geodesic);
        return new Point(t * this.end.x, t * this.end.y);
    }

    /**
     * Propagates the ray through chunks until an intersection with a wall is found.
     *
     * @param chunk           the current chunk in the paving.
     * @param remainingSteps  the remaining number of propagation steps.
     * @param maxDistance     the maximum distance along the ray to propagate, if it is less than or equal to zero it will be ignored.
     * @return the intersection point with a wall, return null if the ray does not intersect a wall.
     */
    public Point propagate(Chunk chunk, int remainingSteps, double maxDistance) {
        if (remainingSteps == 0) {
            return null;
        }

        // Iterate over all possible directions from the chunk.
        for (Direction direction : Direction.values()) {
            // Get the two boundary points of the chunk in the given direction.
            Point[] boundaryPoints = chunk.getPointFromDirection(direction);
            
            // If the ray does not intersect this segment, continue with the next.
            if (!intersectSegment(new Segment(boundaryPoints[0], boundaryPoints[1]))) {
                continue;
            }
            
            if (maxDistance > 0) {
                Point intersection = intersectionToGeodesic(Geodesic.fromTwoPoints(boundaryPoints[0], boundaryPoints[1]));
                double distToIntersection = Distance.hyperbolicDistanceToCenter(intersection);
                if (distToIntersection >= maxDistance) {
                    return null;
                }
            }
            
            // Check if a wall exists in this direction using the chunk's hash and the wallSeed.
            if (chunk.getHash(wallSeed, direction)) {
                // If there is a wall, return the intersection point with the geodesic representing that wall.
                Geodesic geodesic = Geodesic.fromTwoPoints(boundaryPoints[0], boundaryPoints[1]);
                return intersectionToGeodesic(geodesic);
            }
            // Otherwise, propagate the ray into the neighboring chunk recursively.
            return propagate(chunk.getNeighbors(direction), remainingSteps - 1, maxDistance);
        }

        // If no segment intersection was found, throw an error.
        // throw new RuntimeException("No intersection found");
        return new Point(0, 0);
    }

    /**
     * Initiates the ray throwing process from the given central chunk.
     *
     * @param centerChunk the central chunk of the paving.
     * @return the intersection point of the ray with a wall.
     */
    public Point throwRaySteps(Chunk centerChunk, int steps, double maxDistance) {
        Point intersection = propagate(centerChunk, steps, maxDistance);
        if (intersection == null) {
            return this.end;
        }
        return intersection;
    }
}
