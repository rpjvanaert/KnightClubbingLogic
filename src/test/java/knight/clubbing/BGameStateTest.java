package knight.clubbing;

import knight.clubbing.core.BGameState;
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

}