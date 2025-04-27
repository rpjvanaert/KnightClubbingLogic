package knight.clubbing.data.bitboard;

public class BGameState {

    private int capturedPiece;
    private int enPassantFile;
    private int castlingRights;
    private int fiftyMoveCounter;

    public static final int clearWhiteKingsideMask = 0b1110;
    public static final int clearWhiteQueensideMask = 0b1101;
    public static final int clearBlackKingsideMask = 0b1011;
    public static final int clearBlackQueensideMask = 0b0111;

    public BGameState(int capturedPiece, int enPassantFile, int castlingRights, int fiftyMoveCounter) {
        this.capturedPiece = capturedPiece;
        this.enPassantFile = enPassantFile;
        this.castlingRights = castlingRights;
        this.fiftyMoveCounter = fiftyMoveCounter;
    }

    public boolean hasKingsideCastleRight(boolean white) {
        int mask = white ? 1 : 4;
        return (capturedPiece & mask) != 0;
    }

    public boolean hasQueenSideCastleRight(boolean white) {
        int mask = white ? 2 : 8;
        return (capturedPiece & mask) != 0;
    }

    public int getCapturedPiece() {
        return capturedPiece;
    }

    public void setCapturedPiece(int capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public int getEnPassantFile() {
        return enPassantFile;
    }

    public void setEnpassantFile(int enPassantFile) {
        this.enPassantFile = enPassantFile;
    }

    public int getCastlingRights() {
        return castlingRights;
    }

    public void setCastlingRights(int castlingRights) {
        this.castlingRights = castlingRights;
    }

    public int getFiftyMoveCounter() {
        return fiftyMoveCounter;
    }

    public void setFiftyMoveCounter(int fiftyMoveCounter) {
        this.fiftyMoveCounter = fiftyMoveCounter;
    }
}
