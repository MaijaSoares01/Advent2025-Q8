package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Coordinate> coordinates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/junctionBoxes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] xyzLine = line.split(",");
                coordinates.add(new Coordinate(Long.parseLong(xyzLine[0].trim()),Long.parseLong(xyzLine[1].trim()),Long.parseLong(xyzLine[2].trim())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int n = coordinates.size();

        // Build all pairs with squared distances
        List<Pair> pairs = new ArrayList<>();

        //\(d=\sqrt{(x_{2}-x_{1})^{2}+(y_{2}-y_{1})^{2}+(z_{2}-z_{1})^{2}}\)
        for (int i = 0; i < n; i++) {
            Coordinate cord = coordinates.get(i);
            for (int v = i + 1; v < n; v++) {
                Coordinate cord2 = coordinates.get(v);
                long dx = cord2.getX() - cord.getX();
                long dy = cord2.getY() - cord.getY();
                long dz = cord2.getZ() - cord.getZ();
                long dist2 = dx * dx + dy * dy + dz * dz;
                pairs.add(new Pair(i, v, dist2));
            }
        }
        // Sort pairs ascending by distance
        pairs.sort(Comparator.comparingLong(p -> p.dist2));
//        // Initialize Union-Find structure for circuits
//        UnionFind uf = new UnionFind(n);
//        int circuitCount = n;
//        long lastXProduct = -1; // store product of last connected pair's X coordinates

        // 1. Connect the first 1000 pairs for Part 1
        int maxConnections = Math.min(1000, pairs.size());
        UnionFind ufPart1 = new UnionFind(n);
        for (int i = 0; i < maxConnections; i++) {
            Pair p = pairs.get(i);
            boolean merged = ufPart1.union(p.i1, p.i2);
            if (merged) {
                Coordinate c1 = coordinates.get(p.i1);
                Coordinate c2 = coordinates.get(p.i2);
                c1.getCoordinates().add(c2);
                c2.getCoordinates().add(c1);
            }
        }
// Calculate circuit sizes for Part 1
        Map<Integer, Integer> circuitSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = ufPart1.find(i);
            circuitSizes.put(root, circuitSizes.getOrDefault(root, 0) + 1);
        }
        List<Integer> sizes = new ArrayList<>(circuitSizes.values());
        sizes.sort(Comparator.reverseOrder());
        long productPart1 = 1;
        for (int i = 0; i < Math.min(3, sizes.size()); i++) {
            productPart1 *= sizes.get(i);
        }
        System.out.println("Part 1: Product of the three largest circuit sizes: " + productPart1);

// 2. Continue from 1000th pair onwards until all connected for Part 2
        UnionFind ufPart2 = new UnionFind(n);
        for (int i = 0; i < maxConnections; i++) {
            ufPart2.union(pairs.get(i).i1, pairs.get(i).i2);  // replay connections done in part 1
        }
// Count current number of circuits
        Set<Integer> roots = new HashSet<>();
        for (int i = 0; i < n; i++) {
            roots.add(ufPart2.find(i));
        }
        int circuitCount = roots.size();

        long lastXProduct = -1;
        for (int i = maxConnections; i < pairs.size(); i++) {
            Pair p = pairs.get(i);
            if (ufPart2.union(p.i1, p.i2)) {
                circuitCount--;
                if (circuitCount == 1) {
                    Coordinate c1 = coordinates.get(p.i1);
                    Coordinate c2 = coordinates.get(p.i2);
                    lastXProduct = c1.getX() * c2.getX();
                    break;
                }
            }
        }

        // Multiply top three largest circuits sizes (or fewer if less)
        long product = 1;
        for (int i = 0; i < Math.min(3, sizes.size()); i++) {
            product *= sizes.get(i);
        }

        System.out.println("Circuit sizes (desc): " + sizes);
        System.out.println("Product of the three largest circuit sizes: " + product);
        if (lastXProduct != -1) {
            System.out.println("Part 2: Product of X coords of last connected junction boxes: " + lastXProduct);
        } else {
            System.out.println("All junction boxes were already connected in part 1.");
        }
    }
}

