/**
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

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
import com.jme3.util.BufferUtils;
import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Paving;

public class Main extends SimpleApplication {

    /**
     * Depth of the paving
     */
    static final int DEPTH = 6;

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
    static final float GRAVITY = 0.3f;

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
    private final ActionListener actionListener = new ActionListener() {
        public void onAction(final String name, final boolean isPressed, final float tpf) {
            if ("MoveUp".equals(name) && isPressed && cam.getLocation().y <= 3) {
                move.z = 7;
            }
            switch (name) {
                case "MoveForward":
                    move.x = isPressed ? 1 : (move.x == 1 ? 0 : move.x);
                    break;
                case "MoveBackward":
                    move.x = isPressed ? -1 : (move.x == -1 ? 0 : move.x);
                    break;
                case "MoveLeft":
                    move.y = isPressed ? 1 : (move.y == 1 ? 0 : move.y);
                    break;
                case "MoveRight":
                    move.y = isPressed ? -1 : (move.y == -1 ? 0 : move.y);
                    break;
                default: break;
            }
        }        
    };

    public static void main(final String[] args) {
        Main app = new Main();
        
        // SETTINGS
        app.setShowSettings(false);
        app.setPauseOnLostFocus(false);
        app.setDisplayStatView(false);
        app.loseFocus();
        Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

        app.start();
    }

    @Override
    public void simpleInitApp() {
        // BACKGROUND
        viewPort.setBackgroundColor(ColorRGBA.Cyan);

        // CAMERA
        flyCam.setMoveSpeed(0);
        cam.setLocation(new Vector3f(0, 3, 0));

        // INPUT
        inputManager.addMapping("MoveForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("MoveBackward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("MoveUp", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener, "MoveForward", "MoveDown", "MoveBackward", "MoveRight", "MoveLeft",
                "MoveUp");

        for (final Chunk chunk : paving.getAllNeighbors(DEPTH)) {
            List<Point> vertices = new ArrayList<>(chunk.vertices);

            for (int i = 0; i < 4; i++) {
                vertices.set(i, chunk.vertices.get(i).mul(SCALE));
            }

            Vector2f[] quad = new Vector2f[] {
                new Vector2f((float)vertices.get(0).x, (float)vertices.get(0).y),
                new Vector2f((float)vertices.get(1).x, (float)vertices.get(1).y),
                new Vector2f((float)vertices.get(2).x, (float)vertices.get(2).y),
                new Vector2f((float)vertices.get(3).x, (float)vertices.get(3).y)
            };

            Geometry g = createBlockFrom2DQuad(quad, 1);
            geometries.add(g);
            rootNode.attachChild(g);
        }
        updateGeometry();
    }

    /**
     * Creates a 3D block geometry from a 2D quadrilateral base.
     *
     * @param base2D an array of 2D vectors representing the vertices of the base quadrilateral.
     * @param height the height to extrude the base quadrilateral into a 3D block.
     * @return a Geometry object representing the 3D block with the specified base and height.
     */
    public Geometry createBlockFrom2DQuad(final Vector2f[] base2D, final float height) {

        Vector3f[] base3D = new Vector3f[4];
        Vector3f[] top3D = new Vector3f[4];

        for (int i = 0; i < 4; i++) {
            base3D[i] = new Vector3f(base2D[i].x, 0, base2D[i].y);
            top3D[i] = base3D[i].add(0, height, 0);
        }

        Vector3f[] vertices = new Vector3f[] {
                base3D[0], base3D[1], base3D[2], base3D[3],
                top3D[0], top3D[1], top3D[2], top3D[3]
        };

        List<Point> verticesList = new ArrayList<>();
        for (final Vector3f v : vertices) {
            verticesList.add(new Point(v.x, v.z));
        }

        int[] indices = {
            0, 1, 2, 0, 2, 3,
            4, 6, 5, 4, 7, 6,
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0
        };

        Mesh mesh = new Mesh();
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));
        mesh.updateBound();

        Geometry geom = new Geometry("Block", mesh);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        geom.setMaterial(mat);

        return geom;
    }

    @Override
    public void simpleUpdate(final float tpf) {
        Complex direction2D = new Complex(cam.getDirection().x, cam.getDirection().z);

        if (move.x != 0 || move.y != 0) {
            Complex move2D = Complex.exponent(1,  new Vector2f(move.x, move.y).getAngle());
            paving.applyMovement(-move2D.getAngle() + direction2D.getAngle(), tpf * SPEED);
            updateGeometry();
        }

        if (cam.getLocation().y >= 3 || move.z != 0) {
            move.z -= GRAVITY;
            cam.setLocation(cam.getLocation().add(0, move.z * tpf, 0));
            if (cam.getLocation().y < 3) {
                move.z = 0;
                cam.setLocation(cam.getLocation().add(0, 3 - cam.getLocation().y, 0));
            }

        }
    }

    /**
     * Updates the geometries in the scene based on the current positioning of chunks.
     * This method recalculates the 3D positions of the vertices for each chunk,
     * scales them, and updates the corresponding mesh buffers. It ensures that
     * the visual representation of the tiling reflects the current state of the
     * hyperbolic paving and applies appropriate color textures.
     */
    private void updateGeometry() {
        List<Chunk> chunks = paving.getAllNeighbors(DEPTH);
        for (int i = 0; i < chunks.size(); i++) {
            Chunk chunk = chunks.get(i);
            List<Point> vertices = new ArrayList<>(chunk.vertices);

            for (int j = 0; j < 4; j++) {
                vertices.set(j, chunk.vertices.get(j).toGnomonicModel().mul(SCALE));
            }

            Vector3f[] base3D = new Vector3f[4];
            Vector3f[] top3D = new Vector3f[4];

            for (int j = 0; j < 4; j++) {
                base3D[j] = new Vector3f((float)vertices.get(j).x, 0, (float)vertices.get(j).y);
                top3D[j] = base3D[j].add(0, 1, 0);
            }
            
            Geometry g = geometries.get(i);
            Mesh mesh = g.getMesh();

            Vector3f[] verts = new Vector3f[] {
                    base3D[0], base3D[1], base3D[2], base3D[3],
                    top3D[0], top3D[1], top3D[2], top3D[3]
            };

            mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(verts));
            mesh.updateBound();

            g.getMaterial().setColor("Color", getColorTexture(chunk));
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
        int index = chunk.hashCode();
        
        int r = (index & 0xFF0000) >> 16;
        int g = (index & 0x00FF00) >> 8;
        int b = index & 0x0000FF;
        
        return new ColorRGBA(r / 255f, g / 255f, b / 255f, 1);
    }
}
