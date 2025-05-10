# Understanding 3D Visual Effects in Hyperbolic Geometry

https://github.com/user-attachments/assets/5156987d-9099-42a6-9bd4-1a86abcd819e

In the 3D rendering engine, two striking visual effects consistently appear when the viewpoint moves: an apparent **rotation during lateral movement**, and a **zooming effect when moving forward**. While these phenomena may seem strange to an observer used to Euclidean geometry, they arise naturally from the fundamental properties of hyperbolic space.

## Zooming Effect When Moving Forward

When the observer moves forward in hyperbolic space, objects seem to grow much more dramatically than they would in Euclidean geometry. This is not a bug or a perspective error—it's a reflection of the **metric structure of hyperbolic space**.

In this space, distances expand **exponentially** relative to the center of the model (such as in the Poincaré or Klein models). Thus, walking one unit forward brings you **much closer** to nearby objects than it would in flat space, resulting in a pronounced **zoom effect**. This is a direct consequence of increased spatial density: with each step, you cover a “larger” region of space than the previous one.

## Apparent Rotation During Lateral Movement

In hyperbolic geometry, the rules of parallelism and parallel transport differ significantly from those of Euclidean space. When moving sideways (for example, to the right), **parallel transport** in hyperbolic space is non-trivial: the orientation of a "fixed" vector changes as you move, even if you're maintaining a constant heading.

You can better visualize this effect by running the 2D Poincaré renderer. And move to the right or left to see how the paving changes orientation.

```bash
./gradlew run2D # On Mac or Linux
./gradlew.bat run2D # On Windows
```
