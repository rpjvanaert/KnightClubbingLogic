package knight.clubbing.moveGeneration;

import knight.clubbing.moveGeneration.magic.Magic;
import knight.clubbing.moveGeneration.magic.MagicHelper;
import knight.clubbing.moveGeneration.magic.PrecomputedMagics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MagicTest {

    @Test
    public void testEmptyBoardRook() {
        int square = 27; // d4
        long blockers = 0L;
        long expected = MagicHelper.legalMoveBitboardFromBlockers(square, blockers, true);
        long actual = Magic.getRookAttacks(square, blockers);
        assertEquals(expected, actual, "Rook attacks on empty board failed at d4");
    }

    @Test
    public void testEmptyBoardBishop() {
        int square = 27; // d4
        long blockers = 0L;
        long expected = MagicHelper.legalMoveBitboardFromBlockers(square, blockers, false);
        long actual = Magic.getBishopAttacks(square, blockers);
        assertEquals(expected, actual, "Bishop attacks on empty board failed at d4");
    }

    @Test
    public void testFullBlockRook() {
        int square = 27;
        long blockers = ~0L;
        long expected = MagicHelper.legalMoveBitboardFromBlockers(square, blockers, true);
        long actual = Magic.getRookAttacks(square, blockers);
        assertEquals(expected, actual, "Rook full-blocked test failed at d4");
    }

    @Test
    public void testFullBlockBishop() {
        int square = 27;
        long blockers = ~0L;
        long expected = MagicHelper.legalMoveBitboardFromBlockers(square, blockers, false);
        long actual = Magic.getBishopAttacks(square, blockers);
        assertEquals(expected, actual, "Bishop full-blocked test failed at d4");
    }

    @Test
    public void testRandomBlockersConsistency() {
        for (int square = 0; square < 64; square++) {
            long mask = MagicHelper.createMovementMask(square, true);
            long[] patterns = MagicHelper.createAllBlockerBitboards(mask);
            for (long blockers : patterns) {
                long expectedRook = MagicHelper.legalMoveBitboardFromBlockers(square, blockers, true);
                long actualRook = Magic.getRookAttacks(square, blockers);
                assertEquals(expectedRook, actualRook, "Rook mismatch at square " + square);

                long expectedBishop = MagicHelper.legalMoveBitboardFromBlockers(square, blockers, false);
                long actualBishop = Magic.getBishopAttacks(square, blockers);
                assertEquals(expectedBishop, actualBishop, "Bishop mismatch at square " + square);
            }
        }
    }

    @Test
    public void testNoCollisionsInAttackTables() {
        for (int square = 0; square < 64; square++) {
            long[] rookAttacks = Magic.RookAttacks[square];
            long[] bishopAttacks = Magic.BishopAttacks[square];

            boolean[] seenRook = new boolean[rookAttacks.length];
            boolean[] seenBishop = new boolean[bishopAttacks.length];

            long rookMask = Magic.RookMask[square];
            long bishopMask = Magic.BishopMask[square];

            long[] rookPatterns = MagicHelper.createAllBlockerBitboards(rookMask);
            for (long blockers : rookPatterns) {
                int key = (int) (((blockers & rookMask) * PrecomputedMagics.ROOK_MAGICS[square]) >>> PrecomputedMagics.ROOK_SHIFTS[square]);
                assertFalse(seenRook[key], "Collision in rook table at square " + square);
                seenRook[key] = true;
            }

            long[] bishopPatterns = MagicHelper.createAllBlockerBitboards(bishopMask);
            for (long blockers : bishopPatterns) {
                int key = (int) (((blockers & bishopMask) * PrecomputedMagics.BISHOP_MAGICS[square]) >>> PrecomputedMagics.BISHOP_SHIFTS[square]);
                assertFalse(seenBishop[key], "Collision in bishop table at square " + square);
                seenBishop[key] = true;
            }
        }
    }
}