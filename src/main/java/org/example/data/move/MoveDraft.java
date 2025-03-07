package org.example.data.move;

import org.example.data.details.*;

public record MoveDraft(
        PieceType pieceType,
        Color color,
        Coord from,
        Coord to,
        Promotion promotion
) {

    public MoveDraft(PieceType pieceType, Color color, Coord from, Coord to) {
        this(pieceType, color, from, to, null);
    }

    public Piece getPiece() {
        return new Piece(this.pieceType, this.color);
    }
}
