package org.example.data.details;

public enum ThreatType {

    CHECK ('+'),
    CHECKMATE('#'),
    STALEMATE('$'),
    NULL(' ');

    private final char sign;

    ThreatType(char sign) {
        this.sign = sign;
    }

    public char getSign() {
        return sign;
    }
}
