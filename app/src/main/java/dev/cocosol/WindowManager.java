/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

/**
 * Manages the window and OpenGL context.
 * <p>
 * This class encapsulates the initialization and management of the window and OpenGL context.
 * It provides methods to create the window, initialize the OpenGL context, and update and clean up the window.
 */
public class WindowManager {
    public static final float FOV = (float)Math.toRadians(60);

    public static final float Z_NEAR = 0.1f;
    public static final float Z_FAR = 100f;

    public final String title;

    public int width;

    public int height;

    public long window;

    private boolean resize;

    private boolean vSync;

    private final Matrix4f projection;

    public WindowManager(final String title, final int width, final int height, final boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;

        projection = new Matrix4f();
    }

    /**
     * Initializes the window and OpenGL context.
     */
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);

        boolean maximized = false;
        if (width == 0 || height == 0) {
            maximized = true;
            width = 100;
            height = 100;
            GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
        }

        window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResize(true);
        });

        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        if (maximized) {
            GLFW.glfwMaximizeWindow(window);
        } else {
            GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            if (vidMode == null) {
                throw new RuntimeException("Failed to get video mode");
            }
            GLFW.glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(vSync ? 1 : 0);
        GL.createCapabilities();
        GLFW.glfwShowWindow(window);

        GL11.glClearColor(0, 0, 0, 0);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    /**
     * Updates the window and OpenGL context.
     */
    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    /**
     * Cleans up the window and OpenGL context.
     */
    public void cleanUp() {
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }

    public boolean isKeyPressed(final int key) {
        return GLFW.glfwGetKey(window, key) == GLFW.GLFW_PRESS;
    }

    public boolean windowShouldClose() {
        return GLFW.glfwWindowShouldClose(window);
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = width / (float)height;
        return projection.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(final Matrix4f matrix, final int width, final int height) {
        float aspectRatio = width / (float)height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(final boolean resize) {
        this.resize = resize;
    }
}
