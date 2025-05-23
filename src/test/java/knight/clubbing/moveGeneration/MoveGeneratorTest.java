package knight.clubbing.moveGeneration;

import knight.clubbing.core.BBoard;
import knight.clubbing.core.BBoardHelper;
import knight.clubbing.core.BMove;
import knight.clubbing.moveGeneration.magic.PrecomputedMagics;
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

    @Test
    void testWhiteBishop() {
        BBoard board = new BBoard("7k/8/3b4/8/4B3/8/8/K7 w - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);
        List<BMove> bishopMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("e4"))
                .toList();

        assertEquals(13, bishopMoves.size());
        assertEquals(16, moves.length);
    }

    @Test
    void testBlackBishop() {
        BBoard board = new BBoard("7k/8/3b4/8/4B3/8/8/K7 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);
        List<BMove> bishopMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("d6"))
                .toList();

        assertEquals(11, bishopMoves.size());
        assertEquals(13, moves.length);
    }

    @Test
    void testWhiteBishopSpecial() {
        BBoard board = new BBoard("7k/7P/3b4/3p4/4B3/3P4/6P1/K7 w - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);
        List<BMove> bishopMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("e4"))
                .toList();

        assertEquals(4, bishopMoves.size());
        assertTrue(bishopMoves.contains(new BMove(BBoardHelper.stringCoordToIndex("e4"), BBoardHelper.stringCoordToIndex("f3"))));
        assertTrue(bishopMoves.contains(new BMove(BBoardHelper.stringCoordToIndex("e4"), BBoardHelper.stringCoordToIndex("d5"))));
        assertTrue(bishopMoves.contains(new BMove(BBoardHelper.stringCoordToIndex("e4"), BBoardHelper.stringCoordToIndex("f5"))));
        assertTrue(bishopMoves.contains(new BMove(BBoardHelper.stringCoordToIndex("e4"), BBoardHelper.stringCoordToIndex("g6"))));

        assertEquals(10, moves.length);
    }

    @Test
    void testBlackBishopSpecial() {
        BBoard board = new BBoard("5p1k/2P5/3b4/2p5/4Bp2/8/8/K7 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);
        List<BMove> bishopMoves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("d6"))
                .toList();

        assertEquals(3, bishopMoves.size());
        assertTrue(bishopMoves.contains(new BMove(BBoardHelper.stringCoordToIndex("d6"), BBoardHelper.stringCoordToIndex("c7"))));
        assertTrue(bishopMoves.contains(new BMove(BBoardHelper.stringCoordToIndex("d6"), BBoardHelper.stringCoordToIndex("e7"))));
        assertTrue(bishopMoves.contains(new BMove(BBoardHelper.stringCoordToIndex("d6"), BBoardHelper.stringCoordToIndex("e5"))));

        assertEquals(8, moves.length);
    }

    @Test
    void testWhiteKnight() {
        BBoard board = new BBoard("6nk/8/8/7n/8/2N5/5N2/K7 w - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> knight1moves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("c3"))
                .toList();

        List<BMove> knight2moves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("f2"))
                .toList();

        assertEquals(8, knight1moves.size());
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("a2"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("a4"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("b1"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("d1"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("e2"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("e4"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("d5"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("b5"))));

        assertEquals(6, knight2moves.size());
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("d1"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("d3"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("e4"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("g4"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("h3"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("h1"))));
    }

    @Test
    void testBlackKnight() {
        BBoard board = new BBoard("n5nk/8/8/8/8/2N5/5N2/K7 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> knight1moves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("a8"))
                .toList();

        List<BMove> knight2moves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("g8"))
                .toList();

        assertEquals(2, knight1moves.size());
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("a8"), BBoardHelper.stringCoordToIndex("b6"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("a8"), BBoardHelper.stringCoordToIndex("c7"))));

        assertEquals(3, knight2moves.size());
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("g8"), BBoardHelper.stringCoordToIndex("e7"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("g8"), BBoardHelper.stringCoordToIndex("f6"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("g8"), BBoardHelper.stringCoordToIndex("h6"))));
    }

    @Test
    void testWhiteKnightSpecial() {
        BBoard board = new BBoard("n5nk/8/8/1P6/p3P3/1PNp3P/5N2/K7 w - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> knight1moves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("c3"))
                .toList();

        List<BMove> knight2moves = Arrays.stream(moves)
                .filter(Objects::nonNull)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("f2"))
                .toList();

        assertEquals(6, knight1moves.size());
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("a2"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("a4"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("b1"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("d1"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("e2"))));
        assertTrue(knight1moves.contains(new BMove(BBoardHelper.stringCoordToIndex("c3"), BBoardHelper.stringCoordToIndex("d5"))));

        assertEquals(4, knight2moves.size());
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("d1"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("d3"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("g4"))));
        assertTrue(knight2moves.contains(new BMove(BBoardHelper.stringCoordToIndex("f2"), BBoardHelper.stringCoordToIndex("h1"))));

        assertEquals(18, moves.length);
    }

    @Test
    void testPositionSpecial1() {
        BBoard board = new BBoard("rnbqkbnr/p1pppppp/8/1p6/P7/8/1PPPPPPP/RNBQKBNR w KQkq - 0 2");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        assertEquals(0, Arrays.stream(moves)
                .filter(move -> move.moveFlagName().equals("unknown"))
                .count()
        );

        //todo more
    }

    @Test
    void testPositionKiwipeteBase() {
        BBoard board = new BBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        assertEquals(0, Arrays.stream(moves)
                .filter(move -> move.moveFlagName().equals("unknown"))
                .count()
        );

        //todo more
    }

    @Test
    void testPositionKiwipete_a2a4() {
        BBoard board = new BBoard("r3k2r/p1ppqpb1/bn2pnp1/3PN3/Pp2P3/2N2Q1p/1PPBBPPP/R3K2R b KQkq a3 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);
        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> b4moves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("b4"))
                .toList();

        assertEquals(3, b4moves.size());
    }

    @Test
    void testPositionKiwipete_e2b5() {
        BBoard board = new BBoard("r3k2r/p1ppqpb1/bn2pnp1/1B1PN3/1p2P3/2N2Q1p/PPPB1PPP/R3K2R b KQkq - 1 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        assertFalse(Arrays.stream(moves).anyMatch(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("d7")));

        assertEquals(39, moves.length);
    }

    @Test
    void testRookPawnPin() {
        BBoard board = new BBoard("8/8/2R1pk2/8/8/8/8/K7 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> pawnMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("e6"))
                .toList();

        assertEquals(0, pawnMoves.size(), "Pawn moves should be empty, but is " + pawnMoves);
    }

    @Test
    void testRookKnightPin() {
        BBoard board = new BBoard("8/8/2R1nk2/8/8/8/8/K7 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> pawnMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("e6"))
                .toList();

        assertEquals(0, pawnMoves.size(), "Pawn moves should be empty, but is " + pawnMoves);
    }

    @Test
    void testBishopPawnPin() {
        BBoard board = new BBoard("8/5k2/4p3/8/2B5/8/8/K7 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> pawnMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("e6"))
                .toList();

        assertEquals(0, pawnMoves.size(), "Pawn moves should be empty, but is " + pawnMoves);
    }

    @Test
    void testBishopKnightPin() {
        BBoard board = new BBoard("8/5k2/4n3/8/2B5/8/8/K7 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> pawnMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("e6"))
                .toList();

        assertEquals(0, pawnMoves.size(), "Pawn moves should be empty, but is " + pawnMoves);
    }

    @Test
    void testDoubleCheck1() {
        BBoard board = new BBoard("7q/4k3/4n3/5N2/8/8/3K4/4R3 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> knightMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("e6"))
                .toList();

        List<BMove> queenMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("h6"))
                .toList();

        assertEquals(0, knightMoves.size(), "Knight moves should be empty, but is " + knightMoves);
        assertEquals(0, queenMoves.size(), "Queen moves should be empty, but is " + queenMoves);
        assertEquals(6, moves.length, "Moves should be 6, but is " + moves.length);

        Arrays.stream(moves)
                .forEach(move -> assertEquals(BBoardHelper.stringCoordToIndex("e7"), move.startSquare()));
    }

    @Test
    void testPromotionWhite1() {
        BBoard board = new BBoard("7k/1P6/8/8/8/8/1p6/7K w - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> pawnMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("b7"))
                .toList();

        assertEquals(4, pawnMoves.size(), "Pawn moves should be 4, but is " + pawnMoves);
        assertEquals(7, moves.length, "Moves should be 6, but is " + moves.length);
    }

    @Test
    void testPromotionBlack1() {
        BBoard board = new BBoard("7k/1P6/8/8/8/8/1p6/7K b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> pawnMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("b2"))
                .toList();

        assertEquals(4, pawnMoves.size(), "Pawn moves should be 4, but is " + pawnMoves);
        assertEquals(7, moves.length, "Moves should be 6, but is " + moves.length);
    }

    @Test
    void testPromotionWhite2() {
        BBoard board = new BBoard("N1n4k/1P6/8/8/8/8/1p6/n1N4K w - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> pawnMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("b7"))
                .toList();

        assertEquals(8, pawnMoves.size(), "Pawn moves should be 8, but is " + pawnMoves);
    }

    @Test
    void testPromotionBlack2() {
        BBoard board = new BBoard("N1n4k/1P6/8/8/8/8/1p6/n1N4K b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        List<BMove> pawnMoves = Arrays.stream(moves)
                .filter(move -> move.startSquare() == BBoardHelper.stringCoordToIndex("b2"))
                .toList();

        assertEquals(8, pawnMoves.size(), "Pawn moves should be 8, but is " + pawnMoves);
    }

    @Test
    void testBishopCheck1() {
        BBoard board = new BBoard("5nn1/1k3nn1/8/8/8/6K1/6B1/8 b - - 0 1");
        MoveGenerator moveGenerator = new MoveGenerator(board);

        BMove[] moves = moveGenerator.generateMoves(false);

        Arrays.stream(moves)
                .forEach(move -> assertEquals(BBoardHelper.stringCoordToIndex("b7"), move.startSquare()));

        assertEquals(6, moves.length, "Moves should be 6, but is " + moves.length);
    }
}