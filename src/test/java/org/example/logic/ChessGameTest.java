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
        //todo black side short and both side long in reinitialized game
    }
}