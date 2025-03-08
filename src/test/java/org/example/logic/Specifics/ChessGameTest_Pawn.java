package org.example.logic.Specifics;

import org.example.data.move.Move;
import org.example.data.move.MoveDraft;
import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.PieceType;
import org.example.logic.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessGameTest_Pawn {

    private ChessGame chessGame;

    @BeforeEach
    void setChessGame() {
        this.chessGame = new ChessGame();
    }

    @Test
    void testSingleMove() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 3)
        );

        this.chessGame.submitMove(moveDraft);
        assertEquals(1, this.chessGame.getMoves().size());
        Move result = this.chessGame.getMoves().get(0);

        assertEquals(moveDraft.pieceType(), result.pieceType());
        assertEquals(moveDraft.color(), result.color());
        assertEquals(moveDraft.from(), result.from());
        assertEquals(moveDraft.to(), result.to());
        assertEquals(moveDraft.promotion(), result.promotion());
    }

    @Test
    void testDoubleMove() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 4)
        );

        this.chessGame.submitMove(moveDraft);
        assertEquals(1, this.chessGame.getMoves().size());
        Move result = this.chessGame.getMoves().get(0);

        assertEquals(moveDraft.pieceType(), result.pieceType());
        assertEquals(moveDraft.color(), result.color());
        assertEquals(moveDraft.from(), result.from());
        assertEquals(moveDraft.to(), result.to());
        assertEquals(moveDraft.promotion(), result.promotion());
    }

    @Test
    void testInvalidDoubleMove() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 3),
                Coord.of('e', 5)
        );

        assertFalse(this.chessGame.submitMove(moveDraft));
        assertEquals(0, this.chessGame.getMoves().size());
    }

    @Test
    void testInvalidMove_ColorInactive() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('e', 7),
                Coord.of('e', 6)
        );

        assertFalse(this.chessGame.submitMove(moveDraft));
        assertEquals(0, this.chessGame.getMoves().size());
    }

    @Test
    void testInvalidMove_changeRow_noTake() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('f', 3)
        );

        assertFalse(this.chessGame.submitMove(moveDraft));
        assertEquals(0, this.chessGame.getMoves().size());
    }

    @Test
    void testInvalidMove_tooFar() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 5)
        );

        assertFalse(this.chessGame.submitMove(moveDraft));
        assertEquals(0, this.chessGame.getMoves().size());
    }

    @Test
    void testInvalidMove_intoOccupied() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 4)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('e', 7),
                Coord.of('e', 5)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 4),
                Coord.of('e', 5)
        );

        assertTrue(this.chessGame.submitMove(moveDraft1));
        assertTrue(this.chessGame.submitMove(moveDraft2));
        assertFalse(this.chessGame.submitMove(moveDraft3));
        assertEquals(2, this.chessGame.getMoves().size());

        assertNull(this.chessGame.getBoard().getPieceOn(Coord.of('e', 2)));
        assertNotEquals(null, this.chessGame.getBoard().getPieceOn(Coord.of('e', 4)));
        assertNull(this.chessGame.getBoard().getPieceOn(Coord.of('e', 7)));
        assertNotEquals(null, this.chessGame.getBoard().getPieceOn(Coord.of('e', 5)));
    }

    @Test
    void testMove_setOfThree() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 4)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('e', 7),
                Coord.of('e', 5)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('d', 2),
                Coord.of('d', 4)
        );

        assertTrue(this.chessGame.submitMove(moveDraft1));
        assertTrue(this.chessGame.submitMove(moveDraft2));
        assertTrue(this.chessGame.submitMove(moveDraft3));
        assertEquals(3, this.chessGame.getMoves().size());

        assertNull(this.chessGame.getBoard().getPieceOn(Coord.of('e', 2)));
        assertNotEquals(null, this.chessGame.getBoard().getPieceOn(Coord.of('e', 4)));
        assertNull(this.chessGame.getBoard().getPieceOn(Coord.of('e', 7)));
        assertNotEquals(null, this.chessGame.getBoard().getPieceOn(Coord.of('e', 5)));
        assertNull(this.chessGame.getBoard().getPieceOn(Coord.of('d', 2)));
        assertNotEquals(null, this.chessGame.getBoard().getPieceOn(Coord.of('d', 4)));
    }

    @Test
    void testInvalidMove_doubleIntoOccupied() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('a', 2),
                Coord.of('a', 3)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('e', 7),
                Coord.of('e', 5)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('a', 3),
                Coord.of('a', 4)
        );

        MoveDraft moveDraft4 = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('e', 5),
                Coord.of('e', 4)
        );

        MoveDraft moveDraft5 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 4)
        );

        assertTrue(this.chessGame.submitMove(moveDraft1));
        assertTrue(this.chessGame.submitMove(moveDraft2));
        assertTrue(this.chessGame.submitMove(moveDraft3));
        assertTrue(this.chessGame.submitMove(moveDraft4));
        assertFalse(this.chessGame.submitMove(moveDraft5));
    }

    @Test
    void testTakeMoveSpecial() {
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e4"))));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("f7"), Coord.of("f5"))));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e4"), Coord.of("f5"))));
    }

    @Test
    void testEnPassant() {
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("a2"), Coord.of("a4"))));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("a7"), Coord.of("a6"))));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("a4"), Coord.of("a5"))));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("b7"), Coord.of("b5"))));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("a5"), Coord.of("b6"))));
    }
}