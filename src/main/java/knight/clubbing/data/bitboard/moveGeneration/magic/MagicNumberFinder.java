package knight.clubbing.data.bitboard.moveGeneration.magic;

import java.util.*;
import java.util.stream.Collectors;

public class MagicNumberFinder {

    static final int BOARD_SIZE = 64;

    static final int[] ROOK_SHIFTS = new int[BOARD_SIZE];
    static final long[] ROOK_MASKS = new long[BOARD_SIZE];
    static final long[][] ROOK_ATTACKS = new long[BOARD_SIZE][];
    static final long[] ROOK_MAGICS = new long[BOARD_SIZE];

    static final int[] BISHOP_SHIFTS = new int[BOARD_SIZE];
    static final long[] BISHOP_MASKS = new long[BOARD_SIZE];
    static final long[][] BISHOP_ATTACKS = new long[BOARD_SIZE][];
    static final long[] BISHOP_MAGICS = new long[BOARD_SIZE];

    public static void start() {
        generateRookData();
        generateBishopData();
    }

    static void generateRookData() {
        for (int square = 0; square < 64; square++) {
            final int sq = square;
            long mask = rookMask(sq);
            List<Long> blockers = generateBlockerPermutations(mask);
            List<Long> attacks = blockers.stream()
                    .map(b -> rookAttacks(sq, b))
                    .collect(Collectors.toList());

            int bits = Long.bitCount(mask);
            int tableSize = 1 << bits;
            long magic = findMagicNumber(sq, mask, blockers, attacks, bits);

            long[] attackTable = new long[tableSize];
            for (int i = 0; i < blockers.size(); i++) {
                long b = blockers.get(i);
                long a = attacks.get(i);
                int index = (int)(((b & mask) * magic) >>> (64 - bits));
                attackTable[index] = a;
            }

            ROOK_SHIFTS[sq] = 64 - bits;
            ROOK_MASKS[sq] = mask;
            ROOK_MAGICS[sq] = magic;
            ROOK_ATTACKS[sq] = attackTable;

            System.out.printf("Rook square %2d done, magic = 0x%016X\n", sq, magic);
        }
    }

    static void generateBishopData() {
        for (int square = 0; square < 64; square++) {
            final int sq = square;
            long mask = bishopMask(sq);
            List<Long> blockers = generateBlockerPermutations(mask);
            List<Long> attacks = blockers.stream()
                    .map(b -> bishopAttacks(sq, b))
                    .collect(Collectors.toList());

            int bits = Long.bitCount(mask);
            int tableSize = 1 << bits;
            long magic = findMagicNumber(sq, mask, blockers, attacks, bits);

            long[] attackTable = new long[tableSize];
            for (int i = 0; i < blockers.size(); i++) {
                long b = blockers.get(i);
                long a = attacks.get(i);
                int index = (int)(((b & mask) * magic) >>> (64 - bits));
                attackTable[index] = a;
            }

            BISHOP_SHIFTS[sq] = 64 - bits;
            BISHOP_MASKS[sq] = mask;
            BISHOP_MAGICS[sq] = magic;
            BISHOP_ATTACKS[sq] = attackTable;

            System.out.printf("Bishop square %2d done, magic = 0x%016X\n", sq, magic);
        }
    }

    static long rookMask(int square) {
        long mask = 0L;
        int rank = square / 8;
        int file = square % 8;

        for (int f = file + 1; f < 7; f++) mask |= 1L << (rank * 8 + f);
        for (int f = file - 1; f > 0; f--) mask |= 1L << (rank * 8 + f);
        for (int r = rank + 1; r < 7; r++) mask |= 1L << (r * 8 + file);
        for (int r = rank - 1; r > 0; r--) mask |= 1L << (r * 8 + file);

        return mask;
    }

