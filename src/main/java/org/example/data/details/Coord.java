package org.example.data.details;

import java.awt.*;
import java.util.Objects;

public class Coord {
    private final int x;
    private final int y;

    public Coord(int x, int y) {
        if (0 > x || x > 7)
            throw new IllegalArgumentException("Column out of range. Please provide a character from 0 to 7.");

        if (0 > y || y > 7)
            throw new IllegalArgumentException("Row out of range. Please provide a character from 0 to 7.");

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

    public static Coord of (char column, char row) {
        return new Coord(columnToNumber(column), rowToNumber(row - '0'));
    }

    public static Coord of (String notation) {
        if (notation.length() != 2)
            throw new IllegalArgumentException("notation is too long!");

        return Coord.of(notation.charAt(0), notation.charAt(1));
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

    public char getNotationColumn() {
        return (char)('a' + this.x);
    }

    public char getNotationRow() {
        return (char)(this.y + '1');
    }

    public String getNotation() {
        return "" + getNotationColumn() + getNotationRow();
    }

    @Override
    public String toString() {
        return "Coord{" + x + ", " + y + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coord coord = (Coord) o;
        return x == coord.x && y == coord.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
