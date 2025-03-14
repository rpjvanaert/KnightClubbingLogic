package knight.clubbing;

import knight.clubbing.logic.ChessGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
/*
public class PerftTest {

    @Test
    void testBasicPerft() {
        int depth;
        long expected;
        long possibilities;
        ChessGame chessGame;

        depth = 0;
        expected = 1;
        chessGame = new ChessGame();
        possibilities = chessGame.perft(depth);
        assertEquals(expected, possibilities);

        depth = 1;
        expected = 20;
        chessGame = new ChessGame();
        possibilities = chessGame.perft(depth);
        assertEquals(expected, possibilities);

        depth = 2;
        expected = 400;
        chessGame = new ChessGame();
        possibilities = chessGame.perft(depth);
        assertEquals(expected, possibilities);

        depth = 3;
        expected = 8902;
        chessGame = new ChessGame();
        possibilities = chessGame.perft(depth);
        assertEquals(expected, possibilities);

        depth = 4;
        expected = 197281;
        chessGame = new ChessGame();
        possibilities = chessGame.perft(depth);
        assertEquals(expected, possibilities);
    }

    @Test
    void testPerftDivideStartPositionDepth1() {
        ChessGame game = new ChessGame();
        long expectedNodes = 20;

        assertEquals(expectedNodes, game.perftDivide(1), "Incorrect Perft(1) result.");
    }

    @Test
    void testPerftDivideStartPositionDepth2() {
        ChessGame game = new ChessGame();
        long expectedNodes = 400;
        assertEquals(expectedNodes, game.perftDivide(2), "Incorrect Perft(2) result.");
    }

    @Test
    void testPerftDivideStartPositionDepth3() {
        ChessGame game = new ChessGame();
        long expectedNodes = 8902;
        assertEquals(expectedNodes, game.perftDivide(3), "Incorrect Perft(3) result.");
    }

    @Test
    void testPerftDivideCastling() {
        ChessGame game = new ChessGame("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        long expectedNodes = 34;

        assertEquals(expectedNodes, game.perftDivide(1), "Incorrect Perft(1) result for castling.");
    }

    @Test
    void testPerftDivideEnPassant() {
        ChessGame game = new ChessGame("rnbqkbnr/pppppppp/8/8/4Pp2/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 2");
        long expectedNodes = 48;

        assertEquals(expectedNodes, game.perftDivide(1), "Incorrect Perft(1) result for en passant.");
    }

    @Test
    void testPerftPosition2() {
        ChessGame game = new ChessGame("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 0");
        long expectedNodes;
        int depth;

        expectedNodes = 48;
        depth = 1;
        assertEquals(expectedNodes, game.perftDivide(depth));

        expectedNodes = 2039;
        depth = 2;
        assertEquals(expectedNodes, game.perft(depth));

        expectedNodes = 97862;
        depth = 3;
        assertEquals(expectedNodes, game.perft(depth));

        expectedNodes = 4085603;
        depth = 4;
        assertEquals(expectedNodes, game.perft(depth));

        expectedNodes = 193690690;
        depth = 5;
        assertEquals(expectedNodes, game.perft(depth));
    }

    @Test
    void testPerftPosition3() {
        ChessGame game = new ChessGame("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 0");
        long expectedNodes;
        int depth;

        expectedNodes = 14;
        depth = 1;
        assertEquals(expectedNodes, game.perftDivide(depth));

        expectedNodes = 191;
        depth = 2;
        assertEquals(expectedNodes, game.perft(depth));

        expectedNodes = 2812;
        depth = 3;
        assertEquals(expectedNodes, game.perft(depth));

        expectedNodes = 43238;
        depth = 4;
        assertEquals(expectedNodes, game.perft(depth));

        expectedNodes = 674624;
        depth = 5;
        assertEquals(expectedNodes, game.perft(depth));
    }
}

 */
