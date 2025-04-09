from manim import *
import numpy as np
import random

DISK_RADIUS = 3

def get_random_point_in_disk(max_radius=DISK_RADIUS):
    r = max_radius * np.sqrt(random.random())
    theta = 2 * PI * random.random()
    x = r * np.cos(theta)
    y = r * np.sin(theta)
    return np.array([x, y, 0])

def get_poincare_geodesic(p1, p2, disk_radius=DISK_RADIUS):
    x1, y1 = p1[0], p1[1]
    x2, y2 = p2[0], p2[1]
    epsilon = 1e-8

    if np.linalg.norm(p1 - p2) < epsilon:
        return VMobject()

    if abs(x1 * y2 - y1 * x2) < epsilon * disk_radius**2:
        return Line(p1, p2, color=BLUE_C)
    
    else:
        n1 = x1**2 + y1**2
        n2 = x2**2 + y2**2
        D = 2 * (x1 * y2 - y1 * x2)

        if abs(D) < epsilon:
            return Line(p1, p2, color=RED)

        R_sq = disk_radius**2
        cx = (y2 * (n1 + R_sq) - y1 * (n2 + R_sq)) / D
        cy = (x1 * (n2 + R_sq) - x2 * (n1 + R_sq)) / D
        center = np.array([cx, cy, 0])

        r_sq = (x1 - cx)**2 + (y1 - cy)**2
        
        if r_sq < epsilon:
            return Line(p1, p2, color=RED)

        r = np.sqrt(r_sq)

        angle1 = np.arctan2(y1 - cy, x1 - cx)
        angle2 = np.arctan2(y2 - cy, x2 - cx)

        sweep_angle = angle2 - angle1

        if sweep_angle > PI:
            sweep_angle -= 2 * PI
        elif sweep_angle <= -PI:
            sweep_angle += 2 * PI
            
        try:
            return Arc(radius=r,
                       arc_center=center,
                       start_angle=angle1,
                       angle=sweep_angle,
                       color=BLUE_C,
                       num_components=9)
        except Exception as e:
            print(f"Erreur lors de la création de l'Arc Manim: {e}")
            print(f"p1={p1}, p2={p2}, center={center}, r={r}, angle1={angle1}, angle2={angle2}, sweep={sweep_angle}")
            return Line(p1, p2, color=RED)


class Main(Scene):
    def construct(self):
        self.camera.background_color = BLACK

        title = Tex("Geodesie in the Poincaré disk model", font_size=40).to_edge(UP)
        self.play(Write(title))

        disk_boundary = Circle(radius=DISK_RADIUS, color=WHITE, stroke_width=2)
        disk_label = Tex("Border of the disk", font_size=24)\
            .next_to(disk_boundary, DOWN, buff=0.3)\
            .shift(LEFT * DISK_RADIUS*0.8)
        
        disk_interior_note = Tex(r"inside = Hyperbolic plan", font_size=24)\
            .next_to(disk_boundary, RIGHT, buff=0.3)\
            .shift(UP * DISK_RADIUS*0.8)

        self.play(Create(disk_boundary), Write(disk_label), Write(disk_interior_note), run_time=1.5)
        self.wait(1)
        self.play(FadeOut(disk_label), FadeOut(disk_interior_note))

        p1_start = get_random_point_in_disk()
        p2_start = get_random_point_in_disk()

        while np.linalg.norm(p1_start - p2_start) < DISK_RADIUS * 0.3:
            p2_start = get_random_point_in_disk()

        p1_vt_x = ValueTracker(p1_start[0])
        p1_vt_y = ValueTracker(p1_start[1])
        p2_vt_x = ValueTracker(p2_start[0])
        p2_vt_y = ValueTracker(p2_start[1])

        p1_dot = Dot(color=YELLOW, radius=0.08)
        p2_dot = Dot(color=GREEN, radius=0.08)
        
        p1_dot.add_updater(
            lambda m: m.move_to(np.array([p1_vt_x.get_value(), p1_vt_y.get_value(), 0]))
        )
        p2_dot.add_updater(
            lambda m: m.move_to(np.array([p2_vt_x.get_value(), p2_vt_y.get_value(), 0]))
        )

        p1_label = Tex("P1", font_size=30).add_updater(lambda m: m.next_to(p1_dot, UR, buff=0.1))
        p2_label = Tex("P2", font_size=30).add_updater(lambda m: m.next_to(p2_dot, UR, buff=0.1))

        self.play(FadeIn(p1_dot, scale=0.5), FadeIn(p2_dot, scale=0.5), Write(p1_label), Write(p2_label))
        self.wait(0.5)

        geodesic_mob = always_redraw(
            lambda: get_poincare_geodesic(p1_dot.get_center(), p2_dot.get_center(), disk_radius=DISK_RADIUS)
        )

        geodesic_label = Tex("Geodesie (the shortest way)", font_size=28).to_edge(DOWN)
        self.play(Create(geodesic_mob), Write(geodesic_label), run_time=1.5)
        self.wait(1.5)

        num_moves = 4
        self.wait(0.5)

        for i in range(num_moves):
            p1_target = get_random_point_in_disk()
            p2_target = get_random_point_in_disk()
            while np.linalg.norm(p1_target - p2_target) < DISK_RADIUS * 0.4:
                p2_target = get_random_point_in_disk()
            while np.linalg.norm(p1_target - p1_dot.get_center()) < DISK_RADIUS * 0.3:
                p1_target = get_random_point_in_disk()
            while np.linalg.norm(p2_target - p2_dot.get_center()) < DISK_RADIUS * 0.3:
                p2_target = get_random_point_in_disk()

            self.play(
                p1_vt_x.animate.set_value(p1_target[0]),
                p1_vt_y.animate.set_value(p1_target[1]),
                p2_vt_x.animate.set_value(p2_target[0]),
                p2_vt_y.animate.set_value(p2_target[1]),
                run_time=3.5,
                rate_func=smooth
            )
            self.wait(0.5)

        self.wait(3)

        self.play(FadeOut(p1_dot), FadeOut(p2_dot), FadeOut(p1_label), FadeOut(p2_label), FadeOut(geodesic_mob), FadeOut(geodesic_label), FadeOut(title), FadeOut(disk_boundary))
        self.wait(0.5)
