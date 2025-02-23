package org.example.data;

import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.Piece;
import org.example.data.details.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertEquals("r  n  b  q  k  b  n  r  \n" +
                "p  p  p  p  p  p  p  p  \n" +
                "-  -  -  -  -  -  -  -  \n" +
                "-  -  -  -  -  -  -  -  \n" +
                "-  -  -  -  -  -  -  -  \n" +
                "-  -  -  -  -  -  -  -  \n" +
                "P  P  P  P  P  P  P  P  \n" +
                "R  N  B  Q  K  B  N  R  \n",
                this.board.getDisplay());
    }

    @Test
    void getBoard() {
        Piece[][] actualBoard = board.getBoard();

        // Define the expected piece layout based on the FEN
        Piece[][] expectedBoard = {
                // Ranks 8 to 1 (from top to bottom)
                {new Piece(PieceType.ROOK, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.ROOK, Color.BLACK)},
                {new Piece(PieceType.KNIGHT, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.KNIGHT, Color.BLACK)},
                {new Piece(PieceType.BISHOP, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.BISHOP, Color.BLACK)},
                {new Piece(PieceType.QUEEN, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.QUEEN, Color.BLACK)},
                {new Piece(PieceType.KING, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.KING, Color.BLACK)},
                {new Piece(PieceType.BISHOP, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.BISHOP, Color.BLACK)},
                {new Piece(PieceType.KNIGHT, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.KNIGHT, Color.BLACK)},
                {new Piece(PieceType.ROOK, Color.WHITE), new Piece(PieceType.PAWN, Color.WHITE), null, null, null, null, new Piece(PieceType.PAWN, Color.BLACK), new Piece(PieceType.ROOK, Color.BLACK)}
        };

        // Check each position on the board against the expected board layout
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
    void getPieceOn() {
        Coord coord = new Coord(1,0);
        Piece piece = new Piece(PieceType.KNIGHT, Color.WHITE);
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
        assertEquals("-", this.board.getEnPassantSquare());

        this.board.setEnPassantSquare("e3");

        assertEquals("e3", this.board.getEnPassantSquare());
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
}