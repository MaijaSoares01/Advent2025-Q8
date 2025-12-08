package org.example;

// Helper class to store pairs of indices and their squared distance
public class Pair {
    int i1, i2;
    long dist2;
    Pair(int i1, int i2, long dist2) {
        this.i1 = i1;
        this.i2 = i2;
        this.dist2 = dist2;
    }
}
