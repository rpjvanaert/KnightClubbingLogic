package org.example.data.details;

public enum Color {
    WHITE,
    BLACK;

    public Color other() {
        if (this == WHITE)
            return BLACK;

        return WHITE;
    }
}