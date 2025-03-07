package org.example.logic.Specifics;

import org.example.data.Board;
import org.example.data.move.Move;
import org.example.data.move.MoveDraft;
import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.PieceType;
import org.example.logic.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessGameTest_Knight {

    private ChessGame chessGame;

    @BeforeEach
    void setChessGame() {
        this.chessGame = new ChessGame();
    }

    @Test
    void testMove() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('b', 1),
                Coord.of('c', 3)
        );

        this.chessGame.submitMove(moveDraft);
        assertEquals(1, this.chessGame.getMoves().size());
        Move result = this.chessGame.getMoves().get(0);

        assertEquals(moveDraft.pieceType(), result.pieceType());
        assertEquals(moveDraft.color(), result.color());
        assertEquals(moveDraft.from(), result.from());
        assertEquals(moveDraft.to(), result.to());
    }

    @Test
    void testInvalidMove_ColorInactive() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.KNIGHT,
                Color.BLACK,
                Coord.of('b', 1),
                Coord.of('c', 3)
        );

        assertFalse(this.chessGame.submitMove(moveDraft));
        assertEquals(0, this.chessGame.getMoves().size());
    }

    @Test
    void testInvalidMove_toWrong1() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('b', 1),
                Coord.of('b', 3)
        );

        assertFalse(this.chessGame.submitMove(moveDraft));
        assertEquals(0, this.chessGame.getMoves().size());
    }

    @Test
    void testInvalidMove_toWrong2() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('b', 1),
                Coord.of('c', 4)
        );

        assertFalse(this.chessGame.submitMove(moveDraft));
        assertEquals(0, this.chessGame.getMoves().size());
    }

    @Test
    void testInvalidMove_toOccupied() {
        MoveDraft moveDraft = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('b', 1),
                Coord.of('d', 2)
        );

        assertFalse(this.chessGame.submitMove(moveDraft));
        assertEquals(0, this.chessGame.getMoves().size());
    }

    @Test
    void testMove_knightsOut() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('b', 1),
                Coord.of('c', 3)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.KNIGHT,
                Color.BLACK,
                Coord.of('b', 8),
                Coord.of('c', 6)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('g', 1),
                Coord.of('f', 3)
        );

        MoveDraft moveDraft4 = new MoveDraft(
                PieceType.KNIGHT,
                Color.BLACK,
                Coord.of('g', 8),
                Coord.of('f', 6)
        );

        assertTrue(this.chessGame.submitMove(moveDraft1));
        assertTrue(this.chessGame.submitMove(moveDraft2));
        assertTrue(this.chessGame.submitMove(moveDraft3));
        assertTrue(this.chessGame.submitMove(moveDraft4));

        Board board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft1.from()));
        assertEquals(moveDraft1.getPiece(), board.getPieceOn(moveDraft1.to()));
        assertNull(board.getPieceOn(moveDraft2.from()));
        assertEquals(moveDraft2.getPiece(), board.getPieceOn(moveDraft2.to()));
        assertNull(board.getPieceOn(moveDraft3.from()));
        assertEquals(moveDraft3.getPiece(), board.getPieceOn(moveDraft3.to()));
        assertNull(board.getPieceOn(moveDraft4.from()));
        assertEquals(moveDraft4.getPiece(), board.getPieceOn(moveDraft4.to()));
    }

    @Test
    void testMove_knightsInNOut() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('b', 1),
                Coord.of('c', 3)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.KNIGHT,
                Color.BLACK,
                Coord.of('b', 8),
                Coord.of('c', 6)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('c', 3),
                Coord.of('b', 1)
        );

        MoveDraft moveDraft4 = new MoveDraft(
                PieceType.KNIGHT,
                Color.BLACK,
                Coord.of('c', 6),
                Coord.of('b', 8)
        );

        assertTrue(this.chessGame.submitMove(moveDraft1));
        assertTrue(this.chessGame.submitMove(moveDraft2));

        Board board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft1.from()));
        assertEquals(moveDraft1.getPiece(), board.getPieceOn(moveDraft1.to()));
        assertNull(board.getPieceOn(moveDraft2.from()));
        assertEquals(moveDraft2.getPiece(), board.getPieceOn(moveDraft2.to()));

        assertTrue(this.chessGame.submitMove(moveDraft3));
        assertTrue(this.chessGame.submitMove(moveDraft4));

        board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft3.from()));
        assertEquals(moveDraft3.getPiece(), board.getPieceOn(moveDraft3.to()));
        assertNull(board.getPieceOn(moveDraft4.from()));
        assertEquals(moveDraft4.getPiece(), board.getPieceOn(moveDraft4.to()));
    }

    @Test
    void testMove_knightsAround() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('b', 1),
                Coord.of('c', 3)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.KNIGHT,
                Color.BLACK,
                Coord.of('g', 8),
                Coord.of('f', 6)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('c', 3),
                Coord.of('d', 5)
        );

        MoveDraft moveDraft4 = new MoveDraft(
                PieceType.KNIGHT,
                Color.BLACK,
                Coord.of('f', 6),
                Coord.of('e', 4)
        );

        MoveDraft moveDraft5 = new MoveDraft(
                PieceType.KNIGHT,
                Color.WHITE,
                Coord.of('d', 5),
                Coord.of('e', 3)
        );

        MoveDraft moveDraft6 = new MoveDraft(
                PieceType.KNIGHT,
                Color.BLACK,
                Coord.of('e', 4),
                Coord.of('d', 6)
        );

        assertTrue(this.chessGame.submitMove(moveDraft1));
        assertTrue(this.chessGame.submitMove(moveDraft2));

        Board board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft1.from()));
        assertEquals(moveDraft1.getPiece(), board.getPieceOn(moveDraft1.to()));
        assertNull(board.getPieceOn(moveDraft2.from()));
        assertEquals(moveDraft2.getPiece(), board.getPieceOn(moveDraft2.to()));

        assertTrue(this.chessGame.submitMove(moveDraft3));
        assertTrue(this.chessGame.submitMove(moveDraft4));

        board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft3.from()));
        assertEquals(moveDraft3.getPiece(), board.getPieceOn(moveDraft3.to()));
        assertNull(board.getPieceOn(moveDraft4.from()));
        assertEquals(moveDraft4.getPiece(), board.getPieceOn(moveDraft4.to()));

        assertTrue(this.chessGame.submitMove(moveDraft5));
        assertTrue(this.chessGame.submitMove(moveDraft6));

        board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft5.from()));
        assertEquals(moveDraft5.getPiece(), board.getPieceOn(moveDraft5.to()));
        assertNull(board.getPieceOn(moveDraft6.from()));
        assertEquals(moveDraft6.getPiece(), board.getPieceOn(moveDraft6.to()));
    }
}
