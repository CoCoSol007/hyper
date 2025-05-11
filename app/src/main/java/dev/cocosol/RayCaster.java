/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dev.cocosol.caster.Caster;
import dev.cocosol.hyperbolic.Distance;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;
import dev.cocosol.hyperbolic.paving.Paving;

/**
 * A 3D renderer for visualizing the Poincaré disk and hyperbolic paving using
 * ray casting.
 * 
 * This class creates a window, renders the hyperbolic paving along with walls,
 * and
 * displays the rays cast in the scene. It also responds to key events to allow
 * user interaction,
 * such as moving and rotating the paving. The window is resizable while
 * maintaining the aspect ratio
 * of the main rendering area.
 * </p>
 */
public class RayCaster {
    /**
     * The scale factor for the 3D projection.
     */
    private static final double PROJECTION_SCALE_FACTOR = 300.0;

    /**
     * The density of fog applied to the scene.
     * 0.0 for no fog, 1.0 for full fog.
     */
    private static final double FOG_DENSITY = 0.9;

    /**
     * The seed value for the maze.
     */
    private static final int SEED = 567;

    /**
     * The target aspect ratio for the main rendering area.
     */
    private static final double TARGET_ASPECT_RATIO = 2.0;

    private static int x;

    /**
     * The main entry point for the application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(final String[] args) {
        final Paving paving = new Paving();
        // Initialize Caster with base dimensions, will be updated dynamically
        final Caster caster = new Caster(paving, 1280, 720, RayCaster.SEED);

        final JFrame frame = new JFrame("hyper - ray casting");
        final JPanel panel = RayCaster.createRenderPanel(paving, caster);

        // Set up key listeners for user input.
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                boolean needsRepaint = false;

                // Respond to movement and rotation key events.
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_Q, KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                        paving.applyMovement(Math.PI, 0.01);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                        paving.applyMovement(0, 0.01);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_Z, KeyEvent.VK_W, KeyEvent.VK_UP -> {
                        paving.applyMovement(Math.PI / 2, 0.01);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
                        paving.applyMovement(-Math.PI / 2, 0.01);
                        needsRepaint = true;
                    }
                    default -> {
                        break;
                    }
                }

                if (needsRepaint) {
                    panel.repaint();
                }
            }
        });

        // Mouse move event
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(final MouseEvent me) {
                final int deltaX = me.getX() - RayCaster.x;
                RayCaster.x = me.getX();
                paving.applyRotation(deltaX / 150.0);
                panel.repaint();
            }
        });

        // Set up the frame properties.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        SwingUtilities.invokeLater(panel::requestFocusInWindow);

        // Add component listener to handle resizing and repaint
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                panel.repaint();
            }
        });
    }

    /**
     * Creates and returns a JPanel that renders both the ray-casting view and the
     * Poincaré disk.
     *
     * @param paving the hyperbolic paving to render.
     * @param caster the ray caster for computing intersections.
     * @return a JPanel configured for rendering.
     */
    private static JPanel createRenderPanel(final Paving paving, final Caster caster) {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Graphics2D g2 = (Graphics2D) g;

                final int panelWidth = this.getWidth();
                final int panelHeight = this.getHeight();

                // Calculate rendering area dimensions maintaining target aspect ratio
                int renderWidth;
                int renderHeight;
                int offsetX = 0;
                int offsetY = 0;

                final double panelRatio = (double) panelWidth / panelHeight;

                if (panelRatio > RayCaster.TARGET_ASPECT_RATIO) {
                    renderHeight = panelHeight;
                    renderWidth = (int) (renderHeight * RayCaster.TARGET_ASPECT_RATIO);
                    offsetX = (panelWidth - renderWidth) / 2;
                } else {
                    renderWidth = panelWidth;
                    renderHeight = (int) (renderWidth / RayCaster.TARGET_ASPECT_RATIO);
                    offsetY = (panelHeight - renderHeight) / 2;
                }

                // Update caster dimensions dynamically based on render area
                caster.screenWidth = renderWidth;
                caster.screenHeight = renderHeight;

                // Compute intersection points for each ray based on the current render width
                final Point[] intersectionPoints = caster.castRay();

                // Fill the entire panel background (handles letter/pillar boxing)
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, panelWidth, panelHeight);

