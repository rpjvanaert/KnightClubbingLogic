package knight.clubbing.data.bitboard;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BBoardHelperTest {

    @Test
    void coordToIndex() {
        assertEquals(0, BBoardHelper.coordToIndex("a1"));
        assertEquals(1, BBoardHelper.coordToIndex("b1"));
        assertEquals(2, BBoardHelper.coordToIndex("c1"));
        assertEquals(3, BBoardHelper.coordToIndex("d1"));
        assertEquals(4, BBoardHelper.coordToIndex("e1"));
        assertEquals(5, BBoardHelper.coordToIndex("f1"));
        assertEquals(6, BBoardHelper.coordToIndex("g1"));
        assertEquals(7, BBoardHelper.coordToIndex("h1"));

        assertEquals(8, BBoardHelper.coordToIndex("a2"));
        assertEquals(9, BBoardHelper.coordToIndex("b2"));
        assertEquals(10, BBoardHelper.coordToIndex("c2"));
        assertEquals(11, BBoardHelper.coordToIndex("d2"));
        assertEquals(12, BBoardHelper.coordToIndex("e2"));
        assertEquals(13, BBoardHelper.coordToIndex("f2"));
        assertEquals(14, BBoardHelper.coordToIndex("g2"));
        assertEquals(15, BBoardHelper.coordToIndex("h2"));

        assertEquals(16, BBoardHelper.coordToIndex("a3"));
        assertEquals(17, BBoardHelper.coordToIndex("b3"));
        assertEquals(18, BBoardHelper.coordToIndex("c3"));
        assertEquals(19, BBoardHelper.coordToIndex("d3"));
        assertEquals(20, BBoardHelper.coordToIndex("e3"));
        assertEquals(21, BBoardHelper.coordToIndex("f3"));
        assertEquals(22, BBoardHelper.coordToIndex("g3"));
        assertEquals(23, BBoardHelper.coordToIndex("h3"));

        assertEquals(24, BBoardHelper.coordToIndex("a4"));
        assertEquals(25, BBoardHelper.coordToIndex("b4"));
        assertEquals(26, BBoardHelper.coordToIndex("c4"));
        assertEquals(27, BBoardHelper.coordToIndex("d4"));
        assertEquals(28, BBoardHelper.coordToIndex("e4"));
        assertEquals(29, BBoardHelper.coordToIndex("f4"));
        assertEquals(30, BBoardHelper.coordToIndex("g4"));
        assertEquals(31, BBoardHelper.coordToIndex("h4"));

        assertEquals(32, BBoardHelper.coordToIndex("a5"));
        assertEquals(33, BBoardHelper.coordToIndex("b5"));
        assertEquals(34, BBoardHelper.coordToIndex("c5"));
        assertEquals(35, BBoardHelper.coordToIndex("d5"));
        assertEquals(36, BBoardHelper.coordToIndex("e5"));
        assertEquals(37, BBoardHelper.coordToIndex("f5"));
        assertEquals(38, BBoardHelper.coordToIndex("g5"));
        assertEquals(39, BBoardHelper.coordToIndex("h5"));

        assertEquals(40, BBoardHelper.coordToIndex("a6"));
        assertEquals(41, BBoardHelper.coordToIndex("b6"));
        assertEquals(42, BBoardHelper.coordToIndex("c6"));
        assertEquals(43, BBoardHelper.coordToIndex("d6"));
        assertEquals(44, BBoardHelper.coordToIndex("e6"));
        assertEquals(45, BBoardHelper.coordToIndex("f6"));
        assertEquals(46, BBoardHelper.coordToIndex("g6"));
        assertEquals(47, BBoardHelper.coordToIndex("h6"));

        assertEquals(48, BBoardHelper.coordToIndex("a7"));
        assertEquals(49, BBoardHelper.coordToIndex("b7"));
        assertEquals(50, BBoardHelper.coordToIndex("c7"));
        assertEquals(51, BBoardHelper.coordToIndex("d7"));
        assertEquals(52, BBoardHelper.coordToIndex("e7"));
        assertEquals(53, BBoardHelper.coordToIndex("f7"));
        assertEquals(54, BBoardHelper.coordToIndex("g7"));
        assertEquals(55, BBoardHelper.coordToIndex("h7"));

        assertEquals(56, BBoardHelper.coordToIndex("a8"));
        assertEquals(57, BBoardHelper.coordToIndex("b8"));
        assertEquals(58, BBoardHelper.coordToIndex("c8"));
        assertEquals(59, BBoardHelper.coordToIndex("d8"));
        assertEquals(60, BBoardHelper.coordToIndex("e8"));
        assertEquals(61, BBoardHelper.coordToIndex("f8"));
        assertEquals(62, BBoardHelper.coordToIndex("g8"));
        assertEquals(63, BBoardHelper.coordToIndex("h8"));

        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.coordToIndex("a9"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.coordToIndex("i1"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.coordToIndex("a"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.coordToIndex("9"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.coordToIndex("  "));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.coordToIndex(" 9"));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.coordToIndex("a "));
    }

    @Test
    void indexToCoord() {
        assertEquals("a1", BBoardHelper.indexToCoord(0));
        assertEquals("b1", BBoardHelper.indexToCoord(1));
        assertEquals("c1", BBoardHelper.indexToCoord(2));
        assertEquals("d1", BBoardHelper.indexToCoord(3));
        assertEquals("e1", BBoardHelper.indexToCoord(4));
        assertEquals("f1", BBoardHelper.indexToCoord(5));
        assertEquals("g1", BBoardHelper.indexToCoord(6));
        assertEquals("h1", BBoardHelper.indexToCoord(7));

        assertEquals("a2", BBoardHelper.indexToCoord(8));
        assertEquals("b2", BBoardHelper.indexToCoord(9));
        assertEquals("c2", BBoardHelper.indexToCoord(10));
        assertEquals("d2", BBoardHelper.indexToCoord(11));
        assertEquals("e2", BBoardHelper.indexToCoord(12));
        assertEquals("f2", BBoardHelper.indexToCoord(13));
        assertEquals("g2", BBoardHelper.indexToCoord(14));
        assertEquals("h2", BBoardHelper.indexToCoord(15));

        assertEquals("a3", BBoardHelper.indexToCoord(16));
        assertEquals("b3", BBoardHelper.indexToCoord(17));
        assertEquals("c3", BBoardHelper.indexToCoord(18));
        assertEquals("d3", BBoardHelper.indexToCoord(19));
        assertEquals("e3", BBoardHelper.indexToCoord(20));
        assertEquals("f3", BBoardHelper.indexToCoord(21));
        assertEquals("g3", BBoardHelper.indexToCoord(22));
        assertEquals("h3", BBoardHelper.indexToCoord(23));

        assertEquals("a4", BBoardHelper.indexToCoord(24));
        assertEquals("b4", BBoardHelper.indexToCoord(25));
        assertEquals("c4", BBoardHelper.indexToCoord(26));
        assertEquals("d4", BBoardHelper.indexToCoord(27));
        assertEquals("e4", BBoardHelper.indexToCoord(28));
        assertEquals("f4", BBoardHelper.indexToCoord(29));
        assertEquals("g4", BBoardHelper.indexToCoord(30));
        assertEquals("h4", BBoardHelper.indexToCoord(31));

        assertEquals("a5", BBoardHelper.indexToCoord(32));
        assertEquals("b5", BBoardHelper.indexToCoord(33));
        assertEquals("c5", BBoardHelper.indexToCoord(34));
        assertEquals("d5", BBoardHelper.indexToCoord(35));
        assertEquals("e5", BBoardHelper.indexToCoord(36));
        assertEquals("f5", BBoardHelper.indexToCoord(37));
        assertEquals("g5", BBoardHelper.indexToCoord(38));
        assertEquals("h5", BBoardHelper.indexToCoord(39));

        assertEquals("a6", BBoardHelper.indexToCoord(40));
        assertEquals("b6", BBoardHelper.indexToCoord(41));
        assertEquals("c6", BBoardHelper.indexToCoord(42));
        assertEquals("d6", BBoardHelper.indexToCoord(43));
        assertEquals("e6", BBoardHelper.indexToCoord(44));
        assertEquals("f6", BBoardHelper.indexToCoord(45));
        assertEquals("g6", BBoardHelper.indexToCoord(46));
        assertEquals("h6", BBoardHelper.indexToCoord(47));

        assertEquals("a7", BBoardHelper.indexToCoord(48));
        assertEquals("b7", BBoardHelper.indexToCoord(49));
        assertEquals("c7", BBoardHelper.indexToCoord(50));
        assertEquals("d7", BBoardHelper.indexToCoord(51));
        assertEquals("e7", BBoardHelper.indexToCoord(52));
        assertEquals("f7", BBoardHelper.indexToCoord(53));
        assertEquals("g7", BBoardHelper.indexToCoord(54));
        assertEquals("h7", BBoardHelper.indexToCoord(55));

        assertEquals("a8", BBoardHelper.indexToCoord(56));
        assertEquals("b8", BBoardHelper.indexToCoord(57));
        assertEquals("c8", BBoardHelper.indexToCoord(58));
        assertEquals("d8", BBoardHelper.indexToCoord(59));
        assertEquals("e8", BBoardHelper.indexToCoord(60));
        assertEquals("f8", BBoardHelper.indexToCoord(61));
        assertEquals("g8", BBoardHelper.indexToCoord(62));
        assertEquals("h8", BBoardHelper.indexToCoord(63));

        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.indexToCoord(-1));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.indexToCoord(64));
        assertThrows(IllegalArgumentException.class, () -> BBoardHelper.indexToCoord(999));
    }

}