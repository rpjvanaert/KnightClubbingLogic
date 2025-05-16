package knight.clubbing.data.bitboard.moveGeneration.magic;

import knight.clubbing.data.bitboard.FileUtil;

import java.io.*;
import java.nio.file.Path;

public class MagicBuild {

    private static final String fileName = "magicData.bin";
    private static final Path PATH = Path.of(fileName);

    public static void main(String[] args) {
        MagicNumberFinder.start();

        MagicData data = new MagicData();
        data.rookMasks = MagicNumberFinder.ROOK_MASKS;
        data.rookShifts = MagicNumberFinder.ROOK_SHIFTS;
        data.rookMagics = MagicNumberFinder.ROOK_MAGICS;
        data.rookAttacks = MagicNumberFinder.ROOK_ATTACKS;

        data.bishopMasks = MagicNumberFinder.BISHOP_MASKS;
        data.bishopShifts = MagicNumberFinder.BISHOP_SHIFTS;
        data.bishopMagics = MagicNumberFinder.BISHOP_MAGICS;
        data.bishopAttacks = MagicNumberFinder.BISHOP_ATTACKS;

        FileUtil.save(data, PATH);
    }
}
