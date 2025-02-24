package org.example.data.move;

import org.example.data.details.Coord;
import org.example.data.details.Piece;
import org.example.logic.ChessGame;

public class MoveState {
    public final Coord from;
    public final Coord to;
    public final Piece movedPiece;
    public final Piece capturedPiece;
    public final String previousCastlingRights;
    public final Coord previousEnPassantTarget;
    public final int previousHalfMoveClock;
    public final int previousFullMoveNumber;

    public MoveState(ChessGame game, Move move) {
        this.from = move.from();
        this.to = move.to();
        this.movedPiece = game.getBoard().getPieceOn(from);
        this.capturedPiece = game.getBoard().getPieceOn(to);
        this.previousCastlingRights = game.getBoard().getCastlingRights();
        this.previousEnPassantTarget = game.getBoard().getEnPassantSquare();
        this.previousHalfMoveClock = game.getBoard().getHalfmoveClock();
        this.previousFullMoveNumber = game.getBoard().getFullmoveNumber();
    }
}

