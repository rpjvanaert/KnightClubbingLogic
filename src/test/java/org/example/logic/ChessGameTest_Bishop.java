package org.example.logic;

import org.example.data.Board;
import org.example.data.move.MoveDraft;
import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.MoveType;
import org.example.data.details.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChessGameTest_Bishop {

    private ChessGame chessGame;

    @BeforeEach
    void setChessGame() {
        this.chessGame = new ChessGame();
    }

    @Test
    void testMove() {
        MoveDraft moveDraft1 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 4),
                ""
        );

        MoveDraft moveDraft2 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('e', 7),
                Coord.of('e', 5),
                ""
        );

        MoveDraft moveDraft3 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.BISHOP,
                Color.WHITE,
                Coord.of('f', 1),
                Coord.of('c', 4),
                ""
        );

        MoveDraft moveDraft4 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.BISHOP,
                Color.BLACK,
                Coord.of('f', 8),
                Coord.of('c', 5),
                ""
        );

        System.out.println(this.chessGame.getBoard().getDisplay());
        assertTrue(this.chessGame.submitMove(moveDraft1));
        System.out.println(this.chessGame.getBoard().getDisplay());
        assertTrue(this.chessGame.submitMove(moveDraft2));
        System.out.println(this.chessGame.getBoard().getDisplay());
        assertTrue(this.chessGame.submitMove(moveDraft3));
        System.out.println(this.chessGame.getBoard().getDisplay());
        assertTrue(this.chessGame.submitMove(moveDraft4));
        System.out.println(this.chessGame.getBoard().getDisplay());

        Board board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft1.from()));
        assertEquals(moveDraft1.getPiece(), board.getPieceOn(moveDraft1.to()));
        assertNull(board.getPieceOn(moveDraft2.from()));
        assertEquals(moveDraft2.getPiece(), board.getPieceOn(moveDraft2.to()));
        assertNull(board.getPieceOn(moveDraft3.from()));
        assertEquals(moveDraft3.getPiece(), board.getPieceOn(moveDraft3.to()));
        assertNull(board.getPieceOn(moveDraft4.from()));
        assertEquals(moveDraft4.getPiece(), board.getPieceOn(moveDraft4.to()));
        assertEquals("r  n  b  q  k  -  n  r  \n" +
                "p  p  p  p  -  p  p  p  \n" +
                "-  -  -  -  -  -  -  -  \n" +
                "-  -  b  -  p  -  -  -  \n" +
                "-  -  B  -  P  -  -  -  \n" +
                "-  -  -  -  -  -  -  -  \n" +
                "P  P  P  P  -  P  P  P  \n" +
                "R  N  B  Q  K  -  N  R  \n",board.getDisplay());
        System.out.println(board.getDisplay());
    }

    @Test
    void testInvalidMove_vertical() {
        MoveDraft moveDraft1 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 4),
                ""
        );

        MoveDraft moveDraft2 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('e', 7),
                Coord.of('e', 5),
                ""
        );

        MoveDraft moveDraft3 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.BISHOP,
                Color.WHITE,
                Coord.of('f', 1),
                Coord.of('f', 4),
                ""
        );

        assertTrue(this.chessGame.submitMove(moveDraft1));
        assertTrue(this.chessGame.submitMove(moveDraft2));
        assertFalse(this.chessGame.submitMove(moveDraft3));

        Board board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft1.from()));
        assertEquals(moveDraft1.getPiece(), board.getPieceOn(moveDraft1.to()));
        assertNull(board.getPieceOn(moveDraft2.from()));
        assertEquals(moveDraft2.getPiece(), board.getPieceOn(moveDraft2.to()));
        assertEquals(moveDraft3.getPiece(), board.getPieceOn(moveDraft3.from()));
    }

    @Test
    void testInvalidMove_weird() {
        MoveDraft moveDraft1 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.PAWN,
                Color.WHITE,
                Coord.of('e', 2),
                Coord.of('e', 4),
                ""
        );

        MoveDraft moveDraft2 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.PAWN,
                Color.BLACK,
                Coord.of('e', 7),
                Coord.of('e', 5),
                ""
        );

        MoveDraft moveDraft3 = new MoveDraft(
                MoveType.NORMAL,
                PieceType.BISHOP,
                Color.WHITE,
                Coord.of('f', 1),
                Coord.of('c', 5),
                ""
        );

        assertTrue(this.chessGame.submitMove(moveDraft1));
        assertTrue(this.chessGame.submitMove(moveDraft2));
        assertFalse(this.chessGame.submitMove(moveDraft3));

        Board board = this.chessGame.getBoard();
        assertNull(board.getPieceOn(moveDraft1.from()));
        assertEquals(moveDraft1.getPiece(), board.getPieceOn(moveDraft1.to()));
        assertNull(board.getPieceOn(moveDraft2.from()));
        assertEquals(moveDraft2.getPiece(), board.getPieceOn(moveDraft2.to()));
        assertEquals(moveDraft3.getPiece(), board.getPieceOn(moveDraft3.from()));
    }
}
