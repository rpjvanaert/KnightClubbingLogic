package org.example.data.details;

import java.awt.*;

public class Coord {
    private final int x;
    private final int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getPoint() {
        return new Point(this.x, this.y);
    }

    public Coord getAdjacent(int xDiff, int yDiff) {
        return new Coord(
                this.x + xDiff,
                this.y + yDiff
        );
    }

    public static Coord of(char column, int row) {
        return new Coord(columnToNumber(column), rowToNumber(row));
    }

    private static int columnToNumber(char c) {
        if (c >= 'a' && c <= 'h') {
            return c - 'a';
        } else {
            throw new IllegalArgumentException("Character out of range. Please provide a character from 'a' to 'h'.");
        }
    }

    private static int rowToNumber(int row) {
        if (0 <= row && row <= 8) {
            return row - 1;
        } else {
            throw new IllegalArgumentException("Row out of range. Please provide a character from 1 to 8.");
        }
    }

    @Override
    public String toString() {
        return "Coord{" + x + ", " + y + '}';
    }
}
