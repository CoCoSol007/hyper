from manim import *
import numpy as np

class Main(ThreeDScene):
    def construct(self):
        axes_range = 3
        t_geodesic_range = [-2.5, 2.5]
        geodesic_2_phi = 0.8

        z_axis_height = axes_range * 1.5
        axes = ThreeDAxes(
            x_range=[-axes_range, axes_range, 1],
            y_range=[-axes_range, axes_range, 1],
            z_range=[-z_axis_height, z_axis_height, 1],
            x_length=axes_range * 2,
            y_length=axes_range * 2,
            z_length=z_axis_height * 2,
        )
        self.set_camera_orientation(phi=75 * DEGREES, theta=-60 * DEGREES, distance=12)
        self.add(axes)

        hyperboloid_top = Surface(
            lambda u, v: axes.c2p(np.sinh(u) * np.cos(v),
                                    np.sinh(u) * np.sin(v),
                                    np.cosh(u)),
            u_range=[-axes_range, axes_range],
            v_range=[-axes_range, axes_range],
            resolution=(24, 48),
            fill_color=BLUE,
        )

        poincare_disk = Circle(
            radius=1,
            color=GREEN,
            stroke_width=3,
            fill_color=GREEN,
            fill_opacity=0.1
        ).move_to(ORIGIN)

        def project_point(point):
            x, y, z = point
            if 1 + z < 1e-9:
                return np.array([np.inf, np.inf, 0])
            return np.array([x / (1 + z), y / (1 + z), 0])

        def geodesic_hyperboloid_param_1(t):
            angle = PI / 4
            x0 = 0
            y0 = np.sinh(t)
            z0 = np.cosh(t)
            x = - y0 * np.sin(angle)
            y = y0 * np.cos(angle)
            return np.array([x, y, z0])

        def geodesic_disk_param_1(t):
            return project_point(geodesic_hyperboloid_param_1(t))

        geodesic_on_hyperboloid_1 = ParametricFunction(
            geodesic_hyperboloid_param_1, t_range=t_geodesic_range,
            color=RED, stroke_width=5
        )
        geodesic_on_disk_1 = ParametricFunction(
            geodesic_disk_param_1, t_range=t_geodesic_range,
            color=YELLOW, stroke_width=5, use_smoothing=False
        )

        def geodesic_hyperboloid_param_2(t, phi):
            return np.array([
                np.sinh(phi) * np.cosh(t),
                np.sinh(t),
                np.cosh(phi) * np.cosh(t)
            ])

        def geodesic_disk_param_2(t, phi):
            return project_point(geodesic_hyperboloid_param_2(t, phi))

        geodesic_on_hyperboloid_2 = ParametricFunction(
            lambda t: geodesic_hyperboloid_param_2(t, geodesic_2_phi),
            t_range=t_geodesic_range, color=ORANGE, stroke_width=5
        )
        geodesic_on_disk_2 = ParametricFunction(
            lambda t: geodesic_disk_param_2(t, geodesic_2_phi),
            t_range=t_geodesic_range, color=BLUE, stroke_width=5, use_smoothing=False
        )

        self.play(Create(hyperboloid_top), run_time=3)
        self.play(Create(poincare_disk), run_time=2)
        self.wait(1)

        self.play(Create(geodesic_on_hyperboloid_1), Create(geodesic_on_disk_1), run_time=4)
        self.wait(1)

        t_tracker_1 = ValueTracker(t_geodesic_range[0])
        point_on_hyperboloid_1 = Dot3D(color=RED, radius=0.08)
        point_on_disk_1 = Dot3D(color=YELLOW, radius=0.08)
        projection_line_1 = DashedLine(ORIGIN, UP, color=WHITE, stroke_width=2)

        point_on_hyperboloid_1.add_updater(
            lambda mob: mob.move_to(geodesic_hyperboloid_param_1(t_tracker_1.get_value()))
        )
        point_on_disk_1.add_updater(
            lambda mob: mob.move_to(geodesic_disk_param_1(t_tracker_1.get_value()))
        )
        projection_line_1.add_updater(
            lambda mob: mob.put_start_and_end_on(point_on_hyperboloid_1.get_center(),
                                                   point_on_disk_1.get_center())
        )

        self.play(FadeIn(point_on_hyperboloid_1), FadeIn(point_on_disk_1), Create(projection_line_1))
        self.play(t_tracker_1.animate.set_value(t_geodesic_range[1]), run_time=6, rate_func=linear)
        self.play(FadeOut(point_on_hyperboloid_1), FadeOut(point_on_disk_1), FadeOut(projection_line_1), run_time=1)
        self.remove(point_on_hyperboloid_1, point_on_disk_1, projection_line_1)
        self.wait(1)

        self.play(Create(geodesic_on_hyperboloid_2), Create(geodesic_on_disk_2), run_time=4)
        self.wait(1)

        t_tracker_2 = ValueTracker(t_geodesic_range[0])
        point_on_hyperboloid_2 = Dot3D(color=ORANGE, radius=0.08)
        point_on_disk_2 = Dot3D(color=BLUE, radius=0.08)
        projection_line_2 = DashedLine(ORIGIN, UP, color=WHITE, stroke_width=2)

        point_on_hyperboloid_2.add_updater(
            lambda mob: mob.move_to(geodesic_hyperboloid_param_2(t_tracker_2.get_value(), geodesic_2_phi))
        )
        point_on_disk_2.add_updater(
            lambda mob: mob.move_to(geodesic_disk_param_2(t_tracker_2.get_value(), geodesic_2_phi))
        )
        projection_line_2.add_updater(
            lambda mob: mob.put_start_and_end_on(point_on_hyperboloid_2.get_center(),
                                                   point_on_disk_2.get_center())
        )

        self.play(FadeIn(point_on_hyperboloid_2), FadeIn(point_on_disk_2), Create(projection_line_2))
        self.play(t_tracker_2.animate.set_value(t_geodesic_range[1]), run_time=6, rate_func=linear)
        self.play(FadeOut(point_on_hyperboloid_2), FadeOut(point_on_disk_2), FadeOut(projection_line_2), run_time=1)
        self.remove(point_on_hyperboloid_2, point_on_disk_2, projection_line_2)

        self.wait(3)
