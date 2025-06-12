/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
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

import dev.cocosol.hyperbolic.Projection;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;
import dev.cocosol.hyperbolic.paving.Paving;

/**
 * A 2D renderer for visualizing the Poincaré disk.
 * 
 * This class creates a window that visualizes the hyperbolic paving, with
 * controls to move and rotate the paving.
 */
public class Renderer2D {
    /**
     * The main entry point of the application. It initializes the Paving and
     * JFrame,
     * and sets up key listeners for user interaction.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(final String[] args) {
        Projection type = Projection.defaultProjection();
        if (args.length != 0) {
            type = Projection.fromString(args[0]);
        }

        final Paving paving = new Paving();
        final JFrame frame = new JFrame("hyper");

        final JPanel panel = Renderer2D.getJPanel(paving, type);

        // Add key listener for user interaction
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                boolean needsRepaint = false;

                // Process key events for movement and rotation
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
                    case KeyEvent.VK_M -> {
                        paving.applyRotation(Math.PI / 100);
                        needsRepaint = true;
                    }
                    case KeyEvent.VK_L -> {
                        paving.applyRotation(-Math.PI / 100);
                        needsRepaint = true;
                    }
                    default -> {
                        break;
                    }
                }

                // Repaint the panel if needed
                if (needsRepaint) {
                    panel.repaint();
                }
            }
        });

        // Set up the frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Request focus for the panel so it can receive key events
        SwingUtilities.invokeLater(panel::requestFocusInWindow);
    }

    /**
     * Returns a JPanel that renders the Paving in a 2D view.
     *
     * @param paving the Paving object to render
     * @return the JPanel that will render the Paving
     */
    private static JPanel getJPanel(final Paving paving, final Projection projection) {
        final JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Graphics2D g2 = (Graphics2D) g;

                final int w = this.getWidth();
                final int h = this.getHeight();
                final int scale = Math.min(w, h) / 2 - 20;
                final int centerX = w / 2;

                final int centerY = h / 2;
                // Draw the unit circle

                if (projection != Projection.GNOMONIC && projection != Projection.HALFPLANE) {
                    g2.setColor(Color.GRAY);
                    g2.drawOval(centerX - scale, centerY - scale, scale * 2, scale * 2);
                }
                g2.drawOval(centerX, centerY, 2, 2); // Draw the center point

                // Draw the neighbors of the Paving
                for (final Chunk chunk : paving.getAllNeighbors(4)) {
                    for (final Direction direction : Direction.values()) {
                        final Point[] points = chunk.getPointFromDirection(direction);

                        if (projection == Projection.KLEIN) {
                            for (int i = 0; i < points.length; i++) {
                                points[i] = points[i].toKleinModel(); // Convert to Klein model
                            }
                        }
                        if (projection == Projection.GNOMONIC) {
                            for (int i = 0; i < points.length; i++) {
                                points[i] = points[i].toGnomonicModel().mul(0.3); // Convert to GAns model
                            }
                        }
                        if (projection == Projection.HALFPLANE) {
                            for (int i = 0; i < points.length;  i++) {
                                points[i] = points[i].toHalfPlaneModel();
                                points[i].y -= 1;
                            }
                        }

                        g2.setColor(Color.DARK_GRAY);
                        final int[] xPoints = new int[] {
                                (int) (points[0].x * scale + centerX),
                                (int) (points[1].x * scale + centerX)
                        };
                        final int[] yPoints = new int[] {
                                (int) (-points[0].y * scale + centerY),
                                (int) (-points[1].y * scale + centerY)
                        };
                        g2.drawPolygon(xPoints, yPoints, 2); // Draw a line between points
                    }
                }
            }
        };

        // Make the panel focusable so it can capture key events
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        return panel;
    }
}
