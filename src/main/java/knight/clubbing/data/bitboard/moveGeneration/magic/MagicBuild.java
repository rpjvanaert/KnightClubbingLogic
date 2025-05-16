package knight.clubbing.data.bitboard.moveGeneration.magic;

import java.io.*;

public class MagicBuild {

    public static void main(String[] args) {
        // Trigger static generation
        MagicNumberFinder.start();

        // Pack data
        MagicData data = new MagicData();
        data.rookMasks = MagicNumberFinder.ROOK_MASKS;
        data.rookShifts = MagicNumberFinder.ROOK_SHIFTS;
        data.rookMagics = MagicNumberFinder.ROOK_MAGICS;
        data.rookAttacks = MagicNumberFinder.ROOK_ATTACKS;

        data.bishopMasks = MagicNumberFinder.BISHOP_MASKS;
        data.bishopShifts = MagicNumberFinder.BISHOP_SHIFTS;
        data.bishopMagics = MagicNumberFinder.BISHOP_MAGICS;
        data.bishopAttacks = MagicNumberFinder.BISHOP_ATTACKS;

        try {
            FileOutputStream fos = new FileOutputStream("magicData.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            oos.close();
            System.out.println("Magic data saved to magicData.bin");
        } catch (IOException e) {
            System.err.println("Failed to save magic data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
