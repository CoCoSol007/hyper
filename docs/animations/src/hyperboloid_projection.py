from manim import *
import numpy as np

def c(a, b):
    return np.sqrt(a**2 + b**2 + 1)

class Main(ThreeDScene):
    def construct(self):
        axes = ThreeDAxes()
        
        self.set_camera_orientation(phi=70 * DEGREES, theta=50 * DEGREES, zoom=1.2)

        U_max = 1.5

        nappe_sup = Surface(
            lambda u, v: np.array([
                np.sinh(u) * np.cos(v),
                np.sinh(u) * np.sin(v),
                np.cosh(u)
            ]),
            u_range=[0, U_max],
            v_range=[0, TAU],
            resolution=(32, 64),
            fill_color=BLUE,
            fill_opacity=0.6
        )

        unit_disk = Circle(radius=1, color=BLUE, fill_opacity=0.3)

        x_tracker = ValueTracker(0)
        y_tracker = ValueTracker(0)

        point_M = always_redraw(lambda: Dot(
            point=[x_tracker.get_value(), y_tracker.get_value(), c(x_tracker.get_value(), y_tracker.get_value())],
            color=RED
        ))

        point_P = always_redraw(lambda: Dot(
            point=[
                x_tracker.get_value() / (c(x_tracker.get_value(), y_tracker.get_value()) + 1),
                y_tracker.get_value() / (c(x_tracker.get_value(), y_tracker.get_value()) + 1),
                0
            ],
            color=YELLOW
        ))

        line_MP = always_redraw(lambda: Line(
            start=(0,0,-1),
            end=point_M.get_center(),
            color=WHITE
        ))

        self.add(axes, nappe_sup, unit_disk, point_M, point_P, line_MP)

        self.begin_ambient_camera_rotation(rate=0.1)

        self.play(
            x_tracker.animate.set_value(0.8),
            y_tracker.animate.set_value(0.6),
            run_time=3,
            rate_func=smooth
        )

        self.play(
            x_tracker.animate.set_value(np.random.uniform(-1, 1)),
            y_tracker.animate.set_value(np.random.uniform(-1, 1)),
            run_time=4,
            rate_func=smooth
        )

        for angle in np.linspace(0, 2 * np.pi, 20):
            self.play(
                x_tracker.animate.set_value(np.cos(angle)),
                y_tracker.animate.set_value(np.sin(angle)),
                run_time=0.5,
                rate_func=smooth
            )

        self.play(
            x_tracker.animate.set_value(0),
            y_tracker.animate.set_value(0),
            run_time=3,
            rate_func=smooth
        )

        self.wait(2)
