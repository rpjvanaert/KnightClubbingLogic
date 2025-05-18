package knight.clubbing.data.bitboard.core;

public record BMove(short value) {

    public static final int noFlag = 0b0000;
    public static final int enPassantCaptureFlag = 0b0001;
    public static final int castleFlag = 0b0010;
    public static final int pawnTwoUpFlag = 0b0011;

    public static final int promoteToQueenFlag = 0b0100;
    public static final int promoteToKnightFlag = 0b0101;
    public static final int promoteToRookFlag = 0b0110;
    public static final int promoteToBishopFlag = 0b0111;

    private static final int startSquareMask = 0b0000000000111111;
    private static final int targetSquareMask = 0b0000111111000000;
    private static final int flagMask = 0b1111000000000000;

    public BMove(int startSquare, int targetSquare) {
        this((short) (startSquare | (targetSquare << 6)));
    }

    public BMove(int startSquare, int targetSquare, int flag) {
        this((short) (startSquare | (targetSquare << 6) | (flag << 12)));
    }

    public boolean isNull() {
        return value == 0;
    }

    public int startSquare() {
        return value & startSquareMask;
    }

    public int targetSquare() {
        return (value & targetSquareMask) >>> 6;
    }

    public int moveFlag() {
        return value >>> 12;
    }

    public boolean isPromotion() {
        return moveFlag() >= promoteToQueenFlag;
    }

    public int promotionPieceType() {
        return switch (moveFlag()) {
            case promoteToRookFlag -> BPiece.rook;
            case promoteToKnightFlag -> BPiece.knight;
            case promoteToBishopFlag -> BPiece.bishop;
            case promoteToQueenFlag -> BPiece.queen;
            default -> BPiece.none;
        };
    }

    public static BMove nullMove() {
        return new BMove((short) 0);
    }

    public String moveFlagName() {
        return switch (moveFlag()) {
            case noFlag -> "-";
            case enPassantCaptureFlag -> "en passant";
            case castleFlag -> "castle";
            case pawnTwoUpFlag -> "pawn two up";
            case promoteToQueenFlag -> "promote to queen";
            case promoteToKnightFlag -> "promote to knight";
            case promoteToRookFlag -> "promote to rook";
            case promoteToBishopFlag -> "promote to bishop";
            default -> "unknown";
        };
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BMove move = (BMove) o;
        return value == move.value;
    }


    @Override
    public String toString() {
        return "BMove[" +
                "start=" + BBoardHelper.indexToStringCoord(startSquare()) +
                ", target=" + BBoardHelper.indexToStringCoord(targetSquare()) +
                ", flag=" + moveFlagName() +
                ']';
    }

    @Override
    public int hashCode() {
        return Short.hashCode(value);
    }
}
