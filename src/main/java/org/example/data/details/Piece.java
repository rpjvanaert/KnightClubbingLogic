package org.example.data.details;

public record Piece(PieceType pieceType, Color color) {

    public char getChar() {
        return color.equals(Color.WHITE) ? pieceType.fen() : Character.toLowerCase(pieceType.fen());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return pieceType == piece.pieceType && color == piece.color;
    }

}
