package org.example.logic;

import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.MoveType;
import org.example.data.details.PieceType;
import org.example.data.move.MoveDraft;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessGameTest {

    @Test
    void testCastling() {
        ChessGame chessGame;

        chessGame = new ChessGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.WHITE, Coord.of("e1"), Coord.of("g1"), MoveType.CASTLE_SHORT)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.BLACK, Coord.of("e8"), Coord.of("g8"), MoveType.CASTLE_SHORT)));
        assertEquals(2, chessGame.getMoves().size());

        chessGame = new ChessGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.WHITE, Coord.of("e1"), Coord.of("c1"), MoveType.CASTLE_LONG)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.BLACK, Coord.of("e8"), Coord.of("c8"), MoveType.CASTLE_LONG)));
        assertEquals(2, chessGame.getMoves().size());
    }

    @Test
    void testRegularOpening() {
        ChessGame chessGame = new ChessGame();

        //1
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("d2"), Coord.of("d4"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("d7"), Coord.of("d5"), MoveType.NORMAL)));
        //2
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("g1"), Coord.of("f3"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("g8"), Coord.of("f6"), MoveType.NORMAL)));
        //3
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("c2"), Coord.of("c4"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e6"), MoveType.NORMAL)));
        //4
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c1"), Coord.of("g5"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("b8"), Coord.of("d7"), MoveType.NORMAL)));
        //5
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e3"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("f8"), Coord.of("e7"), MoveType.NORMAL)));
        //6
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("b1"), Coord.of("c3"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.BLACK, Coord.of("e8"), Coord.of("g8"), MoveType.CASTLE_SHORT)));
        //7
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a1"), Coord.of("c1"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("f8"), Coord.of("e8"), MoveType.NORMAL)));
        //8
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.QUEEN, Color.WHITE, Coord.of("d1"), Coord.of("c2"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("a7"), Coord.of("a6"), MoveType.NORMAL)));
        //9
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("c4"), Coord.of("d5"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e6"), Coord.of("d5"), MoveType.NORMAL)));
        //10
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("d3"), MoveType.NORMAL)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("c7"), Coord.of("c6"), MoveType.NORMAL)));
        //11
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.WHITE, Coord.of("e1"), Coord.of("g1"), MoveType.CASTLE_SHORT)));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("f6"), Coord.of("e4"), MoveType.NORMAL)));
    }
}