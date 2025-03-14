package knight.clubbing.data.move;

import knight.clubbing.data.Board;
import knight.clubbing.data.details.*;

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

    public static MoveDraft fromUci(String uci, Board board) {
        if (uci.length() < 4 || uci.length() > 5) {
            throw new IllegalArgumentException("Invalid UCI format: " + uci);
        }

        Coord from = Coord.of(uci.substring(0, 2));
        Coord to = Coord.of(uci.substring(2, 4));
        Promotion promotion = (uci.length() == 5) ? Promotion.fromChar(uci.charAt(4)) : null;

        Piece piece = board.getPieceOn(from);
        if (piece == null) {
            throw new IllegalArgumentException("No piece found on: " + from);
        }

        return new MoveDraft(piece.pieceType(), piece.color(), from, to, promotion);
    }

}
