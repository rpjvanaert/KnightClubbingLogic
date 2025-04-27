package knight.clubbing.data.details;

public enum Color {
    WHITE(1, 0),
    BLACK(-1, 1);

    public final int direction;
    public final int index;

    Color(int direction, int index) {
        this.direction = direction;
        this.index = index;
    }

    public boolean isWhite() {
        return this == WHITE;
    }

    public Color other() {
        if (this == WHITE)
            return BLACK;

        return WHITE;
    }
}