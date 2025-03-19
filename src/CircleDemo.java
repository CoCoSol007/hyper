package src;

import src.hyperbolic.Circle;
import src.hyperbolic.Geodesic;
import src.hyperbolic.Point;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;


public class CircleDemo extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        new_circle(new Point(0, 0), 1, g);
        Geodesic geo = Geodesic.from_two_points(new Point(0.2,-0.2), new Point(0.7,-0.1));
        new_circle(geo.get_euclidean_center(), geo.get_euclidean_radius(), g);
        new_circle(new Point(0.2,-0.2), 0.01,g);
        new_circle(new Point(0.7,-0.1), 0.01,g);
        new_circle(new Point(-0.7,0.1), 0.01,g);
        Circle c = new Circle(new Point(-0.7,0.1),2);
        new_circle(c.get_euclidean_center(),c.get_euclidean_radius(),g);

        Point[] list = geo.get_ideal_points();
        new_circle(list[0], 0.01,g);
        new_circle(list[1], 0.01,g);
    }

    public void new_circle(Point center, double radius, Graphics g) {
        g.setColor(Color.BLUE);
        int x = (int) ((center.x * 250 + 250) - radius*250);
        int y = (int) ((250 - 250 * center.y) - radius*250);
        g.drawOval(x, y, (int) (radius*500), (int) (radius*500));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Swing");
        CircleDemo panel = new CircleDemo();

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

