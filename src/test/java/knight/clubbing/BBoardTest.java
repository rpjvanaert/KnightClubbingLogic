package knight.clubbing;

import knight.clubbing.core.BBoard;
import knight.clubbing.core.BBoardHelper;
import knight.clubbing.core.BMove;
import knight.clubbing.core.BPiece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BBoardTest {

    private BBoard b = new BBoard();

    @BeforeEach
    void setUp() {
        b = new BBoard();
    }

    @Test
    void setAndGet() {

        b.set(BPiece.whitePawn, 0);
        assertTrue(b.get(BPiece.whitePawn, 0));
        assertFalse(b.get(BPiece.whitePawn, 1));
        assertFalse(b.get(BPiece.whitePawn, 63));
        assertFalse(b.get(BPiece.whitePawn, 32));
    }

    @Test
    void clear() {
        b.set(11,63);

        assertTrue(b.get(11,63));
        b.clear(11,63);
        assertFalse(b.get(11,63));
    }

    @Test
    void move() {
        b.set(5,32);

        assertTrue(b.get(5,32));
        b.move(5, 32, 33);
        assertFalse(b.get(5,32));
        assertTrue(b.get(5,33));
    }

    //todo more robust debugging of loadPosition tests

    @Test
    void loadStartPosition() {
        BBoard board = new BBoard();

        for (int i = 8; i < 16; i++) {
            assertEquals(BPiece.whitePawn, board.pieceBoards[i], "Expected white pawn at " + i);
            assertTrue((board.getBitboard(BPiece.whitePawn) & (1L << i)) != 0, "Bitboard missing white pawn at " + i);
        }

        for (int i = 48; i < 56; i++) {
            assertEquals(BPiece.blackPawn, board.pieceBoards[i], "Expected black pawn at " + i);
            assertTrue((board.getBitboard(BPiece.blackPawn) & (1L << i)) != 0, "Bitboard missing black pawn at " + i);
        }

        for (int i = 16; i < 48; i++) {
            assertEquals(BPiece.none, board.pieceBoards[i], "Expected none at " + i);
        }

        assertEquals(BPiece.makePiece(BPiece.rook, BPiece.white), board.pieceBoards[0]);
        assertEquals(129L, board.getBitboard(BPiece.whiteRook));
        assertEquals(66L, board.getBitboard(BPiece.whiteKnight));

        assertEquals(0b1111, board.state.getCastlingRights(), "Expected full castling rights");
        assertTrue(board.isWhiteToMove, "Expected white to move");
        assertEquals(0, board.state.getEnPassantFile(), "Expected no en passant file");
        assertNotEquals(0, board.state.getZobristKey(), "Expected non-zero Zobrist key");
    }

    @Test
    void loadPosition() {
        String fen = "r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3";
        BBoard board = new BBoard(fen);

        assertFalse(board.isWhiteToMove, "It should be Black to move");
        assertEquals(BPiece.whiteKnight, board.pieceBoards[BBoardHelper.f1 + BBoardHelper.rowLength * 2], "White knight should be on f3");
        assertEquals(BPiece.blackKnight, board.pieceBoards[BBoardHelper.c1 + BBoardHelper.rowLength * 5], "Black knight should be on c6");
        assertEquals(BPiece.whiteBishop, board.pieceBoards[BBoardHelper.b1 + BBoardHelper.rowLength * 4], "White bishop should be on b5");
        assertEquals(BPiece.blackPawn, board.pieceBoards[BBoardHelper.e1 + BBoardHelper.rowLength * 4], "Black pawn should be on e5");
    }

    @Test
    void testBasicMove() {
        BBoard board = new BBoard();
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4"), BMove.pawnTwoUpFlag);
        long keyBefore = board.state.getZobristKey();

        board.makeMove(move, false);

        assertEquals(BPiece.whitePawn, board.pieceBoards[BBoardHelper.stringCoordToIndex("e4")]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("e2")]);
        assertNotEquals(keyBefore, board.state.getZobristKey());
    }

    @Test
    void testCapture() {
        BBoard board = new BBoard("rnbqkbnr/ppp1pppp/3p4/4P3/8/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2");
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("e5"), BBoardHelper.stringCoordToIndex("d6"));

        board.makeMove(move, false);

        assertEquals(BPiece.whitePawn, board.pieceBoards[BBoardHelper.stringCoordToIndex("d6")]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("e5")]);
        assertEquals(0, board.state.getFiftyMoveCounter());
    }

    @Test
    void testPromotion() {
        BBoard board = new BBoard("8/P7/8/8/8/8/8/k6K w - - 0 1");
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("a7"), BBoardHelper.stringCoordToIndex("a8"), BMove.promoteToQueenFlag);

        board.makeMove(move, false);

        assertEquals(BPiece.whiteQueen, board.pieceBoards[BBoardHelper.a8]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("a7")]);
    }

    @Test
    void testEnPassant() {
        BBoard board = new BBoard("8/8/8/4pP2/8/8/8/8 w - e6 0 1");
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("f5"), BBoardHelper.stringCoordToIndex("e6"), BMove.enPassantCaptureFlag);

        board.makeMove(move, false);

        assertEquals(BPiece.whitePawn, board.pieceBoards[BBoardHelper.stringCoordToIndex("e6")]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("f5")]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("e5")]);
    }

    @Test
    void testKingsideCastle() {
        BBoard board = new BBoard("rnbqk2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQK2R w KQkq - 0 1");
        BMove move = new BMove(BBoardHelper.e1, BBoardHelper.g1, BMove.castleFlag);

        board.makeMove(move, false);

        assertEquals(BPiece.whiteKing, board.pieceBoards[BBoardHelper.g1]);
        assertEquals(BPiece.whiteRook, board.pieceBoards[BBoardHelper.f1]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.e1]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.h1]);
    }

    @Test
    void testQueensideCastle() {
        BBoard board = new BBoard("r3kbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 1");
        BMove move = new BMove(BBoardHelper.e1, BBoardHelper.c1, BMove.castleFlag);

        board.makeMove(move, false);

        assertEquals(BPiece.whiteKing, board.pieceBoards[BBoardHelper.c1]);
        assertEquals(BPiece.whiteRook, board.pieceBoards[BBoardHelper.d1]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.e1]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.a1]);
    }

    @Test
    void testBasicUndoMove() {
        BBoard board = new BBoard();
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4"), BMove.pawnTwoUpFlag);
        long keyBefore = board.state.getZobristKey();
        long[] bitboardsBefore = board.getCopyBitboards();
        int[] pieceBoardsBefore = board.getCopyPieceBoards();

        board.makeMove(move, false);

        assertEquals(BPiece.whitePawn, board.pieceBoards[BBoardHelper.stringCoordToIndex("e4")]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("e2")]);
        assertNotEquals(keyBefore, board.state.getZobristKey());

        board.undoMove(move, false);

        assertEquals(keyBefore, board.state.getZobristKey());
        assertArrayEquals(bitboardsBefore, board.getBitboards());
        assertArrayEquals(pieceBoardsBefore, board.getPieceBoards());
    }

    @Test
    void testUndoCapture() {
        BBoard board = new BBoard("rnbqkbnr/ppp1pppp/3p4/4P3/8/8/PPPP1PPP/RNBQKBNR w KQkq - 0 2");
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("e5"), BBoardHelper.stringCoordToIndex("d6"));
        long keyBefore = board.state.getZobristKey();
        long[] bitboardsBefore = board.getCopyBitboards();
        int[] pieceBoardsBefore = board.getCopyPieceBoards();

        board.makeMove(move, false);

        assertEquals(BPiece.whitePawn, board.pieceBoards[BBoardHelper.stringCoordToIndex("d6")]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("e5")]);
        assertEquals(0, board.state.getFiftyMoveCounter());

        board.undoMove(move, false);

        assertEquals(keyBefore, board.state.getZobristKey());
        assertArrayEquals(bitboardsBefore, board.getBitboards());
        assertArrayEquals(pieceBoardsBefore, board.getPieceBoards());
    }

    @Test
    void testUndoPromotion() {
        BBoard board = new BBoard("8/P7/8/8/8/8/8/k6K w - - 0 1");
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("a7"), BBoardHelper.stringCoordToIndex("a8"), BMove.promoteToQueenFlag);
        long keyBefore = board.state.getZobristKey();
        long[] bitboardsBefore = board.getCopyBitboards();
        int[] pieceBoardsBefore = board.getCopyPieceBoards();

        board.makeMove(move, false);

        assertEquals(BPiece.whiteQueen, board.pieceBoards[BBoardHelper.a8]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("a7")]);

        board.undoMove(move, false);

        assertEquals(keyBefore, board.state.getZobristKey());
        assertArrayEquals(bitboardsBefore, board.getBitboards());
        assertArrayEquals(pieceBoardsBefore, board.getPieceBoards());
    }

    @Test
    void testUndoEnPassant() {
        BBoard board = new BBoard("8/8/8/4pP2/8/8/8/8 w - e6 0 1");
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("f5"), BBoardHelper.stringCoordToIndex("e6"), BMove.enPassantCaptureFlag);
        long keyBefore = board.state.getZobristKey();
        long[] bitboardsBefore = board.getCopyBitboards();
        int[] pieceBoardsBefore = board.getCopyPieceBoards();

        board.makeMove(move, false);

        assertEquals(BPiece.whitePawn, board.pieceBoards[BBoardHelper.stringCoordToIndex("e6")]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("f5")]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.stringCoordToIndex("e5")]);

        board.undoMove(move, false);

        assertEquals(keyBefore, board.state.getZobristKey());
        assertArrayEquals(bitboardsBefore, board.getBitboards());
        assertArrayEquals(pieceBoardsBefore, board.getPieceBoards());
    }

    @Test
    void testUndoKingsideCastle() {
        BBoard board = new BBoard("rnbqk2r/pppppppp/8/8/8/8/PPPPPPPP/RNBQK2R w KQkq - 0 1");
        BMove move = new BMove(BBoardHelper.e1, BBoardHelper.g1, BMove.castleFlag);
        long keyBefore = board.state.getZobristKey();
        long[] bitboardsBefore = board.getCopyBitboards();
        int[] pieceBoardsBefore = board.getCopyPieceBoards();

        board.makeMove(move, false);

        assertEquals(BPiece.whiteKing, board.pieceBoards[BBoardHelper.g1]);
        assertEquals(BPiece.whiteRook, board.pieceBoards[BBoardHelper.f1]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.e1]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.h1]);

        board.undoMove(move, false);

        assertEquals(keyBefore, board.state.getZobristKey());
        assertArrayEquals(bitboardsBefore, board.getBitboards());
        assertArrayEquals(pieceBoardsBefore, board.getPieceBoards());
    }

    @Test
    void testUndoQueensideCastle() {
        BBoard board = new BBoard("r3kbnr/pppppppp/8/8/8/8/PPPPPPPP/R3KBNR w KQkq - 0 1");
        BMove move = new BMove(BBoardHelper.e1, BBoardHelper.c1, BMove.castleFlag);
        long keyBefore = board.state.getZobristKey();
        long[] bitboardsBefore = board.getCopyBitboards();
        int[] pieceBoardsBefore = board.getCopyPieceBoards();

        board.makeMove(move, false);

        assertEquals(BPiece.whiteKing, board.pieceBoards[BBoardHelper.c1]);
        assertEquals(BPiece.whiteRook, board.pieceBoards[BBoardHelper.d1]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.e1]);
        assertEquals(BPiece.none, board.pieceBoards[BBoardHelper.a1]);

        board.undoMove(move, false);

        assertEquals(keyBefore, board.state.getZobristKey());
        assertArrayEquals(bitboardsBefore, board.getBitboards());
        assertArrayEquals(pieceBoardsBefore, board.getPieceBoards());
    }
}