package knight.clubbing.data.bitboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BBoardTest {

    private BBoard b = new BBoard();

    @BeforeEach
    void setUp() {
        b = new BBoard();
    }

    @Test
    void setAndGet() {

        b.set(0, 0);
        assertTrue(b.get(0, 0));
        assertFalse(b.get(0, 1));
        assertFalse(b.get(0, 63));
        assertFalse(b.get(0, 32));
    }

    @Test
    void clear() {
        b.set(11,63);

        assertTrue(b.get(11,63));
        b.clear(11,63);
        assertFalse(b.get(11,63));
    }

    @Test
    void move() {
        b.set(5,32);

        assertTrue(b.get(5,32));
        b.move(5, 32, 33);
        assertFalse(b.get(5,32));
        assertTrue(b.get(5,33));
    }
}