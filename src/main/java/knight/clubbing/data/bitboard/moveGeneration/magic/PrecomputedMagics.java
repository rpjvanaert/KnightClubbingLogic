package knight.clubbing.data.bitboard.moveGeneration.magic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public final class PrecomputedMagics {

    private PrecomputedMagics() {
        // Prevent instantiation
    }

    public static final long[] ROOK_MAGICS;
    public static final int[] ROOK_SHIFTS;
    public static final long[] ROOK_MASKS;
    public static final long[][] ROOK_ATTACKS;

    public static final long[] BISHOP_MAGICS;
    public static final int[] BISHOP_SHIFTS;
    public static final long[] BISHOP_MASKS;
    public static final long[][] BISHOP_ATTACKS;

    static {
        MagicData data;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("magicData.bin"))) {
            data = (MagicData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load magicData.bin", e);
        }

        ROOK_MAGICS = data.rookMagics;
        ROOK_SHIFTS = data.rookShifts;
        ROOK_MASKS = data.rookMasks;
        ROOK_ATTACKS = data.rookAttacks;

        BISHOP_MAGICS = data.bishopMagics;
        BISHOP_SHIFTS = data.bishopShifts;
        BISHOP_MASKS = data.bishopMasks;
        BISHOP_ATTACKS = data.bishopAttacks;
    }
}