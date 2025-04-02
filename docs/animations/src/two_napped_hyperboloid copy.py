from manim import *
import numpy as np

class Main(ThreeDScene):
    def construct(self):
        axes = ThreeDAxes()
        self.set_camera_orientation(phi=75 * DEGREES, theta=30 * DEGREES)
        
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
        
        nappe_inf = Surface(
            lambda u, v: np.array([
                np.sinh(u) * np.cos(v),
                np.sinh(u) * np.sin(v),
                -np.cosh(u)
            ]),
            u_range=[0, U_max],
            v_range=[0, TAU],
            resolution=(32, 64),
            fill_color=BLUE,
            fill_opacity=0.6
        )
        
        self.add(axes, nappe_sup, nappe_inf)
        self.begin_ambient_camera_rotation(rate=0.1)
        self.wait(4)
