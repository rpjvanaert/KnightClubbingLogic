package knight.clubbing.moveGeneration;

import knight.clubbing.core.BBoardHelper;
import knight.clubbing.core.BCoord;
import knight.clubbing.movegen.PrecomputedMoveData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrecomputedMoveDataTest {

    private final PrecomputedMoveData data = PrecomputedMoveData.getInstance();

    @Test
    void testAlignMaskIncludesSelf() {
        long[][] alignMask = data.getAlignMask();
        for (int i = 0; i < 64; i++) {
            assertTrue((alignMask[i][i] & (1L << i)) != 0, "Align mask must include self for square " + i);
            assertEquals(1L << i, alignMask[i][i], "Align mask incorrect self for square " + i + "\n" + BBoardHelper.bitboardToDisplay(alignMask[i][i]));
        }
    }

    @Test
    void testAlignedSquaresHaveNonZeroMask() {
        long[][] alignMask = data.getAlignMask();
        for (int i = 0; i < 64; i++) {
            BCoord a = BBoardHelper.coordFromIndex(i);
            for (int j = 0; j < 64; j++) {
                BCoord b = BBoardHelper.coordFromIndex(j);
                if (areAligned(a, b)) {
                    long mask = alignMask[i][j];
                    assertNotEquals(0L, mask, String.format("Align mask should not be zero for aligned squares %d and %d", i, j));
                }
            }
        }
    }

    @Test
    void testUnalignedSquaresHaveZeroMask() {
        long[][] alignMask = data.getAlignMask();
        for (int i = 0; i < 64; i++) {
            BCoord a = BBoardHelper.coordFromIndex(i);
            for (int j = 0; j < 64; j++) {
                BCoord b = BBoardHelper.coordFromIndex(j);
                if (!areAligned(a, b)) {
                    long mask = alignMask[i][j];
                    assertEquals(0L, mask, String.format("Align mask should be zero for unaligned squares %d and %d", i, j));
                }
            }
        }
    }

    @Test
    void testAlignMaskIncludesBothSquares() {
        long[][] alignMask = data.getAlignMask();
        for (int i = 0; i < 64; i++) {
            for (int j = 0; j < 64; j++) {
                long mask = alignMask[i][j];
                if (mask != 0) {
                    assertTrue((mask & (1L << i)) != 0, "Align mask should include start square " + i + " - " + j);
                    assertTrue((mask & (1L << j)) != 0, "Align mask should include target square for " + i + " - " + j);
                }
            }
        }
    }

    private boolean areAligned(BCoord a, BCoord b) {
        int dx = b.getFileIndex() - a.getFileIndex();
        int dy = b.getRankIndex() - a.getRankIndex();
        return dx == 0 || dy == 0 || Math.abs(dx) == Math.abs(dy);
    }

}