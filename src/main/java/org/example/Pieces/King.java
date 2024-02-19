package org.example.Pieces;

import org.example.Board;
import org.example.Spot;

public class King extends Piece {
    public King(boolean white) {
        super(white);
    }

    public boolean isCastlingDone() {
        return this.isCastlingDone();
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        return false;
    }
}