                for (int i = 0; i < intersectionPoints.length && i < renderWidth; i++) {
                    if (intersectionPoints[i] == null) {
                        continue;
                    }

                    // Compute the hyperbolic distance from the viewer to the intersection point.
                    final double depth = Distance.hyperbolicDistanceToCenter(intersectionPoints[i]);

                    if (depth < 1e-6) {
                        continue;
                    }

                    // Calculate apparent height based on hyperbolic distance.
                    // Scale factor might need adjustment depending on desired vertical FOV relative
                    // to renderHeight
                    final double effectiveScaleFactor = RayCaster.PROJECTION_SCALE_FACTOR * (renderHeight / 500.0);
                    final double projectedHalfHeight = effectiveScaleFactor / (Math.cosh(depth));

                    // Calculate screen Y coordinates relative to the render area center
                    int yTop = (int) (renderHeight / 2.0 - projectedHalfHeight);
                    int yBottom = (int) (renderHeight / 2.0 + projectedHalfHeight);

                    // Clamp Y coordinates within the render area height [0, renderHeight]
                    yTop = Math.max(0, yTop);
                    yBottom = Math.min(renderHeight, yBottom);

                    // Calculate brightness based on depth using exponential decay.
                    float brightness = (float) Math.exp(-depth * RayCaster.FOG_DENSITY);
                    brightness = Math.max(0.0f, Math.min(1.0f, brightness));
                    g2.setColor(new Color(brightness, brightness, brightness));

                    // Draw the vertical line, offsetting to the correct screen position
                    final int screenX = i + offsetX;
                    int screenYTop = yTop + offsetY;
                    int screenYBottom = yBottom + offsetY;

                    // Ensure drawing happens within the calculated bounds and avoids drawing if
                    // height is zero or negative
                    if (screenYBottom > screenYTop) {
                        screenYTop = Math.max(0, screenYTop);
                        screenYBottom = Math.min(panelHeight, screenYBottom);
                        if (screenYBottom > screenYTop) {
                            g2.drawLine(screenX, screenYTop, screenX, screenYBottom);
                        }
                    }
                }

                // Draw the minimap in the top-right corner relative to panel size
                final int mapAreaWidth = panelWidth / 6;
                final int mapAreaHeight = panelHeight / 3;
                final int mapCenterX = panelWidth - mapAreaWidth / 2 - 15;
                final int mapCenterY = mapAreaHeight / 2 + 15;

                int scale = Math.min(mapAreaWidth, mapAreaHeight) / 2 - 10;
                if (scale <= 0) {
                    scale = 1;
                }

                // Draw a filled circle at the center of the minimap
                g2.setColor(Color.GRAY);
                g2.fillOval(mapCenterX - scale, mapCenterY - scale, scale * 2, scale * 2);

                g2.setColor(Color.DARK_GRAY);
                g2.drawOval(mapCenterX - scale, mapCenterY - scale, scale * 2, scale * 2);

                for (final Chunk chunk : paving.getAllNeighbors(4)) {
                    for (final Direction direction : Direction.values()) {
                        if (!chunk.getHash(RayCaster.SEED, direction)) {
                            continue;
                        }

                        final Point[] wallPoints = chunk.getPointFromDirection(direction);
                        if (wallPoints == null || wallPoints.length < 2 || wallPoints[0] == null
                                || wallPoints[1] == null) {
                            continue;
                        }

                        g2.setColor(Color.BLACK);
                        final int x1 = (int) (wallPoints[0].x * scale + mapCenterX);
                        final int y1 = (int) (-wallPoints[0].y * scale + mapCenterY); // Invert Y for screen coordinates
                        final int x2 = (int) (wallPoints[1].x * scale + mapCenterX);
                        final int y2 = (int) (-wallPoints[1].y * scale + mapCenterY); // Invert Y

                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        };

        panel.setFocusable(true);
        return panel;
    }
}
