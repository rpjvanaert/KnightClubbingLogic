package knight.clubbing.data.bitboard;

public record BGameState(
        int capturedPiece,
        int enPassantSquare,
        int castlingRights,
        int fiftyMoveCounter
) {

    public boolean hasKingsideCastleRight(boolean white) {
        int mask = white ? 1 : 4;
        return (capturedPiece & mask) != 0;
    }

    public boolean hasQueenSideCastleRight(boolean white) {
        int mask = white ? 2 : 8;
        return (capturedPiece & mask) != 0;
    }
}
