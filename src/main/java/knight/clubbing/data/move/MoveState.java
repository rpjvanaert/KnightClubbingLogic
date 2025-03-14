package knight.clubbing.data.move;

import knight.clubbing.data.details.Coord;
import knight.clubbing.data.details.Piece;
import knight.clubbing.data.details.Promotion;
import knight.clubbing.logic.ChessGame;

public class MoveState {
    public final Coord from;
    public final Coord to;
    public final Piece movedPiece;
    public final Piece capturedPiece;
    public final String previousCastlingRights;
    public final Coord previousEnPassantTarget;
    public final int previousHalfMoveClock;
    public final int previousFullMoveNumber;
    public final Promotion promotion;

    public MoveState(ChessGame game, Move move) {
        this.from = move.from();
        this.to = move.to();
        this.movedPiece = game.getBoard().getPieceOn(from);
        this.capturedPiece = game.getBoard().getPieceOn(to);
        this.previousCastlingRights = game.getBoard().getCastlingRights();
        this.previousEnPassantTarget = game.getBoard().getEnPassantSquare();
        this.previousHalfMoveClock = game.getBoard().getHalfmoveClock();
        this.previousFullMoveNumber = game.getBoard().getFullmoveNumber();
        this.promotion = move.promotion();
    }

    public MoveState(Coord from, Coord to, Piece movedPiece, Piece capturedPiece, String previousCastlingRights, Coord previousEnPassantTarget, int previousHalfMoveClock, int previousFullMoveNumber, Promotion promotion) {
        this.from = from;
        this.to = to;
        this.movedPiece = movedPiece;
        this.capturedPiece = capturedPiece;
        this.previousCastlingRights = previousCastlingRights;
        this.previousEnPassantTarget = previousEnPassantTarget;
        this.previousHalfMoveClock = previousHalfMoveClock;
        this.previousFullMoveNumber = previousFullMoveNumber;
        this.promotion = promotion;
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

