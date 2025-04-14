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
    // --- Constants for Rendering ---
    // Adjust this value to change the apparent height of walls.
    // A larger value makes walls appear taller at the same distance.
    private final static double PROJECTION_SCALE_FACTOR = 300.0; // Anciennement lié à WALL_HEIGHT

    // Adjust this value to change fog density.
    // 0.0 = no fog. Higher values = denser fog (objects fade faster).
    private final static double FOG_DENSITY = 0.4;

    /**
     * The main entry point for the application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Initialize the hyperbolic paving and the ray caster.
        Paving paving = new Paving();
        // Caster parameters might need adjustment depending on desired FOV etc.
        Caster caster = new Caster(paving, 500, 500, 567); // Assuming width/height set later

        // Create a JFrame window.
        JFrame frame = new JFrame("hyper - ray casting (Improved Projection)");

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
        frame.setSize(1000, 500); // Initial size
        frame.add(panel);
        frame.setLocationRelativeTo(null); // Center on screen
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

                int w = getWidth(); // Total panel width
                int h = getHeight(); // Total panel height
                int viewWidth = w / 2; // Width for the ray casting view
                int mapWidth = w / 2;  // Width for the map view
                int mapCenterX = viewWidth + mapWidth / 2; // Center X for the Poincaré disk map
                int mapCenterY = h / 2; // Center Y for the Poincaré disk map

                // Update the caster's dimensions based on current panel size.
                // We only use half the width for the 3D view.
                caster.screenWidth = viewWidth;
                caster.screenHeight = h;

                // --- Ray-Casting Rendering (Left Half) ---
                // Compute intersection points for each ray.
                Point[] intersectionPoints = caster.castRay();

                // Clear the background for the 3D view (optional, depending on desired effect)
                g2.setColor(Color.BLACK); // Example: black background
                g2.fillRect(0, 0, viewWidth, h);

                for (int i = 0; i < intersectionPoints.length && i < viewWidth; i++) {
                    if (intersectionPoints[i] == null) continue; // Skip if ray didn't hit anything

                    // Compute the hyperbolic distance from the viewer (assumed center after transform)
                    // to the intersection point.
                    double depth = Distance.hyperbolicDistanceToCenter(intersectionPoints[i]);

                    // Avoid issues with zero or very small depth if the point is exactly at the center
                    if (depth < 1e-6) {
                         depth = 1e-6;
                    }

                    // --- Improved Projection ---
                    // Calculate apparent height based on hyperbolic distance.
                    // Height decreases as depth increases. Using 1/(1+depth) avoids division by zero.
                    // Alternative: use PROJECTION_SCALE_FACTOR / Math.cosh(depth);
                    double projectedHalfHeight = PROJECTION_SCALE_FACTOR / (1.0 + depth);

                    // Calculate screen Y coordinates for the wall slice top and bottom.
                    int yTop = (int) (h / 2.0 - projectedHalfHeight);
                    int yBottom = (int) (h / 2.0 + projectedHalfHeight);

                    // Clamp Y coordinates to screen bounds.
                    yTop = Math.max(0, yTop);
                    yBottom = Math.min(h, yBottom);


                    // --- Improved Color (Fog Effect) ---
                    // Calculate brightness based on depth using exponential decay.
                    // Brightness is close to 1 for depth=0 and decreases towards 0.
                    float brightness = (float) Math.exp(-depth * FOG_DENSITY);
                    // Clamp brightness to valid range [0, 1] just in case.
                    brightness = Math.max(0.0f, Math.min(1.0f, brightness));
                    // Set color (grayscale based on brightness).
                    g2.setColor(new Color(brightness, brightness, brightness));


                    // Draw the vertical line ("wall slice") for the current ray.
                    if (yBottom > yTop) { // Only draw if the slice has positive height
                        g2.drawLine(i, yTop, i, yBottom);
                    }
                }

                // --- Poincaré Disk Rendering (Right Half) ---
                // Clear background for map view
                g2.setColor(Color.WHITE);
                g2.fillRect(viewWidth, 0, mapWidth, h);

                // Scaling factor for drawing the disk within the right half.
                int scale = Math.min(mapWidth, h) / 2 - 20;

                // Draw the boundary of the unit circle (the Poincaré disk).
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawOval(mapCenterX - scale, mapCenterY - scale, scale * 2, scale * 2);
                // Draw a small point for the disk's center.
                g2.setColor(Color.BLACK);
                g2.fillOval(mapCenterX - 1, mapCenterY - 1, 3, 3);

                // Render the neighbors of the paving, drawing wall segments where applicable.
                // The number of neighbors (5) determines how much of the map is drawn.
                for (Chunk chunk : paving.getAllNeighbors(5)) {
                    for (Direction direction : Direction.values()) {
                        // Check if there's a wall in this direction for the chunk.
                        // Assuming getHash(0, direction) returns true if there IS a wall.
                        // Adjust logic if it means something else.
                        if (!chunk.getHash(567, direction)) {
                            continue; // No wall here
                        }

                        // Retrieve the boundary points for the current wall segment.
                        Point[] wallPoints = chunk.getPointFromDirection(direction);
                        if (wallPoints == null || wallPoints.length < 2 || wallPoints[0] == null || wallPoints[1] == null) continue;

                        g2.setColor(Color.DARK_GRAY);
                        // Convert Poincaré coordinates to screen coordinates for the map.
                        int x1 = (int) (wallPoints[0].x * scale + mapCenterX);
                        int y1 = (int) (-wallPoints[0].y * scale + mapCenterY); // Invert Y for screen coordinates
                        int x2 = (int) (wallPoints[1].x * scale + mapCenterX);
                        int y2 = (int) (-wallPoints[1].y * scale + mapCenterY); // Invert Y

                        // Draw the wall segment on the map.
                        g2.drawLine(x1, y1, x2, y2);
                    }
                }

                // --- Draw the Rays on the Poincaré Disk ---
                g2.setColor(Color.RED);
                for (Point point : intersectionPoints) {
                     if (point == null) continue; // Skip if ray didn't hit

                    // Convert intersection point coordinates to map screen coordinates.
                    int endX = (int) (point.x * scale + mapCenterX);
                    int endY = (int) (-point.y * scale + mapCenterY); // Invert Y

                    // Draw the line representing the ray from center to intersection.
                    g2.drawLine(mapCenterX, mapCenterY, endX, endY);
                }
            }
        };

        // Enable focus on the panel to capture key events.
        panel.setFocusable(true);
        // No need for requestFocusInWindow here, SwingUtilities.invokeLater in main is better.
        return panel;
    }
}
