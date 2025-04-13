# Isometries

## Euclidean Isometry

A **Euclidean isometry** is a transformation of the Euclidean space that preserves distances between all points. In other words, if you have a map $ f: \mathbb{R}^n \to \mathbb{R}^n $, it is an isometry if, for every pair of points $ x $ and $ y $ in the space, the distance between $ x $ and $ y $ is exactly the same as the distance between $ f(x) $ and $ f(y) $; mathematically,  
$$
d(f(x), f(y)) = d(x, y).
$$
  
The consequence of this property is that the overall shape, size, and angles of geometric figures remain unchanged by the transformation. Common examples of Euclidean isometries include:

- **Translations:** Shifting every point of a figure by the same distance in a given direction.
- **Rotations:** Turning the figure around a fixed center while keeping the distances from the center the same.
- **Reflections:** Flipping the figure over a line (in the plane) or a plane (in space) such that the mirror image is congruent to the original.
- **Glide Reflections:** A combination of a reflection and a translation along the direction of the reflecting line.

Together, these transformations form the Euclidean group, which encompasses all the symmetries of Euclidean space.

## Non-Euclidean Isometries – An Intuitive Overview

In non-Euclidean geometries, the notion of an isometry retains the essential idea of distance preservation, but it adapts to the underlying curved space. In particular, the isometries of hyperbolic space differ from those of Euclidean and spherical space in the following ways:

- **Hyperbolic Translations:** In hyperbolic space, the concept of a translation differs from the Euclidean case. Instead of sliding along a straight line in flat space, hyperbolic translations move points along curves or geodesics (the equivalent of “straight lines” in hyperbolic geometry) in a manner that preserves the hyperbolic distance. Mathematically, a hyperbolic translation function can be written as a Mobius transformation of the form $ f(z) = \frac{z - a}{1 - \overline{a} z} $, where $ a $ is a complex number.
  
- **Rotations:** Around a fixed point, you can rotate figures similarly to Euclidean rotations, but the way distances and angles behave is governed by the hyperbolic metric. These rotations preserve the structure and distance relationships as defined in the hyperbolic model. Mathematically, a hyperbolic rotation function can be written as a Mobius transformation of the form $ f(z) = e^{i \theta} z $, where $ \theta $ is the angle of rotation.
  
- **Reflections and Glide Reflections:** Just like in Euclidean geometry, hyperbolic geometry allows for reflections across geodesic lines and combinations of reflections and translations (glide reflections), all while preserving the hyperbolic distances. Mathematically, a reflection function can be written as a inverse function with respect to the euclidean circle that represents the geodesic line. We have, where $ R $ is the radius of the circle and $ (x_0, y_0) $ is the center of the circle, $ (x, y) $ is the point of reflection and $ (f(x), g(y)) $ is the reflected point.
$$
f(x) = x_0 + \frac{R^2 (x - x_0)}{(x - x_0)^2 + (y - y_0)^2}
$$
$$
g(y) = y_0 + \frac{R^2 (y - y_0)}{(x - x_0)^2 + (y - y_0)^2}
$$


Intuitively, hyperbolic isometries are all the “symmetry moves” that you can apply without altering the intrinsic geometry of a hyperbolic plane—no stretching or shrinking occurs, only repositioning within the framework of hyperbolic distance. They form a rich group of transformations that often exhibit properties markedly different from our everyday experience of Euclidean space.



