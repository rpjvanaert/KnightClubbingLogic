package knight.clubbing.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BMoveTest {

    @Test
    void testConstructorAndFields() {
        BMove move = new BMove(12, 28, BMove.enPassantCaptureFlag);
        assertEquals(12, move.startSquare());
        assertEquals(28, move.targetSquare());
        assertEquals(BMove.enPassantCaptureFlag, move.moveFlag());
    }

    @Test
    void testNoFlagMove() {
        BMove move = new BMove(0, 63);
        assertEquals(0, move.moveFlag());
        assertEquals(0, move.startSquare());
        assertEquals(63, move.targetSquare());
        assertFalse(move.isPromotion());
    }

    @Test
    void testPromotionFlags() {
        BMove queenPromo = new BMove(10, 18, BMove.promoteToQueenFlag);
        assertTrue(queenPromo.isPromotion());
        assertEquals(BPiece.queen, queenPromo.promotionPieceType());

        BMove knightPromo = new BMove(20, 28, BMove.promoteToKnightFlag);
        assertEquals(BPiece.knight, knightPromo.promotionPieceType());

        BMove bishopPromo = new BMove(30, 38, BMove.promoteToBishopFlag);
        assertEquals(BPiece.bishop, bishopPromo.promotionPieceType());

        BMove rookPromo = new BMove(40, 48, BMove.promoteToRookFlag);
        assertEquals(BPiece.rook, rookPromo.promotionPieceType());
    }

    @Test
    void testNonPromotionReturnsNone() {
        BMove move = new BMove(5, 15);
        assertEquals(BPiece.none, move.promotionPieceType());
    }

    @Test
    void testEqualsAndHashCode() {
        BMove m1 = new BMove(12, 28, BMove.castleFlag);
        BMove m2 = new BMove(12, 28, BMove.castleFlag);
        BMove m3 = new BMove(12, 29, BMove.castleFlag);

        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
        assertNotEquals(m1, m3);
    }

    @Test
    void testToStringFormat() {
        BMove move = new BMove(1, 2, BMove.pawnTwoUpFlag);
        String str = move.toString();

        assertTrue(str.contains("start=b1"));
        assertTrue(str.contains("target=c1"));
        assertTrue(str.contains("flag=" + move.moveFlagName()));
    }

    @Test
    void testNullMove() {
        BMove nullMove = BMove.nullMove();
        assertTrue(nullMove.isNull());
        assertEquals(0, nullMove.startSquare());
        assertEquals(0, nullMove.targetSquare());
        assertEquals(0, nullMove.moveFlag());
    }
}