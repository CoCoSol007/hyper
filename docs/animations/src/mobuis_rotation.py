from manim import *
import numpy as np

class Main(Scene):
    def construct(self):
        disk_radius = 2.5
        
        center_p_complex = 0.5 + 0.2j
        
        total_rotation_angle = 2 * PI
        
        if abs(center_p_complex) >= 1:
             raise ValueError(f"Le module du centre p ({center_p_complex}) doit être inférieur à 1. Obtenu {abs(center_p_complex)}")

        grid_step = 0.5
        n_grid_lines = int(2 * disk_radius / grid_step) + 1
        animation_runtime = 10
        dot_radius = 0.03

        def complex_to_R3(z: complex) -> np.ndarray:
            return np.array([z.real * disk_radius, z.imag * disk_radius, 0])

        def R3_to_complex(point: np.ndarray) -> complex:
            if disk_radius == 0: return 0j
            return (point[0] / disk_radius) + 1j * (point[1] / disk_radius)

        def mobius_rotate_point(initial_point_R3: np.ndarray, center_p: complex, angle: float) -> np.ndarray:
            z = R3_to_complex(initial_point_R3)

            if abs(center_p) < 1e-9:
                transformed_z = np.exp(1j * angle) * z
                return complex_to_R3(transformed_z)

            denom1 = (1 - center_p.conjugate() * z)
            if abs(denom1) < 1e-9: return initial_point_R3
            z_prime = (z - center_p) / denom1

            z_rotated = np.exp(1j * angle) * z_prime

            denom2 = (1 + center_p.conjugate() * z_rotated)
            if abs(denom2) < 1e-9: return complex_to_R3(center_p)
            
            transformed_z = (z_rotated + center_p) / denom2
            return complex_to_R3(transformed_z)


        title = Text("Poincaré disk model : Rotation", font_size=36).to_edge(UP)
        self.play(Write(title))

        disk_boundary = Circle(radius=disk_radius, color=BLUE, stroke_width=2)
        disk_boundary.set_fill(BLUE, opacity=0.05)
        
        center_p_marker_pos = complex_to_R3(center_p_complex)
        center_p_marker = Dot(center_p_marker_pos, radius=dot_radius * 1.5, color=RED, z_index=1) # Au dessus des points
        center_p_label = MathTex(f"p", font_size=28, color=RED)
        center_p_label.next_to(center_p_marker, DR, buff=0.1)
        
        self.play(Create(disk_boundary))
        self.play(FadeIn(center_p_marker), FadeIn(center_p_label))
        self.wait(0.5)

        points_group = VGroup()
        initial_positions = [] 
        
        xs = np.linspace(-disk_radius * 0.99, disk_radius * 0.99, n_grid_lines)
        ys = np.linspace(-disk_radius * 0.99, disk_radius * 0.99, n_grid_lines)

        for x in xs:
            for y in ys:
                point_coord = np.array([x, y, 0])
                if np.linalg.norm(point_coord[:2]) < disk_radius * 0.99:
                    dist_sq = np.sum((point_coord - center_p_marker_pos)**2)
                    if dist_sq > (dot_radius * 2)**2 :
                        dot = Dot(point_coord, radius=dot_radius, color=WHITE)
                        points_group.add(dot)
                        initial_positions.append(point_coord.copy()) # Stocker la position initiale

        self.play(FadeIn(points_group, scale=0.7))
        self.wait(0.5)

        
        current_angle = ValueTracker(0) 
        
        angle_display_label = MathTex("\\theta = ", font_size=28).to_corner(UL)
        angle_display_value = DecimalNumber(
                current_angle.get_value(),
                num_decimal_places=2,
                show_ellipsis=False,
                unit=" rad",
                font_size=28
        )
        angle_display_value.next_to(angle_display_label, RIGHT)
        angle_display_value.add_updater(lambda d: d.set_value(current_angle.get_value())) # Met à jour la valeur affichée
        angle_display_group = VGroup(angle_display_label, angle_display_value)
        
        self.play(Write(angle_display_group))

        def continuous_rotation_updater(group):
            angle = current_angle.get_value()
            for i, dot in enumerate(group):
                initial_pos = initial_positions[i]
                new_pos = mobius_rotate_point(initial_pos, center_p_complex, angle)
                dot.move_to(new_pos)

        points_group.add_updater(continuous_rotation_updater)
        
        self.add(points_group) 

        self.play(
            current_angle.animate.set_value(total_rotation_angle),
            run_time=animation_runtime,
            rate_func=linear
        )

        points_group.remove_updater(continuous_rotation_updater)
        self.wait(1)
        

        self.play(FadeOut(points_group), FadeOut(disk_boundary), FadeOut(center_p_marker), 
                  FadeOut(center_p_label), FadeOut(title), FadeOut(angle_display_group))
        self.wait(1)
