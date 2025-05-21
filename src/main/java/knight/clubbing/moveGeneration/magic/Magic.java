package knight.clubbing.moveGeneration.magic;

import static knight.clubbing.moveGeneration.magic.PrecomputedMagics.*;

public final class Magic {

    public static final long[] RookMask = new long[64];
    public static final long[] BishopMask = new long[64];

    public static final long[][] RookAttacks = new long[64][];
    public static final long[][] BishopAttacks = new long[64][];

    private Magic() {
        // Prevent instantiation
    }

    static {
        for (int square = 0; square < 64; square++) {
            RookMask[square] = MagicHelper.createMovementMask(square, true);
            BishopMask[square] = MagicHelper.createMovementMask(square, false);
        }

        for (int i = 0; i < 64; i++) {
            RookAttacks[i] = createTable(i, true, ROOK_MAGICS[i], ROOK_SHIFTS[i]);
            BishopAttacks[i] = createTable(i, false, BISHOP_MAGICS[i], BISHOP_SHIFTS[i]);
        }
    }

    public static long getSliderAttacks(int square, long blockers, boolean ortho) {
        return ortho ? getRookAttacks(square, blockers) : getBishopAttacks(square, blockers);
    }

    public static long getRookAttacks(int square, long blockers) {
        long key = ((blockers & RookMask[square]) * ROOK_MAGICS[square]) >>> ROOK_SHIFTS[square];
        return RookAttacks[square][(int) key];
    }

    public static long getBishopAttacks(int square, long blockers) {
        long key = ((blockers & BishopMask[square]) * BISHOP_MAGICS[square]) >>> BISHOP_SHIFTS[square];
        return BishopAttacks[square][(int) key];
    }

    public static long getRookIndex(int square, long blockers) {
        long masked = blockers & Magic.RookMask[square];
        return (masked * PrecomputedMagics.ROOK_MAGICS[square]) >>> PrecomputedMagics.ROOK_SHIFTS[square];
    }

    public static long getBishopIndex(int square, long blockers) {
        long masked = blockers & Magic.BishopMask[square];
        return (masked * PrecomputedMagics.BISHOP_MAGICS[square]) >>> PrecomputedMagics.BISHOP_SHIFTS[square];
    }


    private static long[] createTable(int square, boolean isRook, long magic, int shift) {
        int numBits = 64 - shift;
        int size = 1 << numBits;
        long[] table = new long[size];

        long movementMask = MagicHelper.createMovementMask(square, isRook);
        long[] blockerPatterns = MagicHelper.createAllBlockerBitboards(movementMask);

        for (long pattern : blockerPatterns) {
            long index = (pattern * magic) >>> shift;
            long moves = MagicHelper.legalMoveBitboardFromBlockers(square, pattern, isRook);
            table[(int) index] = moves;
        }

        return table;
    }
}