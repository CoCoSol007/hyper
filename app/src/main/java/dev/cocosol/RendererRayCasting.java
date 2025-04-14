/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */
package dev.cocosol;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dev.cocosol.hyperbolic.Distance;
import dev.cocosol.hyperbolic.Point;
import dev.cocosol.hyperbolic.caster.Caster;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;
import dev.cocosol.hyperbolic.paving.Paving;

/**
 * A 2D renderer for visualizing the Poincaré disk and hyperbolic paving using ray casting.
 * <p>
 * This class creates a window, renders the hyperbolic paving along with walls, and
 * displays the rays cast in the scene. It also responds to key events to allow user interaction,
 * such as moving and rotating the paving.
 * </p>
 */
public class RendererRayCasting {
    // Constant representing the wall height used for perspective scaling.
    private final static double WALL_HEIGHT = 500;

    /**
     * The main entry point for the application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Initialize the hyperbolic paving and the ray caster.
        Paving paving = new Paving();
        Caster caster = new Caster(paving, 500, 500, 0);
        
        // Create a JFrame window.
        JFrame frame = new JFrame("hyper - ray casting");

        // Set up the rendering panel with the ray caster.
        JPanel panel = createRenderPanel(paving, caster);

        // Set up key listeners for user input.
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean needsRepaint = false;

                // Respond to movement and rotation key events.
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Q, KeyEvent.VK_LEFT -> {
                        // Move left by applying movement along PI radians.
                        paving.applyMovement(Math.PI);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                        // Move right by applying movement along 0 radians.
                        paving.applyMovement(0);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_Z, KeyEvent.VK_UP -> {
                        // Move up by applying movement along PI/2 radians.
                        paving.applyMovement(Math.PI / 2);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        // Move down by applying movement along -PI/2 radians.
                        paving.applyMovement(-Math.PI / 2);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_L -> {
                        // Rotate the paving in a clockwise direction.
                        paving.applyRotation(Math.PI / 100);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_M -> {
                        // Rotate the paving in a counter-clockwise direction.
                        paving.applyRotation(-Math.PI / 100);
                        needsRepaint = true;
                    }
                }

                // If any movement or rotation occurred, repaint the panel.
                if (needsRepaint) {
                    panel.repaint();
                }
            }
        });

        // Set up the frame properties.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Request focus so that key events are captured.
        SwingUtilities.invokeLater(panel::requestFocusInWindow);
    }

    /**
     * Creates and returns a JPanel that renders both the ray-casting view and the Poincaré disk.
     *
     * @param paving the hyperbolic paving to render.
     * @param caster the ray caster for computing intersections.
     * @return a JPanel configured for rendering.
     */
    private static JPanel createRenderPanel(Paving paving, Caster caster) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int w = getWidth(), h = getHeight();
                // Update the caster's dimensions based on current panel size.
                caster.screenWidth = w / 2;
                caster.screenHeight = h;

                // --- Ray-Casting Rendering ---
                // Compute intersection points for each ray.
                Point[] intersectionPoints = caster.castRay();
                for (int i = 0; i < intersectionPoints.length; i++) {
                    // Compute the hyperbolic distance from each intersection point to the center.
                    double depth = Distance.hyperbolicDistanceToCenter(intersectionPoints[i]);
                    // Set the color brightness based on depth (clamped to a valid range).
                    g2.setColor(Color.getHSBColor(0, 0, (float) (Math.clamp(depth * 0.5, 0, 1))));
                    // Calculate vertical line endpoints for drawing the "wall slice".
                    int[] xPoints = new int[] {i, i};
                    int[] yPoints = new int[] {
                        (int) (h / 2 - Math.clamp(1 - depth, 0, 5) * WALL_HEIGHT),
                        (int) (h / 2 + Math.clamp(1 - depth, 0, 5) * WALL_HEIGHT)
                    };
                    // Draw the vertical line for the current ray.
                    g2.drawPolygon(xPoints, yPoints, 2);
                }

                // --- Poincaré Disk Rendering ---
                int scale = Math.min(w / 2, h) / 2 - 20; // Scaling factor for drawing the disk.
                int centerX = 750; // X-coordinate for the disk center (offset to the right).
                int centerY = h / 2; // Y-coordinate for the disk center.

                // Draw the boundary of the unit circle (the Poincaré disk).
                g2.setColor(Color.GRAY);
                g2.drawOval(centerX - scale, centerY - scale, scale * 2, scale * 2);
                // Draw a small point for the disk's center.
                g2.drawOval(centerX, centerY, 2, 2);

                // Render the neighbors of the paving, drawing wall segments where applicable.
                for (Chunk chunk : paving.getAllNeighbors(5)) {
                    for (Direction direction : Direction.values()) {
                        if (!chunk.getHash(0, direction)) {
                            continue;
                        }

                        // Retrieve the boundary points for the current wall segment.
                        Point[] wallPoints = chunk.getPointFromDirection(direction);
                        g2.setColor(Color.DARK_GRAY);
                        int[] xPoints = new int[] {
                            (int) (wallPoints[0].x * scale + centerX),
                            (int) (wallPoints[1].x * scale + centerX)
                        };
                        int[] yPoints = new int[] {
                            (int) (-wallPoints[0].y * scale + centerY),
                            (int) (-wallPoints[1].y * scale + centerY)
                        };
                        // Draw the wall segment.
                        g2.drawPolygon(xPoints, yPoints, 2);
                    }
                }

                // --- Draw the Rays on the Poincaré Disk ---
                g2.setColor(Color.RED);
                for (Point point : intersectionPoints) {
                    int[] xPoints = new int[] {centerX, (int) (point.x * scale + centerX)};
                    int[] yPoints = new int[] {centerY, (int) (-point.y * scale + centerY)};
                    // Draw the line representing the ray.
                    g2.drawPolygon(xPoints, yPoints, 2);
                }
            }
        };

        // Enable focus on the panel to capture key events.
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        return panel;
    }
}
