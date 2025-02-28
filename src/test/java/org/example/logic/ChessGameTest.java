package org.example.logic;

import org.example.data.Board;
import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.PieceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessGameTest {

    private ChessGame chessGame;

    @BeforeEach
    void setChessGame() {
        this.chessGame = new ChessGame();
    }

    @Test
    void validateBoardInitialisation() {
        // Act
        Board board = chessGame.getBoard();

        // Assert
        assertEquals(PieceType.ROOK, board.getPieceOn(new Coord(0, 0)).pieceType());
        assertEquals(Color.WHITE, board.getPieceOn(new Coord(0, 0)).color());
        assertEquals(PieceType.KNIGHT, board.getPieceOn(new Coord(1, 0)).pieceType());
        assertEquals(Color.WHITE, board.getPieceOn(new Coord(1, 0)).color());
        assertEquals(PieceType.BISHOP, board.getPieceOn(new Coord(2, 0)).pieceType());
        assertEquals(Color.WHITE, board.getPieceOn(new Coord(2, 0)).color());
        assertEquals(PieceType.QUEEN, board.getPieceOn(new Coord(3, 0)).pieceType());
        assertEquals(Color.WHITE, board.getPieceOn(new Coord(3, 0)).color());
        assertEquals(PieceType.KING, board.getPieceOn(new Coord(4, 0)).pieceType());
        assertEquals(Color.WHITE, board.getPieceOn(new Coord(4, 0)).color());
        assertEquals(PieceType.BISHOP, board.getPieceOn(new Coord(5, 0)).pieceType());
        assertEquals(Color.WHITE, board.getPieceOn(new Coord(5, 0)).color());
        assertEquals(PieceType.KNIGHT, board.getPieceOn(new Coord(6, 0)).pieceType());
        assertEquals(Color.WHITE, board.getPieceOn(new Coord(6, 0)).color());
        assertEquals(PieceType.ROOK, board.getPieceOn(new Coord(7, 0)).pieceType());
        assertEquals(Color.WHITE, board.getPieceOn(new Coord(7, 0)).color());

        for (int col = 0; col < 8; col++) {
            assertEquals(PieceType.PAWN, board.getPieceOn(new Coord(col, 1)).pieceType());
            assertEquals(Color.WHITE, board.getPieceOn(new Coord(col, 1)).color());
        }

        assertEquals(PieceType.ROOK, board.getPieceOn(new Coord(0, 7)).pieceType());
        assertEquals(Color.BLACK, board.getPieceOn(new Coord(0, 7)).color());
        assertEquals(PieceType.KNIGHT, board.getPieceOn(new Coord(1, 7)).pieceType());
        assertEquals(Color.BLACK, board.getPieceOn(new Coord(1, 7)).color());
        assertEquals(PieceType.BISHOP, board.getPieceOn(new Coord(2, 7)).pieceType());
        assertEquals(Color.BLACK, board.getPieceOn(new Coord(2, 7)).color());
        assertEquals(PieceType.QUEEN, board.getPieceOn(new Coord(3, 7)).pieceType());
        assertEquals(Color.BLACK, board.getPieceOn(new Coord(3, 7)).color());
        assertEquals(PieceType.KING, board.getPieceOn(new Coord(4, 7)).pieceType());
        assertEquals(Color.BLACK, board.getPieceOn(new Coord(4, 7)).color());
        assertEquals(PieceType.BISHOP, board.getPieceOn(new Coord(5, 7)).pieceType());
        assertEquals(Color.BLACK, board.getPieceOn(new Coord(5, 7)).color());
        assertEquals(PieceType.KNIGHT, board.getPieceOn(new Coord(6, 7)).pieceType());
        assertEquals(Color.BLACK, board.getPieceOn(new Coord(6, 7)).color());
        assertEquals(PieceType.ROOK, board.getPieceOn(new Coord(7, 7)).pieceType());
        assertEquals(Color.BLACK, board.getPieceOn(new Coord(7, 7)).color());

        for (int col = 0; col < 8; col++) {
            assertEquals(PieceType.PAWN, board.getPieceOn(new Coord(col, 6)).pieceType());
            assertEquals(Color.BLACK, board.getPieceOn(new Coord(col, 6)).color());
        }

        for (int row = 2; row < 6; row++) {
            for (int col = 0; col < 8; col++) {
                assertNull(board.getPieceOn(new Coord(col, row)), "Expected empty square");
            }
        }
    }
}