package knight.clubbing.logic;

import knight.clubbing.data.Board;
import knight.clubbing.data.details.Color;
import knight.clubbing.data.details.Coord;
import knight.clubbing.data.details.PieceType;
import knight.clubbing.data.details.Promotion;
import knight.clubbing.data.move.MoveDraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoveMakerTest {

    private ChessGame chessGame;

    @BeforeEach
    void setChessGame() {
        this.chessGame = new ChessGame();
    }

    @Test
    void testStandardPawn() {
        List<MoveDraft> expectedFirst;
        List<MoveDraft> result;

        expectedFirst = List.of(
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e3")),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e4"))
        );
        result = MoveMaker.generatePawnMoves(chessGame.getBoard(), Coord.of("e2"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e4"))));


        expectedFirst = List.of(
                new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e6")),
                new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e5"))
        );
        result = MoveMaker.generatePawnMoves(chessGame.getBoard(), Coord.of("e7"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e6"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("d2"), Coord.of("d3")),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("d2"), Coord.of("d4"))
        );
        result = MoveMaker.generatePawnMoves(chessGame.getBoard(), Coord.of("d2"));
        System.out.println(chessGame.getBoard().getColorDisplay());
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("d2"), Coord.of("d4"))));


        expectedFirst = List.of(
                new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("d7"), Coord.of("d6")),
                new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("d7"), Coord.of("d5"))
        );
        result = MoveMaker.generatePawnMoves(chessGame.getBoard(), Coord.of("d7"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("d7"), Coord.of("d5"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e4"), Coord.of("e5")),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e4"), Coord.of("d5"))
        );
        result = MoveMaker.generatePawnMoves(chessGame.getBoard(), Coord.of("e4"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e4"), Coord.of("d5"))));


        expectedFirst = List.of(
                new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e6"), Coord.of("e5")),
                new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e6"), Coord.of("d5"))
        );
        result = MoveMaker.generatePawnMoves(chessGame.getBoard(), Coord.of("e6"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e6"), Coord.of("d5"))));
    }

    @Test
    void testSpecialPawn() {
        Board board;
        List<MoveDraft> expected;
        List<MoveDraft> result;

        board = new Board("k4r1r/6P1/8/8/8/8/8/K7 w - - 0 1");
        expected = List.of(
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("g8"), Promotion.PROMOTION_QUEEN),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("g8"), Promotion.PROMOTION_ROOK),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("g8"), Promotion.PROMOTION_BISHOP),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("g8"), Promotion.PROMOTION_KNIGHT),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("f8"), Promotion.PROMOTION_QUEEN),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("f8"), Promotion.PROMOTION_ROOK),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("f8"), Promotion.PROMOTION_BISHOP),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("f8"), Promotion.PROMOTION_KNIGHT),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("h8"), Promotion.PROMOTION_QUEEN),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("h8"), Promotion.PROMOTION_ROOK),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("h8"), Promotion.PROMOTION_BISHOP),
                new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("g7"), Coord.of("h8"), Promotion.PROMOTION_KNIGHT)
        );

        result = MoveMaker.generatePawnMoves(board, Coord.of("g7"));
        assertEquals(expected, result);
    }

    @Test
    void testStandardKnight() {
        List<MoveDraft> expectedFirst;
        List<MoveDraft> result;

        expectedFirst = List.of(
                new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("g1"), Coord.of("f3")),
                new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("g1"), Coord.of("h3"))
        );
        result = MoveMaker.generateKnightMoves(chessGame.getBoard(), Coord.of("g1"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("g1"), Coord.of("f3"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("g8"), Coord.of("f6")),
                new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("g8"), Coord.of("h6"))
        );
        result = MoveMaker.generateKnightMoves(chessGame.getBoard(), Coord.of("g8"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("g8"), Coord.of("h6"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("f3"), Coord.of("d4")),
                new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("f3"), Coord.of("e5")),
                new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("f3"), Coord.of("g1")),
                new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("f3"), Coord.of("g5")),
                new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("f3"), Coord.of("h4"))
        );
        result = MoveMaker.generateKnightMoves(chessGame.getBoard(), Coord.of("f3"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("f3"), Coord.of("e5"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("h6"), Coord.of("f5")),
                new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("h6"), Coord.of("g4")),
                new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("h6"), Coord.of("g8"))
        );
        result = MoveMaker.generateKnightMoves(chessGame.getBoard(), Coord.of("h6"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("h6"), Coord.of("f5"))));
    }

    @Test
    void testStandardBishop() {
        List<MoveDraft> expectedFirst;
        List<MoveDraft> result;

        expectedFirst = List.of();
        result = MoveMaker.generateBishopMoves(chessGame.getBoard(), Coord.of("c1"));
        assertEquals(expectedFirst, result);

        expectedFirst = List.of();
        result = MoveMaker.generateBishopMoves(chessGame.getBoard(), Coord.of("f1"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e4"))));

        expectedFirst = List.of();
        result = MoveMaker.generateBishopMoves(chessGame.getBoard(), Coord.of("c8"));
        assertEquals(expectedFirst, result);

        expectedFirst = List.of();
        result = MoveMaker.generateBishopMoves(chessGame.getBoard(), Coord.of("f8"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e5"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("e2")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("d3")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("c4")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("b5")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("a6"))
        );
        result = MoveMaker.generateBishopMoves(chessGame.getBoard(), Coord.of("f1"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("c4"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("f8"), Coord.of("e7")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("f8"), Coord.of("d6")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("f8"), Coord.of("c5")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("f8"), Coord.of("b4")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("f8"), Coord.of("a3"))
        );
        result = MoveMaker.generateBishopMoves(chessGame.getBoard(), Coord.of("f8"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("f8"), Coord.of("b4"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("d5")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("e6")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("f7")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("d3")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("e2")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("f1")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("b5")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("a6")),
                new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("b3"))
        );
        result = MoveMaker.generateBishopMoves(chessGame.getBoard(), Coord.of("c4"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c4"), Coord.of("b3"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("c5")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("d6")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("e7")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("f8")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("c3")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("d2")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("a5")),
                new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("a3"))
        );
        result = MoveMaker.generateBishopMoves(chessGame.getBoard(), Coord.of("b4"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("b4"), Coord.of("a3"))));
    }

    @Test
    void testStandardRook() {
        List<MoveDraft> expectedFirst;
        List<MoveDraft> result;

        expectedFirst = List.of();
        result = MoveMaker.generateRookMoves(chessGame.getBoard(), Coord.of("a1"));
        assertEquals(expectedFirst, result);

        expectedFirst = List.of();
        result = MoveMaker.generateRookMoves(chessGame.getBoard(), Coord.of("h1"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("a2"), Coord.of("a4"))));

        expectedFirst = List.of();
        result = MoveMaker.generateRookMoves(chessGame.getBoard(), Coord.of("a8"));
        assertEquals(expectedFirst, result);

        expectedFirst = List.of();
        result = MoveMaker.generateRookMoves(chessGame.getBoard(), Coord.of("h8"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("h7"), Coord.of("h5"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a1"), Coord.of("a2")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a1"), Coord.of("a3"))


        );
        result = MoveMaker.generateRookMoves(chessGame.getBoard(), Coord.of("a1"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a1"), Coord.of("a3"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h8"), Coord.of("h7")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h8"), Coord.of("h6"))


        );
        result = MoveMaker.generateRookMoves(chessGame.getBoard(), Coord.of("h8"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h8"), Coord.of("h6"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("b3")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("c3")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("d3")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("e3")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("f3")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("g3")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("h3")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("a2")),
                new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("a1"))
        );
        result = MoveMaker.generateRookMoves(chessGame.getBoard(), Coord.of("a3"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a3"), Coord.of("e3"))));

        expectedFirst = List.of(
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("g6")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("f6")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("e6")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("d6")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("c6")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("b6")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("a6")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("h7")),
                new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("h8"))
        );
        result = MoveMaker.generateRookMoves(chessGame.getBoard(), Coord.of("h6"));
        assertEquals(expectedFirst, result);
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("h6"), Coord.of("e6"))));
    }
}