from manim import *
import numpy as np

class Main(ThreeDScene):
    def construct(self):
        axes_range = 3
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
            fill_opacity=0.5,
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

        def circle_hyperboloid(u_const, v):
            return np.array([np.sinh(u_const) * np.cos(v),
                             np.sinh(u_const) * np.sin(v),
                             np.cosh(u_const)])

        def boost(point, alpha=0.5):
            x, y, z = point
            return np.array([np.cosh(alpha)*x + np.sinh(alpha)*z,
                             y,
                             np.sinh(alpha)*x + np.cosh(alpha)*z])

        def circle_disk(u_const, v):
            return project_point(circle_hyperboloid(u_const, v))
        
        circle_hyp1 = ParametricFunction(
            lambda v: circle_hyperboloid(1, v),
            t_range=[0, TAU],
            color=RED,
            stroke_width=5
        )
        circle_disk1 = ParametricFunction(
            lambda v: circle_disk(1, v),
            t_range=[0, TAU],
            color=YELLOW,
            stroke_width=5,
            use_smoothing=False
        )

        alpha = 0.5
        circle_hyp2 = ParametricFunction(
            lambda v: boost(circle_hyperboloid(1, v), alpha),
            t_range=[0, TAU],
            color=ORANGE,
            stroke_width=5
        )
        circle_disk2 = ParametricFunction(
            lambda v: project_point(boost(circle_hyperboloid(1, v), alpha)),
            t_range=[0, TAU],
            color=BLUE,
            stroke_width=5,
            use_smoothing=False
        )

        self.play(Create(hyperboloid_top), run_time=3)
        self.play(Create(poincare_disk), run_time=2)
        self.wait(1)

        self.play(Create(circle_hyp1), Create(circle_disk1), run_time=4)
        self.wait(1)

        v_tracker = ValueTracker(0)
        point_hyp1 = Dot3D(color=RED, radius=0.08)
        point_disk1 = Dot3D(color=YELLOW, radius=0.08)
        proj_line1 = DashedLine(ORIGIN, UP, color=WHITE, stroke_width=2)

        point_hyp1.add_updater(
            lambda mob: mob.move_to(circle_hyperboloid(1, v_tracker.get_value()))
        )
        point_disk1.add_updater(
            lambda mob: mob.move_to(circle_disk(1, v_tracker.get_value()))
        )
        proj_line1.add_updater(
            lambda mob: mob.put_start_and_end_on(point_hyp1.get_center(),
                                                   point_disk1.get_center())
        )

        self.play(FadeIn(point_hyp1), FadeIn(point_disk1), Create(proj_line1))
        self.play(v_tracker.animate.set_value(TAU), run_time=6, rate_func=linear)
        self.play(FadeOut(point_hyp1), FadeOut(point_disk1), FadeOut(proj_line1), run_time=1)
        self.remove(point_hyp1, point_disk1, proj_line1)

        self.wait(1)
        self.play(Create(circle_hyp2), Create(circle_disk2), run_time=4)
        self.wait(1)

        v_tracker2 = ValueTracker(0)
        point_hyp2 = Dot3D(color=ORANGE, radius=0.08)
        point_disk2 = Dot3D(color=BLUE, radius=0.08)
        proj_line2 = DashedLine(ORIGIN, UP, color=WHITE, stroke_width=2)

        point_hyp2.add_updater(
            lambda mob: mob.move_to(boost(circle_hyperboloid(1, v_tracker2.get_value()), alpha))
        )
        point_disk2.add_updater(
            lambda mob: mob.move_to(project_point(boost(circle_hyperboloid(1, v_tracker2.get_value()), alpha)))
        )
        proj_line2.add_updater(
            lambda mob: mob.put_start_and_end_on(point_hyp2.get_center(),
                                                   point_disk2.get_center())
        )

        self.play(FadeIn(point_hyp2), FadeIn(point_disk2), Create(proj_line2))
        self.play(v_tracker2.animate.set_value(TAU), run_time=6, rate_func=linear)
        self.play(FadeOut(point_hyp2), FadeOut(point_disk2), FadeOut(proj_line2), run_time=1)
        self.remove(point_hyp2, point_disk2, proj_line2)

        self.wait(3)
