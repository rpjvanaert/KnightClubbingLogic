package knight.clubbing;

import knight.clubbing.data.move.Move;

public interface MoveListener {
    void notify(Move move);
}
