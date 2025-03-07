package org.example.logic;

import org.example.data.Board;
import org.example.data.details.Castling;
import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.PieceType;
import org.example.data.move.MoveDraft;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleCheckerTest {

    @Test
    void testInvalidCases() {
        ChessGame chessGame = new ChessGame();
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e4"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e5"))));

        assertFalse(RuleChecker.isKingInCheck(chessGame.getBoard(), Color.WHITE));
        assertFalse(RuleChecker.isKingInCheck(chessGame.getBoard(), Color.BLACK));

        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.QUEEN, Color.WHITE, Coord.of("d1"), Coord.of("e2"))));
        assertFalse(RuleChecker.isKingInCheck(chessGame.getBoard(), Color.BLACK));

        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.QUEEN, Color.BLACK, Coord.of("d8"), Coord.of("h4"))));
        assertFalse(RuleChecker.isKingInCheck(chessGame.getBoard(), Color.WHITE));
    }

    @Test
    void testBishop() {
        ChessGame chessGame = new ChessGame();
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e4"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e5"))));

        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("g1"), Coord.of("f3"))));
        assertFalse(RuleChecker.isKingInCheck(chessGame.getBoard(), Color.BLACK));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("d7"), Coord.of("d6"))));
        assertFalse(RuleChecker.isKingInCheck(chessGame.getBoard(), Color.WHITE));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("b5"))));
        assertTrue(RuleChecker.isKingInCheck(chessGame.getBoard(), Color.BLACK));
    }

    @Test
    void testCastlingCorrect() {
        Board board;

        board = new Board("rnbqk2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQK2R w KQkq - 0 1");
        assertTrue(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.KING));

        board = new Board("rnbqk2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQK2R b KQkq - 0 1");
        assertTrue(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.KING));

        board = new Board("r3kbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 1");
        assertTrue(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.QUEEN));

        board = new Board("r3kbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR b KQkq - 0 1");
        assertTrue(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.QUEEN));
    }

    @Test
    void testCastlingInvalid_obstructingPieces() {
        Board board;

        board = new Board();
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.KING));

        board = new Board();
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.KING));

        board = new Board();
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.QUEEN));

        board = new Board();
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.QUEEN));

        board = new Board("rn2k1nr/pppppppp/8/8/8/8/PPPPPPPP/RN2K1NR w KQkq - 0 1");
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.KING));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.KING));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.QUEEN));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.QUEEN));
    }

    @Test
    void testCastlingInvalid_kingInCheck() {
        Board board;

        board = new Board("r3k2r/pp1p1ppp/8/2Q5/2q5/8/PP1P1PPP/R3K2R w KQkq - 0 1");
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.KING));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.KING));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.QUEEN));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.QUEEN));

        board = new Board("r3k2r/pppp1ppp/8/4Q3/4q3/8/PPPP1PPP/R3K2R w KQkq - 0 1");
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.KING));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.KING));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.QUEEN));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.QUEEN));

        board = new Board("r3k2r/8/b6b/8/8/B6B/8/R3K2R w KQkq - 0 1");
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.KING));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.KING));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.QUEEN));
        assertFalse(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.QUEEN));
    }

    @Test
    void testCastling_kingNotInCheck() {
        Board board;

        board = new Board("r3k2r/2ppppp1/8/RR5R/rr5r/8/2PPPPP1/R3K2R w KQkq - 0 1");
        assertTrue(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.KING));
        assertTrue(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.KING));
        assertTrue(RuleChecker.isCastlingPossible(board, Color.WHITE, Castling.QUEEN));
        assertTrue(RuleChecker.isCastlingPossible(board, Color.BLACK, Castling.QUEEN));
    }
}