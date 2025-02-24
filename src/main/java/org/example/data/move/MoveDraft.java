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

    public Piece getPiece() {
        return new Piece(this.pieceType, this.color);
    }
}
