# Basic mathematics in the Poincaré disk model

## Mapping the Two-Sheeted Hyperboloid onto the Poincaré Disk

### hyperboloid

In hyperbolic geometry, one elegant way to visualize non-Euclidean space is through the Poincaré disk model. This model can be derived from a higher-dimensional perspective by considering a two-sheeted hyperboloid. By introducing an extra dimension, we can construct the hyperboloid where its equation typically takes the form : $$x^2 + y^2 - z^2 = -1$$ with one sheet (usually the upper one) representing the set of points with $z > 0$. 

https://github.com/user-attachments/assets/fc13c232-8755-4ced-a09e-4cbd4d35e02d

### Points

The key idea is to project points from the hyperboloid onto the unit disk. This is done through a central projection that "flattens" the curved hyperbolic space into a disk while preserving angles—a property known as conformality. In this mapping, each point on the hyperboloid is associated with a unique point in the disk, thus providing an intuitive and accurate representation of hyperbolic distances and lines.

#### Math part

Consider the upper sheet of the one-sheeted hyperboloid defined by the equation:
$$H = \{ (a, b, c) \in \mathbb{R}^3 \mid a^2 + b^2 - c^2 = -1, \quad c > 0 \}.$$

The Poincaré disk model is given by the unit disk in the Euclidean plane:
$$D = \{ (x, y) \in \mathbb{R}^2 \mid x^2 + y^2 < 1 \}$$

The stereographic projection from the upper sheet of the hyperboloid model $H$ onto the Poincaré disk $D$ is defined as follows. Given a point $P(a, b, c) \in H $, the corresponding point $P'$ in the disk $D$ is:
$$P' \left( x, y \right) = \left( \frac{a}{c+1}, \frac{b}{c+1} \right).$$

This establishes a map between the upper sheet of the hyperboloid and the unit disk.

*A simple [Desmos](https://www.desmos.com/3d/nh9airbdob) example*

https://github.com/user-attachments/assets/9eab0d88-f3f9-42a8-b193-ebd15bfb2888

### Geodesies

In general geometry, a geodesic is the shortest path between two points in a given space. In Euclidean geometry, geodesics are straight lines. However, in the hyperbolic plane, which follows the principles of hyperbolic geometry, geodesics behave quite differently due to the curvature of the space.

The hyperbolic plane, because of this curvature, the shortest paths, or geodesics, are not straight lines in the usual sense but instead take specific forms depending on the model used to represent the hyperbolic plane.

https://github.com/user-attachments/assets/9a24ac4c-7504-471e-b9ec-f49a3f9c79f5

## Circles

Circles in the hyperbolic plane are also quite different from their Euclidean counterparts. A hyperbolic circle consists of all points at a fixed hyperbolic distance from a given center. However, due to the nature of hyperbolic distance, these circles often appear distorted in Euclidean representations of the hyperbolic plane. In the Poincaré disk model, hyperbolic circles resemble Euclidean circles but have different radii when measured with hyperbolic distance. The key difference is that the hyperbolic radius grows exponentially compared to the Euclidean radius.

https://github.com/user-attachments/assets/7b52a693-1928-4205-96b5-63fa361ec08e

## Geometry in the Poincaré disk model

TODO
