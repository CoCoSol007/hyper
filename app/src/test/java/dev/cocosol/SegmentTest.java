/*
 * SPDX-License-Identifier: GPL-3.0-or-later
 * Copyright (C) 2025 CoCoSol - Hyper
 * Copyright (C) 2025 Plouf-Charles - Hyper
 */

package dev.cocosol;

import org.junit.Assert;
import org.junit.Test;

public class SegmentTest {

    @Test
    public void testValidSegmentCreation() {
        final Point a = new Point(0, 0);
        final Point b = new Point(1, 1);
        final Segment segment = new Segment(a, b);
        Assert.assertNotNull(segment);
    }

    @Test
    public void testInvalidSegmentSamePoints() {
        final Point a = new Point(2, 2);
        final Exception exception = Assert.assertThrows(IllegalArgumentException.class, () -> {
            new Segment(a, new Point(2, 2));
        });
        Assert.assertEquals("Points must be different", exception.getMessage());
    }

    @Test
    public void testSegmentsIntersect() {
        final Segment s1 = new Segment(new Point(0, 0), new Point(2, 2));
        final Segment s2 = new Segment(new Point(0, 2), new Point(2, 0));
        Assert.assertTrue(s1.intersect(s2));
    }

    @Test
    public void testSegmentsDoNotIntersect() {
        final Segment s1 = new Segment(new Point(0, 0), new Point(1, 1));
        final Segment s2 = new Segment(new Point(2, 2), new Point(3, 3));
        Assert.assertFalse(s1.intersect(s2));
    }

    @Test
    public void testSegmentsColinearButNotOverlapping() {
        final Segment s1 = new Segment(new Point(0, 0), new Point(1, 1));
        final Segment s2 = new Segment(new Point(2, 2), new Point(3, 3));
        Assert.assertFalse(s1.intersect(s2));
    }

    @Test
    public void testIntersectAtEndpoint() {
        final Segment s1 = new Segment(new Point(0, 0), new Point(1, 1));
        final Segment s2 = new Segment(new Point(1, 1), new Point(2, 0));
        Assert.assertFalse(s1.intersect(s2));
    }

    @Test
    public void testParallelSegmentsDoNotIntersect() {
        final Segment s1 = new Segment(new Point(0, 0), new Point(1, 0));
        final Segment s2 = new Segment(new Point(0, 1), new Point(1, 1));
        Assert.assertFalse(s1.intersect(s2));
    }
}
