package org.example.logic;

import org.example.data.ChessException;
import org.example.MoveListener;
import org.example.data.*;
import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.Piece;
import org.example.data.details.PieceType;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {

    private final Board board;
    private final List<Move> moves;

    private final List<MoveListener> listeners;

    public ChessGame() {
        this.board = new Board();
        this.moves = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void addListener(MoveListener moveListener) {
        this.listeners.add(moveListener);
    }

    public void removeListener(MoveListener moveListener) {
        this.listeners.remove(moveListener);
    }

    private void notifyListeners(Move move) {
        this.listeners.forEach(listener -> listener.notify(move));
    }

    public boolean submitMove(MoveDraft moveDraft) {
        try {
            isPseudoLegal(moveDraft);
            Move move = getLegalMove(moveDraft);

            executeMove(move);
            notifyListeners(move);

        } catch (ChessException exception) {
            System.err.println(exception.getMessage());
            return false;
        }
        return true;
    }

    private void isPseudoLegal(MoveDraft move) throws ChessException {
        //TODO
        if (move.color() != board.getActive()) throw new ChessException("Not turn of given color");

        switch (move.type()) {
            case CASTLING -> {
                if (!isValidCastling(move)) throw new ChessException(("Not valid castling move"));
            }
            case PROMOTION -> {
                if (!isValidNormal(move) && !isValidPromotion(move)) throw new ChessException("Not valid promotion move");
            }
            case NORMAL -> {
                if (!isValidNormal(move)) throw new ChessException("Not valid move");
            }
        }
    }

    private boolean isValidPromotion(MoveDraft move) {
        if (!move.pieceType().equals(PieceType.PAWN)) return false;

        if (!isValidNormal(move)) return false;


        int rank = move.to().getY();
        Color color = move.color();
        int backRank = (color == Color.WHITE) ? 7 : 0;

        if (rank != backRank) return false;

        PieceType promotionPieceType = PieceType.fromFEN(move.special().charAt(0));

        return
                promotionPieceType != null
                && promotionPieceType != PieceType.PAWN
                && promotionPieceType != PieceType.KING;
    }

    private boolean isValidNormal(MoveDraft move) {
        return switch (move.pieceType()) {
            case KING -> isValidKingMove(move);
            case QUEEN -> isValidQueenMove(move);
            case ROOK -> isValidRookMove(move);
            case BISHOP -> isValidBishopMove(move);
            case KNIGHT -> isValidKnightMove(move);
            case PAWN -> isValidPawnMove(move);
        };
    }

    private boolean isValidPawnMove(MoveDraft move) {

        int direction = move.color() == Color.WHITE ? 1 : -1;
        int rankDiff = move.to().getY() - move.from().getY();
        int fileDiff = move.to().getX() - move.from().getX();

        if (rankDiff * direction <= 0) return false;

        if (fileDiff == 0) {

            if (board.getPieceOn(move.to()) != null) return false;

            if (rankDiff * direction > 1 && move.from().getY() != (move.color() == Color.WHITE ? 1 : 6)) return false;
            if (rankDiff > 2) return false;

            if (rankDiff == 2) {
                Coord intermediate = move.to().getAdjacent(0, -direction);

                // Check if intermediate is empty
                if (this.board.getPieceOn(intermediate) != null) return false;
            }
        } else {

            // Check if pawn is capturing diagonally

            // Pawn can only move a single file when capturing
            if (fileDiff != 1 || rankDiff != 1) return false;

            // Check if destination has an opponent piece
            Piece targetPiece = this.board.getPieceOn(move.to());

            if (move.special().equals("ep")) {
                //TODO en passant
            }

            if (targetPiece == null || targetPiece.color() == move.color()) return false;


        }

        return true;
    }

    private boolean isValidKnightMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        if (!((rankDiff == 2 && fileDiff == 1) || (rankDiff == 1 && fileDiff == 2))) return false;

        Piece target = board.getPieceOn(move.to());

        if (target != null && target.color() == move.color()) return false;

        return true;
    }

    private boolean isValidBishopMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        if (rankDiff != fileDiff) return false;

        int rankDirection = Integer.compare(move.to().getY(), move.from().getY());
        int fileDirection = Integer.compare(move.to().getX(), move.from().getX());

        int currentRank = move.from().getY() + rankDirection;
        int currentFile = move.from().getX() + fileDirection;

        while (currentFile != move.to().getX() || currentRank != move.to().getY()) {
            if (this.board.getPieceOn(new Coord(currentFile, currentRank)) != null) return false;

            currentFile += fileDirection;
            currentRank += rankDirection;
        }

        Piece target = board.getPieceOn(move.to());
        if (target != null && target.color() == move.color()) return false;

        return true;
    }

    private boolean isValidRookMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        if (!(rankDiff == 0 && fileDiff > 0) && !(rankDiff > 0 && fileDiff == 0)) return false;

        int rankDirection = Integer.compare(move.to().getY(), move.from().getY());
        int fileDirection = Integer.compare(move.to().getX(), move.from().getX());

        int currentRank = move.from().getY() + rankDirection;
        int currentFile = move.from().getX() + fileDirection;

        while (currentFile != move.to().getX() || currentRank != move.to().getY()) {
            if (this.board.getPieceOn(new Coord(currentFile, currentRank)) != null) return false;

            currentFile += fileDirection;
            currentRank += rankDirection;
        }

        Piece target = board.getPieceOn(move.to());
        if (target != null && target.color() == move.color()) return false;

        return true;
    }

    private boolean isValidQueenMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        boolean isRookMove = (rankDiff == 0 && fileDiff > 0) || (rankDiff > 0 && fileDiff == 0);
        boolean isBishopMove = (rankDiff == fileDiff);

        if (!isRookMove && !isBishopMove) return false;

        int rankDirection = Integer.compare(move.to().getY(), move.from().getY());
        int fileDirection = Integer.compare(move.to().getX(), move.from().getX());

        int currentRank = move.from().getY() + rankDirection;
        int currentFile = move.from().getX() + fileDirection;

        while (currentFile != move.to().getX() || currentRank != move.to().getY()) {
            if (board.getPieceOn(new Coord(currentFile, currentRank)) != null) return false;

            currentRank += rankDirection;
            currentFile += fileDirection;
        }

        Piece targetPiece = board.getPieceOn(move.to());
        if (targetPiece != null && targetPiece.color() == move.color()) return false;

        // All checks passed, the move is valid
        return true;
    }

    private boolean isValidKingMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        if (rankDiff > 1 || fileDiff > 1) return false;

        Piece target = board.getPieceOn(move.to());
        if (target != null && target.color() == move.color()) return false;

        return true;
    }

    private boolean isValidCastling(MoveDraft move) {
        String side = String.valueOf(move.special().charAt(0));
        if (move.color() == Color.WHITE) side = side.toUpperCase();

        return this.board.getCastlingRights().contains(side);
    }

    private Move getLegalMove(MoveDraft move) throws ChessException {
        //TODO do checks for king. That is; checks, checkmates, hard pins and stalemates.


        return new Move(
                "todo", // TODO
                move.type(),
                move.pieceType(),
                move.color(),
                move.from(),
                move.to(),
                move.special()
        );
    }

    private void executeMove(Move move) {
        switch (move.type()) {
            case NORMAL -> executeNormal(move);
            case CASTLING -> executeCastling(move);
            case PROMOTION -> executePromotion(move);
        }
    }

    private void executePromotion(Move move) {
    }

    private void executeCastling(Move move) {

    }

    private void executeNormal(Move move) {
        this.board.emptySquare(move.from());
        this.board.setPieceOn(move.getPiece(), move.to());
        this.moves.add(move);
        this.board.setActive(move.color().other());
        /*
        switch (move.pieceType()) {
            case PAWN -> movePawnNormal(move);
            case KING -> moveKingNormal(move);
            case QUEEN -> moveQueenNormal(move);
            case ROOK -> moveRookNormal(move);
            case BISHOP -> moveBishopNormal(move);
            case KNIGHT -> moveKnightNormal(move);
        }
         */
    }

    private void moveKnightNormal(Move move) {

    }

    private void moveBishopNormal(Move move) {
    }

    private void moveRookNormal(Move move) {
    }

    private void moveQueenNormal(Move move) {
    }

    private void moveKingNormal(Move move) {
    }

    private void movePawnNormal(Move move) {
        this.board.emptySquare(move.from());
        this.board.setPieceOn(move.getPiece(), move.to());
        this.moves.add(move);
        this.board.setActive(move.color().other());
    }
}
