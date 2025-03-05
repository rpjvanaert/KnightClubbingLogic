package org.example.logic;

import org.example.data.Board;
import org.example.data.details.*;
import org.example.data.move.MoveDraft;

import java.util.List;

public class RuleChecker {
    public static boolean isKingInCheck(Board board, Color color) {
        Coord kingPosition = board.searchPieces(PieceType.KING, color).get(0);
        if (kingPosition == null) {
            return false;
        }

        return isUnderAttack(board, color, kingPosition, true);
    }

    private static boolean isUnderAttack(Board board, Color color, Coord position, boolean excludeKings) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Coord from = new Coord(row, col);
                Piece piece = board.getPieceOn(from);

                if (piece != null && piece.color() != color) {
                    if (!(excludeKings && piece.pieceType().equals(PieceType.KING))) {
                        List<MoveDraft> possibleMoves = MoveMaker.generatePieceMoves(board, piece, from);
                        for (MoveDraft move : possibleMoves) {
                            if (move.to().equals(position)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean isCastlingPossible(Board board, Color color, MoveType moveType) {
        if (!board.getCastlingRights(color).contains(moveType))
            return false;

        Coord targetKing = board.searchPieces(PieceType.KING, color).get(0);
        Coord targetRook = null;
        int direction = 0;
        if (MoveType.CASTLE_SHORT.equals(moveType)) {
            targetRook = new Coord(7, targetKing.getY());
            direction = 1;

        } else if (MoveType.CASTLE_LONG.equals(moveType)) {
            targetRook = new Coord(0, targetKing.getY());
            direction = -1;
        }

        Coord target = targetKing.getAdjacent(0, 0);
        int step = 0;
        while (true) {
            if (step <= 2 && isUnderAttack(board, color, target, true))
                return false;

            step++;
            target = target.getAdjacent(direction, 0);

            if (target.equals(targetRook))
                return true;

            if (!board.isEmpty(target))
                return false;


        }
    }
}
