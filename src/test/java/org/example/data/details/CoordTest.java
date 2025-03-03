package org.example.data.details;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CoordTest {

    private static final String constructorColumnException = "Column out of range. Please provide a character from 0 to 7.";
    private static final String constructorRowException = "Row out of range. Please provide a character from 0 to 7.";

    @Test
    void testGetX() {
        Coord coord = new Coord(3, 5);
        assertEquals(3, coord.getX());
    }

    @Test
    void testGetY() {
        Coord coord = new Coord(3, 5);
        assertEquals(5, coord.getY());
    }

    @Test
    void testInvalidConstructor() {
        Exception exception;

        exception = assertThrows(IllegalArgumentException.class, () -> new Coord(8,0));
        assertEquals(constructorColumnException, exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> new Coord(-1,0));
        assertEquals(constructorColumnException, exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> new Coord(0,8));
        assertEquals(constructorRowException, exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> new Coord(0,-1));
        assertEquals(constructorRowException, exception.getMessage());
    }

    @Test
    void testGetPoint() {
        Coord coord = new Coord(3, 5);
        Point point = coord.getPoint();
        assertEquals(3, point.x);
        assertEquals(5, point.y);
    }

    @Test
    void testGetAdjacent() {
        Coord coord = new Coord(3, 5);
        Coord adjacent = coord.getAdjacent(1, -1);
        assertEquals(4, adjacent.getX());
        assertEquals(4, adjacent.getY());
    }

    @Test
    void testOfValidInputs() {
        Coord coord = Coord.of('b', 2);
        assertEquals(1, coord.getX());
        assertEquals(1, coord.getY());
    }

    @Test
    void testOfInvalidColumn() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Coord.of('z', 3));
        assertEquals("Character out of range. Please provide a character from 'a' to 'h'.", exception.getMessage());
    }

    @Test
    void testOfInvalidRow() {
        Exception exception;
        exception = assertThrows(IllegalArgumentException.class, () -> Coord.of('c', 9));
        assertEquals("Row out of range. Please provide a character from 1 to 8.", exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> Coord.of('c', -1));
        assertEquals("Row out of range. Please provide a character from 1 to 8.", exception.getMessage());
    }

    @Test
    void testAllValidNotations() {
        assertEquals(new Point(0,0), Coord.of('a', 1).getPoint());
        assertEquals(new Point(0,1), Coord.of('a', 2).getPoint());
        assertEquals(new Point(0,2), Coord.of('a', 3).getPoint());
        assertEquals(new Point(0,3), Coord.of('a', 4).getPoint());
        assertEquals(new Point(0,4), Coord.of('a', 5).getPoint());
        assertEquals(new Point(0,5), Coord.of('a', 6).getPoint());
        assertEquals(new Point(0,6), Coord.of('a', 7).getPoint());
        assertEquals(new Point(0,7), Coord.of('a', 8).getPoint());

        assertEquals(new Point(1,0), Coord.of('b', 1).getPoint());
        assertEquals(new Point(1,1), Coord.of('b', 2).getPoint());
        assertEquals(new Point(1,2), Coord.of('b', 3).getPoint());
        assertEquals(new Point(1,3), Coord.of('b', 4).getPoint());
        assertEquals(new Point(1,4), Coord.of('b', 5).getPoint());
        assertEquals(new Point(1,5), Coord.of('b', 6).getPoint());
        assertEquals(new Point(1,6), Coord.of('b', 7).getPoint());
        assertEquals(new Point(1,7), Coord.of('b', 8).getPoint());

        assertEquals(new Point(2,0), Coord.of('c', 1).getPoint());
        assertEquals(new Point(2,1), Coord.of('c', 2).getPoint());
        assertEquals(new Point(2,2), Coord.of('c', 3).getPoint());
        assertEquals(new Point(2,3), Coord.of('c', 4).getPoint());
        assertEquals(new Point(2,4), Coord.of('c', 5).getPoint());
        assertEquals(new Point(2,5), Coord.of('c', 6).getPoint());
        assertEquals(new Point(2,6), Coord.of('c', 7).getPoint());
        assertEquals(new Point(2,7), Coord.of('c', 8).getPoint());

        assertEquals(new Point(3,0), Coord.of('d', 1).getPoint());
        assertEquals(new Point(3,1), Coord.of('d', 2).getPoint());
        assertEquals(new Point(3,2), Coord.of('d', 3).getPoint());
        assertEquals(new Point(3,3), Coord.of('d', 4).getPoint());
        assertEquals(new Point(3,4), Coord.of('d', 5).getPoint());
        assertEquals(new Point(3,5), Coord.of('d', 6).getPoint());
        assertEquals(new Point(3,6), Coord.of('d', 7).getPoint());
        assertEquals(new Point(3,7), Coord.of('d', 8).getPoint());

        assertEquals(new Point(4,0), Coord.of('e', 1).getPoint());
        assertEquals(new Point(4,1), Coord.of('e', 2).getPoint());
        assertEquals(new Point(4,2), Coord.of('e', 3).getPoint());
        assertEquals(new Point(4,3), Coord.of('e', 4).getPoint());
        assertEquals(new Point(4,4), Coord.of('e', 5).getPoint());
        assertEquals(new Point(4,5), Coord.of('e', 6).getPoint());
        assertEquals(new Point(4,6), Coord.of('e', 7).getPoint());
        assertEquals(new Point(4,7), Coord.of('e', 8).getPoint());

        assertEquals(new Point(5,0), Coord.of('f', 1).getPoint());
        assertEquals(new Point(5,1), Coord.of('f', 2).getPoint());
        assertEquals(new Point(5,2), Coord.of('f', 3).getPoint());
        assertEquals(new Point(5,3), Coord.of('f', 4).getPoint());
        assertEquals(new Point(5,4), Coord.of('f', 5).getPoint());
        assertEquals(new Point(5,5), Coord.of('f', 6).getPoint());
        assertEquals(new Point(5,6), Coord.of('f', 7).getPoint());
        assertEquals(new Point(5,7), Coord.of('f', 8).getPoint());

        assertEquals(new Point(6,0), Coord.of('g', 1).getPoint());
        assertEquals(new Point(6,1), Coord.of('g', 2).getPoint());
        assertEquals(new Point(6,2), Coord.of('g', 3).getPoint());
        assertEquals(new Point(6,3), Coord.of('g', 4).getPoint());
        assertEquals(new Point(6,4), Coord.of('g', 5).getPoint());
        assertEquals(new Point(6,5), Coord.of('g', 6).getPoint());
        assertEquals(new Point(6,6), Coord.of('g', 7).getPoint());
        assertEquals(new Point(6,7), Coord.of('g', 8).getPoint());

        assertEquals(new Point(7,0), Coord.of('h', 1).getPoint());
        assertEquals(new Point(7,1), Coord.of('h', 2).getPoint());
        assertEquals(new Point(7,2), Coord.of('h', 3).getPoint());
        assertEquals(new Point(7,3), Coord.of('h', 4).getPoint());
        assertEquals(new Point(7,4), Coord.of('h', 5).getPoint());
        assertEquals(new Point(7,5), Coord.of('h', 6).getPoint());
        assertEquals(new Point(7,6), Coord.of('h', 7).getPoint());
        assertEquals(new Point(7,7), Coord.of('h', 8).getPoint());
    }

    @Test
    void testNotationColumn() {
        assertEquals('a', new Coord(0, 0).getNotationColumn());
        assertEquals('b', new Coord(1, 0).getNotationColumn());
        assertEquals('c', new Coord(2, 0).getNotationColumn());
        assertEquals('d', new Coord(3, 0).getNotationColumn());
        assertEquals('e', new Coord(4, 0).getNotationColumn());
        assertEquals('f', new Coord(5, 0).getNotationColumn());
        assertEquals('g', new Coord(6, 0).getNotationColumn());
        assertEquals('h', new Coord(7, 0).getNotationColumn());
    }

    @Test
    void testNotationRow() {
        assertEquals('1', new Coord(0, 0).getNotationRow());
        assertEquals('2', new Coord(0, 1).getNotationRow());
        assertEquals('3', new Coord(0, 2).getNotationRow());
        assertEquals('4', new Coord(0, 3).getNotationRow());
        assertEquals('5', new Coord(0, 4).getNotationRow());
        assertEquals('6', new Coord(0, 5).getNotationRow());
        assertEquals('7', new Coord(0, 6).getNotationRow());
        assertEquals('8', new Coord(0, 7).getNotationRow());
    }

    @Test
    void testNotation() {
        assertEquals("a1", new Coord(0, 0).getNotation());
        assertEquals("a2", new Coord(0, 1).getNotation());
        assertEquals("a3", new Coord(0, 2).getNotation());
        assertEquals("a4", new Coord(0, 3).getNotation());
        assertEquals("a5", new Coord(0, 4).getNotation());
        assertEquals("a6", new Coord(0, 5).getNotation());
        assertEquals("a7", new Coord(0, 6).getNotation());
        assertEquals("a8", new Coord(0, 7).getNotation());

        assertEquals("b1", new Coord(1, 0).getNotation());
        assertEquals("b2", new Coord(1, 1).getNotation());
        assertEquals("b3", new Coord(1, 2).getNotation());
        assertEquals("b4", new Coord(1, 3).getNotation());
        assertEquals("b5", new Coord(1, 4).getNotation());
        assertEquals("b6", new Coord(1, 5).getNotation());
        assertEquals("b7", new Coord(1, 6).getNotation());
        assertEquals("b8", new Coord(1, 7).getNotation());

        assertEquals("c1", new Coord(2, 0).getNotation());
        assertEquals("c2", new Coord(2, 1).getNotation());
        assertEquals("c3", new Coord(2, 2).getNotation());
        assertEquals("c4", new Coord(2, 3).getNotation());
        assertEquals("c5", new Coord(2, 4).getNotation());
        assertEquals("c6", new Coord(2, 5).getNotation());
        assertEquals("c7", new Coord(2, 6).getNotation());
        assertEquals("c8", new Coord(2, 7).getNotation());

        assertEquals("d1", new Coord(3, 0).getNotation());
        assertEquals("d2", new Coord(3, 1).getNotation());
        assertEquals("d3", new Coord(3, 2).getNotation());
        assertEquals("d4", new Coord(3, 3).getNotation());
        assertEquals("d5", new Coord(3, 4).getNotation());
        assertEquals("d6", new Coord(3, 5).getNotation());
        assertEquals("d7", new Coord(3, 6).getNotation());
        assertEquals("d8", new Coord(3, 7).getNotation());

        assertEquals("e1", new Coord(4, 0).getNotation());
        assertEquals("e2", new Coord(4, 1).getNotation());
        assertEquals("e3", new Coord(4, 2).getNotation());
        assertEquals("e4", new Coord(4, 3).getNotation());
        assertEquals("e5", new Coord(4, 4).getNotation());
        assertEquals("e6", new Coord(4, 5).getNotation());
        assertEquals("e7", new Coord(4, 6).getNotation());
        assertEquals("e8", new Coord(4, 7).getNotation());

        assertEquals("f1", new Coord(5, 0).getNotation());
        assertEquals("f2", new Coord(5, 1).getNotation());
        assertEquals("f3", new Coord(5, 2).getNotation());
        assertEquals("f4", new Coord(5, 3).getNotation());
        assertEquals("f5", new Coord(5, 4).getNotation());
        assertEquals("f6", new Coord(5, 5).getNotation());
        assertEquals("f7", new Coord(5, 6).getNotation());
        assertEquals("f8", new Coord(5, 7).getNotation());

        assertEquals("g1", new Coord(6, 0).getNotation());
        assertEquals("g2", new Coord(6, 1).getNotation());
        assertEquals("g3", new Coord(6, 2).getNotation());
        assertEquals("g4", new Coord(6, 3).getNotation());
        assertEquals("g5", new Coord(6, 4).getNotation());
        assertEquals("g6", new Coord(6, 5).getNotation());
        assertEquals("g7", new Coord(6, 6).getNotation());
        assertEquals("g8", new Coord(6, 7).getNotation());

        assertEquals("h1", new Coord(7, 0).getNotation());
        assertEquals("h2", new Coord(7, 1).getNotation());
        assertEquals("h3", new Coord(7, 2).getNotation());
        assertEquals("h4", new Coord(7, 3).getNotation());
        assertEquals("h5", new Coord(7, 4).getNotation());
        assertEquals("h6", new Coord(7, 5).getNotation());
        assertEquals("h7", new Coord(7, 6).getNotation());
        assertEquals("h8", new Coord(7, 7).getNotation());
    }

    @Test
    void testToString() {
        Coord coord;

        coord = new Coord(2, 3);
        assertEquals("c4", coord.toString());

        coord = new Coord(7,7);
        assertEquals("h8", coord.toString());

        coord = new Coord(0, 0);
        assertEquals("a1", coord.toString());

        coord = new Coord(4, 4);
        assertEquals("e5", coord.toString());

        coord = new Coord(1, 4);
        assertEquals("b5", coord.toString());
    }
}
