# Isometries

## Euclidean Isometry

A **Euclidean [isometry](https://en.wikipedia.org/wiki/Isometry)** is a transformation of the Euclidean space that preserves distances between all points. In other words, if you have a map $f: \mathbb{R}^n \to \mathbb{R}^n$, it is an isometry if, for every pair of points $x$ and $y$ in the space, the distance between $x$ and $y$ is exactly the same as the distance between $f(x)$ and $f(y)$; mathematically,

$$d(f(x), f(y)) = d(x, y).$$
  
The consequence of this property is that the overall shape, size, and angles of geometric figures remain unchanged by the transformation. Common examples of Euclidean isometries include:

- **[Translations](https://en.wikipedia.org/wiki/Translation_(geometry)):** Shifting every point of a figure by the same distance in a given direction.
- **[Rotations](https://en.wikipedia.org/wiki/Rotation_(mathematics)):** Turning the figure around a fixed center while keeping the distances from the center the same.
- **[Reflections](https://en.wikipedia.org/wiki/Reflection_(mathematics)):** Flipping the figure over a line (in the plane) or a plane (in space) such that the mirror image is congruent to the original.

https://github.com/user-attachments/assets/f230a181-cefe-4da6-aec3-966e8b1aa580

Together, these transformations form the Euclidean group, which encompasses all the symmetries of Euclidean space.

## Non-Euclidean Isometries – An Intuitive Overview

> [!IMPORTANT]
> It is useful to understand the key concepts of complex numbers and their geometric interpretation. For more information, see the [Complex Numbers](/docs/complex_numbers.md) page.

In non-Euclidean geometries, the notion of an isometry retains the essential idea of distance preservation, but it adapts to the underlying curved space. In particular, the isometries of hyperbolic space differ from those of Euclidean and spherical space in the following ways:

- **Hyperbolic Translations:** In hyperbolic space, the concept of a translation differs from the Euclidean case. Instead of sliding along a straight line in flat space, hyperbolic translations move points along geodesics in a manner that preserves the hyperbolic distance. Mathematically, a hyperbolic translation function can be written as a [Mobius transformation](https://en.wikipedia.org/wiki/M%C3%B6bius_transformation) of the form $f(z) = \frac{z - a}{1 - \overline{a} z}$, where $a$ is a [complex number](https://en.wikipedia.org/wiki/Complex_number).

https://github.com/user-attachments/assets/c9113e5d-2f8a-4996-af72-3f9cad6f2782

- **Rotations:** Around a fixed point, you can rotate figures similarly to Euclidean rotations, but the way distances and angles behave is governed by the hyperbolic metric. These rotations preserve the structure and distance relationships as defined in the hyperbolic model. Mathematically, a hyperbolic rotation function can be written as a [Mobius transformation](https://en.wikipedia.org/wiki/M%C3%B6bius_transformation) of the form $f(z) = e^{i \theta} z$, where $\theta$ is the angle of rotation.

https://github.com/user-attachments/assets/b7bea3fe-10f0-4d41-838d-b4e75249307a
  
- **Reflections and Glide Reflections:** Just like in Euclidean geometry, hyperbolic geometry allows for reflections across geodesic lines and combinations of reflections and translations (glide reflections), all while preserving the hyperbolic distances. Mathematically, a reflection function can be written as a [inverse function](https://en.wikipedia.org/wiki/Inversive_geometry) with respect to the euclidean circle that represents the geodesic line. We have, where $R$ is the radius of the circle and $(x_0, y_0)$ is the center of the circle, $(x, y)$ is the point of reflection and $(f(x), g(y))$ is the reflected point.

$$f(x) = x_0 + \frac{R^2 (x - x_0)}{(x - x_0)^2 + (y - y_0)^2}$$

$$g(y) = y_0 + \frac{R^2 (y - y_0)}{(x - x_0)^2 + (y - y_0)^2}$$



https://github.com/user-attachments/assets/e2048917-90da-4aa3-92c3-97be560b9f47




Intuitively, hyperbolic isometries are all the “symmetry moves” that you can apply without altering the intrinsic geometry of a hyperbolic plane—no stretching or shrinking occurs, only repositioning within the framework of hyperbolic distance. They form a rich group of transformations that often exhibit properties markedly different from our everyday experience of Euclidean space.



