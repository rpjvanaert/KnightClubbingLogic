package org.example.data.details;

public enum PieceType {
    KING('K'),
    QUEEN('Q'),
    ROOK('R'),
    BISHOP('B'),
    KNIGHT('N'),
    PAWN('P');

    private char fen;

    PieceType(char fen) {
        this.fen = fen;
    }

    public char fen() {
        return fen;
    }

    public static PieceType fromFEN(char fen) {
        for (PieceType type : PieceType.values()) {
            if (type.fen == Character.toUpperCase(fen)) {
                return type;
            }
        }
        return null;
    }
}