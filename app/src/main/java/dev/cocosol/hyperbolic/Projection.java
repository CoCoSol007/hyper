/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol.hyperbolic;

/**
 * Enum representing the different types of projections available.
 * Currently, it supports Poincaré and Klein projections.
 */
public enum Projection {

    /**
     * Poincaré projection.
     * This projection is a conformal map of the hyperbolic plane onto the unit
     * disk.
     */
    POINCARE,

    /**
     * Klein projection.
     * This projection is a unconformal map of the hyperbolic plane onto the unit
     * disk.
     */
    KLEIN,

    /**
     * Gnomonic projection.
     */
    GNOMONIC;

    /**
     * Returns the projection matching the given name, or the default projection if
     * no match is found.
     *
     * @param name the name of the projection, case insensitive
     * @return the projection matching the given name, or the default projection if
     *         no match is found
     */
    public static Projection fromString(final String name) {
        for (final Projection projection : Projection.values()) {
            if (projection.name().equalsIgnoreCase(name)) {
                return projection;
            }
        }
        System.err.println("Unknown projection: " + name);
        return defaultProjection();
    }

    /**
     * Returns the default projection, which is Poincaré.
     * 
     * @return the default projection
     */
    public static Projection defaultProjection() {
        return POINCARE;
    }
}
