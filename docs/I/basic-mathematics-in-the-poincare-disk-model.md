# Basic mathematics in the Poincaré disk model

## Mapping the Two-Sheeted Hyperboloid onto the Poincaré Disk

### hyperboloid

In hyperbolic geometry, one elegant way to visualize non-Euclidean space is through the Poincaré disk model. This model can be derived from a higher-dimensional perspective by considering a [two-sheeted hyperboloid](https://en.wikipedia.org/wiki/Hyperboloid#Hyperboloid_of_two_sheets). By introducing an extra dimension, we can construct the [hyperboloid](https://en.wikipedia.org/wiki/Hyperboloid) where its equation typically takes the form : $$x^2 + y^2 - z^2 = -1$$ with one sheet (usually the upper one) representing the set of points with $z > 0$.

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

_A simple [Desmos](https://www.desmos.com/3d/nh9airbdob) example_

https://github.com/user-attachments/assets/43b8c25a-ea0f-447e-a6e8-42bb7ddf9f10

### Geodesies

In general geometry, a [geodesic](https://en.wikipedia.org/wiki/Geodesic) is the shortest path between two points in a given space. In Euclidean geometry, geodesics are straight lines. However, in the hyperbolic plane, which follows the principles of hyperbolic geometry, geodesics behave quite differently due to the curvature of the space.

The hyperbolic plane, because of this curvature, the shortest paths, or geodesics, are not straight lines in the usual sense but instead take specific forms depending on the model used to represent the hyperbolic plane.

https://github.com/user-attachments/assets/9a24ac4c-7504-471e-b9ec-f49a3f9c79f5

### Circles

Circles in the hyperbolic plane are also quite different from their Euclidean counterparts. A hyperbolic circle consists of all points at a fixed hyperbolic distance from a given center. However, due to the nature of hyperbolic distance, these circles often appear distorted in Euclidean representations of the hyperbolic plane. In the Poincaré disk model, hyperbolic circles resemble Euclidean circles but have different radii when measured with hyperbolic distance. The key difference is that the hyperbolic radius grows exponentially compared to the Euclidean radius.

https://github.com/user-attachments/assets/7b52a693-1928-4205-96b5-63fa361ec08e

## Geometry in the Poincaré disk model

The Poincaré disk model provides a conformal map of the hyperbolic plane onto the Euclidean unit disk $D={(x,y)∈R^2∣x^2+y^2<1}$. This means that while distances and straight lines (geodesics) are represented differently, angles between intersecting curves are preserved just as they are in the hyperbolic space itself.

### Hyperbolic Distance (Intuitive Notion)

One of the most striking features of the Poincaré disk is how it represents hyperbolic distance. Unlike the Euclidean plane where distance is uniform, in the Poincaré disk, distances become increasingly large as you approach the boundary circle ($x^2+y^2=1$). Imagine the disk as a map of an infinite world; the boundary circle represents "infinity." Moving from the center towards the edge requires exponentially more effort (or covers exponentially more hyperbolic distance) for the same Euclidean distance traveled on the map.

For two points P1​ and P2​ inside the disk, the hyperbolic distance d(P1​,P2​) is not simply the Euclidean distance between them. The formula involves logarithms and depends on their positions relative to the center and the boundary. Intuitively, two points close to the boundary, even if they look close in the Euclidean sense within the disk, can be very far apart in terms of hyperbolic distance.

Understanding the reason for this formula is not necessary:

$$
\begin{aligned}
d(u,v)&= 2\ln{\frac{\lVert u-v\rVert + \sqrt{\lVert u\rVert^2 \lVert v\rVert^2 - 2u\cdot v + 1}}{\sqrt{(1-\lVert u\rVert^2)(1-\lVert v\rVert^2)}}}
\end{aligned}
$$

### Geodesics in the Poincaré Disk

In hyperbolic geometry, geodesics are the paths of shortest distance between two points. In the Poincaré disk model, these paths are not Euclidean straight lines (unless they pass through the center of the disk). Instead, geodesics take two forms:

- Diameters: Any straight line segment that passes through the center of the disk (i.e., a diameter) represents a hyperbolic geodesic.
- Circular Arcs Orthogonal to the Boundary: Any arc of a Euclidean circle that intersects the boundary circle ($x^2+y^2=1$) at a right angle (90 degrees) represents a hyperbolic geodesic.

The circular arcs orthogonal to the boundary can be described by two reals a and b such that the circle is equal to $x^2+y^2+a.x+b.y=0$

https://github.com/user-attachments/assets/f39a0f90-59ad-4b11-a409-f35d7ea86962

### Circles (Brief Mention)

Hyperbolic circles (sets of points equidistant from a hyperbolic center) are also represented within the Poincaré disk. They appear as Euclidean circles, but crucially, their Euclidean center does not generally coincide with their hyperbolic center (unless the center is at the origin of the disk). Furthermore, the relationship between the hyperbolic radius and the Euclidean radius is non-linear, reflecting the distortion of distance near the boundary. However, for the scope of this project, we won't delve deeply into the properties and construction of hyperbolic circles.
