package org.example.data.move;

import org.example.data.details.*;

public record Move (
        String notation,
        PieceType pieceType,
        Color color,
        Coord from,
        Coord to,
        Promotion promotion
) {

    public Piece getPiece() {
        return new Piece(this.pieceType, this.color);
    }
}
