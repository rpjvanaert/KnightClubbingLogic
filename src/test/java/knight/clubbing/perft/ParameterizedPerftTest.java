package knight.clubbing.perft;

import knight.clubbing.core.BBoard;
import knight.clubbing.core.BMove;
import knight.clubbing.core.BPiece;
import knight.clubbing.movegen.MoveGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ParameterizedPerftTest {

    private static final Path PERFT_FILE = Path.of("src", "test", "resources", "perft", "standard.epd");
    private static final Logger logger = Logger.getLogger(ParameterizedPerftTest.class.getName());

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("perftCaseStream")
    @Tag("perft")
    void perftTest(PerftCase perftCase) {
        System.out.println("Testing FEN: " + perftCase.fen + " at depth " + perftCase.depth + " expecting " + perftCase.expectedNodes + " nodes");
        BBoard board = new BBoard(perftCase.fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        long timeStart = System.nanoTime();
        assertEquals(perftCase.expectedNodes, perft(moveGenerator, perftCase.depth));
        long timeEnd = System.nanoTime();
        logPerformance(timeEnd, timeStart, perftCase.expectedNodes);
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("perftCaseStream")
    @Tag("perft")
    void perftTestParallel(PerftCase perftCase) {
        System.out.println("Testing FEN: " + perftCase.fen + " at depth " + perftCase.depth + " expecting " + perftCase.expectedNodes + " nodes");
        BBoard board = new BBoard(perftCase.fen);
        MoveGenerator moveGenerator = new MoveGenerator(board);

        long timeStart = System.nanoTime();
        assertEquals(perftCase.expectedNodes, perftParallel(moveGenerator, perftCase.depth));
        long timeEnd = System.nanoTime();
        logPerformance(timeEnd, timeStart, perftCase.expectedNodes);
    }

    private static Stream<PerftCase> perftCaseStream() throws Exception {
        return Files.lines(PERFT_FILE)
                .map(line -> {
                    String fen = line.split(";")[0].trim();
                    String[] parts = line.split(";");
                    for (String part : parts) {
                        part = part.trim();
                        if (part.startsWith("D3")) {
                            int depth = 3;
                            long expectedNodes = Long.parseLong(part.split(" ")[1]);
                            return new PerftCase(fen, depth, expectedNodes);
                        }
                    }
                    throw new IllegalArgumentException("Invalid line " + line);
                });
    }

    long perft(MoveGenerator moveGenerator, int depth) {
        if (depth == 0) return 1;

        long nodes = 0;

        BMove[] moves = moveGenerator.generateMoves(false);

        for (BMove move : moves) {
            assertNotNull(move);

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

    private static void logPerformance(long timeEnd, long timeStart, long nodes) {
        long elapsedNs = timeEnd - timeStart;
        double elapsedSeconds = elapsedNs / 1000000000.0;
        double mnps = nodes / elapsedSeconds / 1_000_000.0;
        logger.info(String.format("%.3f Mnps", mnps));
    }

    private static class PerftCase {
        final String fen;
        final int depth;
        final long expectedNodes;

        PerftCase(String fen, int depth, long expectedNodes) {
            this.fen = fen;
            this.depth = depth;
            this.expectedNodes = expectedNodes;
        }

        @Override
        public String toString() {
            return "PerftCase{" +
                    "fen='" + fen + '\'' +
                    ", depth=" + depth +
                    ", expectedNodes=" + expectedNodes +
                    '}';
        }
    }
}
