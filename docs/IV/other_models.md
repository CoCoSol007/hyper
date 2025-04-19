# Other Models

## The Beltrami–Klein Model

Another important representation of the hyperbolic plane is the **[Beltrami–Klein model](https://en.wikipedia.org/wiki/Beltrami%E2%80%93Klein_model)**, also known as the projective model of hyperbolic geometry. Unlike the Poincaré disk model, the Beltrami–Klein model is not [conformal](https://en.wikipedia.org/wiki/Conformal_map), meaning it does not preserve angles. However, it offers other advantages, particularly in how it represents geodesics.

In one of the [earlier chapters](I\basic-mathematics-in-the-poincare-disk-model.md), we introduced the Poincaré disk model, derived by projecting the upper sheet of a two-sheeted [hyperboloid](https://en.wikipedia.org/wiki/Hyperboloid) onto the unit disk in the plane $ z = 0 $. The Beltrami–Klein model is constructed in a similar way, but instead of projecting onto the plane $ z = 0 $, we project the upper sheet of the hyperboloid onto the plane $ z = 1 $. This subtle change leads to a model with distinct geometric properties.

This [animation](https://www.youtube.com/watch?v=9D9AAcbflbU) illustrates the relationship between the Beltrami–Klein and the Poincaré disk models beautifully.

You can also observe the differences between the two models in the image below:
![alt text](image.png)

### Key Characteristics

The Beltrami–Klein model is notable for its **projective nature**. It maps the hyperbolic plane into the interior of the Euclidean unit disk, just like the Poincaré disk model. However, unlike the Poincaré model, it **does not preserve angles**. As a result:
- Angles are distorted, especially near the boundary of the disk.
- Circles in the hyperbolic plane appear as **flattened ellipses**.
- **Geodesics** are represented by **straight line segments** within the disk, connecting points inside the boundary—this is one of the major strengths of the model.

This is in contrast to the Poincaré disk model, which **is conformal**, preserving angles but representing geodesics as arcs of circles orthogonal to the boundary.

### Conversion Between Models

One of the convenient features of these two models is that it's easy to **convert a point from the Poincaré disk model to the Beltrami–Klein model**, and vice versa. Let:

- $\mathbf{p} = (p_x, p_y)$ be the coordinates of a point in the Poincaré disk model, with $\|\mathbf{p}\| < 1$.
- $\mathbf{k} = (k_x, k_y)$ be the coordinates of a point in the Beltrami–Klein model, with $\|\mathbf{k}\| < 1$.

#### From Poincaré to Beltrami–Klein

The mapping $\mathbf{p} \mapsto \mathbf{k}$ is given by:

$$\mathbf{k} = \frac{2\,\mathbf{p}}{1 + \|\mathbf{p}\|^2}.$$

This formula ensures that points in the Poincaré disk are sent to corresponding points in the Klein disk.

#### From Beltrami–Klein to Poincaré

Conversely, the mapping $\mathbf{k} \mapsto \mathbf{p}$ is:

$$\mathbf{p} = \frac{\mathbf{k}}{1 + \sqrt{1 - \|\mathbf{k}\|^2}}.$$

Here, the denominator restores the conformal scaling necessary to recover the Poincaré metric.

For a visual explanation of how to perform this conversion, see this [video](https://www.youtube.com/watch?v=n55NQbG-Uos).

## Half-Plane Model

ToDo...