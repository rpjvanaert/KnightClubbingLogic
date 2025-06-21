package knight.clubbing.core;

import java.nio.ByteBuffer;
import java.util.Random;

public class BZobrist {

    private static long[][] piecesArray;
    private static long[] castlingRights;
    private static long[] enPassantFile;
    private static long sideToMove;

    static {
        piecesArray = new long[BPiece.maxPieceIndex + 1][64];
        castlingRights  = new long[16];
        enPassantFile = new long[9];

        final int seed = 29426028;
        Random random = new Random(seed);

        for (int squareIndex = 0; squareIndex < 64; squareIndex++) {

            for (int piece : BPiece.pieceIndices) {
                piecesArray[piece][squareIndex] = Random64BitNumber(random);
            }
        }

        for (int i = 0; i < castlingRights.length; i++) {
            castlingRights[i] = Random64BitNumber(random);
        }

        for (int i = 0; i < enPassantFile.length; i++) {
            enPassantFile[i] = (i == 0) ? 0 : Random64BitNumber(random);
        }

        sideToMove = Random64BitNumber(random);
    }

    public static long CalculateZobristKey(BBoard board) {
        long zobristKey = 0;

        for (int squareIndex = 0; squareIndex < 64; squareIndex++) {
            int piece = board.pieceBoards[squareIndex];

            if (BPiece.getPieceType(piece) != BPiece.none) {
                zobristKey ^= piecesArray[piece][squareIndex];
            }
        }

        zobristKey ^= enPassantFile[board.state.getEnPassantFile()];

        if (!board.isWhiteToMove) {
            zobristKey ^= sideToMove;
        }

        zobristKey ^= castlingRights[board.state.getCastlingRights()];

        int epFile = board.state.getEnPassantFile();
        if (epFile >= 0 && epFile < enPassantFile.length) {
            zobristKey ^= enPassantFile[epFile];
        }


        return zobristKey;
    }

    private static long Random64BitNumber(Random random) {
        byte[] buffer = new byte[8];
        random.nextBytes(buffer);
        return ByteBuffer.wrap(buffer).getLong();
    }

    public static long[][] getPiecesArray() {
        return piecesArray;
    }

    public static long[] getCastlingRights() {
        return castlingRights;
    }

    public static long[] getEnPassantFile() {
        return enPassantFile;
    }

    public static long getSideToMove() {
        return sideToMove;
    }
}
