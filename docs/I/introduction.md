# introduction

## Introduction to Euclidean Geometry & Beyond

For centuries, [Euclidean geometry](https://en.wikipedia.org/wiki/Euclidean_geometry) was seen as the absolute framework
to describe space. It’s built on a handful of simple, seemingly self-evident axioms—basic truths about points, lines,
and planes. Among these, one stands out: the Fifth Axiom, or
the [Parallel Postulate](https://en.wikipedia.org/wiki/Parallel_postulate), which essentially states that through any
point not on a given line, there is exactly one line parallel to the given line.

But here’s where things get interesting. Mathematicians eventually realized that this axiom isn’t the only way to build
a coherent, consistent geometry. If you tweak or reject the Fifth Axiom, you open the door to entirely new types of
geometries — [non-Euclidean](https://en.wikipedia.org/wiki/Non-Euclidean_geometry) ones. These are just as logically
valid, but they describe worlds where the rules of parallel lines, angles, and distances behave differently.

To understand these "strange" geometries, it helps to think about how we represent dimensions. Our computer screens,
after all, are two-dimensional, but we often view convincing three-dimensional images on them. This illusion comes from
projections—rules for translating 3D objects into a 2D view, like orthographic or perspective projection. Similarly, we
can visualize [non-Euclidean spaces by projecting](https://www.youtube.com/watch?v=xHvAqDuWG2M) them onto familiar 2D
surfaces.

One of the most famous of these non-Euclidean spaces
is [hyperbolic geometry](https://en.wikipedia.org/wiki/Hyperbolic_geometry), where the Parallel Postulate fails
dramatically—infinitely many lines can pass through a point and never intersect a given line. There’s
also [elliptic geometry](https://en.wikipedia.org/wiki/Elliptic_geometry), but we won’t focus much on that here.
Instead, we’ll dive into hyperbolic space and explore it through a powerful
tool: [the Poincaré disk model](https://en.wikipedia.org/wiki/Poincar%C3%A9_disk_model).

## The Poincaré disk model

![poincaré disk model](/docs/disk.svg)

In the [Poincaré disk](https://en.wikipedia.org/wiki/Poincar%C3%A9_disk_model), the entire infinite hyperbolic plane is
compressed into the interior of a circle. But there’s a catch—the edge of the circle represents points at infinity. Just
like how, in real life, the horizon feels endlessly far away no matter how far you travel, the boundary of the Poincaré
disk gives a visual sense of infinity. It’s similar to how parallel lines in a perspective drawing appear to meet at a
vanishing point: it's not that the lines actually meet, but our projection forces that behavior.

## Project Overview and Objectives

Building on this foundation of non-Euclidean exploration, this project serves as a dynamic 3D rendering engine that
leverages [ray-casting](https://en.wikipedia.org/wiki/Ray_casting) techniques to navigate an infinite maze set in a
hyperbolic world—visualized through the Poincaré disk model. Rather than simply crafting a labyrinth to solve for
amusement, the primary goal is educational. This project acts as a concise yet comprehensive tutorial, offering insights
into several advanced concepts:

- **Non-Euclidean Spaces:** An exploration of geometries beyond the Euclidean, with a focus on hyperbolic space and its
  unique properties.
- **The Poincaré Disk:** A detailed look at how the infinite hyperbolic plane is mapped within a finite circle,
  providing an intuitive grasp of infinity and projection.
- **Hyperbolic Tiling:** An examination of the intricate patterns that emerge
  from [tessellations in hyperbolic geometry](http://aleph0.clarku.edu/~djoyce/poincare/poincare.html).
- **Maze Algorithms and Ray-casting:** A demonstration of algorithmic techniques for maze generation and the application
  of [ray-casting](https://en.wikipedia.org/wiki/Ray_casting) in a 3D environment, all within the context of
  non-Euclidean rules.

Through a series of videos and animations (using the project itself or [Manim](https://www.manim.community/)), this
project is designed to clarify how
these concepts interconnect, offering
both a practical rendering engine and a brief documentation resource. The aim is to provide a hands-on understanding of
non-Euclidean spaces, making abstract mathematical theories accessible through interactive visualizations.
