package org.example.logic;

import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.PieceType;
import org.example.data.move.MoveDraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleCheckerTest {

    private ChessGame chessGame;

    @BeforeEach
    void setChessGame() {
        this.chessGame = new ChessGame();
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e4"))));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e5"))));
    }

    @Test
    void testInvalidCases() {
        assertFalse(RuleChecker.isKingInCheck(this.chessGame.getBoard(), Color.WHITE));
        assertFalse(RuleChecker.isKingInCheck(this.chessGame.getBoard(), Color.BLACK));

        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.QUEEN, Color.WHITE, Coord.of("d1"), Coord.of("e2"))));
        assertFalse(RuleChecker.isKingInCheck(this.chessGame.getBoard(), Color.BLACK));

        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.QUEEN, Color.BLACK, Coord.of("d8"), Coord.of("h4"))));
        assertFalse(RuleChecker.isKingInCheck(this.chessGame.getBoard(), Color.WHITE));
    }

    @Test
    void testBishop() {
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("g1"), Coord.of("f3"))));
        assertFalse(RuleChecker.isKingInCheck(this.chessGame.getBoard(), Color.BLACK));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("d7"), Coord.of("d6"))));
        assertFalse(RuleChecker.isKingInCheck(this.chessGame.getBoard(), Color.WHITE));
        assertTrue(this.chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("b5"))));
        assertTrue(RuleChecker.isKingInCheck(this.chessGame.getBoard(), Color.BLACK));
    }
}