from manim import *
import numpy as np
import cmath


class Main(Scene):
    def construct(self):
        disk_radius = 2
        geodesic_center_x = 2.5
        geodesic_center_y = 0
        geodesic_center_coords = np.array([geodesic_center_x, geodesic_center_y, 0])

        geodesic_radius_sq = (
            geodesic_center_x**2 + geodesic_center_y**2 - disk_radius**2
        )
        geodesic_radius = np.sqrt(geodesic_radius_sq)

        grid_step = 0.4
        n_grid_lines = int(2 * disk_radius / grid_step) + 1
        animation_runtime = 5

        def reflect_point_across_circle(point: np.ndarray) -> np.ndarray:
            x, y = point[0], point[1]
            x0, y0 = geodesic_center_coords[0], geodesic_center_coords[1]
            R2 = geodesic_radius_sq

            delta_x = x - x0
            delta_y = y - y0
            dist_sq = delta_x**2 + delta_y**2

            if dist_sq < 1e-12:
                print(
                    f"Warning: Point {point[:2]} is very close to the center of inversion {geodesic_center_coords[:2]}"
                )
                return point

            factor = R2 / dist_sq

            reflected_x = x0 + factor * delta_x
            reflected_y = y0 + factor * delta_y

            return np.array([reflected_x, reflected_y, 0])

        title = Text("Poincaré disk model : Reflexion", font_size=36).to_edge(UP)
        self.play(Write(title))

        disk_boundary = Circle(radius=disk_radius, color=BLUE, stroke_width=2)
        disk_boundary.set_fill(BLUE, opacity=0.1)
        origin_marker = Dot(ORIGIN, radius=0.05, color=YELLOW)

        self.play(Create(disk_boundary), Create(origin_marker))
        self.wait(0.5)

        points_group = VGroup()
        xs = np.linspace(-disk_radius * 0.99, disk_radius * 0.99, n_grid_lines)
        ys = np.linspace(-disk_radius * 0.99, disk_radius * 0.99, n_grid_lines)

        for x in xs:
            for y in ys:
                point_coord = np.array([x, y, 0])
                if np.linalg.norm(point_coord[:2]) < disk_radius * 0.999:
                    dot = Dot(point_coord, radius=0.025, color=WHITE)
                    points_group.add(dot)

        self.play(FadeIn(points_group, scale=0.5))
        self.wait(1)

        intersect_x = disk_radius**2 / geodesic_center_x
        intersect_y_sq = disk_radius**2 - intersect_x**2
        intersect_y = np.sqrt(intersect_y_sq)

        intersect_p1 = np.array([intersect_x, intersect_y, 0])
        intersect_p2 = np.array([intersect_x, -intersect_y, 0])

        geodesic_arc = ArcBetweenPoints(
            intersect_p1,
            intersect_p2,
            radius=geodesic_radius,
            color=GREEN,
            stroke_width=3,
        )

        self.play(Create(geodesic_arc))
        self.wait(1)

        transform_function = lambda p: reflect_point_across_circle(p)

        self.play(
            ApplyPointwiseFunction(transform_function, points_group),
            origin_marker.animate.move_to(reflect_point_across_circle(ORIGIN)),
            run_time=animation_runtime,
        )
        self.wait(2)

        self.play(
            FadeOut(points_group),
            FadeOut(disk_boundary),
            FadeOut(origin_marker),
            FadeOut(title),
            FadeOut(geodesic_arc),
        )
        self.wait(1)
