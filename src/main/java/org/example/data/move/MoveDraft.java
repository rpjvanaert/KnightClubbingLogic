package org.example.data.move;

import org.example.data.details.*;

public record MoveDraft(
        PieceType pieceType,
        Color color,
        Coord from,
        Coord to,
        MoveType type
) {

    public MoveDraft(PieceType pieceType, Color color, Coord from, Coord to) {
        this(pieceType, color, from, to, MoveType.NORMAL);
    }

    public Piece getPiece() {
        return new Piece(this.pieceType, this.color);
    }
}
