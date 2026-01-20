package rpjvanaert.movegen;

import rpjvanaert.core.BBoardHelper;

public class MoveUtility {

    public static final long FileA = 0x0101010101010101L;

    public static final long Rank1 = 0xFFL;
    public static final long Rank2 = Rank1 << 8;
    public static final long Rank3 = Rank2 << 8;
    public static final long Rank4 = Rank3 << 8;
    public static final long Rank5 = Rank4 << 8;
    public static final long Rank6 = Rank5 << 8;
    public static final long Rank7 = Rank6 << 8;
    public static final long Rank8 = Rank7 << 8;

    public static final long notAFile = ~FileA;
    public static final long notHFile = ~(FileA << 7);

    public static final long[] KnightAttacks = new long[64];
    public static final long[] KingMoves = new long[64];
    public static final long[] WhitePawnAttacks = new long[64];
    public static final long[] BlackPawnAttacks = new long[64];

    public static final long WHITE_KINGSIDE_MASK = 1L << BBoardHelper.f1 | 1L << BBoardHelper.g1;
    public static final long BLACK_KINGSIDE_MASK = 1L << BBoardHelper.f8 | 1L << BBoardHelper.g8;

    public static final long WHITE_QUEENSIDE_MASK2 = 1L << BBoardHelper.d1 | 1L << BBoardHelper.c1;
    public static final long BLACK_QUEENSIDE_MASK2 = 1L << BBoardHelper.d8 | 1L << BBoardHelper.c8;

    public static final long WHITE_QUEENSIDE_MASK = WHITE_QUEENSIDE_MASK2 | 1L << BBoardHelper.b1;
    public static final long BLACK_QUEENSIDE_MASK = BLACK_QUEENSIDE_MASK2 | 1L << BBoardHelper.b8;

    private MoveUtility() {} // Prevent instantiation

    public static boolean containsSquare(long bitboard, int square) {
        return ((bitboard >> square) & 1L) != 0;
    }

    public static long pawnAttacks(long pawnBitboard, boolean isWhite) {
        if (isWhite) {
            return ((pawnBitboard << 9) & notAFile) | ((pawnBitboard << 7) & notHFile);
        } else {
            return ((pawnBitboard >>> 7) & notAFile) | ((pawnBitboard >>> 9) & notHFile);
        }
    }

    public static long shift(long bitboard, int numSquaresToShift) {
        return numSquaresToShift > 0
                ? bitboard << numSquaresToShift
                : bitboard >>> -numSquaresToShift;
    }

    static {
        int[][] orthoDir = { {-1, 0}, {0, 1}, {1, 0}, {0, -1} };
        int[][] diagDir = { {-1, -1}, {-1, 1}, {1, 1}, {1, -1} };
        int[][] knightJumps = {
                {-2, -1}, {-2, 1}, {-1, 2}, {1, 2},
                {2, 1}, {2, -1}, {1, -2}, {-1, -2}
        };

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                processSquare(x, y, orthoDir, diagDir, knightJumps);
            }
        }
    }

    private static void processSquare(int x, int y, int[][] orthoDir, int[][] diagDir, int[][] knightJumps) {
        int squareIndex = y * 8 + x;

        for (int[] dir : orthoDir) {
            int dx = dir[0], dy = dir[1];
            int nx = x + dx, ny = y + dy;
            if (validSquareIndex(nx, ny)) {
                KingMoves[squareIndex] |= 1L << (ny * 8 + nx);
            }
        }

        for (int[] dir : diagDir) {
            int dx = dir[0], dy = dir[1];
            int nx = x + dx, ny = y + dy;
            if (validSquareIndex(nx, ny)) {
                KingMoves[squareIndex] |= 1L << (ny * 8 + nx);
            }
        }

        for (int[] jump : knightJumps) {
            int nx = x + jump[0], ny = y + jump[1];
            if (validSquareIndex(nx, ny)) {
                KnightAttacks[squareIndex] |= 1L << (ny * 8 + nx);
            }
        }

        if (validSquareIndex(x + 1, y + 1)) {
            WhitePawnAttacks[squareIndex] |= 1L << ((y + 1) * 8 + (x + 1));
        }
        if (validSquareIndex(x - 1, y + 1)) {
            WhitePawnAttacks[squareIndex] |= 1L << ((y + 1) * 8 + (x - 1));
        }

        if (validSquareIndex(x + 1, y - 1)) {
            BlackPawnAttacks[squareIndex] |= 1L << ((y - 1) * 8 + (x + 1));
        }
        if (validSquareIndex(x - 1, y - 1)) {
            BlackPawnAttacks[squareIndex] |= 1L << ((y - 1) * 8 + (x - 1));
        }
    }

    private static boolean validSquareIndex(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }
}
