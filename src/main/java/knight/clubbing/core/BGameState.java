package knight.clubbing.core;

public class BGameState {

    private int capturedPiece;
    private int enPassantFile;
    private int castlingRights;
    private int fiftyMoveCounter;
    private long zobristKey;

    public static final int clearWhiteKingsideMask = 0b1110;
    public static final int clearWhiteQueensideMask = 0b1101;
    public static final int clearBlackKingsideMask = 0b1011;
    public static final int clearBlackQueensideMask = 0b0111;

    public BGameState(int capturedPiece, int enPassantFile, int castlingRights, int fiftyMoveCounter, long zobristKey) {
        this.capturedPiece = capturedPiece;
        this.enPassantFile = enPassantFile;
        this.castlingRights = castlingRights;
        this.fiftyMoveCounter = fiftyMoveCounter;
        this.zobristKey = zobristKey;
    }

    public BGameState() {
        this.capturedPiece = 0;
        this.enPassantFile = 0;
        this.castlingRights = 0;
        this.fiftyMoveCounter = 0;
    }

    public BGameState(BGameState other) {
        this.capturedPiece = other.capturedPiece;
        this.enPassantFile = other.enPassantFile;
        this.castlingRights = other.castlingRights;
        this.fiftyMoveCounter = other.fiftyMoveCounter;
        this.zobristKey = other.zobristKey;
    }


    public boolean hasKingSideCastleRight(boolean white) {
        int mask = white ? 1 : 4;
        return (castlingRights & mask) != 0;
    }

    public boolean hasQueenSideCastleRight(boolean white) {
        int mask = white ? 2 : 8;
        return (castlingRights & mask) != 0;
    }

    public int getCapturedPiece() {
        return capturedPiece;
    }

    public int getEnPassantFile() {
        return enPassantFile;
    }

    public int getCastlingRights() {
        return castlingRights;
    }

    public int getFiftyMoveCounter() {
        return fiftyMoveCounter;
    }

    public long getZobristKey() {
        return zobristKey;
    }
}
