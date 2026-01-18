package rpjvanaert.opening;

import java.util.Objects;

public class OpeningBookEntry {
    private long zobristKey;
    private String move;
    private int score;
    private int depth;

    public OpeningBookEntry() {
        // No-args constructor required by JDBI and other frameworks
    }

    public OpeningBookEntry(long zobristKey, String move, int score, int depth) {
        this.zobristKey = zobristKey;
        this.move = move;
        this.score = score;
        this.depth = depth;
    }

    public long getZobristKey() {
        return zobristKey;
    }

    public String getMove() {
        return move;
    }

    public int getScore() {
        return score;
    }

    public int getDepth() {
        return depth;
    }

    public void setZobristKey(long zobristKey) {
        this.zobristKey = zobristKey;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public String toString() {
        return "OpeningBookEntry{" +
                "zobristKey=" + zobristKey +
                ", move='" + move + '\'' +
                ", score=" + score +
                ", depth=" + depth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OpeningBookEntry that = (OpeningBookEntry) o;
        return zobristKey == that.zobristKey && score == that.score && depth == that.depth && Objects.equals(move, that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zobristKey, move, score, depth);
    }
}