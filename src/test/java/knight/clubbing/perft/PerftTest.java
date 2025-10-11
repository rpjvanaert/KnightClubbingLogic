package knight.clubbing.perft;

import knight.clubbing.core.BBoard;
import knight.clubbing.core.BMove;
import knight.clubbing.core.BPiece;
import knight.clubbing.movegen.MoveGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class PerftTest {

    private static final Logger logger = Logger.getLogger(PerftTest.class.getName());

    /**
     * Perft position 1 - initial
     * rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
     * -------------------
     * depth    |   nodes
     * 1            20
     * 2            400
     * 3            8902
     * 4            197281
     * 5            4865609
     * 6            119060324
     */

    @Test @Tag("perft")
    void perftPosition1() {
        int nodes = 119060324;
        int depth = 6;

        BBoard board = new BBoard();
        MoveGenerator moveGenerator = new MoveGenerator(board);

        long timeStart = System.nanoTime();
        assertEquals(nodes, perftParallel(moveGenerator, depth));
        long timeEnd = System.nanoTime();
        logPerformance(timeEnd, timeStart, nodes);

    }

    /**
     * Perft position 2 - Kiwipete
     * r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -
     * -------------------
     * depth    |   nodes
     * 1            48
     * 2            2039
     * 3            97862
     * 4            4085603
     * 5            193690690
     * 6            8031647685
     */

    @Test @Tag("perft")
    void perftPosition2() {
        String fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -";
        int nodes = 193690690;
        int depth = 5;

        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        long timeStart = System.nanoTime();
        assertEquals(nodes, perftParallel(moveGenerator, depth));
        long timeEnd = System.nanoTime();
        logPerformance(timeEnd, timeStart, nodes);
    }

    /**
     * Perft position 3
     * 8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1
     * -------------------
     * depth    |   nodes
     * 1            14
     * 2            191
     * 3            2812
     * 4            43238
     * 5            674624
     * 6            11030083
     */

    @Test @Tag("perft")
    void perftPosition3() {
        String fen = "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ";
        int nodes = 11030083;
        int depth = 6;

        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        long timeStart = System.nanoTime();
        assertEquals(nodes, perftParallel(moveGenerator, depth));
        long timeEnd = System.nanoTime();
        logPerformance(timeEnd, timeStart, nodes);
    }

    /**
     * Perft position 4
     * r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1
     * -------------------
     * depth    |   nodes
     * 1            6
     * 2            264
     * 3            9467
     * 4            422333
     * 5            15833292
     * 6            706045033
     */

    @Test @Tag("perft")
    void perftPosition4() {
        String fen = "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1";
        int nodes = 15833292;
        int depth = 5;

        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        long timeStart = System.nanoTime();
        assertEquals(nodes, perft(moveGenerator, depth));
        long timeEnd = System.nanoTime();
        logPerformance(timeEnd, timeStart, nodes);
    }

    /**
     * Perft position 5
     * rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8
     * -------------------
     * depth    |   nodes
     * 1            44
     * 2            1486
     * 3            62379
     * 4            2103487
     * 5            89941194
     */

    @Test @Tag("perft")
    void perftPosition5() {
        String fen = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";
        int nodes = 89941194;
        int depth = 5;

        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        long timeStart = System.nanoTime();
        assertEquals(nodes, perftParallel(moveGenerator, depth));
        long timeEnd = System.nanoTime();
        logPerformance(timeEnd, timeStart, nodes);
    }

    /**
     * Perft position 6
     * r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10
     * -------------------
     * depth    |   nodes
     * 1            46
     * 2            2079
     * 3            89890
     * 4            3894594
     * 5            164075551
     * 6            6923051137
     */

    @Test @Tag("perft")
    void perftPosition6() {
        String fen = "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10";
        int nodes = 164075551;
        int depth = 5;

        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        long timeStart = System.nanoTime();
        assertEquals(nodes, perftParallel(moveGenerator, depth));
        long timeEnd = System.nanoTime();
        logPerformance(timeEnd, timeStart, nodes);
    }

    long perft(MoveGenerator moveGenerator, int depth) {
        if (depth == 0) return 1;

        long nodes = 0;

        BMove[] moves = moveGenerator.generateMoves(false);

        for (BMove move : moves) {
            assertNotNull(move);
            //int piece = moveGenerator.getBoard().getPieceBoards()[move.startSquare()];
            //assertNotEquals(0, piece, "piece=0 fault: " + move + " for FEN: " + moveGenerator.getBoard().exportFen());
            //assertEquals(moveGenerator.getBoard().isWhiteToMove, BPiece.isWhite(piece));
            //assertNotEquals("unknown", move.moveFlagName(), "unknown moveFlagName: " + move.moveFlagName());

            moveGenerator.getBoard().makeMove(move, false);
            nodes += perft(moveGenerator, depth - 1);
            moveGenerator.getBoard().undoMove(move, false);
        }

        return nodes;
    }

    long perftParallel(MoveGenerator moveGenerator, int depth) {
        if (depth == 0) return 1;

        BMove[] moves = moveGenerator.generateMoves(false);

        return Arrays.stream(moves).parallel().mapToLong(move -> {

            int piece = moveGenerator.getBoard().getPieceBoards()[move.startSquare()];
            assertNotEquals(0, piece, "piece=0 fault: " + move + " for FEN: " + moveGenerator.getBoard().exportFen());
            assertEquals(moveGenerator.getBoard().isWhiteToMove, BPiece.isWhite(piece));
            assertNotEquals("unknown", move.moveFlagName(), "unknown moveFlagName: " + move.moveFlagName());

            BBoard childBoard = new BBoard(moveGenerator.getBoard());
            MoveGenerator childMoveGenerator = new MoveGenerator(childBoard);

            childBoard.makeMove(move, true);

            long result = perft(childMoveGenerator, depth - 1);

            childBoard.undoMove(move, true);

            return result;
        }).sum();
    }

    long perftDivide(MoveGenerator moveGenerator, int depth) {
        if (depth == 0) return 1;

        long nodes = 0;

        BMove[] moves = moveGenerator.generateMoves(false);

        for (BMove move : moves) {
            assertNotNull(move);
            int piece = moveGenerator.getBoard().getPieceBoards()[move.startSquare()];
            assertNotEquals(0, piece, "piece=0 fault: " + move + " for FEN: " + moveGenerator.getBoard().exportFen());
            assertEquals(moveGenerator.getBoard().isWhiteToMove, BPiece.isWhite(piece));
            assertNotEquals("unknown", move.moveFlagName(), "unknown moveFlagName: " + move.moveFlagName());

            moveGenerator.getBoard().makeMove(move, false);
            long moveNodes = perft(moveGenerator, depth - 1);

            System.out.println(move.getUci() + ": " + moveNodes);

            nodes += moveNodes;
            moveGenerator.getBoard().undoMove(move, false);
        }

        return nodes;
    }

    private static void logPerformance(long timeEnd, long timeStart, int nodes) {
        long elapsedNs = timeEnd - timeStart;
        double elapsedSeconds = elapsedNs / 1000000000.0;
        double mnps = nodes / elapsedSeconds / 1_000_000.0;
        logger.info(String.format("%.3f Mnps", mnps));
    }
}
