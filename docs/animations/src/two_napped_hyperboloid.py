from manim import *
import numpy as np

class Main(ThreeDScene):
    def construct(self):
        axes_config = {
            "x_range": [-3, 3, 1],
            "y_range": [-3, 3, 1],
            "z_range": [-3, 3, 1],
            "x_length": 6,
            "y_length": 6,
            "z_length": 6,
        }
        axes = ThreeDAxes(**axes_config)
        axes_labels = axes.get_axis_labels(x_label="x", y_label="y", z_label="z")

        self.set_camera_orientation(phi=70 * DEGREES, theta=-110 * DEGREES, distance=12)

        U_max = 1.8
        surface_resolution = (24, 48)

        one_sheet_color = ORANGE
        two_sheet_color = BLUE
        transition_time = 4

        one_sheet_upper = Surface(
            lambda u, v: axes.c2p(np.cosh(u) * np.cos(v), np.cosh(u) * np.sin(v), np.sinh(u)),
            u_range=[0, U_max],
            v_range=[0, TAU],
            resolution=surface_resolution,
            fill_color=one_sheet_color,
            fill_opacity=0.7,
        )

        one_sheet_lower = Surface(
            lambda u, v: axes.c2p(np.cosh(u) * np.cos(v), np.cosh(u) * np.sin(v), np.sinh(u)),
            u_range=[-U_max, 0],
            v_range=[0, TAU],
            resolution=surface_resolution,
            fill_color=one_sheet_color,
            fill_opacity=0.7,
        )

        hyperboloid_one_sheet = VGroup(one_sheet_upper, one_sheet_lower)

        two_sheet_upper = Surface(
            lambda u, v: axes.c2p(np.sinh(u) * np.cos(v), np.sinh(u) * np.sin(v), np.cosh(u)),
            u_range=[0.01, U_max],
            v_range=[0, TAU],
            resolution=surface_resolution,
            fill_color=two_sheet_color,
            fill_opacity=0.7,
        )

        two_sheet_lower = Surface(
            lambda u, v: axes.c2p(np.sinh(u) * np.cos(v), np.sinh(u) * np.sin(v), -np.cosh(u)),
            u_range=[0.01, U_max],
            v_range=[0, TAU],
            resolution=surface_resolution,
            fill_color=two_sheet_color,
            fill_opacity=0.7,
        )

        hyperboloid_two_sheets = VGroup(two_sheet_upper, two_sheet_lower)

        title_style = {"font_size": 36}
        eq_style = {"font_size": 28}

        title1 = Text("One sheet hyperboloid", **title_style).to_corner(UL)
        equation1 = MathTex("x^2 + y^2 - z^2 = 1", color=one_sheet_color, **eq_style).next_to(title1, DOWN, buff=0.2)

        title2 = Text("Two sheet hyperboloid", **title_style).to_corner(UL)
        equation2 = MathTex("z^2 - x^2 - y^2 = 1", color=two_sheet_color, **eq_style).next_to(title2, DOWN, buff=0.2)

        current_title = title1.copy()
        current_equation = equation1.copy()
        self.add_fixed_in_frame_mobjects(current_title, current_equation)

        self.add(axes, axes_labels)
        self.play(Create(hyperboloid_one_sheet), Write(current_title), Write(current_equation), run_time=2)
        self.begin_ambient_camera_rotation(rate=0.08, about="theta")
        self.wait(2)

        self.play(
            Transform(hyperboloid_one_sheet, hyperboloid_two_sheets),
            Transform(current_title, title2),
            Transform(current_equation, equation2),
            run_time=transition_time,
            rate_func=smooth
        )

        self.wait(4)
        self.stop_ambient_camera_rotation()
        self.wait(1)