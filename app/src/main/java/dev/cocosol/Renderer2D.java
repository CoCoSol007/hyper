/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 */

package dev.cocosol;

import dev.cocosol.hyperbolic.Point;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;
import dev.cocosol.hyperbolic.paving.Paving;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/// A 2D renderer for the Poincaré disk
public class Renderer2D {
    public static void main(String[] args) {
        Paving paving = new Paving();
        JFrame frame = new JFrame("hyper");

        JPanel panel = getJPanel(paving);


        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                boolean needsRepaint = false;

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
                        case KeyEvent.VK_L  -> {
                            paving.applyRotation(Math.PI / 100);
                            needsRepaint = true;
                        }
                        case KeyEvent.VK_M  -> {
                            paving.applyRotation(-Math.PI / 100);
                            needsRepaint = true;
                        }
                    };

                    if (needsRepaint) {
                        panel.repaint();
                    }
                }
            });


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        SwingUtilities.invokeLater(panel::requestFocusInWindow);
    }

    /// Returns the JPanel with the Paving
    private static JPanel getJPanel(Paving paving) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                int w = getWidth(), h = getHeight();
                int scale = Math.min(w, h) / 2 - 20;
                int centerX = w / 2, centerY = h / 2;

                g2.setColor(Color.GRAY);
                g2.drawOval(centerX - scale, centerY - scale, scale * 2, scale * 2);
                g2.drawOval(centerX, centerY, 2, 2);

                for (Chunk chunk : paving.getAllNeighbors(5)) {
                    for (Direction direction : Direction.values()) {
                        Point[] points = chunk.getPointFromDirection(direction);
                        g2.setColor(Color.DARK_GRAY);
                        int[] xPoints = new int[] {(int) (points[0].x * scale + centerX),(int) (points[1].x * scale + centerX)};
                        int[] yPoints = new int[] {(int) ( - points[0].y * scale + centerY),(int) ( - points[1].y * scale + centerY)};
                        g2.drawPolygon(xPoints, yPoints, 2);
                    }
                }
            }
        };

        panel.setFocusable(true);
        panel.requestFocusInWindow();
        return panel;
    }
}