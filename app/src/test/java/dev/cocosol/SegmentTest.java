/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SegmentTest {

    @Test
    public void testValidSegmentCreation() {
        Point a = new Point(0, 0);
        Point b = new Point(1, 1);
        Segment segment = new Segment(a, b);
        assertNotNull(segment);
    }

    @Test
    public void testInvalidSegmentSamePoints() {
        Point a = new Point(2, 2);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Segment(a, new Point(2, 2));
        });
        assertEquals("Points must be different", exception.getMessage());
    }

    @Test
    public void testSegmentsIntersect() {
        Segment s1 = new Segment(new Point(0, 0), new Point(2, 2));
        Segment s2 = new Segment(new Point(0, 2), new Point(2, 0));
        assertTrue(s1.intersect(s2));
    }

    @Test
    public void testSegmentsDoNotIntersect() {
        Segment s1 = new Segment(new Point(0, 0), new Point(1, 1));
        Segment s2 = new Segment(new Point(2, 2), new Point(3, 3));
        assertFalse(s1.intersect(s2));
    }

    @Test
    public void testSegmentsColinearButNotOverlapping() {
        Segment s1 = new Segment(new Point(0, 0), new Point(1, 1));
        Segment s2 = new Segment(new Point(2, 2), new Point(3, 3));
        assertFalse(s1.intersect(s2));
    }

    @Test
    public void testIntersectAtEndpoint() {
        Segment s1 = new Segment(new Point(0, 0), new Point(1, 1));
        Segment s2 = new Segment(new Point(1, 1), new Point(2, 0));
        assertFalse(s1.intersect(s2));
    }

    @Test
    public void testParallelSegmentsDoNotIntersect() {
        Segment s1 = new Segment(new Point(0, 0), new Point(1, 0));
        Segment s2 = new Segment(new Point(0, 1), new Point(1, 1));
        assertFalse(s1.intersect(s2));
    }
}
