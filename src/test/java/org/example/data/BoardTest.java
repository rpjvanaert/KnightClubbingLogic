package org.example.data;

import org.example.data.details.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setup() {
        this.board = new Board();
    }

    @Test
    void exportFEN() {
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", this.board.exportFEN());
    }

    @Test
    void getDisplay() {
        assertEquals("R  N  B  Q  K  B  N  R  \n" +
                        "P  P  P  P  P  P  P  P  \n" +
                        "-  -  -  -  -  -  -  -  \n" +
                        "-  -  -  -  -  -  -  -  \n" +
                        "-  -  -  -  -  -  -  -  \n" +
                        "-  -  -  -  -  -  -  -  \n" +
                        "p  p  p  p  p  p  p  p  \n" +
                        "r  n  b  q  k  b  n  r  \n",
                this.board.getDisplay());
    }

    @Test
    void getBoard() {
        Piece[][] actualBoard = board.getBoard();

        Piece[][] expectedBoard = {
                {new Piece(PieceType.ROOK, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.ROOK, Color.BLACK)},
                {new Piece(PieceType.KNIGHT, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.KNIGHT, Color.BLACK)},
                {new Piece(PieceType.BISHOP, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.BISHOP, Color.BLACK)},
                {new Piece(PieceType.QUEEN, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.QUEEN, Color.BLACK)},
                {new Piece(PieceType.KING, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.KING, Color.BLACK)},
                {new Piece(PieceType.BISHOP, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.BISHOP, Color.BLACK)},
                {new Piece(PieceType.KNIGHT, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.KNIGHT, Color.BLACK)},
                {new Piece(PieceType.ROOK, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.ROOK, Color.BLACK)}
        };

        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (expectedBoard[rank][file] == null) {
                    assertNull(actualBoard[file][rank]);
                    break;
                }
                assertEquals(expectedBoard[rank][file], actualBoard[file][rank]);
            }
        }
    }

    @Test
    void testImportFen() {
        String fen = "k4r1r/6P1/8/8/8/8/8/K7 w - - 0 1";
        Board board = new Board(fen);

        System.out.println(board.getDisplay());
        System.out.println(board.searchPieces(PieceType.KING, Color.WHITE));
        System.out.println(board.searchPieces(PieceType.KING, null));
        System.out.println(board.searchPieces(PieceType.ROOK, null));
        System.out.println(board.searchPieces(PieceType.PAWN, null));

        assertEquals(new Piece(PieceType.KING, Color.WHITE), board.getPieceOn(new Coord(0,0)));
        //assertEquals(new Piece(PieceType.PAWN, Color.WHITE), board.getPieceOn(new Coord(6,6)));
        assertEquals(new Piece(PieceType.KING, Color.BLACK), board.getPieceOn(new Coord(0,7)));
        assertEquals(new Piece(PieceType.ROOK, Color.BLACK), board.getPieceOn(new Coord(7,7)));
        assertEquals(new Piece(PieceType.ROOK, Color.BLACK), board.getPieceOn(new Coord(5,7)));
    }

    @Test
    void getPieceOn() {
        Coord coord = new Coord(1,7);
        Piece piece = new Piece(PieceType.KNIGHT, Color.BLACK);
        assertEquals(piece, board.getPieceOn(coord));
    }

    @Test
    void testRightSide_on_RightSide() {
        Coord coord = Coord.of("e1");
        Piece piece = new Piece(PieceType.KING, Color.WHITE);
        assertEquals(piece, board.getPieceOn(coord));
    }

    @Test
    void setPieceOn() {
        Coord coord = new Coord(3,3);
        Piece piece = new Piece(PieceType.KNIGHT, Color.WHITE);

        board.setPieceOn(piece, coord);

        assertEquals(piece, board.getPieceOn(coord));
    }

    @Test
    void countPieces() {
        int expectedPawnsWhite = 8;
        Piece pawnWhite = new Piece(PieceType.PAWN, Color.WHITE);

        int expectedRooksBlack = 2;
        Piece rookBlack = new Piece(PieceType.ROOK, Color.BLACK);

        int expectedKingsWhite = 1;
        Piece kingWhite = new Piece(PieceType.KING, Color.WHITE);

        assertEquals(expectedPawnsWhite, this.board.countPieces(pawnWhite));
        assertEquals(expectedRooksBlack, this.board.countPieces(rookBlack), this.board.getDisplay());
        assertEquals(expectedKingsWhite, this.board.countPieces(kingWhite));

        assertEquals(0 , this.board.countPieces(null));
        assertEquals(0, this.board.countPieces(new Piece(null, null)));
        assertEquals(0, this.board.countPieces(new Piece(PieceType.PAWN, null)));
        assertEquals(0, this.board.countPieces(new Piece(null, Color.WHITE)));
    }

    @Test
    void equals() {
        assertEquals(new Coord(0, 0), new Coord(0, 0));
        assertEquals(new Coord(2, 0), new Coord(2, 0));
        assertEquals(new Coord(0, 5), new Coord(0, 5));

        assertNotEquals(new Coord(0, 0), new Coord(0, 3));
        assertNotEquals(new Coord(0, 0), null);
        assertNotEquals(new Coord(0, 0), PieceType.PAWN);
    }

    @Test
    void searchPieces() {
        assertEquals(8, this.board.searchPieces(PieceType.PAWN, Color.WHITE).size());
        assertEquals(2, this.board.searchPieces(PieceType.ROOK, Color.WHITE).size());
        assertEquals(2, this.board.searchPieces(PieceType.KNIGHT, Color.WHITE).size());
        assertEquals(2, this.board.searchPieces(PieceType.BISHOP, Color.WHITE).size());
        assertEquals(1, this.board.searchPieces(PieceType.QUEEN, Color.WHITE).size());
        assertEquals(1, this.board.searchPieces(PieceType.KING, Color.WHITE).size());

        assertEquals(8, this.board.searchPieces(PieceType.PAWN, Color.BLACK).size());
        assertEquals(2, this.board.searchPieces(PieceType.ROOK, Color.BLACK).size());
        assertEquals(2, this.board.searchPieces(PieceType.KNIGHT, Color.BLACK).size());
        assertEquals(2, this.board.searchPieces(PieceType.BISHOP, Color.BLACK).size());
        assertEquals(1, this.board.searchPieces(PieceType.QUEEN, Color.BLACK).size());
        assertEquals(1, this.board.searchPieces(PieceType.KING, Color.BLACK).size());

        List<Coord> coords;
        coords = this.board.searchPieces(PieceType.PAWN, Color.BLACK);
        assertTrue(coords.contains(Coord.of('a', '7')));
        assertTrue(coords.contains(Coord.of('b', '7')));
        assertTrue(coords.contains(Coord.of('c', '7')));
        assertTrue(coords.contains(Coord.of('d', '7')));
        assertTrue(coords.contains(Coord.of('e', '7')));
        assertTrue(coords.contains(Coord.of('f', '7')));
        assertTrue(coords.contains(Coord.of('g', '7')));
        assertTrue(coords.contains(Coord.of('h', '7')));

        coords = this.board.searchPieces(PieceType.KNIGHT, Color.WHITE);
        assertTrue(coords.contains(Coord.of('b', '1')));
        assertTrue(coords.contains(Coord.of('g', '1')));

        coords = this.board.searchPieces(PieceType.QUEEN, Color.BLACK);
        assertTrue(coords.contains(Coord.of('d', '8')));

        coords = this.board.searchPieces(PieceType.KING, null);
        assertEquals(2, coords.size());
        assertTrue(coords.contains(Coord.of('e', '1')));
        assertTrue(coords.contains(Coord.of('e', '8')));

        coords = this.board.searchPieces(null, Color.WHITE);
        assertEquals(16, coords.size());
        assertTrue(coords.contains(Coord.of('a', '1')));
        assertTrue(coords.contains(Coord.of('b', '1')));
        assertTrue(coords.contains(Coord.of('c', '1')));
        assertTrue(coords.contains(Coord.of('d', '1')));
        assertTrue(coords.contains(Coord.of('e', '1')));
        assertTrue(coords.contains(Coord.of('f', '1')));
        assertTrue(coords.contains(Coord.of('g', '1')));
        assertTrue(coords.contains(Coord.of('h', '1')));
        assertTrue(coords.contains(Coord.of('a', '2')));
        assertTrue(coords.contains(Coord.of('b', '2')));
        assertTrue(coords.contains(Coord.of('c', '2')));
        assertTrue(coords.contains(Coord.of('d', '2')));
        assertTrue(coords.contains(Coord.of('e', '2')));
        assertTrue(coords.contains(Coord.of('f', '2')));
        assertTrue(coords.contains(Coord.of('g', '2')));
        assertTrue(coords.contains(Coord.of('h', '2')));
    }

    @Test
    void emptySquare() {
        Piece piece = new Piece(PieceType.ROOK, Color.WHITE);
        Coord coord = new Coord(0,0);

        assertEquals(piece, this.board.getPieceOn(coord));
        this.board.emptySquare(coord);
        assertNull(this.board.getPieceOn(coord));
    }

    @Test
    void getActive() {
        assertEquals(Color.WHITE, this.board.getActive());

        this.board.setActive(Color.BLACK);

        assertEquals(Color.BLACK, this.board.getActive());

    }

    @Test
    void getCastlingRights() {
        assertEquals("KQkq", this.board.getCastlingRights());

        this.board.setCastlingRights("kq");

        assertEquals("kq", this.board.getCastlingRights());
    }

    @Test
    void getEnPassantSquare() {
        assertNull(this.board.getEnPassantSquare());

        this.board.setEnPassantSquare(Coord.of("e3"));

        assertEquals(Coord.of("e3"), this.board.getEnPassantSquare());
    }

    @Test
    void getHalfmoveClock() {
        assertEquals(0, this.board.getHalfmoveClock());

        assertEquals(1, this.board.tickHalfmoveClock());

        assertEquals(2, this.board.tickHalfmoveClock());

        assertEquals(2, this.board.getHalfmoveClock());

        this.board.resetHalfmoveClock();

        assertEquals(1, this.board.getHalfmoveClock());
    }

    @Test
    void getFullmoveNumber() {
        assertEquals(1, this.board.getFullmoveNumber());

        assertEquals(2, this.board.tickFullmoveNumber());

        assertEquals(3, this.board.tickFullmoveNumber());

        assertEquals(3, this.board.getFullmoveNumber());
    }

    @Test
    void getCastlingRightsForWhite() {
        List<MoveType> expected = List.of(MoveType.CASTLE_SHORT, MoveType.CASTLE_LONG);

        List<MoveType> result = this.board.getCastlingRights(Color.WHITE);

        assertEquals(expected, result);
    }

    @Test
    void getCastlingRightsForBlack() {
        List<MoveType> expected = List.of(MoveType.CASTLE_SHORT, MoveType.CASTLE_LONG);

        List<MoveType> result = this.board.getCastlingRights(Color.BLACK);

        assertEquals(expected, result);
    }

    @Test
    void getCastlingRightsWithExclusion() {
        this.board.setCastlingRights("Kk");

        List<MoveType> expected = List.of(MoveType.CASTLE_SHORT);
        List<MoveType> result;

        result = this.board.getCastlingRights(Color.WHITE);
        assertEquals(expected, result);

        result = this.board.getCastlingRights(Color.BLACK);
        assertEquals(expected, result);
    }

    @Test
    void getCastlingRightsWithNothing() {
        this.board.setCastlingRights("-");
        List<MoveType> expected = List.of();

        List<MoveType> result = this.board.getCastlingRights(Color.WHITE);

        assertEquals(expected, result);
    }

    @Test
    void getCastlingRightsWithDifferentSides() {
        this.board.setCastlingRights("Kq");

        List<MoveType> expected;
        List<MoveType> result;

        expected = List.of(MoveType.CASTLE_SHORT);

        result = this.board.getCastlingRights(Color.WHITE);
        assertEquals(expected, result);

        expected = List.of(MoveType.CASTLE_LONG);

        result = this.board.getCastlingRights(Color.BLACK);
        assertEquals(expected, result);
    }
}