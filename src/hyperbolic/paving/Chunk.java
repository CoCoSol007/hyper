package src.hyperbolic.paving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import src.hyperbolic.Geodesic;
import src.hyperbolic.HyperbolicMath;
import src.hyperbolic.Point;

public class Chunk {
    public Point[] points;
    private static final int p = 4;
    private static final int q = 5;

    public static final Chunk ORIGIN_CHUNK = get_origin_chunk();

    public static double get_distance() {
        double a = Math.tan(Math.PI / 2 - Math.PI / q);
        double b =  Math.tan(Math.PI / p);
        return Math.sqrt((a-b)/(2*a+2*b));
    }


    public static Chunk get_origin_chunk() {
        double d = get_distance();
        return new Chunk(new Point[]{new Point(-d, d), new Point(d, d), new Point(d, -d), new Point(-d, -d)});
    }

    public Geodesic[] get_side_geodesics() {
        Geodesic[] geodesics = new Geodesic[4];
        for (int i = 0; i < 4; i++) {
            Point p1 = this.points[i % 4];
            Point p2 = this.points[(i + 1) % 4];
            geodesics[i] = Geodesic.from_two_points(p1, p2);
        }
        return geodesics;
    }

    public Chunk(Point[] points) {
        if (points.length != 4) {
            throw new IllegalArgumentException("Chunk must have 4 points");
        }
        this.points = points;
    }

    public static void main(String[] args) {
        HashSet<Chunk> pavings = Chunk.get_pavings(3);
        for (Chunk chunk : pavings) {
            System.out.println(chunk);
        }

        System.out.println(pavings.size());
    }



    //// return true if the chunks are equal
    public boolean equals(Chunk other) {
        if (this == other) return true;
        if (other == null) return false;
        for (int i = 0; i < 4; i++) {
            if (!this.points[i].equals(other.points[0])) continue;
            boolean direct = true, reverse = true;
            for (int j = 0; j < n; j++) {
                if (!this.points[(i + j) % n].equals(other.points[j])) direct = false;
                if (!this.points[(i - j + n) % n].equals(other.points[j])) reverse = false;
                if (!direct && !reverse) break;
            }
            if (direct || reverse) return true;
        }
        return false;
    }



    public static HashSet<Chunk> get_pavings(int n) {
        if (n == 0) {
            return new HashSet<>(List.of(ORIGIN_CHUNK));
        }

        HashSet<Chunk> neighbors = get_pavings(n - 1);
        HashSet<Chunk> new_neighbors = new HashSet<>();
        for (Chunk neighbor : neighbors) {
            Chunk[] neighbor_neighbors = neighbor.get_neighbors();
            for (Chunk neighbor_neighbor : neighbor_neighbors) {
                if (neighbor_neighbor == null) {
                    continue;
                }
                boolean already_exists = false;
                for (Chunk neighbor_neighbor2 : neighbors) {
                    if (neighbor_neighbor.equals(neighbor_neighbor2)) {
                        already_exists = true;
                    }
                }
                if (!already_exists) {
                    new_neighbors.add(neighbor_neighbor);

                }
            }
        }

        neighbors.addAll(new_neighbors);

        return neighbors;
    }


    private static boolean contains_chunk(HashSet<Chunk> set, Chunk candidate) {
        for (Chunk element : set) {
            if (element.equals(candidate)) {
                return true;
            }
        }
        return false;
    }

public Chunk get_inverted_chunk_with_respect_to_geodesic(Geodesic geodesic) {
    Point[] inverted_points = new Point[4];
    for (int i = 0; i < 4; i++) {
        inverted_points[i] = HyperbolicMath.inverse_with_respect_to_geodesic(points[i], geodesic);
        if (inverted_points[i].equals(new Point(0.25, -1.25))) {
            return null;
        }
    }

    return new Chunk(inverted_points);
}

public Chunk[] get_neighbors() {
    Chunk[] neighbors = new Chunk[4];

    Geodesic[] geodesics = this.get_side_geodesics();
    for (int i = 0; i < 4; i++) {
        Geodesic geodesic = geodesics[i];
        neighbors[i] = this.get_inverted_chunk_with_respect_to_geodesic(geodesic);
    }

    return neighbors;
}

    public String toString() {
        return "Polygon"+Arrays.toString(this.points);
    }
}
