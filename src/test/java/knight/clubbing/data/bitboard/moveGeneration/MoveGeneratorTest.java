package knight.clubbing.data.bitboard.moveGeneration;

import knight.clubbing.data.bitboard.core.BBoard;
import knight.clubbing.data.bitboard.core.BBoardHelper;
import knight.clubbing.data.bitboard.core.BMove;
import knight.clubbing.data.bitboard.moveGeneration.magic.PrecomputedMagics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorTest {

    @BeforeAll
    static void callPrecomputed() {
        long[] test = PrecomputedMagics.BISHOP_MAGICS;
        PrecomputedMoveData.getInstance();
    }

    @Test
    void testInitialPositionLegalMoves() {
        BBoard board = new BBoard();
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);
        System.out.println(Arrays.toString(moves));
        assertEquals(20, moves.length);
    }

    @Test
    void testCastlingBothSidesWhite() {
        BBoard board = new BBoard("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> castleMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.moveFlag() == BMove.castleFlag)
                .toList();

        assertEquals(2, castleMoves.size());
        assertTrue(castleMoves.contains(new BMove(BBoardHelper.e1, BBoardHelper.g1, BMove.castleFlag)));
        assertTrue(castleMoves.contains(new BMove(BBoardHelper.e1, BBoardHelper.c1, BMove.castleFlag)));

        List<BMove> otherKingMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.moveFlag() != BMove.castleFlag)
                .filter(move -> move.startSquare() == BBoardHelper.e1)
                .toList();

        assertEquals(5, otherKingMoves.size());
        assertTrue(otherKingMoves.contains(new BMove(BBoardHelper.e1, BBoardHelper.d1)));
        assertTrue(otherKingMoves.contains(new BMove(BBoardHelper.e1, BBoardHelper.stringCoordToIndex("d2"))));
        assertTrue(otherKingMoves.contains(new BMove(BBoardHelper.e1, BBoardHelper.stringCoordToIndex("e2"))));
        assertTrue(otherKingMoves.contains(new BMove(BBoardHelper.e1, BBoardHelper.stringCoordToIndex("f2"))));
        assertTrue(otherKingMoves.contains(new BMove(BBoardHelper.e1, BBoardHelper.f1)));

        List<BMove> rookA1Moves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.a1)
                .toList();

        assertEquals(10, rookA1Moves.size());
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("b1"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("c1"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("d1"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("a2"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("a3"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("a4"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("a5"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("a6"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("a7"))));
        assertTrue(rookA1Moves.contains(new BMove(BBoardHelper.a1, BBoardHelper.stringCoordToIndex("a8"))));

        List<BMove> rookH1Moves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.h1)
                .toList();

        assertEquals(9, rookH1Moves.size()); //todo fix h1h8 move not added
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("g1"))));
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("f1"))));
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("h2"))));
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("h3"))));
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("h4"))));
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("h5"))));
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("h6"))));
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("h7"))));
        assertTrue(rookH1Moves.contains(new BMove(BBoardHelper.h1, BBoardHelper.stringCoordToIndex("h8"))));
    }

    @Test
    void testCastlingWhiteInvalid() {
        BBoard board = new BBoard("r3k2r/8/8/8/8/7n/1R4R1/R1N1K2R w KQk - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> castleMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.moveFlag() == BMove.castleFlag)
                .toList();

        List<BMove> kingMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.e1)
                .toList();

        assertEquals(0, castleMoves.size());
        assertEquals(4, kingMoves.size());
    }

    @Test
    void testCastlingBlackInvalid() {
        BBoard board = new BBoard("r3k2r/8/8/8/8/7n/1R4R1/R1N1K2R b KQkq - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> castleMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.moveFlag() == BMove.castleFlag)
                .toList();

        List<BMove> kingMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.e8)
                .toList();

        assertEquals(1, castleMoves.size());
        assertEquals(6, kingMoves.size());

        board = new BBoard("r3k2r/8/8/8/8/7n/8/R1N1K2R b KQ - 0 1");
        moveGenerator = new MoveGenerator(board);
        moves = moveGenerator.generateMoves(false);

        castleMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.moveFlag() == BMove.castleFlag)
                .toList();

        kingMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.e8)
                .toList();

        assertEquals(0, castleMoves.size());
        assertEquals(5, kingMoves.size());
    }
}