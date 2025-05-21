package knight.clubbing.moveGeneration;


import knight.clubbing.moveGeneration.magic.Magic;
import knight.clubbing.moveGeneration.magic.PrecomputedMagics;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static knight.clubbing.moveGeneration.magic.Magic.getBishopIndex;
import static org.junit.jupiter.api.Assertions.*;

public class MagicBitboardTest {

    @Test
    void testRookAndBishopTablesInitialized() {
        for (int square = 0; square < 64; square++) {
            assertNotNull(Magic.RookAttacks[square]);
            assertNotNull(Magic.BishopAttacks[square]);
            assertEquals(1 << (64 - PrecomputedMagics.ROOK_SHIFTS[square]), Magic.RookAttacks[square].length);
            assertEquals(1 << (64 - PrecomputedMagics.BISHOP_SHIFTS[square]), Magic.BishopAttacks[square].length);
        }
    }

    @Test
    void testRookAndBishopAttackCorrectness() {
        Random rng = new Random(0);
        for (int square = 0; square < 64; square++) {
            for (int i = 0; i < 10; i++) {
                long blockers = rng.nextLong() & Magic.RookMask[square];
                long rookExpected = generateRookAttacksBruteForce(square, blockers);
                long rookActual = Magic.getRookAttacks(square, blockers);
                assertEquals(rookExpected, rookActual, "Rook mismatch at square " + square);

                blockers = rng.nextLong() & Magic.BishopMask[square];
                long bishopExpected = generateBishopAttacksBruteForce(square, blockers);
                long bishopActual = Magic.getBishopAttacks(square, blockers);
                assertEquals(bishopExpected, bishopActual, "Bishop mismatch at square " + square);
            }
        }
    }

    @Test
    void testNoBlockers() {
        for (int square = 0; square < 64; square++) {
            long rookAttacks = Magic.getRookAttacks(square, 0L);
            long rookExpected = generateRookAttacksBruteForce(square, 0L);
            assertEquals(rookExpected, rookAttacks);

            long bishopAttacks = Magic.getBishopAttacks(square, 0L);
            long bishopExpected = generateBishopAttacksBruteForce(square, 0L);
            assertEquals(bishopExpected, bishopAttacks);
        }
    }

    @Test
    void testFullyBlocked() {
        for (int square = 0; square < 64; square++) {
            long rookAttacks = Magic.getRookAttacks(square, ~0L);
            long bishopAttacks = Magic.getBishopAttacks(square, ~0L);
            assertTrue(rookAttacks != 0);
            assertTrue(bishopAttacks != 0);
        }
    }

    @Test
    void testBlockersAtEdges() {
        int square = 27;
        long edgeBlockers = (1L << 0) | (1L << 7) | (1L << 56) | (1L << 63);
        long rookExpected = generateRookAttacksBruteForce(square, edgeBlockers);
        long bishopExpected = generateBishopAttacksBruteForce(square, edgeBlockers);
        assertEquals(rookExpected, Magic.getRookAttacks(square, edgeBlockers));
        assertEquals(bishopExpected, Magic.getBishopAttacks(square, edgeBlockers));
    }

    @Test
    void testSymmetry() {
        for (int square = 0; square < 32; square++) {
            int mirrorSquare = 63 - square;
            long blockers = 0x0000001818000000L;
            long rookA = Magic.getRookAttacks(square, blockers);
            long rookB = Long.reverse(Magic.getRookAttacks(mirrorSquare, Long.reverse(blockers)));
            assertEquals(rookA, rookB, "Rook symmetry failed at " + square);

            long bishopA = Magic.getBishopAttacks(square, blockers);
            long bishopB = Long.reverse(Magic.getBishopAttacks(mirrorSquare, Long.reverse(blockers)));
            assertEquals(bishopA, bishopB, "Bishop symmetry failed at " + square);
        }
    }

