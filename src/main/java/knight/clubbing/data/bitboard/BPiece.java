package knight.clubbing.data.bitboard;

public class BPiece {

    public static final int none = 0;
    public static final int pawn = 1;
    public static final int knight = 2;
    public static final int bishop = 3;
    public static final int rook = 4;
    public static final int queen = 5;
    public static final int king = 6;

    public static final int white = 0;
    public static final int black = 8;

    public static final int whitePawn = pawn | white;
    public static final int whiteKnight = knight | white;
    public static final int whiteBishop = bishop | white;
    public static final int whiteRook = rook | white;
    public static final int whiteQueen = queen | white;
    public static final int whiteKing = king | white;

    public static final int blackPawn = pawn | black;
    public static final int blackKnight = knight | black;
    public static final int blackBishop = bishop | black;
    public static final int blackRook = rook | black;
    public static final int blackQueen = queen | black;
    public static final int blackKing = king | black;

    public static final int maxPieceIndex = blackKing;

    private static final int typeMask = 0b0111;
    private static final int colorMask = 0b1000;

    public static final int[] pieceIndices = {
            whitePawn, whiteKnight, whiteBishop, whiteRook, whiteQueen, whiteKing,
            blackPawn, blackKnight, blackBishop, blackRook, blackQueen, blackKing
    };

    public static int makePiece(int pieceType, int pieceColor) {
        return pieceType | pieceColor;
    }

    public static int makePiece(int pieceType, boolean isWhite) {
        return pieceType | (isWhite ? white : black);
    }

    public static boolean isWhite(int piece) {
        return getPieceColor(piece) == white && piece != none;
    }

    public static int getPieceColor(int piece) {
        return piece & colorMask;
    }

    public static int getPieceType(int piece) {
        return piece & typeMask;
    }

    public static boolean isOrthogonalMover(int piece) {
        int pieceType = getPieceType(piece);
        return pieceType == rook || pieceType == queen;
    }

    public static boolean isDiagonalMover(int piece) {
        int pieceType = getPieceType(piece);
        return pieceType == bishop || pieceType == queen;
    }

    public static boolean isSlidingMover(int piece) {
        int pieceType = getPieceType(piece);
        return pieceType == bishop || pieceType == rook || pieceType == queen;
    }

    public static char getChar(int piece) {
        int pieceType = getPieceType(piece);
        char symbol = switch (pieceType) {
            case pawn -> 'p';
            case knight -> 'n';
            case bishop -> 'b';
            case rook -> 'r';
            case queen -> 'q';
            case king -> 'k';
            default -> throw new IllegalArgumentException("Invalid piece: " + pieceType);
        };
        if (isWhite(piece)) {
            return Character.toUpperCase(symbol);
        }
        return symbol;
    }

}
