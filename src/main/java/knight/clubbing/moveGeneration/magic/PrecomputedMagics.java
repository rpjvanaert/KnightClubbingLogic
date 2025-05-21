package knight.clubbing.moveGeneration.magic;

import knight.clubbing.FileUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class PrecomputedMagics {

    private static final String fileName = "magicData.bin";
    private static final Path path = Paths.get(fileName);

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
        data = FileUtil.load(MagicData.class, path);

        if (data == null) {
            throw new RuntimeException("MagicData not found");
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