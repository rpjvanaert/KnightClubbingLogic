package knight.clubbing;

import knight.clubbing.core.BBoard;
import knight.clubbing.core.BMove;
import knight.clubbing.core.BPiece;
import knight.clubbing.moveGeneration.MoveGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class PerftTest {

    @Test @Tag("perft")
    void perftPosition1() {
        BBoard board = new BBoard();
        MoveGenerator moveGenerator = new MoveGenerator(board);

        assertEquals(20, perftParallel(moveGenerator, 1));

        board = new BBoard();
        moveGenerator = new MoveGenerator(board);
        assertEquals(400, perftParallel(moveGenerator, 2));

        board = new BBoard();
        moveGenerator = new MoveGenerator(board);
        assertEquals(8902, perftParallel(moveGenerator, 3));

        board = new BBoard();
        moveGenerator = new MoveGenerator(board);
        assertEquals(197281, perftParallel(moveGenerator, 4));

        board = new BBoard();
        moveGenerator = new MoveGenerator(board);
        assertEquals(4865609, perftParallel(moveGenerator, 5));
    }

    @Test @Tag("perft")
    void perftPosition2() { //a.k.a. Kiwipete
        String fen = "r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -";
        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);
        /*

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

         */

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(193690690, perftParallel(moveGenerator, 5));

        /*

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(8031647685L, perftParallel(moveGenerator, 6));

         */
    }

    @Test @Tag("perft")
    void perftPosition3() {
        String fen = "8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1 ";
        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);
        /*

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

         */

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(11030083, perftParallel(moveGenerator, 6));
    }

    @Test @Tag("perft")
    void perftPosition4() {
        String fen = "r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1";
        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);
        /*

        assertEquals(6, perft(moveGenerator, 1));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(264, perft(moveGenerator, 2));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(9467, perft(moveGenerator, 3));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(422333, perft(moveGenerator, 4));

         */

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(15833292, perft(moveGenerator, 5));

        /*
        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(706045033, perftParallel(moveGenerator, 6));

         */
    }

    @Test @Tag("perft")
    void perftPosition5() {
        String fen = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";
        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);
        /*

        assertEquals(44, perft(moveGenerator, 1));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(1486, perft(moveGenerator, 2));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(62379, perft(moveGenerator, 3));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(2103487, perft(moveGenerator, 4));

         */

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(89941194, perftParallel(moveGenerator, 5));
    }

    @Test @Tag("perft")
    void perftPosition6() {
        String fen = "r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10";
        BBoard board = new BBoard(fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);
        /*

        assertEquals(46, perft(moveGenerator, 1));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(2079, perft(moveGenerator, 2));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(89890, perft(moveGenerator, 3));

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(3894594, perft(moveGenerator, 4));

         */

        board = new BBoard(fen);
        moveGenerator = new MoveGenerator(board);
        assertEquals(164075551, perftParallel(moveGenerator, 5));
    }

    long perft(MoveGenerator moveGenerator, int depth) {
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

}
