/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;

public class Main {
	public static void main(String[] args) {
		System.out.println(Version.getVersion());
		WindowManager windowManager = new WindowManager("hyper", 1000, 600, true);
		windowManager.init();

		while (!windowManager.windowShouldClose()) {
			windowManager.update();
		}
		windowManager.cleanUp();
	}
}