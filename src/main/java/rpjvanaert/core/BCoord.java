package rpjvanaert.core;

import java.io.Serializable;

public class BCoord implements Comparable<BCoord>, Serializable {

    private final int fileIndex;
    private final int rankIndex;

    public BCoord(int fileIndex, int rankIndex) {
        this.fileIndex = fileIndex;
        this.rankIndex = rankIndex;
    }

    public BCoord(int squareIndex) {
        this(BBoardHelper.fileIndex(squareIndex), BBoardHelper.rankIndex(squareIndex));
    }

    public boolean isLightSquare() {
        return (fileIndex + rankIndex) % 2 != 0;
    }

    public BCoord add(BCoord other) {
        return new BCoord(this.fileIndex + other.fileIndex, this.rankIndex + other.rankIndex);
    }

    public BCoord subtract(BCoord other) {
        return new BCoord(this.fileIndex - other.fileIndex, this.rankIndex - other.rankIndex);
    }

    public BCoord multiply(int m) {
        return new BCoord(this.fileIndex * m, this.rankIndex * m);
    }

    public boolean isValidSquare() {
        return fileIndex >= 0 && fileIndex < 8 && rankIndex >= 0 && rankIndex < 8;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public int getRankIndex() {
        return rankIndex;
    }

    @Override
    public int compareTo(BCoord other) {
        int rankDiff = this.rankIndex - other.rankIndex;
        return (rankDiff != 0) ? rankDiff : (this.fileIndex - other.fileIndex);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BCoord other)) return false;
        return fileIndex == other.fileIndex && rankIndex == other.rankIndex;
    }

    @Override
    public int hashCode() {
        return 8 * rankIndex + fileIndex;
    }

    public int getSquareIndex() {
        return BBoardHelper.indexFromCoord(fileIndex, rankIndex);
    }
}
