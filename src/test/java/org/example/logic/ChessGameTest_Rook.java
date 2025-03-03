package org.example.logic;

import org.example.data.Board;
import org.example.data.move.MoveDraft;
import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessGameTest_Rook {

    private ChessGame chessGame;

    @BeforeEach
    void setChessGame() {
        this.chessGame = new ChessGame();
    }

    @Test
    void testMove() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('a', 2),
                Coord.of('a', 4)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('a', 7),
                Coord.of('a', 5)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.ROOK,
                Color.WHITE,
                Coord.of('a', 1),
                Coord.of('a', 3)
        );

        MoveDraft moveDraft4 = new MoveDraft(
                PieceType.ROOK,
                Color.BLACK,
                Coord.of('a', 8),
                Coord.of('a', 6)
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
    void testMove_multiple() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('a', 2),
                Coord.of('a', 4)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('h', 7),
                Coord.of('h', 5)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.ROOK,
                Color.WHITE,
                Coord.of('a', 1),
                Coord.of('a', 3)
        );

        MoveDraft moveDraft4 = new MoveDraft(
                PieceType.ROOK,
                Color.BLACK,
                Coord.of('h', 8),
                Coord.of('h', 6)
        );

        MoveDraft moveDraft5 = new MoveDraft(
                PieceType.ROOK,
                Color.WHITE,
                Coord.of('a', 3),
                Coord.of('f', 3)
        );

        MoveDraft moveDraft6 = new MoveDraft(
                PieceType.ROOK,
                Color.BLACK,
                Coord.of('h', 6),
                Coord.of('c', 6)
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

        assertTrue(this.chessGame.submitMove(moveDraft5));
        assertTrue(this.chessGame.submitMove(moveDraft6));

        assertNull(board.getPieceOn(moveDraft5.from()));
        assertEquals(moveDraft5.getPiece(), board.getPieceOn(moveDraft5.to()));
        assertNull(board.getPieceOn(moveDraft6.from()));
        assertEquals(moveDraft6.getPiece(), board.getPieceOn(moveDraft6.to()));
    }

    @Test
    void testMove_back() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('a', 2),
                Coord.of('a', 4)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('h', 7),
                Coord.of('h', 5)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.ROOK,
                Color.WHITE,
                Coord.of('a', 1),
                Coord.of('a', 3)
        );

        MoveDraft moveDraft4 = new MoveDraft(
                PieceType.ROOK,
                Color.BLACK,
                Coord.of('h', 8),
                Coord.of('h', 6)
        );

        MoveDraft moveDraft5 = new MoveDraft(
                PieceType.ROOK,
                Color.WHITE,
                Coord.of('a', 3),
                Coord.of('a', 1)
        );

        MoveDraft moveDraft6 = new MoveDraft(
                PieceType.ROOK,
                Color.BLACK,
                Coord.of('h', 6),
                Coord.of('h', 8)
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

        assertTrue(this.chessGame.submitMove(moveDraft5));
        assertTrue(this.chessGame.submitMove(moveDraft6));

        assertNull(board.getPieceOn(moveDraft5.from()));
        assertEquals(moveDraft5.getPiece(), board.getPieceOn(moveDraft5.to()));
        assertNull(board.getPieceOn(moveDraft6.from()));
        assertEquals(moveDraft6.getPiece(), board.getPieceOn(moveDraft6.to()));
    }

    @Test
    void testInvalidMove_diagonal() {
        MoveDraft moveDraft1 = new MoveDraft(
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('a', 2),
                Coord.of('a', 4)
        );

        MoveDraft moveDraft2 = new MoveDraft(
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('h', 7),
                Coord.of('h', 5)
        );

        MoveDraft moveDraft3 = new MoveDraft(
                PieceType.ROOK,
                Color.WHITE,
                Coord.of('a', 1),
                Coord.of('a', 3)
        );

        MoveDraft moveDraft4 = new MoveDraft(
                PieceType.ROOK,
                Color.BLACK,
                Coord.of('h', 8),
                Coord.of('h', 6)
        );

        MoveDraft moveDraft5 = new MoveDraft(
                PieceType.ROOK,
                Color.WHITE,
                Coord.of('a', 3),
                Coord.of('c', 1)
        );

        MoveDraft moveDraft6 = new MoveDraft(
                PieceType.ROOK,
                Color.BLACK,
                Coord.of('h', 6),
                Coord.of('f', 8)
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

        assertFalse(this.chessGame.submitMove(moveDraft5));
        assertFalse(this.chessGame.submitMove(moveDraft6));

        assertNotEquals(moveDraft5.getPiece(), board.getPieceOn(moveDraft5.to()));
        assertEquals(moveDraft5.getPiece(), board.getPieceOn(moveDraft5.from()));
        assertNotEquals(moveDraft5.getPiece(), board.getPieceOn(moveDraft6.to()));
        assertEquals(moveDraft6.getPiece(), board.getPieceOn(moveDraft6.from()));
    }
}
