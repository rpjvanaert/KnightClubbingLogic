package org.example.data.details;

public enum Promotion {
    PROMOTION_QUEEN("Q", "=Q", PieceType.QUEEN),
    PROMOTION_ROOK("R", "=R", PieceType.ROOK),
    PROMOTION_BISHOP("B", "=B", PieceType.BISHOP),
    PROMOTION_KNIGHT("N", "=N", PieceType.KNIGHT);

    public final String notation;
    public final String fullNotation;
    public final PieceType pieceType;

    Promotion(String notation, String fullNotation, PieceType pieceType) {
        this.notation = notation;
        this.fullNotation = fullNotation;
        this.pieceType = pieceType;
    }

    public static Promotion fromChar(char c) {
        return switch (Character.toUpperCase(c)) {
            case 'Q' -> PROMOTION_QUEEN;
            case 'R' -> PROMOTION_ROOK;
            case 'B' -> PROMOTION_BISHOP;
            case 'N' -> PROMOTION_KNIGHT;
            default -> throw new IllegalArgumentException("Invalid promotion character: " + c);
        };
    }

}
