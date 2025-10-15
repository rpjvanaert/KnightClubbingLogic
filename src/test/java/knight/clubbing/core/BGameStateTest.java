package knight.clubbing.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BGameStateTest {

    @Test
    void testCastlingRights_AllCombinations() {
        for (int i = 0; i < 16; i++) {
            BGameState state = new BGameState(0, 0, i, 0, 0L);

            boolean whiteKingside = (i & 1) != 0;
            boolean whiteQueenside = (i & 2) != 0;
            boolean blackKingside = (i & 4) != 0;
            boolean blackQueenside = (i & 8) != 0;

            assertEquals(whiteKingside, state.hasKingSideCastleRight(true), "White kingside mismatch for " + i);
            assertEquals(whiteQueenside, state.hasQueenSideCastleRight(true), "White queenside mismatch for " + i);
            assertEquals(blackKingside, state.hasKingSideCastleRight(false), "Black kingside mismatch for " + i);
            assertEquals(blackQueenside, state.hasQueenSideCastleRight(false), "Black queenside mismatch for " + i);
        }
    }

    @Test
    void testConstructorAndGetters() {
        int capturedPiece = 3;
        int enPassantFile = 4;
        int castlingRights = 5;
        int fiftyMoveCounter = 12;
        long zobristKey = 0xCAFEBABECAFEL;

        BGameState state = new BGameState(capturedPiece, enPassantFile, castlingRights, fiftyMoveCounter, zobristKey);

        assertEquals(capturedPiece, state.getCapturedPiece());
        assertEquals(enPassantFile, state.getEnPassantFile());
        assertEquals(castlingRights, state.getCastlingRights());
        assertEquals(fiftyMoveCounter, state.getFiftyMoveCounter());
        assertEquals(zobristKey, state.getZobristKey());
    }

    @Test
    void testDefaultConstructor() {
        BGameState state = new BGameState();

        assertEquals(0, state.getCapturedPiece());
        assertEquals(0, state.getEnPassantFile());
        assertEquals(0, state.getCastlingRights());
        assertEquals(0, state.getFiftyMoveCounter());
    }

    @Test
    void testCastlingMasks() {
        assertEquals(0b1110, BGameState.clearWhiteKingsideMask);
        assertEquals(0b1101, BGameState.clearWhiteQueensideMask);
        assertEquals(0b1011, BGameState.clearBlackKingsideMask);
        assertEquals(0b0111, BGameState.clearBlackQueensideMask);
    }

    @Test
    void testEquals_Reflexive() {
        BGameState state = new BGameState(1, 2, 3, 4, 5L);
        assertEquals(state, state);
    }

    @Test
    void testEquals_Symmetric() {
        BGameState state1 = new BGameState(1, 2, 3, 4, 5L);
        BGameState state2 = new BGameState(1, 2, 3, 4, 5L);

        assertEquals(state1, state2);
        assertEquals(state2, state1);
    }

    @Test
    void testEquals_WithNull() {
        BGameState state = new BGameState(1, 2, 3, 4, 5L);
        assertNotEquals(state, null);
    }

    @Test
    void testEquals_WithDifferentClass() {
        BGameState state = new BGameState(1, 2, 3, 4, 5L);
        String other = "not a BGameState";
        assertNotEquals(state, other);
    }

    @Test
    void testEquals_IdenticalStates() {
        BGameState state1 = new BGameState(10, 5, 7, 25, 0xDEADBEEFL);
        BGameState state2 = new BGameState(10, 5, 7, 25, 0xDEADBEEFL);

        assertEquals(state1, state2);
    }

    @Test
    void testEquals_DifferentCapturedPiece() {
        BGameState state1 = new BGameState(1, 2, 3, 4, 5L);
        BGameState state2 = new BGameState(99, 2, 3, 4, 5L);

        assertNotEquals(state1, state2);
    }

    @Test
    void testEquals_DifferentEnPassantFile() {
        BGameState state1 = new BGameState(1, 2, 3, 4, 5L);
        BGameState state2 = new BGameState(1, 99, 3, 4, 5L);

        assertNotEquals(state1, state2);
    }

    @Test
    void testEquals_DifferentCastlingRights() {
        BGameState state1 = new BGameState(1, 2, 3, 4, 5L);
        BGameState state2 = new BGameState(1, 2, 99, 4, 5L);

        assertNotEquals(state1, state2);
    }

    @Test
    void testEquals_DifferentFiftyMoveCounter() {
        BGameState state1 = new BGameState(1, 2, 3, 4, 5L);
        BGameState state2 = new BGameState(1, 2, 3, 99, 5L);

        assertNotEquals(state1, state2);
    }

    @Test
    void testEquals_DifferentZobristKey() {
        BGameState state1 = new BGameState(1, 2, 3, 4, 5L);
        BGameState state2 = new BGameState(1, 2, 3, 4, 999L);

        assertNotEquals(state1, state2);
    }

    @Test
    void testEquals_CopyConstructor() {
        BGameState original = new BGameState(7, 8, 9, 10, 0xABCDEF123L);
        BGameState copy = new BGameState(original);

        assertEquals(original, copy);
    }

    @Test
    void testEquals_DefaultConstructors() {
        BGameState state1 = new BGameState();
        BGameState state2 = new BGameState();

        assertEquals(state1, state2);
    }

}