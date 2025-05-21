/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.caster;

import java.util.ArrayList;
import java.util.List;

import dev.cocosol.Point;

public class CasterResult {
    public List<Double>[] shadowsIntensity;
    public Point[] intersectionPoints;

    public CasterResult(int width) {
        this.shadowsIntensity = new ArrayList[width];
        this.intersectionPoints = new Point[width];
    }
}
