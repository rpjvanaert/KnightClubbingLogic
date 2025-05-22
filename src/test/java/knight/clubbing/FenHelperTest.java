package knight.clubbing;

import knight.clubbing.core.BBoard;
import knight.clubbing.core.BBoardHelper;
import knight.clubbing.core.BPiece;
import knight.clubbing.core.FenHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FenHelperTest {

    @Test
    void testFenStandard_bothWays() {
        BBoard board = new BBoard();

        int[] expectedPieceBoards = new int[]
                {
                        4, 2, 3, 5, 6, 3, 2, 4,
                        1, 1, 1, 1, 1, 1, 1, 1,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0,
                        9, 9, 9, 9, 9, 9, 9, 9,
                        12, 10, 11, 13, 14, 11, 10, 12
                };

        assertArrayEquals(expectedPieceBoards, board.pieceBoards);
        assertEquals(0, board.state.getEnPassantFile());

        String result = board.exportFen();

        assertEquals(FenHelper.startFen, result);
    }

    @Test
    void testFenCustom_bothWays() {
        String customFen = "rnbqkbnr/pppppppp/8/8/3Pp3/8/PPP1PPPP/RNBQKBNR b Kq d3 4 7";

        BBoard board = new BBoard();
        board.loadPosition(customFen);

        assertEquals(BPiece.whitePawn, board.pieceBoards[BBoardHelper.stringCoordToIndex("d4")]);
        assertEquals(BPiece.blackPawn, board.pieceBoards[BBoardHelper.stringCoordToIndex("e4")]);


        assertTrue(board.state.hasKingSideCastleRight(true));
        assertFalse(board.state.hasQueenSideCastleRight(true));
        assertFalse(board.state.hasKingSideCastleRight(false));
        assertTrue(board.state.hasQueenSideCastleRight(false));

        assertEquals(4, board.state.getEnPassantFile());
        assertFalse(board.isWhiteToMove);
        assertEquals(4, board.state.getFiftyMoveCounter());
        assertEquals(13, board.getPlyCount());

        String exportedFen = board.exportFen();
        assertEquals(customFen, exportedFen);
    }
}