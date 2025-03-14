package knight.clubbing.logic;

import knight.clubbing.data.details.*;
import knight.clubbing.data.move.Move;
import knight.clubbing.data.move.MoveDraft;
import knight.clubbing.data.move.MoveState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChessGameTest {

    @Test
    void testCastling() {
        ChessGame chessGame;

        chessGame = new ChessGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.WHITE, Coord.of("e1"), Coord.of("g1"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.BLACK, Coord.of("e8"), Coord.of("g8"))));
        System.out.println(chessGame.getMoves());
        assertEquals(2, chessGame.getMoves().size());

        chessGame = new ChessGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.WHITE, Coord.of("e1"), Coord.of("c1"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.BLACK, Coord.of("e8"), Coord.of("c8"))));
        assertEquals(2, chessGame.getMoves().size());
    }

    @Test
    void testRegularOpening() {
        ChessGame chessGame = new ChessGame();

        //1
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("d2"), Coord.of("d4"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("d7"), Coord.of("d5"))));
        //2
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("g1"), Coord.of("f3"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("g8"), Coord.of("f6"))));
        //3
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("c2"), Coord.of("c4"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e7"), Coord.of("e6"))));
        //4
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("c1"), Coord.of("g5"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("b8"), Coord.of("d7"))));
        //5
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e3"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.BLACK, Coord.of("f8"), Coord.of("e7"))));
        //6
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.WHITE, Coord.of("b1"), Coord.of("c3"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.BLACK, Coord.of("e8"), Coord.of("g8"))));
        //7
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.ROOK, Color.WHITE, Coord.of("a1"), Coord.of("c1"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.ROOK, Color.BLACK, Coord.of("f8"), Coord.of("e8"))));
        //8
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.QUEEN, Color.WHITE, Coord.of("d1"), Coord.of("c2"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("a7"), Coord.of("a6"))));
        //9
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("c4"), Coord.of("d5"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("e6"), Coord.of("d5"))));
        //10
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.BISHOP, Color.WHITE, Coord.of("f1"), Coord.of("d3"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.PAWN, Color.BLACK, Coord.of("c7"), Coord.of("c6"))));
        //11
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KING, Color.WHITE, Coord.of("e1"), Coord.of("g1"))));
        assertTrue(chessGame.submitMove(new MoveDraft(PieceType.KNIGHT, Color.BLACK, Coord.of("f6"), Coord.of("e4"))));
    }
    /*
    @Test
    void testApplyAndUndoNormalMove() {
        ChessGame game = new ChessGame();
        MoveDraft moveDraft = new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e2"), Coord.of("e4"), null);
        Move move = game.submitMove(moveDraft) ? game.getMoves().get(game.getMoves().size() - 1) : null;
        assertNotNull(move);

        MoveState state = game.applyMoveTemporarily(move);
        assertNull(game.getBoard().getPieceOn(Coord.of("e2")));
        assertNotNull(game.getBoard().getPieceOn(Coord.of("e4")));

        game.undoMove(state);
        System.out.println(game.getBoard().getColorDisplay());
        assertNotNull(game.getBoard().getPieceOn(Coord.of("e2")));
        assertNull(game.getBoard().getPieceOn(Coord.of("e4")));
    }

    @Test
    void testApplyAndUndoCapture() {
        ChessGame game = new ChessGame("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR w KQkq - 0 1");
        MoveDraft moveDraft = new MoveDraft(PieceType.PAWN, Color.WHITE,Coord.of("e4"), Coord.of("e5"), null);
        Move move = game.submitMove(moveDraft) ? game.getMoves().get(game.getMoves().size() - 1) : null;
        assertNotNull(move);

        Piece capturedPiece = game.getBoard().getPieceOn(Coord.of("d5"));
        MoveState state = game.applyMoveTemporarily(move);
        assertNull(game.getBoard().getPieceOn(Coord.of("e4")));
        assertNotNull(game.getBoard().getPieceOn(Coord.of("d5")));

        game.undoMove(state);
        assertNotNull(game.getBoard().getPieceOn(Coord.of("e4")));
        assertEquals(capturedPiece, game.getBoard().getPieceOn(Coord.of("d5")));
    }

    @Test
    void testApplyAndUndoCastling() {
        ChessGame game = new ChessGame("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
        MoveDraft moveDraft = new MoveDraft(PieceType.KING, Color.WHITE,Coord.of("e1"), Coord.of("g1"), null);
        Move move = game.submitMove(moveDraft) ? game.getMoves().get(game.getMoves().size() - 1) : null;
        assertNotNull(move);

        MoveState state = game.applyMoveTemporarily(move);
        assertNull(game.getBoard().getPieceOn(Coord.of("e1")));
        assertNotNull(game.getBoard().getPieceOn(Coord.of("g1")));
        assertNull(game.getBoard().getPieceOn(Coord.of("h1")));
        assertNotNull(game.getBoard().getPieceOn(Coord.of("f1")));

        game.undoMove(state);
        System.out.println(game.getBoard().getColorDisplay());
        assertNotNull(game.getBoard().getPieceOn(Coord.of("e1")));
        assertNotNull(game.getBoard().getPieceOn(Coord.of("h1")));
    }

    @Test
    void testApplyAndUndoEnPassant() {
        ChessGame game = new ChessGame("rnbqkbnr/pppppppp/8/8/4Pp2/8/PPPP1PPP/RNBQKBNR w KQkq f6 0 1");
        MoveDraft moveDraft = new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("e4"), Coord.of("f5"),  null);
        Move move = game.submitMove(moveDraft) ? game.getMoves().get(game.getMoves().size() - 1) : null;
        assertNotNull(move);

        MoveState state = game.applyMoveTemporarily(move);
        assertNull(game.getBoard().getPieceOn(Coord.of("e4")));
        assertNotNull(game.getBoard().getPieceOn(Coord.of("f5")));
        assertNull(game.getBoard().getPieceOn(Coord.of("f6")));

        game.undoMove(state);
        assertNotNull(game.getBoard().getPieceOn(Coord.of("e4")));
        assertNotNull(game.getBoard().getPieceOn(Coord.of("f6")));
    }

    @Test
    void testApplyAndUndoPromotion() {
        ChessGame game = new ChessGame("8/P7/8/8/8/8/8/k6K w - - 0 1");
        MoveDraft moveDraft = new MoveDraft(PieceType.PAWN, Color.WHITE, Coord.of("a7"), Coord.of("a8"), Promotion.PROMOTION_QUEEN);
        Move move = game.submitMove(moveDraft) ? game.getMoves().get(game.getMoves().size() - 1) : null;
        assertNotNull(move);

        MoveState state = game.applyMoveTemporarily(move);
        assertNull(game.getBoard().getPieceOn(Coord.of("a7")));
        assertNotNull(game.getBoard().getPieceOn(Coord.of("a8")));
        assertEquals(PieceType.QUEEN, game.getBoard().getPieceOn(Coord.of("a8")).pieceType());

        game.undoMove(state);
        assertNotNull(game.getBoard().getPieceOn(Coord.of("a7")));
        assertNull(game.getBoard().getPieceOn(Coord.of("a8")));
    }

     */
}