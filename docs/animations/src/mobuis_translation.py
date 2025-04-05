from manim import *
import numpy as np

class Main(Scene):
    def construct(self):
        disk_radius = 2.0

        w_complex = 0.5 + 0.2j
        if abs(w_complex) >= 1:
             raise ValueError(f"Magnitude of w must be less than 1. Got {abs(w_complex)}")

        grid_step = 0.4
        n_grid_lines = int(2 * disk_radius / grid_step) + 2
        animation_runtime = 5

        def complex_to_R3(z: complex) -> np.ndarray:
            return np.array([z.real * disk_radius, z.imag * disk_radius, 0])

        def R3_to_complex(point: np.ndarray) -> complex:
            if disk_radius == 0: return 0j
            return (point[0] / disk_radius) + 1j * (point[1] / disk_radius)

        def mobius_transform_point(point: np.ndarray, target_w: complex) -> np.ndarray:
            z = R3_to_complex(point)
            denominator = (1 + target_w.conjugate() * z)
            if abs(denominator) < 1e-9:
                 print(f"Warning: Near zero denominator for z={z}, w={target_w}")
                 return point

            transformed_z = (z + target_w) / denominator
            return complex_to_R3(transformed_z)


        title = Text("Poincaré disk model : Translation", font_size=36).to_edge(UP)
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
                if np.linalg.norm(point_coord[:2]) < disk_radius:
                    dot = Dot(point_coord, radius=0.03, color=WHITE)
                    points_group.add(dot)

        self.play(FadeIn(points_group, scale=0.5))
        self.wait(1)

        transform_function = lambda p: mobius_transform_point(p, w_complex)

        w_label_pos = complex_to_R3(w_complex)
        w_marker = Dot(w_label_pos, color=RED, radius=0.05)
        w_text = MathTex(f"w \\approx {w_complex.real:.2f} + {w_complex.imag:.2f}i", font_size=24)
        w_text.next_to(w_marker, DR, buff=0.1)
        self.play(FadeIn(w_marker), FadeIn(w_text))
        self.wait(0.5)


        self.play(
            ApplyPointwiseFunction(transform_function, points_group),
            origin_marker.animate.move_to(complex_to_R3(w_complex)),
            run_time=animation_runtime
        )
        self.wait(2)

        self.play(FadeOut(points_group), FadeOut(disk_boundary), FadeOut(origin_marker), FadeOut(title), FadeOut(w_marker), FadeOut(w_text))
        self.wait(1)
