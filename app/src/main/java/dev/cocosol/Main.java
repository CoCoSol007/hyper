/**
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

import dev.cocosol.hyperbolic.Projection;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Paving;

public class Main extends SimpleApplication {

    /**
     * Depth of the paving
     */
    static final int DEPTH = 7;

    /**
     * Scale of the paving
     */
    static final float SCALE = 10;

    /**
     * Intensity of the shadow
     */
    static final float SHADOW = 10;

    /**
     * Speed of the camera
     */
    static final float SPEED = 0.25f;

    /**
     * The gravity of the world
     */
    static final float GRAVITY = 15f;

    /**
     * The jump force of the camera
     */
    static final float JUMP_FORCE = 7f;

    /**
     * The projection of the hyperbolic plane
     */
    static Projection projection = Projection.defaultProjection();

    /**
     * The paving of the scene
     */
    Paving paving = new Paving();

    /**
     * The movement vector
     */
    Vector3f move = Vector3f.ZERO;

    /**
     * The geometries of the scene, it regroups all the chunks
     */
    List<Geometry> geometries = new ArrayList<>();

    /**
     * The action listener, it handles the inputs
     */
    private final ActionListener actionListener = (name, isPressed, tpf) -> {
        if ("MoveUp".equals(name) && isPressed && this.cam.getLocation().y <= 3) {
            this.move.y = Main.JUMP_FORCE;
        }
        switch (name) {
            case "MoveForward":
                this.move.x = isPressed ? 1 : (this.move.x == 1 ? 0 : this.move.x);
                break;
            case "MoveBackward":
                this.move.x = isPressed ? -1 : (this.move.x == -1 ? 0 : this.move.x);
                break;
            case "MoveLeft":
                this.move.z = isPressed ? 1 : (this.move.z == 1 ? 0 : this.move.z);
                break;
            case "MoveRight":
                this.move.z = isPressed ? -1 : (this.move.z == -1 ? 0 : this.move.z);
                break;
            default:
                break;
        }
    };

    public static void main(final String[] args) {

        if (args.length != 0) {
            Main.projection = Projection.fromString(args[0]);
        }

        Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

        final Main app = new Main();

        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.setDisplayStatView(false);

        final AppSettings settings = new AppSettings(true);

        settings.setTitle("Hyper");
        settings.setVSync(true);

        final GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final DisplayMode[] modes = device.getDisplayModes();
        final int i = 0;
        settings.setResolution(modes[i].getWidth(), modes[i].getHeight());
        settings.setFrequency(modes[i].getRefreshRate());
        settings.setBitsPerPixel(modes[i].getBitDepth());
        settings.setFullscreen(device.isFullScreenSupported());

        app.setSettings(settings);

        app.start();
    }

    @Override
    public void simpleInitApp() {
        // BACKGROUND
        this.viewPort.setBackgroundColor(ColorRGBA.Cyan);

        // CAMERA
        this.flyCam.setMoveSpeed(0);
        this.cam.setLocation(new Vector3f(0, 3, 0));

        // INPUT
        this.inputManager.addMapping("MoveForward", new KeyTrigger(KeyInput.KEY_W));
        this.inputManager.addMapping("MoveBackward", new KeyTrigger(KeyInput.KEY_S));
        this.inputManager.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_D));
        this.inputManager.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_A));
        this.inputManager.addMapping("MoveUp", new KeyTrigger(KeyInput.KEY_SPACE));
        this.inputManager.addListener(this.actionListener, "MoveForward", "MoveDown", "MoveBackward", "MoveRight",
                "MoveLeft",
                "MoveUp");

        for (final Chunk chunk : this.paving.getAllNeighbors(Main.DEPTH)) {
            final List<Point> vertices = new ArrayList<>(chunk.vertices);

            for (int i = 0; i < 4; i++) {
                vertices.set(i, chunk.vertices.get(i).mul(Main.SCALE));
            }

            final Vector2f[] quad = new Vector2f[] {
                    new Vector2f((float) vertices.get(0).x, (float) vertices.get(0).y),
                    new Vector2f((float) vertices.get(1).x, (float) vertices.get(1).y),
                    new Vector2f((float) vertices.get(2).x, (float) vertices.get(2).y),
                    new Vector2f((float) vertices.get(3).x, (float) vertices.get(3).y)
            };

            final Geometry g = this.createBlockFrom2DQuad(quad, 1);
            this.geometries.add(g);
            this.rootNode.attachChild(g);
        }
        this.updateGeometry();
    }

    /**
     * Creates a 3D block geometry from a 2D quadrilateral base.
     *
     * @param base2D an array of 2D vectors representing the vertices of the base
     *               quadrilateral.
     * @param height the height to extrude the base quadrilateral into a 3D block.
     * @return a Geometry object representing the 3D block with the specified base
     *         and height.
     */
    public Geometry createBlockFrom2DQuad(final Vector2f[] base2D, final float height) {

        final Vector3f[] base3D = new Vector3f[4];
        final Vector3f[] top3D = new Vector3f[4];

        for (int i = 0; i < 4; i++) {
            base3D[i] = new Vector3f(base2D[i].x, 0, base2D[i].y);
            top3D[i] = base3D[i].add(0, height, 0);
        }

        final Vector3f[] vertices = new Vector3f[] {
                base3D[0], base3D[1], base3D[2], base3D[3],
                top3D[0], top3D[1], top3D[2], top3D[3]
        };

        final List<Point> verticesList = new ArrayList<>();
        for (final Vector3f v : vertices) {
            verticesList.add(new Point(v.x, v.z));
        }

        final int[] indices = {
                0, 1, 2, 0, 2, 3,
                4, 6, 5, 4, 7, 6,
                0, 4, 5, 0, 5, 1,
                1, 5, 6, 1, 6, 2,
                2, 6, 7, 2, 7, 3,
                3, 7, 4, 3, 4, 0
        };

        final Mesh mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));
        mesh.updateBound();

        final Geometry geom = new Geometry("Block", mesh);

        final Material mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geom.setMaterial(mat);

        return geom;
    }

    @Override
    public void simpleUpdate(final float tpf) {
        final Complex direction2D = new Complex(this.cam.getDirection().x, this.cam.getDirection().z);

        if (this.move.x != 0 || this.move.z != 0) {
            final Complex move2D = Complex.exponent(1, new Vector2f(this.move.x, this.move.z).getAngle());
            this.paving.applyMovement(-move2D.getAngle() + direction2D.getAngle(), tpf * Main.SPEED);
            this.updateGeometry();
        }

        if (this.cam.getLocation().y >= 3 || this.move.y != 0) {
            this.move.y -= Main.GRAVITY * tpf;
            this.cam.setLocation(this.cam.getLocation().add(0, this.move.y * tpf, 0));
            if (this.cam.getLocation().y < 3) {
                this.move.y = 0;
                this.cam.setLocation(this.cam.getLocation().add(0, 3 - this.cam.getLocation().y, 0));
            }

        }
    }

    /**
     * Updates the geometries in the scene based on the current positioning of
     * chunks.
     * This method recalculates the 3D positions of the vertices for each chunk,
     * scales them, and updates the corresponding mesh buffers. It ensures that
     * the visual representation of the tiling reflects the current state of the
     * hyperbolic paving and applies appropriate color textures.
     */
    private void updateGeometry() {
        final List<Chunk> chunks = this.paving.getAllNeighbors(Main.DEPTH);
        for (int i = 0; i < chunks.size(); i++) {
            final Chunk chunk = chunks.get(i);
            final List<Point> vertices = new ArrayList<>(chunk.vertices);

            for (int j = 0; j < 4; j++) {
                Point p = vertices.get(j);
                p = switch (Main.projection) {
                    case KLEIN -> p.toKleinModel();
                    case GNOMONIC -> p.toGnomonicModel();
                    case POINCARE -> p;
                };
                p = p.mul(Main.SCALE);
                vertices.set(j, p);
            }

            final Vector3f[] base3D = new Vector3f[4];
            final Vector3f[] top3D = new Vector3f[4];

            for (int j = 0; j < 4; j++) {
                base3D[j] = new Vector3f((float) vertices.get(j).x, 0, (float) vertices.get(j).y);
                top3D[j] = base3D[j].add(0, 1, 0);
            }

            final Geometry g = this.geometries.get(i);
            final Mesh mesh = g.getMesh();

            final Vector3f[] verts = new Vector3f[] {
                    base3D[0], base3D[1], base3D[2], base3D[3],
                    top3D[0], top3D[1], top3D[2], top3D[3]
            };

            mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verts));
            mesh.updateBound();

            g.getMaterial().setColor("Color", this.getColorTexture(chunk));
            g.updateModelBound();
            g.updateGeometricState();
        }
    }

    /**
     * Calculates a color for the given chunk based on its distance from the
     * center of the screen.
     *
     * @param vertices the vertices of the chunk as seen on the screen
     * @return the color for the chunk
     */
    private ColorRGBA getColorTexture(final Chunk chunk) {
        final int index = chunk.hashCode();

        final int r = (index & 0xFF0000) >> 16;
        final int g = (index & 0x00FF00) >> 8;
        final int b = index & 0x0000FF;

        return new ColorRGBA(r / 255f, g / 255f, b / 255f, 1);
    }
}
