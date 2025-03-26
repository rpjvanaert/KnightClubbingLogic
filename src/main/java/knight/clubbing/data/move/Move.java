package knight.clubbing.data.move;

import knight.clubbing.data.details.*;

public record Move (
        PieceType pieceType,
        Color color,
        Coord from,
        Coord to,
        Promotion promotion
) {

    public Piece getPiece() {
        return new Piece(this.pieceType, this.color);
    }

    public String getUci() {
        return from.getNotation() + to.getNotation() + (promotion == null ? "" : promotion.notation.toLowerCase());
    }
}
