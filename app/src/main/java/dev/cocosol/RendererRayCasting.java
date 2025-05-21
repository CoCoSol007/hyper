/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
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
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dev.cocosol.caster.Caster;
import dev.cocosol.caster.CasterResult;
import dev.cocosol.caster.Sun;
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
public class RendererRayCasting {
    // --- Constants for Rendering ---

    // Adjust this value to change fog density.
    // 0.0 = no fog. Higher values = denser fog (objects fade faster).
    private final static double FOG_DENSITY = 0.4;

    private final static double WALL_HEIGHT = 0.4;
    private final static double DEFAULT_CAMERA_HEIGHT = 0.15;

    private final static Color WALL_COLOR = new Color(0xABB5FF);
    private final static Color FLOOR_COLOR = new Color(0xBCBFDB);

    private final static double SPEED = 0.01;

    /**
     * The seed value for the maze.
     */
    private final static int SEED = 567;

    /**
     * The x-coordinate of the mouse position.
     */
    private static int x;

    /**
     * The target aspect ratio for the main rendering area.
     */
    private static final double TARGET_ASPECT_RATIO = 2.0;

    /**
     * The main entry point for the application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Initialize the hyperbolic paving and the ray caster.
        Paving paving = new Paving();
        // Caster parameters might need adjustment depending on desired FOV etc.
        Caster caster = new Caster(
            paving, 500, 500, new Sun(0, 0.3), WALL_HEIGHT, DEFAULT_CAMERA_HEIGHT, SEED
        ); // Assuming width/height set later

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

                if (e.getKeyCode() == KeyEvent.VK_Q || e.getKeyCode() == KeyEvent.VK_LEFT) {
                    // Move left by applying movement along PI radians.
                    paving.applyMovement(Math.PI, SPEED);
                    needsRepaint = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // Move right by applying movement along 0 radians.
                    paving.applyMovement(0, SPEED);
                    needsRepaint = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_UP) {
                    // Move up by applying movement along PI/2 radians.
                    paving.applyMovement(Math.PI / 2, SPEED);
                    needsRepaint = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    // Move down by applying movement along -PI/2 radians.
                    paving.applyMovement(-Math.PI / 2, SPEED);
                    needsRepaint = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_L) {
                    // Rotate the paving in a clockwise direction.
                    paving.applyRotation(-Math.PI / 100);
                    caster.sun.horizontalAngle -= Math.PI / 100;
                    needsRepaint = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_M) {
                    // Rotate the paving in a counter-clockwise direction.
                    paving.applyRotation(Math.PI / 100);
                    caster.sun.horizontalAngle += Math.PI / 100;
                    needsRepaint = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    // jump
                    caster.cameraHeight += 0.01;
                    needsRepaint = true;
                }

                if (e.getKeyCode() == KeyEvent.VK_V) {
                    // fall
                    caster.cameraHeight -= 0.01;
                    needsRepaint = true;
                }


                // If any movement or rotation occurred, repaint the panel.
                if (needsRepaint) {
                    panel.repaint();
                }
            }
        });

        // Mouse move event
        frame.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(final MouseEvent me) {
                final int deltaX = me.getX() - RendererRayCasting.x;
                RendererRayCasting.x = me.getX();
                paving.applyRotation(deltaX / 150.0);
                caster.sun.horizontalAngle += deltaX / 150.0;
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

                final int panelWidth = getWidth();
                final int panelHeight = getHeight();

                // Calculate rendering area dimensions maintaining target aspect ratio
                int renderWidth;
                int renderHeight;
                int offsetX = 0;
                int offsetY = 0;

                final double panelRatio = (double) panelWidth / panelHeight;

                if (panelRatio > RendererRayCasting.TARGET_ASPECT_RATIO) {
                    renderHeight = panelHeight;
                    renderWidth = (int) (renderHeight * RendererRayCasting.TARGET_ASPECT_RATIO);
                    offsetX = (panelWidth - renderWidth) / 2;
                } else {
                    renderWidth = panelWidth;
                    renderHeight = (int) (renderWidth / RendererRayCasting.TARGET_ASPECT_RATIO);
                    offsetY = (panelHeight - renderHeight) / 2;
                }

                // Update caster dimensions dynamically based on render area
                caster.screenWidth = renderWidth;
                caster.screenHeight = renderHeight;

                // --- Ray-Casting Rendering (Left Half) ---
                // Compute intersection points for each ray.
                final CasterResult casterResult = caster.castRay();

                // Fill the entire panel background (handles letter/pillar boxing)
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, panelWidth, panelHeight);

                for (int i = 0; i < casterResult.intersectionPoints.length && i < renderWidth; i++) {
                    if (casterResult.intersectionPoints[i] == null) {
                        continue;
                    }

                    // Compute the hyperbolic distance from the viewer (assumed center after transform)
                    // to the intersection point.
                    double depth = Distance.hyperbolicDistanceToCenter(casterResult.intersectionPoints[i]);

                    // Avoid issues with zero or very small depth if the point is exactly at the center
                    if (depth < 1e-6) {
                         depth = 1e-6;
                    }
                    
                    final double minimalAngle = caster.smallerVerticalAngle();
                    
                    // Calculate screen Y coordinates for the wall slice top and bottom.

                    // The angle of the highest ray hitting the wall on the studied width
                    final double angleBottom = Math.atan(caster.cameraHeight / depth);
                    int yBottom = (int) (
                        panelHeight * (
                            (minimalAngle - angleBottom - Math.PI/2) / (2*minimalAngle - Math.PI)
                        )
                    );

                    // The angle of the lowest ray hitting the wall on the studied width
                    final double angleTop = Math.atan((caster.wallHeight - caster.cameraHeight) / depth);
                    int yTop = (int) (
                        panelHeight * (
                            (angleTop + minimalAngle - Math.PI/2) / (2*minimalAngle - Math.PI)
                        )
                    );

                    // Calculate brightness based on depth using exponential decay.
                    // Brightness is close to 1 for depth=0 and decreases towards 0.
                    float brightness = (float) Math.exp(-depth * FOG_DENSITY);
                    // Clamp brightness to valid range [0, 1] just in case.
                    brightness = Math.max(0.0f, Math.min(1.0f, brightness));
                    g2.setColor(darkenedColor(WALL_COLOR, brightness));

                    // Draw the vertical line, offsetting to the correct screen position
                    final int screenX = i + offsetX;
                    int screenYTop = yTop + offsetY;
                    int screenYBottom = yBottom + offsetY;

                    // Ensure drawing happens within the calculated bounds and avoids drawing if
                    // height is zero or negative
                    screenYTop = Math.max(0 + offsetY, screenYTop);
                    screenYBottom = Math.min(panelHeight - offsetY, screenYBottom);
                    if (screenYBottom > screenYTop) {
                        g2.drawLine(screenX, screenYTop, screenX, screenYBottom);
                    }
                    

                    // Draw the shadows
                    List<Double> shadowsIntensity = casterResult.shadowsIntensity[i];
                    for (int j = 0; j < casterResult.shadowsIntensity[i].size(); j++) {
                        double shadowIntensity = shadowsIntensity.get(j);
                        shadowIntensity = Math.max(0.0, Math.min(1.0, shadowIntensity));
                        g2.setColor(darkenedColor(FLOOR_COLOR, (float) (1 - shadowIntensity)));
                        g2.drawLine(screenX, panelHeight - j - offsetY, screenX, panelHeight - j - offsetY);
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
                        if (!chunk.getHash(RendererRayCasting.SEED, direction)) {
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

        // Enable focus on the panel to capture key events.
        panel.setFocusable(true);
        // No need for requestFocusInWindow here, SwingUtilities.invokeLater in main is better.
        return panel;
    }


    private static Color darkenedColor(Color color, float brightness) {
        final float r = color.getRed() / 255f;
        final float g = color.getGreen() / 255f;
        final float b = color.getBlue() / 255f;
        return new Color(
            r * brightness, 
            g * brightness, 
            b * brightness
        );
    } 
}
