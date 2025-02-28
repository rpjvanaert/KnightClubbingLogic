package org.example.data.move;

import org.example.data.details.*;

public record MoveDraft(
        MoveType type,
        PieceType pieceType,
        Color color,
        Coord from,
        Coord to,
        String special
) {

    public MoveDraft(PieceType pieceType, Color color, Coord from, Coord to) {
        this(MoveType.NORMAL, pieceType, color, from, to, "");
    }

    public Piece getPiece() {
        return new Piece(this.pieceType, this.color);
    }
}
