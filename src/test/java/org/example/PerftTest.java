package org.example;

import org.example.data.Board;
import org.example.data.move.MoveDraft;
import org.example.logic.ChessGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PerftTest {

    @Test
    void testBasicPerft() {
        int depth;
        int expected;
        int possibilities;

        depth = 0;
        expected = 1;
        possibilities = perft(depth, new Board().exportFEN());
        assertEquals(expected, possibilities);

        depth = 1;
        expected = 20;
        possibilities = perft(depth, new Board().exportFEN());
        assertEquals(expected, possibilities);

        depth = 2;
        expected = 400;
        possibilities = perft(depth, new Board().exportFEN());
        assertEquals(expected, possibilities);

        depth = 3;
        expected = 8902;
        possibilities = perft(depth, new Board().exportFEN());
        assertEquals(expected, possibilities);

        depth = 4;
        expected = 197281;
        possibilities = perft(depth, new Board().exportFEN());
        assertEquals(expected, possibilities);
    }

    int perft(int depth, String FEN) {
        depth--;
        if (depth == -1)
            return 1;

        int count = 0;

        ChessGame chessGame = new ChessGame(FEN);
        for (MoveDraft move : chessGame.determineAllPseudoLegalMoves()) {
            ChessGame replica = new ChessGame(FEN);
            if (replica.submitMove(move))
                count += perft(depth, replica.getBoard().exportFEN());
        }
        return count;
    }
}
