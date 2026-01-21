package knight.clubbing.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BBoardTest {

    @Test
    void setAndGet() {
        BBoard b = new BBoard();
        b.set(BPiece.whitePawn, 0);
        assertTrue(b.get(BPiece.whitePawn, 0));
        assertFalse(b.get(BPiece.whitePawn, 1));
        assertFalse(b.get(BPiece.whitePawn, 63));
        assertFalse(b.get(BPiece.whitePawn, 32));
    }

    @Test
    void clear() {
        BBoard b = new BBoard();
        b.set(11,63);

        assertTrue(b.get(11,63));
        b.clear(11,63);
        assertFalse(b.get(11,63));
    }

    @Test
    void move() {
        BBoard b = new BBoard();
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

    @Test
    void testIsInCheck_False() {
        assertFalse(new BBoard().isInCheck());
        assertFalse(new BBoard("r2q1rk1/pppbbppp/2n1pn2/1B1p4/2PP4/2N1PN2/PP3PPP/R1BQ1RK1 w Qq - 0 1").isInCheck());
        assertFalse(new BBoard("r3r1k1/pp3ppp/2b5/q3p1P1/2p5/3P1P2/3RNR1P/4K3 w - - 0 26").isInCheck());
        assertFalse(new BBoard("3k3r/p1p1bppp/B1p1pn2/6B1/8/P1N2N2/1P3PPP/R5K1 w - - 2 18").isInCheck());
    }

    @Test
    void testInCheck_Pawn() {
        assertTrue(new BBoard("8/8/8/8/2k5/3P4/8/4K3 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/8/7k/6P1/6K1/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("k7/1P6/8/4K3/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/8/8/5K2/3k4/2P5/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/4p3/3K4/8/5k2/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/8/8/1p6/K4k2/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/8/8/p7/1K3k2/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/8/8/8/5k2/p7/1K6 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/8/8/8/5k2/6p1/7K w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/5p2/6K1/8/8/5k2/8/8 w - - 0 1").isInCheck());
    }

    @Test
    void testInCheck_Bishop() {
        assertTrue(new BBoard("r5rk/5p1p/7R/4B3/8/8/7P/7K b - - 1 1").isInCheck());
        assertTrue(new BBoard("8/8/6K1/2B5/8/4k3/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/2B3K1/8/8/5k2/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6K1/8/8/8/2B5/3k4 b - - 0 1").isInCheck());
        assertTrue(new BBoard("5B2/8/3k2K1/8/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/3k2K1/8/8/6B1/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("4b3/8/3k2K1/8/8/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/7b/3k2K1/8/8/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/3k1K2/8/8/8/1b6/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("3b4/8/3k1K2/8/8/8/8/8 w - - 0 1").isInCheck());
    }

    @Test
    void testInCheck_Knight() {
        assertTrue(new BBoard("8/8/3k3K/8/4N3/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/3k3K/5N2/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/5N2/3k3K/8/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("4N3/8/3k3K/8/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("2N5/8/3k3K/8/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/1N6/3k3K/8/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/3k3K/1N6/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/3k3K/8/2N5/8/8/8 b - - 0 1").isInCheck());

        assertTrue(new BBoard("8/8/6k1/8/5n2/3K4/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/4n3/8/3K4/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/2n5/8/3K4/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/8/1n6/3K4/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/8/8/3K4/1n6/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/8/8/3K4/8/2n5 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/8/8/3K4/8/4n3 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/8/8/3K4/5n2/8 w - - 0 1").isInCheck());
    }

    @Test
    void testInCheck_Rook() {
        assertTrue(new BBoard("8/8/6k1/8/8/3K4/8/6R1 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/4R1k1/8/8/3K4/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6kR/8/8/3K4/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("6R1/8/6k1/8/8/3K4/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/2R3k1/8/8/3K4/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/2K2r2/8/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/2r5/6k1/2K5/8/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/1rK5/8/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/2K5/8/8/2r5/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/2K5/8/8/8/2r5 w - - 0 1").isInCheck());
    }

    @Test
    void testInCheck_Queen() {
        assertTrue(new BBoard("8/8/3Q2k1/2K5/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("4Q3/8/6k1/2K5/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/7Q/6k1/2K5/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/2Q3k1/2K5/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/2K2q2/8/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/4q3/6k1/2K5/8/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/2q5/6k1/2K5/8/8/8/8 w - - 0 1").isInCheck());
    }

    @Test
    void testInCheck_DoubleCheck() {
        assertTrue(new BBoard("8/8/6k1/2K1N3/8/8/2Q5/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/4N3/1Q4k1/2K5/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("4B3/8/1R4k1/2K5/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/2K2r2/8/8/8/6b1 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/1K4r1/8/3b4/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/1K4r1/3n4/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/6k1/1K6/3n4/8/8/5b2 w - - 0 1").isInCheck());
        assertTrue(new BBoard("4b3/8/6k1/2n5/K7/8/8/8 w - - 0 1").isInCheck());
    }

    @Test
    void testInCheck_Mate() {
        assertTrue(new BBoard("3k2R1/7R/8/4p3/8/5K2/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("3k4/3Q4/4K3/4p3/8/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/5K2/4Q3/2qk4/3q4/8/8/8 b - - 0 1").isInCheck());
        assertTrue(new BBoard("8/2K5/8/8/8/6Q1/8/5rkr b - - 0 1").isInCheck());
        assertTrue(new BBoard("6rK/8/5n2/1k6/8/8/8/8 w - - 0 1").isInCheck());
        assertTrue(new BBoard("8/8/8/1k6/8/8/5PPP/3r2K1 w - - 0 1").isInCheck());
    }

    @Test
    void testZobrist_basicRestore() {
        BBoard board = new BBoard();
        long originalKey = board.state.getZobristKey();
        BMove move = new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4"));

        board.makeMove(move, false);
        long newKey = board.state.getZobristKey();

        assertNotEquals(originalKey, newKey);

        board.undoMove(move, false);
        long undoKey = board.state.getZobristKey();

        assertEquals(originalKey, undoKey);
    }

    @Test
    void testZobrist_replicate() {
        String fen = "3k4/3Q4/4K3/4p3/8/8/8/8 b - - 0 1";

        BBoard board1 = new BBoard(fen);
        BBoard board2 = new BBoard(fen);

        assertEquals(board1.state.getZobristKey(), board2.state.getZobristKey());
    }

    @Test
    public void testZobrist_differentFen() {
        BBoard board1 = new BBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BBoard board2 = new BBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");

        assertNotEquals(board1.state.getZobristKey(), board2.state.getZobristKey());
    }

    @Test
    public void testZobrist_differentFen_castling() {
        BBoard board1 = new BBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BBoard board2 = new BBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Qkq - 0 1");

        assertNotEquals(board1.state.getZobristKey(), board2.state.getZobristKey());
    }

    @Test
    public void testZobrist_move_castling() {
        BBoard boardFromFen = new BBoard("rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w Qq - 8 6");
        BBoard boardFromMove = new BBoard("rnbqk2r/pppp1ppp/5n2/2b1p3/2B1P3/5N2/PPPP1PPP/RNBQK2R w KQkq - 4 4");

        boardFromMove.makeMove(new BMove(BBoardHelper.stringCoordToIndex("h1"), BBoardHelper.stringCoordToIndex("g1")), false);
        boardFromMove.makeMove(new BMove(BBoardHelper.stringCoordToIndex("h8"), BBoardHelper.stringCoordToIndex("g8")), false);
        boardFromMove.makeMove(new BMove(BBoardHelper.stringCoordToIndex("g1"), BBoardHelper.stringCoordToIndex("h1")), false);
        boardFromMove.makeMove(new BMove(BBoardHelper.stringCoordToIndex("g8"), BBoardHelper.stringCoordToIndex("h8")), false);

        assertEquals(boardFromFen.exportFen(), boardFromMove.exportFen());
        assertEquals(boardFromFen.state.getZobristKey(), boardFromMove.state.getZobristKey());
    }

    @Test
    public void testZobrist_move_promotion() {
        BBoard boardFromFen = new BBoard("1Q6/8/7k/8/8/8/8/7K b - - 0 1");
        BBoard boardFromMove = new BBoard("8/1P6/7k/8/8/8/8/7K w - - 0 1");

        boardFromMove.makeMove(new BMove(BBoardHelper.stringCoordToIndex("b7"), BBoardHelper.stringCoordToIndex("b8"), BMove.promoteToQueenFlag), false);

        assertEquals(boardFromFen.exportFen(), boardFromMove.exportFen());
        assertEquals(boardFromFen.state.getZobristKey(), boardFromMove.state.getZobristKey());
    }


    @Test
    public void testZobrist_move() {
        BBoard boardFromFen = new BBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        BBoard boardFromMove = new BBoard();
        boardFromMove.makeMove(new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4")), false);

        assertEquals(boardFromFen.state.getZobristKey(), boardFromMove.state.getZobristKey());
    }

    @Test
    public void testZobrist_e4() {
        BBoard boardFromFen = new BBoard();
        BBoard boardFromMove = new BBoard();
        boardFromMove.makeMove(new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4")), false);

        assertNotEquals(boardFromFen.state.getZobristKey(), boardFromMove.state.getZobristKey());
    }

    @Test
    public void testZobrist_sideToMoveChange() {
        BBoard whiteToMove = new BBoard("8/8/8/8/8/8/8/4k3 w - - 0 1");
        BBoard blackToMove = new BBoard("8/8/8/8/8/8/8/4k3 b - - 0 1");

        assertNotEquals(whiteToMove.state.getZobristKey(), blackToMove.state.getZobristKey());
    }

    @Test
    public void testZobrist_fenRoundTrip() {
        String fen = "rnbqkbnr/pppp1ppp/8/4p3/4P3/8/PPPP1PPP/RNBQKBNR w KQkq e6 0 2";
        BBoard board1 = new BBoard(fen);
        BBoard board2 = new BBoard(board1.exportFen());

        assertEquals(board1.exportFen(), board2.exportFen());
        assertEquals(board1.state.getZobristKey(), board2.state.getZobristKey());
    }

    @Test
    public void testZobrist_nullMove() {
        BBoard board = new BBoard();
        long initialKey = board.state.getZobristKey();

        board.makeNullMove();
        assertNotEquals(initialKey, board.state.getZobristKey(), "Zobrist key should change after a null move.");

        board.makeNullMove();
        assertEquals(initialKey, board.state.getZobristKey(), "Zobrist key should remain unchanged after two null moves.");
    }

    @Test
    public void testZobrist_nullMove_enPasasantFile() {
        BBoard board = new BBoard("rnbqkbnr/1ppp1p1p/p7/4pPp1/4P3/8/PPPP2PP/RNBQKBNR w KQkq g6 0 4");
        long initialKey = board.state.getZobristKey();

        board.makeNullMove();
        assertNotEquals(initialKey, board.state.getZobristKey(), "Zobrist key should change after a null move.");

        board.makeNullMove();
        assertNotEquals(initialKey, board.state.getZobristKey(), "Zobrist key should not be the same after two null moves with enPassantFile.");
    }

    @Test
    public void testCastling_inSearch1() {
        // Arrange
        BBoard board = new BBoard("4k2r/8/8/8/8/8/8/4K3 b k - 0 1");
        BMove move1 = new BMove(BBoardHelper.stringCoordToIndex("h8"), BBoardHelper.stringCoordToIndex("h1"));
        BMove move2 = new BMove(BBoardHelper.stringCoordToIndex("e1"), BBoardHelper.stringCoordToIndex("d2"));

        // Act
        board.makeMove(move1, true);
        board.makeMove(move2, true);
        board.undoMove(move2, true);
        board.undoMove(move1, true);

        // Assert
        assertEquals("4k2r/8/8/8/8/8/8/4K3 b k - 0 1", board.exportFen(), "Expected FEN to be unchanged after castling and undoing the move.");
    }

    @Test
    public void testCastling_inSearch2() {
        // Arrange
        BBoard board = new BBoard("4k2r/8/8/8/8/8/8/4K3 b k - 0 1");
        BMove move1 = new BMove(BBoardHelper.stringCoordToIndex("h8"), BBoardHelper.stringCoordToIndex("h1"));
        BMove move2 = new BMove(BBoardHelper.stringCoordToIndex("e1"), BBoardHelper.stringCoordToIndex("d2"));

        // Act
        board.makeMove(move1, true);
        board.makeMove(move2, true);

        // Assert
        assertEquals("4k3/8/8/8/8/8/3K4/7r b - - 2 2", board.exportFen(), "Expected FEN to reflect moves made.");
    }

    @Test
    public void testCopy1() {
        BBoard original = new BBoard("1rbqkb1r/1pp2ppp/p1p2n2/4p3/4P3/2N2N2/PPPP1PPP/1RBQK2R b Kk - 0 1");
        BBoard copy = original.copy();

        assertEquals(original.exportFen(), copy.exportFen(), "Copied board should have the same FEN as the original.");
        assertEquals(original.state.getZobristKey(), copy.state.getZobristKey(), "Copied board should have the same Zobrist key as the original.");
    }

    @Test
    void testEquals_Reflexive() {
        BBoard board = new BBoard();
        assertEquals(board, board);
    }

    @Test
    void testEquals_Symmetric() {
        BBoard board1 = new BBoard();
        BBoard board2 = new BBoard();

        assertEquals(board1, board2);
        assertEquals(board2, board1);
    }

    @Test
    void testEquals_WithNull() {
        BBoard board = new BBoard();
        assertNotEquals(board, null);
    }

    @Test
    void testEquals_WithDifferentClass() {
        BBoard board = new BBoard();
        String other = "not a BBoard";
        assertNotEquals(board, other);
    }

    @Test
    void testEquals_IdenticalStartPosition() {
        BBoard board1 = new BBoard();
        BBoard board2 = new BBoard();

        assertEquals(board1, board2);
    }

    @Test
    void testEquals_IdenticalCustomPosition() {
        String fen = "r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3";
        BBoard board1 = new BBoard(fen);
        BBoard board2 = new BBoard(fen);

        assertEquals(board1, board2);
    }

    @Test
    void testEquals_DifferentSideToMove() {
        BBoard board1 = new BBoard("8/8/8/8/8/8/8/4k3 w - - 0 1");
        BBoard board2 = new BBoard("8/8/8/8/8/8/8/4k3 b - - 0 1");

        assertNotEquals(board1, board2);
    }

    @Test
    void testEquals_DifferentPiecePosition() {
        BBoard board1 = new BBoard();
        BBoard board2 = new BBoard();

        BMove move = new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4"));
        board2.makeMove(move, false);

        assertNotEquals(board1, board2);
    }

    @Test
    void testEquals_DifferentCastlingRights() {
        BBoard board1 = new BBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        BBoard board2 = new BBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w Kq - 0 1");

        assertNotEquals(board1, board2);
    }

    @Test
    void testEquals_DifferentEnPassantFile() {
        BBoard board1 = new BBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
        BBoard board2 = new BBoard("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");

        assertNotEquals(board1, board2);
    }

    @Test
    void testEquals_DifferentFiftyMoveCounter() {
        BBoard board1 = new BBoard("8/8/8/8/8/8/8/4k3 w - - 0 1");
        BBoard board2 = new BBoard("8/8/8/8/8/8/8/4k3 w - - 5 1");

        assertNotEquals(board1, board2);
    }

    @Test
    void testEquals_CopyConstructor() {
        BBoard original = new BBoard("r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3");
        BBoard copy = new BBoard(original);

        assertEquals(original, copy);
    }

    @Test
    void testEquals_CopyMethod() {
        BBoard original = new BBoard("r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R b KQkq - 3 3");
        BBoard copy = original.copy();

        assertEquals(original, copy);
    }

    @Test
    void testEquals_AfterMoveAndUndo() {
        BBoard board1 = new BBoard();
        BBoard board2 = new BBoard();

        BMove move = new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4"));

        assertEquals(board1, board2);

        board2.makeMove(move, false);
        board2.undoMove(move, false);

        assertEquals(board1, board2);
    }

    @Test
    void testEquals_DifferentGameMoveHistory() {
        BBoard board1 = new BBoard();
        BBoard board2 = new BBoard();

        BMove move1 = new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4"));
        BMove move2 = new BMove(BBoardHelper.stringCoordToIndex("e7"), BBoardHelper.stringCoordToIndex("e5"));

        // Same position but different move history
        board1.makeMove(move1, false);
        board1.makeMove(move2, false);

        board2.makeMove(move1, false);
        board2.makeMove(move2, false);

        assertEquals(board1, board2);
    }

    @Test
    void testEquals_WithCachedCheckValues() {
        BBoard board1 = new BBoard("3k4/8/8/8/8/8/8/R3K3 b - - 0 1");
        BBoard board2 = new BBoard("3k4/8/8/8/8/8/8/R3K3 b - - 0 1");

        // Force calculation of cached check values
        board1.isInCheck();
        board2.isInCheck();

        assertEquals(board1, board2);
    }

    @Test
    void testEquals_DifferentCachedCheckState() {
        BBoard board1 = new BBoard("3k4/8/8/8/8/8/8/R3K3 b - - 0 1");
        BBoard board2 = new BBoard("3k4/8/8/8/8/8/8/R3K3 b - - 0 1");

        // Only calculate for one board
        board1.isInCheck();

        // They should still be equal (one has cached value, other doesn't)
        assertEquals(board1, board2);
    }

    @Test
    void testEquals_ComplexPosition() {
        String fen = "r2q1rk1/ppp2ppp/2n1bn2/2b1p3/3pP3/3P1N2/PPP1BPPP/RNBQ1RK1 w - - 0 8";
        BBoard board1 = new BBoard(fen);
        BBoard board2 = new BBoard(fen);

        assertEquals(board1, board2);
    }

    @Test
    void testEquals_AfterMultipleMoves() {
        BBoard board1 = new BBoard();
        BBoard board2 = new BBoard();

        // Make the same sequence of moves on both boards
        BMove[] moves = {
            new BMove(BBoardHelper.stringCoordToIndex("e2"), BBoardHelper.stringCoordToIndex("e4")),
            new BMove(BBoardHelper.stringCoordToIndex("e7"), BBoardHelper.stringCoordToIndex("e5")),
            new BMove(BBoardHelper.stringCoordToIndex("g1"), BBoardHelper.stringCoordToIndex("f3")),
            new BMove(BBoardHelper.stringCoordToIndex("b8"), BBoardHelper.stringCoordToIndex("c6"))
        };

        for (BMove move : moves) {
            board1.makeMove(move, false);
            board2.makeMove(move, false);
        }

        assertEquals(board1, board2);
    }
}