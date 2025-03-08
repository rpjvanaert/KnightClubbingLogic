package org.example;

import org.example.data.Board;
import org.example.data.move.MoveDraft;
import org.example.logic.ChessGame;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PerftTest {

    @Test
    void testBasicPerft() {
        int depth;
        int expected;
        int possibilities;

        depth = 0;
        expected = 1;
        possibilities = perft(new Board().exportFEN(), depth);
        assertEquals(expected, possibilities);

        depth = 1;
        expected = 20;
        possibilities = perft(new Board().exportFEN(), depth);
        assertEquals(expected, possibilities);

        depth = 2;
        expected = 400;
        possibilities = perft(new Board().exportFEN(), depth);
        assertEquals(expected, possibilities);

        depth = 3;
        expected = 8902;
        possibilities = perft(new Board().exportFEN(), depth);
        assertEquals(expected, possibilities);

        depth = 4;
        expected = 197281;
        possibilities = perft(new Board().exportFEN(), depth);
        assertEquals(expected, possibilities);
    }

    @Test
    void testFenPerft() {
        int depth;
        List<String> result;

        depth = 1;
        result = perftFen(depth, new Board().exportFEN());
        result = new ArrayList<>(new LinkedHashSet<>(result));

        //todo something useful here
    }

    int perft(String fen, int depth) {
        if (depth == 0)
            return 1;

        int count = 0;

        ChessGame chessGame = new ChessGame(fen);
        for (MoveDraft move : chessGame.determineAllPseudoLegalMoves()) {
            ChessGame replica = new ChessGame(fen);
            if (replica.submitMove(move))
                count += perft(replica.getBoard().exportFEN(), depth - 1);
        }
        return count;
    }

    List<String> perftFen(int depth, String fen) {
        depth--;
        if (depth == -1)
            return List.of(fen);

        List<String> fens = new ArrayList<>();

        ChessGame chessGame = new ChessGame(fen);
        for (MoveDraft move : chessGame.determineAllPseudoLegalMoves()) {
            ChessGame replica = new ChessGame(fen);
            if (replica.submitMove(move)) {
                fens.addAll(perftFen(depth, replica.getBoard().exportFEN()));
            }

        }
        return fens;
    }
}
