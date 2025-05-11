package knight.clubbing.data.bitboard.core;

public class PopLsbResult {

    public final int index;
    public final long remaining;

    public PopLsbResult(final int index, final long remaining) {
        this.index = index;
        this.remaining = remaining;
    }

    public static PopLsbResult popLsb(long b) {
        int index = Long.numberOfTrailingZeros(b);
        long remaining = b & (b - 1);
        return new PopLsbResult(index, remaining);
    }
}
