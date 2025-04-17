package knight.clubbing.data.bitboard;

import knight.clubbing.data.details.Color;

public class BBoard {
    private long[] bitboards;
    private int[] pieceTypeBoard;
    private long whiteBoard;
    private long blackBoard;

    public BGameState state;

    public boolean isWhiteToMove;
    public Color moveColor;

    public BBoard() {
        bitboards = new long[12];
        pieceTypeBoard = new int[64];
        whiteBoard = 0L;
        blackBoard = 0L;
    }

    public void makeMove(BMove move) {
        this.makeMove(move, false);
    }

    public void makeMove(BMove move, boolean inSearch) {
        int startSquare = move.startSquare();
        int targetSquare = move.targetSquare();
        int moveFlag = move.moveFlag();
        boolean isPromotion = move.isPromotion();
        boolean isEnPassant = moveFlag == BMove.EnPassantCaptureFlag;

        int movedPiece = pieceTypeBoard[startSquare];
        int movedPieceType = BPiece.getPieceType(movedPiece);


        // Update bitboard of moved piece (pawn promotion is done later)

        // handle captures

        // handle king
        //  handle castling
        //      update rook position

        // handle promotion

        //handle pawn moved two forwards, mark en-passant flag

        // update castling rights

        // update zobrist key with new piece position and side to move

        // change side to move, up ply count and fiftyMoveCounter

        // update extra bitboards

        // reset and clear fiftyMoveCounter and 3-fold-repitition history if pawn move or capture
    }

    public void set(int pieceIndex, int squareIndex) {
        checkPieceIndex(pieceIndex);
        checkSquareIndex(squareIndex);
        bitboards[pieceIndex] |= 1L << squareIndex;
    }

    public void clear(int pieceIndex, int squareIndex) {
        checkPieceIndex(pieceIndex);
        checkSquareIndex(squareIndex);
        bitboards[pieceIndex] &= ~(1L << squareIndex);
    }

    public boolean get(int pieceIndex, int squareIndex) {
        checkPieceIndex(pieceIndex);
        checkSquareIndex(squareIndex);
        return (bitboards[pieceIndex] & (1L << squareIndex)) != 0;
    }

    public long getBitboard(int pieceIndex) {
        return bitboards[pieceIndex];
    }

    public void move(int pieceIndex, int fromIndex, int toIndex) {
        if (this.get(pieceIndex, fromIndex)) {
            this.clear(pieceIndex, fromIndex);
            this.set(pieceIndex, toIndex);
        } else
            throw new IllegalArgumentException("bitboard is empty there");
    }

    private static void checkPieceIndex(int pieceIndex) {
        if (pieceIndex < 0 || pieceIndex >= 12)
            throw new IllegalArgumentException("pieceIndex out of range: " + pieceIndex);
    }

    private static void checkSquareIndex(int squareIndex) {
        if (squareIndex < 0 || squareIndex >= 64)
            throw new IllegalArgumentException("squareIndex out of range: " + squareIndex);
    }
}
