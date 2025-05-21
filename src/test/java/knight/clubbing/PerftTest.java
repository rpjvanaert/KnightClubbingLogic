package knight.clubbing;

import knight.clubbing.core.BBoard;
import knight.clubbing.core.BMove;
import knight.clubbing.moveGeneration.MoveGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PerftTest {

    @Test @Tag("perft")
    void perftBasic() {
        BBoard board = new BBoard();
        MoveGenerator moveGenerator = new MoveGenerator(board);

        assertEquals(20, perft(moveGenerator, 1));

        board = new BBoard();
        moveGenerator = new MoveGenerator(board);
        assertEquals(400, perft(moveGenerator, 2));

        board = new BBoard();
        moveGenerator = new MoveGenerator(board);
        assertEquals(8902, perft(moveGenerator, 3));

        board = new BBoard();
        moveGenerator = new MoveGenerator(board);
        assertEquals(197281, perft(moveGenerator, 4));

        board = new BBoard();
        moveGenerator = new MoveGenerator(board);
        assertEquals(4865609, perft(moveGenerator, 5));
    }

    @Test @Tag("perft")
    void perftKiwipete() {
        String fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - ";
        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        assertEquals(48, perft(moveGenerator, 1));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(2039, perft(moveGenerator, 2));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(97862, perft(moveGenerator, 3));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(4085603, perft(moveGenerator, 4));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(193690690, perft(moveGenerator, 5));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(8031647685L, perft(moveGenerator, 6));
    }

    @Test @Tag("perft")
    void perftPosition3() {
        String fen = "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ";
        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        assertEquals(14, perft(moveGenerator, 1));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(191, perft(moveGenerator, 2));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(2812, perft(moveGenerator, 3));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(43238, perft(moveGenerator, 4));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(674624, perft(moveGenerator, 5));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(11030083, perft(moveGenerator, 6));
    }

    long perft(MoveGenerator moveGenerator, int depth) {
        if (depth == 0) return 1;

        long nodes = 0;

        BMove[] moves = moveGenerator.generateMoves(false);

        for (BMove move : moves) {
            assertNotNull(move);
            assertNotEquals(0, moveGenerator.getBoard().getPieceBoards()[move.startSquare()], "piece=0 fault: " + move + " for FEN: " + moveGenerator.getBoard().exportFen());
            assertNotEquals("unknown", move.moveFlagName(), "unknown moveFlagName: " + move.moveFlagName());

            moveGenerator.getBoard().makeMove(move, true);
            nodes += perft(moveGenerator, depth - 1);
            moveGenerator.getBoard().undoMove(move, true);
        }

        return nodes;
    }

    long perftDivide(MoveGenerator moveGenerator, int depth) {
        if (depth == 0) return 1;

        long nodes = 0;

        BMove[] moves = moveGenerator.generateMoves(false);

        for (BMove move : moves) {
            assertNotNull(move);
            assertNotEquals(0, moveGenerator.getBoard().getPieceBoards()[move.startSquare()], "piece=0 fault: " + move + " for FEN: " + moveGenerator.getBoard().exportFen());
            assertNotEquals("unknown", move.moveFlagName(), "unknown moveFlagName: " + move.moveFlagName());

            moveGenerator.getBoard().makeMove(move, true);
            long moveNodes = perft(moveGenerator, depth - 1);

            System.out.println(move.getUci() + ": " + moveNodes);

            nodes += moveNodes;
            moveGenerator.getBoard().undoMove(move, true);
        }

        return nodes;
    }

}
