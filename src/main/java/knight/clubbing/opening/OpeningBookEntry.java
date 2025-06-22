package knight.clubbing.opening;

import java.util.Objects;

public class OpeningBookEntry {
    private long zobristKey;
    private String move;
    private int weight;
    private int depth;
    private String wdl;

    public OpeningBookEntry() {
        // No-args constructor required by JDBI and other frameworks
    }

    public OpeningBookEntry(long zobristKey, String move, int weight, int depth, String wdl) {
        this.zobristKey = zobristKey;
        this.move = move;
        this.weight = weight;
        this.depth = depth;
        this.wdl = wdl;
    }

    public long getZobristKey() {
        return zobristKey;
    }

    public String getMove() {
        return move;
    }

    public int getWeight() {
        return weight;
    }

    public int getDepth() {
        return depth;
    }

    public String getWdl() {
        return wdl;
    }

    public void setZobristKey(long zobristKey) {
        this.zobristKey = zobristKey;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setWdl(String wdl) {
        this.wdl = wdl;
    }

    @Override
    public String toString() {
        return "OpeningBookEntry[" +
                "zobristKey=" + zobristKey +
                ", move='" + move + '\'' +
                ", weight=" + weight +
                ", depth=" + depth +
                ", wdl='" + wdl + '\'' +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OpeningBookEntry that = (OpeningBookEntry) o;
        return zobristKey == that.zobristKey && weight == that.weight && depth == that.depth && Objects.equals(move, that.move) && Objects.equals(wdl, that.wdl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zobristKey, move, weight, depth, wdl);
    }
}