from manim import *
import numpy as np


class Main(Scene):
    def construct(self):
        font_size_title = 36

        title_translation = Text("Translation", font_size=font_size_title).to_edge(UP)
        shape_init_trans = Polygon(
            [-1, 0, 0], [1, 0, 0], [0, 1, 0], color=BLUE, fill_opacity=0.7
        )
        shape_init_trans.move_to(LEFT * 3 + DOWN * 1.5)

        self.play(Write(title_translation))
        current_shape = shape_init_trans.copy()
        self.play(Create(current_shape))
        self.wait(0.5)

        translation_vector = RIGHT * 4 + UP * 1.5
        arrow_translation = Arrow(
            current_shape.get_center(),
            current_shape.get_center() + translation_vector,
            buff=0.1,
            color=YELLOW,
        )
        label_vector = MathTex("\\vec{v}").next_to(
            arrow_translation.get_center(), UP, buff=0.1
        )

        self.play(GrowArrow(arrow_translation), Write(label_vector))
        self.wait(0.5)
        self.play(current_shape.animate.shift(translation_vector))
        self.wait(1.5)
        self.play(
            FadeOut(current_shape),
            FadeOut(arrow_translation),
            FadeOut(label_vector),
            FadeOut(title_translation),
        )
        self.wait(0.5)

        title_rotation = Text("Rotation", font_size=font_size_title).to_edge(UP)
        shape_init_rot = Polygon(
            [-1, -1, 0], [1, -1, 0], [0, 0.5, 0], color=GREEN, fill_opacity=0.7
        )
        shape_init_rot.move_to(RIGHT * 2.5 + DOWN * 1)

        self.play(Write(title_rotation))
        current_shape = shape_init_rot.copy()
        self.play(Create(current_shape))
        self.wait(0.5)

        center_rotation_point = ORIGIN + DOWN * 1
        center_visual = Dot(center_rotation_point, color=RED)
        label_center = MathTex("O").next_to(center_visual, DR, buff=0.1)
        rotation_angle = 120 * DEGREES

        radius_arc = (
            np.linalg.norm(current_shape.get_center() - center_rotation_point) * 0.5
        )
        arc_rotation = Arc(
            radius=radius_arc,
            start_angle=Line(
                center_rotation_point, current_shape.get_center()
            ).get_angle(),
            angle=rotation_angle,
            arc_center=center_rotation_point,
            color=YELLOW,
        )
        arc_rotation.add_tip()

        self.play(FadeIn(center_visual), Write(label_center))
        self.wait(0.5)

        self.play(
            Rotate(
                current_shape, angle=rotation_angle, about_point=center_rotation_point
            ),
            Create(arc_rotation),
        )
        self.wait(2)

        self.play(
            FadeOut(current_shape),
            FadeOut(center_visual),
            FadeOut(label_center),
            FadeOut(arc_rotation),
            FadeOut(title_rotation),
        )
        self.wait(0.5)

        title_reflection = Text("Reflection", font_size=font_size_title).to_edge(UP)
        shape_init_refl = Polygon(
            [-1, 0, 0], [-3, 0, 0], [-1.5, 1.5, 0], color=PURPLE, fill_opacity=0.7
        )
        shape_init_refl.move_to(LEFT * 2.5 + UP * 1.5)

        self.play(Write(title_reflection))
        original_shape_refl = shape_init_refl.copy()
        self.play(Create(original_shape_refl))
        self.wait(0.5)

        reflection_axis = Line(DOWN * 3, UP * 3, color=GRAY, stroke_width=2)
        label_axis = MathTex("d").next_to(reflection_axis.get_end(), RIGHT, buff=0.1)

        reflection_axis2 = Line(LEFT * 4, RIGHT * 4, color=GRAY, stroke_width=2)

        self.play(Create(reflection_axis), Write(label_axis))
        self.wait(0.5)

        reflected_shape = original_shape_refl.copy()
        reflected_shape.flip(axis=UP, about_point=reflection_axis2.get_center())

        self.play(Transform(original_shape_refl, reflected_shape))
        self.wait(2.5)

        self.play(
            FadeOut(original_shape_refl),
            FadeOut(reflection_axis),
            FadeOut(label_axis),
            FadeOut(title_reflection),
        )
        self.wait(1)