    @Test
    void testNoMagicCollision() {
        for (int square = 0; square < 64; square++) {
            long[] blockers = generateAllBlockerCombinations(Magic.RookMask[square]);
            Map<Long, Long> used = new HashMap<>();
            for (long b : blockers) {
                long attacks = Magic.getRookAttacks(square, b);
                long index = Magic.getRookIndex(square, b);
                if (used.containsKey(index)) {
                    assertEquals(used.get(index), attacks, "Rook collision with different attacks at square " + square);
                } else {
                    used.put(index, attacks);
                }
            }
        }
    }

    @Test
    void testNoBishopMagicCollision() {
        for (int square = 0; square < 64; square++) {
            long[] blockers = generateAllBlockerCombinations(Magic.BishopMask[square]);
            Map<Long, Long> used = new HashMap<>();
            for (long b : blockers) {
                long attacks = Magic.getBishopAttacks(square, b);
                long index = getBishopIndex(square, b);
                if (used.containsKey(index)) {
                    assertEquals(used.get(index), attacks,
                            "Bishop collision with different attacks at square " + square + " index " + index);
                } else {
                    used.put(index, attacks);
                }
            }
        }
    }


    static long[] generateAllBlockerCombinations(long mask) {
        int bitCount = Long.bitCount(mask);
        int combinations = 1 << bitCount;
        long[] results = new long[combinations];

        // Store indices of set bits in the mask
        int[] bits = new int[bitCount];
        for (int i = 0, idx = 0; i < 64; i++) {
            if (((mask >> i) & 1) != 0) {
                bits[idx++] = i;
            }
        }

        // Generate all subsets
        for (int i = 0; i < combinations; i++) {
            long blockers = 0L;
            for (int j = 0; j < bitCount; j++) {
                if (((i >> j) & 1) != 0) {
                    blockers |= 1L << bits[j];
                }
            }
            results[i] = blockers;
        }

        return results;
    }

    static long generateRookAttacksBruteForce(int square, long blockers) {
        long attacks = 0L;
        int rank = square / 8, file = square % 8;

        for (int r = rank + 1; r <= 7; r++) {
            int s = r * 8 + file;
            attacks |= 1L << s;
            if (((1L << s) & blockers) != 0) break;
        }
        for (int r = rank - 1; r >= 0; r--) {
            int s = r * 8 + file;
            attacks |= 1L << s;
            if (((1L << s) & blockers) != 0) break;
        }
        for (int f = file + 1; f <= 7; f++) {
            int s = rank * 8 + f;
            attacks |= 1L << s;
            if (((1L << s) & blockers) != 0) break;
        }
        for (int f = file - 1; f >= 0; f--) {
            int s = rank * 8 + f;
            attacks |= 1L << s;
            if (((1L << s) & blockers) != 0) break;
        }

        return attacks;
    }

    static long generateBishopAttacksBruteForce(int square, long blockers) {
        long attacks = 0L;
        int rank = square / 8, file = square % 8;

        for (int dr = 1, df = 1; rank + dr <= 7 && file + df <= 7; dr++, df++) {
            int s = (rank + dr) * 8 + (file + df);
            attacks |= 1L << s;
            if (((1L << s) & blockers) != 0) break;
        }
        for (int dr = 1, df = -1; rank + dr <= 7 && file + df >= 0; dr++, df--) {
            int s = (rank + dr) * 8 + (file + df);
            attacks |= 1L << s;
            if (((1L << s) & blockers) != 0) break;
        }
        for (int dr = -1, df = 1; rank + dr >= 0 && file + df <= 7; dr--, df++) {
            int s = (rank + dr) * 8 + (file + df);
            attacks |= 1L << s;
            if (((1L << s) & blockers) != 0) break;
        }
        for (int dr = -1, df = -1; rank + dr >= 0 && file + df >= 0; dr--, df--) {
            int s = (rank + dr) * 8 + (file + df);
            attacks |= 1L << s;
            if (((1L << s) & blockers) != 0) break;
        }

        return attacks;
    }


}
