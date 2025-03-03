package org.example.data.details;

public enum MoveType {
    NORMAL("", null, null),

    EN_PASSANT("", null, null),
    CASTLE_SHORT("O-O", null, null),
    CASTLE_LONG("O-O-O", null, null),
    PROMOTION_QUEEN("Q", "=Q", PieceType.QUEEN),
    PROMOTION_ROOK("R", "=R", PieceType.ROOK),
    PROMOTION_BISHOP("B", "=B", PieceType.BISHOP),
    PROMOTION_KNIGHT("N", "=N", PieceType.KNIGHT);

    public final String notation;
    private final String fullNotation;
    public final PieceType pieceType;
    MoveType(String notation, String fullNotation, PieceType pieceType) {
        this.notation = notation;
        this.fullNotation = fullNotation;
        this.pieceType = pieceType;
    }

    public String fullNotation () {
        if (fullNotation == null)
            return notation;

        return fullNotation;
    }

    public boolean isPromotion() {
        return fullNotation != null && pieceType != null;
    }
}
