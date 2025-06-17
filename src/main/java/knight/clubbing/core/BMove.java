package knight.clubbing.core;

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
        return moveFlag() == promoteToQueenFlag;
    }

    public boolean isCastle() {
        return moveFlag() == castleFlag;
    }

    public boolean isPawnTwoUp() {
        return moveFlag() == pawnTwoUpFlag;
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

    public static BMove fromUci(String uci, BBoard board) {
        if (uci == null || uci.length() < 4) return BMove.nullMove();

        int from = BBoardHelper.stringCoordToIndex(uci.substring(0, 2));
        int to = BBoardHelper.stringCoordToIndex(uci.substring(2, 4));

        int flag = BMove.noFlag;

        int movePiece = board.pieceBoards[from];
        int targetPiece = board.pieceBoards[to];

        int movePieceType = BPiece.getPieceType(movePiece);
        boolean isPawn = movePieceType == BPiece.pawn;

        if (isPawn && uci.length() == 5) {
            char promo = uci.charAt(4);
            flag = switch (promo) {
                case 'q' -> BMove.promoteToQueenFlag;
                case 'r' -> BMove.promoteToRookFlag;
                case 'b' -> BMove.promoteToBishopFlag;
                case 'k' -> BMove.promoteToKnightFlag;
                default -> BMove.noFlag;
            };
        }

        if (isPawn && targetPiece == BPiece.none && BBoardHelper.fileIndex(from) != BBoardHelper.fileIndex(to))
            flag = BMove.enPassantCaptureFlag;

        if (isPawn && Math.abs(BBoardHelper.rankIndex(from) - BBoardHelper.rankIndex(to)) == 16)
            flag = BMove.pawnTwoUpFlag;

        if (movePieceType == BPiece.king && Math.abs(BBoardHelper.fileIndex(from) - BBoardHelper.fileIndex(to)) == 2)
            flag = BMove.castleFlag;

        return new BMove(from, to, flag);
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

    public String getUci() {
        return
                BBoardHelper.indexToStringCoord(startSquare()) +
                BBoardHelper.indexToStringCoord(targetSquare()) +
                (!isPromotion() ? "" :
                        switch (promotionPieceType()) {
                            case BPiece.queen -> "q";
                            case BPiece.bishop -> "b";
                            case BPiece.rook -> "r";
                            case BPiece.knight -> "n";
                            default -> "";
                        }
                );
    }
}
