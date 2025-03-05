package org.example.data.move;

import org.example.data.details.Coord;
import org.example.data.details.MoveType;
import org.example.data.details.Piece;
import org.example.logic.ChessGame;

public class MoveState {
    public final MoveType moveType;
    public final Coord from;
    public final Coord to;
    public final Piece movedPiece;
    public final Piece capturedPiece;
    public final String previousCastlingRights;
    public final Coord previousEnPassantTarget;
    public final int previousHalfMoveClock;
    public final int previousFullMoveNumber;

    public MoveState(ChessGame game, Move move) {
        this.moveType = move.type();
        this.from = move.from();
        this.to = move.to();
        this.movedPiece = game.getBoard().getPieceOn(from);
        this.capturedPiece = game.getBoard().getPieceOn(to);
        this.previousCastlingRights = game.getBoard().getCastlingRights();
        this.previousEnPassantTarget = game.getBoard().getEnPassantSquare();
        this.previousHalfMoveClock = game.getBoard().getHalfmoveClock();
        this.previousFullMoveNumber = game.getBoard().getFullmoveNumber();
    }

    public MoveState(MoveType moveType, Coord from, Coord to, Piece movedPiece, Piece capturedPiece, String previousCastlingRights, Coord previousEnPassantTarget, int previousHalfMoveClock, int previousFullMoveNumber) {
        this.moveType = moveType;
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.previousCastlingRights = previousCastlingRights;
        this.previousEnPassantTarget = previousEnPassantTarget;
        this.previousHalfMoveClock = previousHalfMoveClock;
        this.previousFullMoveNumber = previousFullMoveNumber;
    }

    @Override
    public String toString() {
        return "MoveState{" +
                "from=" + from +
                ", to=" + to +
                ", movedPiece=" + movedPiece +
                ", capturedPiece=" + capturedPiece +
                ", previousCastlingRights='" + previousCastlingRights + '\'' +
                ", previousEnPassantTarget=" + previousEnPassantTarget +
                ", previousHalfMoveClock=" + previousHalfMoveClock +
                ", previousFullMoveNumber=" + previousFullMoveNumber +
                '}';
    }
}

