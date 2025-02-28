package org.example.logic;

import org.example.data.Board;
import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.Piece;
import org.example.data.details.PieceType;
import org.example.data.move.MoveDraft;

import java.util.List;

public class RuleChecker {
    public static boolean isKingInCheck(Board board, Color color) {
        Coord kingPosition = board.searchPieces(PieceType.KING, color).get(0);
        if (kingPosition == null) {
            return false;
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Coord from = new Coord(row, col);
                Piece piece = board.getPieceOn(from);

                if (piece != null && piece.color() != color) {
                    List<MoveDraft> possibleMoves = MoveMaker.generatePieceMoves(board, piece, from);
                    for (MoveDraft move : possibleMoves) {
                        if (move.to().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
