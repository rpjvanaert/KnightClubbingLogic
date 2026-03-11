package knight.clubbing.core;

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

        assertArrayEquals(expectedPieceBoards, board.getPieceBoards());
        assertEquals(0, board.getState().getEnPassantFile());

        String result = board.exportFen();

        assertEquals(FenHelper.startFen, result);
    }

    @Test
    void testFenCustom_bothWays() {
        String customFen = "rnbqkbnr/pppppppp/8/8/3Pp3/8/PPP1PPPP/RNBQKBNR b Kq d3 4 7";

        BBoard board = new BBoard();
        board.loadPosition(customFen);

        assertEquals(BPiece.whitePawn, board.getPieceBoards()[BBoardHelper.stringCoordToIndex("d4")]);
        assertEquals(BPiece.blackPawn, board.getPieceBoards()[BBoardHelper.stringCoordToIndex("e4")]);


        assertTrue(board.getState().hasKingSideCastleRight(true));
        assertFalse(board.getState().hasQueenSideCastleRight(true));
        assertFalse(board.getState().hasKingSideCastleRight(false));
        assertTrue(board.getState().hasQueenSideCastleRight(false));

        assertEquals(4, board.getState().getEnPassantFile());
        assertFalse(board.isWhiteToMove());
        assertEquals(4, board.getState().getFiftyMoveCounter());
        assertEquals(13, board.getPlyCount());

        String exportedFen = board.exportFen();
        assertEquals(customFen, exportedFen);
    }
}