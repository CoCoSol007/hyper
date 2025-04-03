/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 Hyper
 */

package dev.cocosol;

import dev.cocosol.hyperbolic.paving.Chunk;
import dev.cocosol.hyperbolic.paving.Direction;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Direction[] directions = new Direction[]{Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.RIGHT, Direction.LEFT};
        Chunk c = new Chunk(List.of(directions));
        System.out.println(c);

        int num = c.encode();
        System.out.println(num);
        System.out.println(c.getHash(678567));
    }
}
