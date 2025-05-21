package knight.clubbing.moveGeneration.magic;

import java.io.Serial;
import java.io.Serializable;

public class MagicData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public long[] rookMagics;
    public int[] rookShifts;
    public long[] rookMasks;
    public long[][] rookAttacks;

    public long[] bishopMagics;
    public int[] bishopShifts;
    public long[] bishopMasks;
    public long[][] bishopAttacks;
}