    static long bishopMask(int square) {
        long mask = 0L;
        int rank = square / 8;
        int file = square % 8;

        for (int r = rank + 1, f = file + 1; r < 7 && f < 7; r++, f++)
            mask |= 1L << (r * 8 + f);
        for (int r = rank + 1, f = file - 1; r < 7 && f > 0; r++, f--)
            mask |= 1L << (r * 8 + f);
        for (int r = rank - 1, f = file + 1; r > 0 && f < 7; r--, f++)
            mask |= 1L << (r * 8 + f);
        for (int r = rank - 1, f = file - 1; r > 0 && f > 0; r--, f--)
            mask |= 1L << (r * 8 + f);

        return mask;
    }

    static long rookAttacks(int square, long blockers) {
        long attacks = 0L;
        int rank = square / 8;
        int file = square % 8;

        for (int r = rank + 1; r <= 7; r++) {
            int sq = r * 8 + file;
            attacks |= 1L << sq;
            if (((blockers >>> sq) & 1) != 0) break;
        }
        for (int r = rank - 1; r >= 0; r--) {
            int sq = r * 8 + file;
            attacks |= 1L << sq;
            if (((blockers >>> sq) & 1) != 0) break;
        }
        for (int f = file + 1; f <= 7; f++) {
            int sq = rank * 8 + f;
            attacks |= 1L << sq;
            if (((blockers >>> sq) & 1) != 0) break;
        }
        for (int f = file - 1; f >= 0; f--) {
            int sq = rank * 8 + f;
            attacks |= 1L << sq;
            if (((blockers >>> sq) & 1) != 0) break;
        }

        return attacks;
    }

    static long bishopAttacks(int square, long blockers) {
        long attacks = 0L;
        int rank = square / 8;
        int file = square % 8;

        for (int r = rank + 1, f = file + 1; r <= 7 && f <= 7; r++, f++) {
            int sq = r * 8 + f;
            attacks |= 1L << sq;
            if (((blockers >>> sq) & 1) != 0) break;
        }
        for (int r = rank + 1, f = file - 1; r <= 7 && f >= 0; r++, f--) {
            int sq = r * 8 + f;
            attacks |= 1L << sq;
            if (((blockers >>> sq) & 1) != 0) break;
        }
        for (int r = rank - 1, f = file + 1; r >= 0 && f <= 7; r--, f++) {
            int sq = r * 8 + f;
            attacks |= 1L << sq;
            if (((blockers >>> sq) & 1) != 0) break;
        }
        for (int r = rank - 1, f = file - 1; r >= 0 && f >= 0; r--, f--) {
            int sq = r * 8 + f;
            attacks |= 1L << sq;
            if (((blockers >>> sq) & 1) != 0) break;
        }

        return attacks;
    }

    static List<Long> generateBlockerPermutations(long mask) {
        List<Integer> bits = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            if (((mask >>> i) & 1) != 0) bits.add(i);
        }

        int combinations = 1 << bits.size();
        List<Long> blockers = new ArrayList<>(combinations);

        for (int i = 0; i < combinations; i++) {
            long blocker = 0L;
            for (int j = 0; j < bits.size(); j++) {
                if (((i >>> j) & 1) != 0) {
                    blocker |= 1L << bits.get(j);
                }
            }
            blockers.add(blocker);
        }

        return blockers;
    }

    static long findMagicNumber(int square, long mask, List<Long> blockers, List<Long> attacks, int bits) {
        Random rand = new Random();

        for (int attempt = 0; attempt < 1_000_000; attempt++) {
            long magic = randomMagic(rand);
            Map<Long, Long> used = new HashMap<>();
            boolean fail = false;

            for (int i = 0; i < blockers.size(); i++) {
                long b = blockers.get(i);
                long a = attacks.get(i);
                long index = ((b & mask) * magic) >>> (64 - bits);

                if (used.containsKey(index) && used.get(index) != a) {
                    fail = true;
                    break;
                }
                used.put(index, a);
            }

            if (!fail) return magic;
        }

        throw new RuntimeException("Failed to find magic for square " + square);
    }

    static long randomMagic(Random rand) {
        return rand.nextLong() & rand.nextLong() & rand.nextLong();
    }

}