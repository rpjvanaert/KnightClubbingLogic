package knight.clubbing.moveGeneration.magic;

import knight.clubbing.FileUtil;

import java.nio.file.Path;

public class MagicBuild {

    private static final String fileName = "magicData.bin";
    private static final Path PATH = Path.of(fileName);

    public static void main(String[] args) {
        generateAndSave();
    }

    public static MagicData generateAndSave() {
        MagicData data = generateMagicData();

        FileUtil.save(data, PATH);

        return data;
    }

    private static MagicData generateMagicData() {
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
        return data;
    }
}
