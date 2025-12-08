package org.example;


import java.util.ArrayList;
import java.util.List;

public class Coordinate {
    private long x, y, z;
    private List<Coordinate> connectedCords = new ArrayList<>();

    public Coordinate(long x, long y, long z) {
        this.x = x; this.y = y; this.z = z;
    }

    public long getX() { return x; }
    public long getY() { return y; }
    public long getZ() { return z; }

    public List<Coordinate> getCoordinates() {
        return connectedCords;
    }
}
