# Introduction to Tessellation

# Euclidean paving

A [Euclidean paving](https://en.wikipedia.org/wiki/Tessellation) is a way of dividing an Euclidean space into smaller pieces. We will focus on the [regular tiling](https://www.mathsisfun.com/geometry/tessellation.html) of the plane, which is a special case of the Euclidean paving.
In order to name a tiling we ne to set 2 variables: $p$ the number of edges of the regular polygon and $q$ the number of polygons around a vertex. 

For example: An hexagonal tiling is a tiling with $p=6$ and $q=3$. The main polygons are the hexagons (6 sides) and around each vertex there are 3 polygons.
![Hexagonal tiling](https://upload.wikimedia.org/wikipedia/commons/7/72/Hexagon_Tiling.svg)

By the way, I created a rust lib called [hexing](https://github.com/cocosol007/hexing) to manipulate hexagonal tilings. 

A triangle tiling is a tiling with $p=3$ and $q=6$. The main polygons are the triangles (3 sides) and around each vertex there are 6 polygons.

![Triangle tiling](https://upload.wikimedia.org/wikipedia/commons/2/23/Triangle_Tiling.svg)

In fact for a Euclidean tiling we have to follow :
$\frac{1}{p} + \frac{1}{q} = \frac{1}{2}$, thus it exists only 3 cases of tiling in the euclidean plane: the triangle tiling, the square tiling and the hexagonal tiling.

## Hyperbolic paving

In hyperbolic space the condition $\frac{1}{p} + \frac{1}{q} = \frac{1}{2}$ is a little bit different. In fact, we have $\frac{1}{p} + \frac{1}{q} < \frac{1}{2}$. We can create of infinite types of hyperbolic tilings. 

### Some examples

![Hyperbolic tiling](/II/37.jpg)
![Hyperbolic tiling](/II/45.jpg)
![Hyperbolic tiling](/II/73.jpg)

In [this website](https://www.malinc.se/noneuclidean/en/poincaretiling.php) you can chose your own tiling.