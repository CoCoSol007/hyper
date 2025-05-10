# Implementation

In this chapter, we describe the implementation of a hyperbolic tiling system in Java. We explore how to combine [isometries](../I/isometries.md), a [coordinate system](./coordinates_system.md), and some fundamental mathematical tools to construct a dynamic and interactive 2D representation of hyperbolic space.

## Overview

Hyperbolic space allows infinitely many types of tessellations. However, for a given `{p,q}` tessellation — where `p` is the number of polygon sides and `q` is the number of polygons meeting at each vertex — all such tilings must be of consistent size and shape.

We model the hyperbolic plane within a Euclidean coordinate system by embedding it in a unit disk. The center of this disk is denoted as $C_\infty$, and all transformations (rotations, translations) are applied relative to this center.

## Tile Initialization

We begin by constructing the initial tile (a quadrilateral, for simplicity) centered at $C_\infty$. This tile is defined by:

- **4 vertices**: the corners of the polygon in Euclidean coordinates,
- **a list of directions**: which define neighboring tiles.

These two elements are stored in a structure we call a _chunk_.

To compute the distance from the center $C_\infty$ to a vertex of the central polygon, we use the following formula:

$$
d = \sqrt{\frac{\tan\left(\frac{\pi}{2} - \frac{\pi}{q}\right) - \tan\left(\frac{\pi}{p}\right)}{\tan\left(\frac{\pi}{2} - \frac{\pi}{q}\right) + \tan\left(\frac{\pi}{p}\right)}}
$$

This equation provides the Euclidean radius of a polygon that conforms to the curvature of the hyperbolic plane when placed at the center of the Poincaré disk.

More information about the initialization of the tiling can be found [here](https://www.malinc.se/noneuclidean/en/poincaretiling.php).

## Handling Transformations

### Rotation

Rotating in the hyperbolic plane is straightforward in the Poincaré model. A rotation around the center does not change the chunk we are in — the polygon rotates, but it stays within the same area of the disk. Thus, to apply a rotation:

- We simply rotate all the vertices of the current tile.
- No additional chunk updates are needed.

### Translation

Translations are more complex. In hyperbolic space, translating a tile may cause it to cross into a neighboring chunk. In such cases:

- We must detect whether the current position exits its chunk.
- If it does, we determine through which edge it exited.
- Then, we move the center to the neighboring chunk associated with that edge.

This logic is implemented in the following Java code:

#### Code Explanation

```java
// Translation loop: move until the current tile stays inside its chunk
while (true) {
    Point[] exitingEdge = findExitEdge();
    if (exitingEdge == null) {
        break; // Still inside the current chunk
    }

    Direction dir = centerChunk.getDirectionFromPoints(exitingEdge[0], exitingEdge[1]);
    centerChunk = centerChunk.getNeighbors(dir); // Move to the next chunk
}
```

#### `findExitEdge` Method

This method detects if the tile has crossed the boundary of its chunk. The key idea is to compare the orientation of each edge from the perspective of:

1. The **center of the current chunk** (which is inside),
2. The **center of the disk** (assumed to be origin `(0,0)`, considered "outside" or "inside").

If the orientation signs differ, the edge has been crossed:

```java
public Point[] findExitEdge() {
    Point[] quad = centerChunk.vertices.toArray(new Point[0]);

    for (int i = 0; i < 4; i++) {
        Point p1 = quad[i];
        Point p2 = quad[(i + 1) % 4];

        double orientInside = centerChunk.getCenter().orientation(p1, p2);
        double orientOutside = Point.ORIGIN.orientation(p1, p2);

        if (orientInside * orientOutside < 0) {
            return new Point[] {p1, p2}; // This edge is the exit boundary
        }
    }
    return null; // No exit edge found
}
```

## Generation of the neighbor chunks

To render the hyperbolic tiling beyond the central tile, we need to generate all adjacent _chunks_ — that is, the tiles directly or indirectly connected to the central one.

The generation of neighbors is done iteratively by expanding layers of adjacency around the center. Each chunk has a fixed number of sides (in our case, four), and each side corresponds to a possible direction toward a neighboring chunk.

To find a neighboring chunk in a given direction, we use **reflection**. This operation is covered in the [isometries](../I/isometries.md) chapter: each edge of a polygon defines a mirror line. Reflecting the tile across this line gives the adjacent tile in that direction. By repeating this process across different edges and tiles, we can navigate the infinite structure of hyperbolic space.

### Neighbor Expansion by Depth

The algorithm works recursively:

- At **depth 0**, only the central chunk is present.
- At **depth 1**, we compute the 4 direct neighbors by reflecting the central chunk across each of its 4 edges.
- At **depth 2**, we repeat the process for each of those neighbors, adding any new chunks that weren’t already generated.
- And so on, until we reach the desired depth.

This mechanism ensures that we systematically explore the tiling, without duplicating chunks. Each newly generated chunk is compared to those already known, and only unique ones are added. The result is a "wave" of reflections propagating outward from the center.
