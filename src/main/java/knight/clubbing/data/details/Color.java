package knight.clubbing.data.details;

public enum Color {
    WHITE(1),
    BLACK(-1);

    public final int direction;

    Color(int direction) {
        this.direction = direction;
    }

    public Color other() {
        if (this == WHITE)
            return BLACK;

        return WHITE;
    }
}