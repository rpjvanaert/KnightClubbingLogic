package org.example.logic;

import org.example.data.ChessException;
import org.example.MoveListener;
import org.example.data.*;
import org.example.data.details.*;
import org.example.data.move.Move;
import org.example.data.move.MoveDraft;
import org.example.data.move.MoveState;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {

    protected final Board board;
    private final List<Move> moves;

    private final List<MoveListener> listeners;

    public ChessGame() {
        this.board = new Board();
        this.moves = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    public ChessGame(String fen) {
        this.board = new Board(fen);
        this.moves = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void addListener(MoveListener moveListener) {
        this.listeners.add(moveListener);
    }

    public void removeListener(MoveListener moveListener) {
        this.listeners.remove(moveListener);
    }

    private void notifyListeners(Move move) {
        this.listeners.forEach(listener -> listener.notify(move));
    }

    public boolean submitMove(MoveDraft moveDraft) {
        try {
            isPseudoLegal(moveDraft);
            Move move = getLegalMove(moveDraft);

            executeMove(move);
            notifyListeners(move);

        } catch (ChessException exception) {
            System.err.println(exception.getMessage());
            return false;
        }
        return true;
    }

    private void isPseudoLegal(MoveDraft move) throws ChessException {
        if (move.color() != board.getActive()) throw new ChessException("Not turn of given color");

        switch (move.type()) {
            case CASTLE_LONG:
            case CASTLE_SHORT:
                if (!isValidCastling(move)) throw new ChessException(("Not valid castling move"));
                break;
            case PROMOTION_QUEEN:
            case PROMOTION_ROOK:
            case PROMOTION_BISHOP:
            case PROMOTION_KNIGHT:
                if (!isValidNormal(move) && !isValidPromotion(move)) throw new ChessException("Not valid promotion move");
                break;
            case NORMAL:
            case EN_PASSANT:
                if (!isValidNormal(move)) throw new ChessException("Not valid move");
                break;
        }
    }

    private List<MoveDraft> determineAllPseudoLegalMoves() {
        List<MoveDraft> pseudoLegalMoves = new ArrayList<>();

        for (Coord coord : board.searchPieces(null, board.getActive())) {
            Piece piece = board.getPieceOn(coord);
            pseudoLegalMoves.addAll(MoveMaker.generatePieceMoves(board, piece, coord));
        }

        return pseudoLegalMoves;
    }

    private boolean isValidPromotion(MoveDraft move) {
        if (!move.pieceType().equals(PieceType.PAWN)) return false;

        if (!isValidNormal(move)) return false;


        int rank = move.to().getY();
        Color color = move.color();
        int backRank = (color == Color.WHITE) ? 7 : 0;

        if (rank != backRank) return false;

        return
                move.type().pieceType != null
                && move.type().pieceType != PieceType.PAWN
                && move.type().pieceType != PieceType.KING;
    }

    private boolean isValidNormal(MoveDraft move) {
        return MoveMaker.generatePieceMoves(board, move.from()).contains(move);
    }


    private boolean isValidCastling(MoveDraft move) {
        String side = String.valueOf(move.type().notation);
        if (move.color() == Color.WHITE) side = side.toUpperCase();

        return this.board.getCastlingRights().contains(side);
    }

    private Move getLegalMove(MoveDraft moveDraft) throws ChessException {
        Move move = transformDraftToMove(moveDraft, ThreatType.NULL);
        MoveState moveState = applyMoveTemporarily(move);

        if (RuleChecker.isKingInCheck(board, move.color())) {
            undoMove(moveState);
            throw new ChessException("Move leaves king in check.");
        }

        //ThreatType threat = assessThreat(move.color().other());

        undoMove(moveState);

        return transformDraftToMove(moveDraft, ThreatType.NULL);
    }

    private ThreatType assessThreat(Color opponentColor) {
        Coord kingPosition = board.searchPieces(PieceType.KING, opponentColor).get(0);
        boolean inCheck = RuleChecker.isKingInCheck(board, opponentColor);

        if (inCheck) {
            boolean hasLegalMoves = hasLegalMoves(opponentColor);
            return hasLegalMoves ? ThreatType.CHECK : ThreatType.CHECKMATE;
        } else {
            boolean hasLegalMoves = hasLegalMoves(opponentColor);
            return hasLegalMoves ? ThreatType.NULL : ThreatType.STALEMATE;
        }
    }
    private boolean hasLegalMoves(Color color) {
        List<MoveDraft> moves = determineAllPseudoLegalMoves();
        boolean safe;
        for (MoveDraft moveDraft : moves) {
            MoveState tempState = applyMoveTemporarily(transformDraftToMove(moveDraft, ThreatType.NULL));
            safe = !RuleChecker.isKingInCheck(board, color);
            undoMove(tempState);
            if (safe) return true;
        }
        return false;
    }

    private MoveState applyMoveTemporarily(Move move) {
        MoveState moveState = new MoveState(
                move.from(),
                move.to(),
                move.getPiece(),
                this.board.getPieceOn(move.to()),
                this.board.getCastlingRights(),
                this.board.getEnPassantSquare(),
                this.board.getHalfmoveClock(),
                this.board.getFullmoveNumber()
        );
        //MoveState moveState = new MoveState(this, move);

        board.emptySquare(move.from());
        board.setPieceOn(move.getPiece(), move.to());
        board.setActive(move.color().other());

        return moveState;
    }

    private void undoMove(MoveState moveState) {
        board.setPieceOn(moveState.movedPiece, moveState.from);
        board.setPieceOn(moveState.capturedPiece, moveState.to);
        board.setCastlingRights(moveState.previousCastlingRights);
        board.setEnPassantSquare(moveState.previousEnPassantTarget);
        board.setHalfmoveClock(moveState.previousHalfMoveClock);
        board.setFullmoveNumber(moveState.previousFullMoveNumber);
        board.setActive(moveState.movedPiece.color());
    }


    private static Move transformDraftToMove(MoveDraft move, ThreatType threat) {
        return new Move(
                getNotation(move, threat),
                move.pieceType(),
                move.color(),
                move.from(),
                move.to(),
                move.type()
        );
    }

    private static String getNotation(MoveDraft move, ThreatType threat) {
        return (move.getPiece().pieceType().fen() + move.to().getNotation() + threat.getSign()).trim();
    }

    private void executeMove(Move move) {
        switch (move.type()) {
            case NORMAL ->
                    executeNormal(move);
            case CASTLE_LONG, CASTLE_SHORT ->
                    executeCastling(move);
            case PROMOTION_QUEEN, PROMOTION_ROOK, PROMOTION_BISHOP, PROMOTION_KNIGHT ->
                    executePromotion(move);
        }
    }

    private void executePromotion(Move move) {//todo
    }

    private void executeCastling(Move move) {//todo

    }

    private void executeNormal(Move move) {
        this.board.emptySquare(move.from());
        this.board.setPieceOn(move.getPiece(), move.to());
        this.moves.add(move);
        this.board.setActive(move.color().other());
    }
}
