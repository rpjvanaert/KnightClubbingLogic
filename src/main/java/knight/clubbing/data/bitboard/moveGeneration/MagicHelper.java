package knight.clubbing.data.bitboard.moveGeneration;

import knight.clubbing.data.bitboard.core.BBoardHelper;
import knight.clubbing.data.bitboard.core.BCoord;

import java.util.ArrayList;
import java.util.List;

public final class MagicHelper {

    private MagicHelper() {
    }

    public static long[] createAllBlockerBitboards(long movementMask) {
        List<Integer> moveSquareIndices = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            if (((movementMask >>> i) & 1) == 1) {
                moveSquareIndices.add(i);
            }
        }

        int numPatterns = 1 << moveSquareIndices.size(); // 2^n
        long[] blockerBitboards = new long[numPatterns];

        for (int patternIndex = 0; patternIndex < numPatterns; patternIndex++) {
            long board = 0;
            for (int bitIndex = 0; bitIndex < moveSquareIndices.size(); bitIndex++) {
                int bit = (patternIndex >>> bitIndex) & 1;
                board |= (long) bit << moveSquareIndices.get(bitIndex);
            }
            blockerBitboards[patternIndex] = board;
        }

        return blockerBitboards;
    }

    public static long createMovementMask(int squareIndex, boolean ortho) {
        long mask = 0;
        BCoord[] directions = ortho ? BBoardHelper.ROOK_DIRECTIONS : BBoardHelper.BISHOP_DIRECTIONS;
        BCoord startCoord = new BCoord(squareIndex);

        for (BCoord dir : directions) {
            for (int dst = 1; dst < 8; dst++) {
                BCoord coord = startCoord.add(dir.multiply(dst));
                BCoord nextCoord = startCoord.add(dir.multiply(dst + 1));

                if (nextCoord.isValidSquare()) {
                    mask = BBoardHelper.setSquare(mask, coord.getSquareIndex());
                } else {
                    break;
                }
            }
        }
        return mask;
    }

    public static long legalMoveBitboardFromBlockers(int startSquare, long blockerBitboard, boolean ortho) {
        long bitboard = 0;
        BCoord[] directions = ortho ? BBoardHelper.ROOK_DIRECTIONS : BBoardHelper.BISHOP_DIRECTIONS;
        BCoord startCoord = new BCoord(startSquare);

        for (BCoord dir : directions) {
            for (int dst = 1; dst < 8; dst++) {
                BCoord coord = startCoord.add(dir.multiply(dst));
                if (coord.isValidSquare()) {
                    bitboard = BBoardHelper.setSquare(bitboard, coord.getSquareIndex());
                    if (MoveUtility.containsSquare(blockerBitboard, coord.getSquareIndex())) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        return bitboard;
    }
}