package rpjvanaert.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BBoardHelperTest {

    @Test
    void stringCoordToIndex() {
        assertEquals(0, BBoardHelper.stringCoordToIndex("a1"));
        assertEquals(1, BBoardHelper.stringCoordToIndex("b1"));
        assertEquals(2, BBoardHelper.stringCoordToIndex("c1"));
        assertEquals(3, BBoardHelper.stringCoordToIndex("d1"));
        assertEquals(4, BBoardHelper.stringCoordToIndex("e1"));
        assertEquals(5, BBoardHelper.stringCoordToIndex("f1"));
        assertEquals(6, BBoardHelper.stringCoordToIndex("g1"));
        assertEquals(7, BBoardHelper.stringCoordToIndex("h1"));

        assertEquals(8, BBoardHelper.stringCoordToIndex("a2"));
        assertEquals(9, BBoardHelper.stringCoordToIndex("b2"));
        assertEquals(10, BBoardHelper.stringCoordToIndex("c2"));
        assertEquals(11, BBoardHelper.stringCoordToIndex("d2"));
        assertEquals(12, BBoardHelper.stringCoordToIndex("e2"));
        assertEquals(13, BBoardHelper.stringCoordToIndex("f2"));
        assertEquals(14, BBoardHelper.stringCoordToIndex("g2"));
        assertEquals(15, BBoardHelper.stringCoordToIndex("h2"));

        assertEquals(16, BBoardHelper.stringCoordToIndex("a3"));
        assertEquals(17, BBoardHelper.stringCoordToIndex("b3"));
        assertEquals(18, BBoardHelper.stringCoordToIndex("c3"));
        assertEquals(19, BBoardHelper.stringCoordToIndex("d3"));
        assertEquals(20, BBoardHelper.stringCoordToIndex("e3"));
        assertEquals(21, BBoardHelper.stringCoordToIndex("f3"));
        assertEquals(22, BBoardHelper.stringCoordToIndex("g3"));
        assertEquals(23, BBoardHelper.stringCoordToIndex("h3"));

        assertEquals(24, BBoardHelper.stringCoordToIndex("a4"));
        assertEquals(25, BBoardHelper.stringCoordToIndex("b4"));
        assertEquals(26, BBoardHelper.stringCoordToIndex("c4"));
        assertEquals(27, BBoardHelper.stringCoordToIndex("d4"));
        assertEquals(28, BBoardHelper.stringCoordToIndex("e4"));
        assertEquals(29, BBoardHelper.stringCoordToIndex("f4"));
        assertEquals(30, BBoardHelper.stringCoordToIndex("g4"));
        assertEquals(31, BBoardHelper.stringCoordToIndex("h4"));

        assertEquals(32, BBoardHelper.stringCoordToIndex("a5"));
        assertEquals(33, BBoardHelper.stringCoordToIndex("b5"));
        assertEquals(34, BBoardHelper.stringCoordToIndex("c5"));
        assertEquals(35, BBoardHelper.stringCoordToIndex("d5"));
        assertEquals(36, BBoardHelper.stringCoordToIndex("e5"));
        assertEquals(37, BBoardHelper.stringCoordToIndex("f5"));
        assertEquals(38, BBoardHelper.stringCoordToIndex("g5"));
        assertEquals(39, BBoardHelper.stringCoordToIndex("h5"));

        assertEquals(40, BBoardHelper.stringCoordToIndex("a6"));
        assertEquals(41, BBoardHelper.stringCoordToIndex("b6"));
        assertEquals(42, BBoardHelper.stringCoordToIndex("c6"));
        assertEquals(43, BBoardHelper.stringCoordToIndex("d6"));
        assertEquals(44, BBoardHelper.stringCoordToIndex("e6"));
        assertEquals(45, BBoardHelper.stringCoordToIndex("f6"));
        assertEquals(46, BBoardHelper.stringCoordToIndex("g6"));
        assertEquals(47, BBoardHelper.stringCoordToIndex("h6"));

        assertEquals(48, BBoardHelper.stringCoordToIndex("a7"));
        assertEquals(49, BBoardHelper.stringCoordToIndex("b7"));
        assertEquals(50, BBoardHelper.stringCoordToIndex("c7"));
        assertEquals(51, BBoardHelper.stringCoordToIndex("d7"));
        assertEquals(52, BBoardHelper.stringCoordToIndex("e7"));
        assertEquals(53, BBoardHelper.stringCoordToIndex("f7"));
        assertEquals(54, BBoardHelper.stringCoordToIndex("g7"));
        assertEquals(55, BBoardHelper.stringCoordToIndex("h7"));

        assertEquals(56, BBoardHelper.stringCoordToIndex("a8"));
        assertEquals(57, BBoardHelper.stringCoordToIndex("b8"));
        assertEquals(58, BBoardHelper.stringCoordToIndex("c8"));
        assertEquals(59, BBoardHelper.stringCoordToIndex("d8"));
        assertEquals(60, BBoardHelper.stringCoordToIndex("e8"));
        assertEquals(61, BBoardHelper.stringCoordToIndex("f8"));
        assertEquals(62, BBoardHelper.stringCoordToIndex("g8"));
        assertEquals(63, BBoardHelper.stringCoordToIndex("h8"));

        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.stringCoordToIndex("a9"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.stringCoordToIndex("i1"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.stringCoordToIndex("a"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.stringCoordToIndex("9"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.stringCoordToIndex("  "));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.stringCoordToIndex(" 9"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.stringCoordToIndex("a "));
    }

    @Test
    void indexToStringCoord() {
        assertEquals("a1", BBoardHelper.indexToStringCoord(0));
        assertEquals("b1", BBoardHelper.indexToStringCoord(1));
        assertEquals("c1", BBoardHelper.indexToStringCoord(2));
        assertEquals("d1", BBoardHelper.indexToStringCoord(3));
        assertEquals("e1", BBoardHelper.indexToStringCoord(4));
        assertEquals("f1", BBoardHelper.indexToStringCoord(5));
        assertEquals("g1", BBoardHelper.indexToStringCoord(6));
        assertEquals("h1", BBoardHelper.indexToStringCoord(7));

        assertEquals("a2", BBoardHelper.indexToStringCoord(8));
        assertEquals("b2", BBoardHelper.indexToStringCoord(9));
        assertEquals("c2", BBoardHelper.indexToStringCoord(10));
        assertEquals("d2", BBoardHelper.indexToStringCoord(11));
        assertEquals("e2", BBoardHelper.indexToStringCoord(12));
        assertEquals("f2", BBoardHelper.indexToStringCoord(13));
        assertEquals("g2", BBoardHelper.indexToStringCoord(14));
        assertEquals("h2", BBoardHelper.indexToStringCoord(15));

        assertEquals("a3", BBoardHelper.indexToStringCoord(16));
        assertEquals("b3", BBoardHelper.indexToStringCoord(17));
        assertEquals("c3", BBoardHelper.indexToStringCoord(18));
        assertEquals("d3", BBoardHelper.indexToStringCoord(19));
        assertEquals("e3", BBoardHelper.indexToStringCoord(20));
        assertEquals("f3", BBoardHelper.indexToStringCoord(21));
        assertEquals("g3", BBoardHelper.indexToStringCoord(22));
        assertEquals("h3", BBoardHelper.indexToStringCoord(23));

        assertEquals("a4", BBoardHelper.indexToStringCoord(24));
        assertEquals("b4", BBoardHelper.indexToStringCoord(25));
        assertEquals("c4", BBoardHelper.indexToStringCoord(26));
        assertEquals("d4", BBoardHelper.indexToStringCoord(27));
        assertEquals("e4", BBoardHelper.indexToStringCoord(28));
        assertEquals("f4", BBoardHelper.indexToStringCoord(29));
        assertEquals("g4", BBoardHelper.indexToStringCoord(30));
        assertEquals("h4", BBoardHelper.indexToStringCoord(31));

        assertEquals("a5", BBoardHelper.indexToStringCoord(32));
        assertEquals("b5", BBoardHelper.indexToStringCoord(33));
        assertEquals("c5", BBoardHelper.indexToStringCoord(34));
        assertEquals("d5", BBoardHelper.indexToStringCoord(35));
        assertEquals("e5", BBoardHelper.indexToStringCoord(36));
        assertEquals("f5", BBoardHelper.indexToStringCoord(37));
        assertEquals("g5", BBoardHelper.indexToStringCoord(38));
        assertEquals("h5", BBoardHelper.indexToStringCoord(39));

        assertEquals("a6", BBoardHelper.indexToStringCoord(40));
        assertEquals("b6", BBoardHelper.indexToStringCoord(41));
        assertEquals("c6", BBoardHelper.indexToStringCoord(42));
        assertEquals("d6", BBoardHelper.indexToStringCoord(43));
        assertEquals("e6", BBoardHelper.indexToStringCoord(44));
        assertEquals("f6", BBoardHelper.indexToStringCoord(45));
        assertEquals("g6", BBoardHelper.indexToStringCoord(46));
        assertEquals("h6", BBoardHelper.indexToStringCoord(47));

        assertEquals("a7", BBoardHelper.indexToStringCoord(48));
        assertEquals("b7", BBoardHelper.indexToStringCoord(49));
        assertEquals("c7", BBoardHelper.indexToStringCoord(50));
        assertEquals("d7", BBoardHelper.indexToStringCoord(51));
        assertEquals("e7", BBoardHelper.indexToStringCoord(52));
        assertEquals("f7", BBoardHelper.indexToStringCoord(53));
        assertEquals("g7", BBoardHelper.indexToStringCoord(54));
        assertEquals("h7", BBoardHelper.indexToStringCoord(55));

        assertEquals("a8", BBoardHelper.indexToStringCoord(56));
        assertEquals("b8", BBoardHelper.indexToStringCoord(57));
        assertEquals("c8", BBoardHelper.indexToStringCoord(58));
        assertEquals("d8", BBoardHelper.indexToStringCoord(59));
        assertEquals("e8", BBoardHelper.indexToStringCoord(60));
        assertEquals("f8", BBoardHelper.indexToStringCoord(61));
        assertEquals("g8", BBoardHelper.indexToStringCoord(62));
        assertEquals("h8", BBoardHelper.indexToStringCoord(63));

        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.indexToStringCoord(-1));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.indexToStringCoord(64));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.indexToStringCoord(999));
    }

}