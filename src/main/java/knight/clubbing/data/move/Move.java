package knight.clubbing.data.move;

import knight.clubbing.data.details.*;

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
