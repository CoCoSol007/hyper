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
 * A 3D renderer for visualizing the Poincaré disk and hyperbolic paving using ray casting.
 * <p>
 * This class creates a window, renders the hyperbolic paving along with walls, and
 * displays the rays cast in the scene. It also responds to key events to allow user interaction,
 * such as moving and rotating the paving.
 * </p>
 */
public class RendererRayCasting {
    private final static double PROJECTION_SCALE_FACTOR = 300.0;

    private final static double FOG_DENSITY = 0.6;

    private final static int seed = 567;

    /**
     * The main entry point for the application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        Paving paving = new Paving();

        Caster caster = new Caster(paving, 500, 500, seed);

        JFrame frame = new JFrame("hyper - ray casting");
        JPanel panel = createRenderPanel(paving, caster);


        // Set up key listeners for user input.
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean needsRepaint = false;

                // Respond to movement and rotation key events.
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Q, KeyEvent.VK_LEFT -> {
                        paving.applyMovement(Math.PI);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                        paving.applyMovement(0);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_Z, KeyEvent.VK_UP -> {
                        paving.applyMovement(Math.PI / 2);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        paving.applyMovement(-Math.PI / 2);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_M -> {
                        paving.applyRotation(Math.PI / 100);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_L -> {
                        paving.applyRotation(-Math.PI / 100);
                        needsRepaint = true;
                    }
                }

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

                int w = getWidth();
                int h = getHeight();
                int mapWidth = w / 8;
                int mapCenterX = 15 * w / 16;
                int mapCenterY = h / 8;

                caster.screenWidth = w;
                caster.screenHeight = h;

                // Compute intersection points for each ray.
                Point[] intersectionPoints = caster.castRay();

                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, w, h);

                for (int i = 0; i < intersectionPoints.length && i < w; i++) {
                    if (intersectionPoints[i] == null) continue;

                    // Compute the hyperbolic distance from the viewer to the intersection point.
                    double depth = Distance.hyperbolicDistanceToCenter(intersectionPoints[i]);

                    if (depth < 1e-6) {
                        continue;
                    }

                    // Calculate apparent height based on hyperbolic distance.
                    double projectedHalfHeight = PROJECTION_SCALE_FACTOR / (1.0 + depth);

                    // Calculate screen Y coordinates for the wall slice top and bottom.
                    int yTop = (int) (h / 2.0 - projectedHalfHeight);
                    int yBottom = (int) (h / 2.0 + projectedHalfHeight);

                    // Clamp Y coordinates to screen bounds.
                    yTop = Math.max(0, yTop);
                    yBottom = Math.min(h, yBottom);


                    // Calculate brightness based on depth using exponential decay.
                    float brightness = (float) Math.exp(-depth * FOG_DENSITY);
                    brightness = Math.max(0.0f, Math.min(1.0f, brightness));
                    g2.setColor(new Color(brightness, brightness, brightness));


                    if (yBottom > yTop) {
                        g2.drawLine(i, yTop, i, yBottom);
                    }
                }

                int scale = Math.min(mapWidth, h) / 2 - 20;

                g2.setColor(Color.LIGHT_GRAY);
                g2.drawOval(mapCenterX - scale, mapCenterY - scale, scale * 2, scale * 2);

                for (Chunk chunk : paving.getAllNeighbors(4)) {
                    for (Direction direction : Direction.values()) {
                        if (!chunk.getHash(seed, direction)) {
                            continue;
                        }

                        Point[] wallPoints = chunk.getPointFromDirection(direction);
                        if (wallPoints == null || wallPoints.length < 2 || wallPoints[0] == null || wallPoints[1] == null) continue;

                        g2.setColor(Color.DARK_GRAY);
                        int x1 = (int) (wallPoints[0].x * scale + mapCenterX);
                        int y1 = (int) (-wallPoints[0].y * scale + mapCenterY); // Invert Y for screen coordinates
                        int x2 = (int) (wallPoints[1].x * scale + mapCenterX);
                        int y2 = (int) (-wallPoints[1].y * scale + mapCenterY); // Invert Y

                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        };

        panel.setFocusable(true);
        return panel;
    }
}
