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

        if (!isValidMove(move)) throw new ChessException("Not a valid move");
    }

    public List<MoveDraft> determineAllPseudoLegalMoves() {
        List<MoveDraft> pseudoLegalMoves = new ArrayList<>();

        for (Coord coord : board.searchPieces(null, board.getActive())) {
            Piece piece = board.getPieceOn(coord);
            pseudoLegalMoves.addAll(MoveMaker.generatePieceMoves(board, piece, coord));
        }

        return pseudoLegalMoves;
    }

    public List<Move> determineAllLegalMoves() {
        List<Move> moveList = new ArrayList<>();
        for (MoveDraft moveDraft : determineAllPseudoLegalMoves()) {

            try {
                moveList.add(getLegalMove(moveDraft));
            } catch (ChessException ignored) {

            }
        }
        return moveList;
    }

    private boolean isValidMove(MoveDraft move) {
        return MoveMaker.generatePieceMoves(board, move.from()).contains(move);
    }

    private Move getLegalMove(MoveDraft moveDraft) throws ChessException {
        Move move = transformDraftToMove(moveDraft);
        MoveState moveState = applyMoveTemporarily(move);

        if (RuleChecker.isKingInCheck(board, move.color())) {
            undoMove(moveState);
            throw new ChessException("Move leaves king in check.");
        }

        ThreatType threat = assessThreat(move.color().other());

        undoMove(moveState);

        return transformDraftToMove(moveDraft, threat);
    }

    private ThreatType assessThreat(Color opponentColor) {
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
            MoveState tempState = applyMoveTemporarily(transformDraftToMove(moveDraft));
            safe = !RuleChecker.isKingInCheck(board, color);
            undoMove(tempState);
            if (safe) return true;
        }
        return false;
    }

    private MoveState applyMoveTemporarily(Move move) {
        MoveState moveState = new MoveState(
                move.type(),
                move.from(),
                move.to(),
                move.getPiece(),
                this.board.getPieceOn(move.to()),
                this.board.getCastlingRights(),
                this.board.getEnPassantSquare(),
                this.board.getHalfmoveClock(),
                this.board.getFullmoveNumber()
        );

        this.executeMove(move);

        return moveState;
    }

    private void undoMove(MoveState moveState) {
        board.setPieceOn(moveState.movedPiece, moveState.from);
        if (moveState.capturedPiece == null) {
            board.emptySquare(moveState.to);
        } else {
            board.setPieceOn(moveState.capturedPiece, moveState.to);
        }
        board.setCastlingRights(moveState.previousCastlingRights);
        board.setEnPassantSquare(moveState.previousEnPassantTarget);
        board.setHalfmoveClock(moveState.previousHalfMoveClock);
        board.setFullmoveNumber(moveState.previousFullMoveNumber);
        board.setActive(moveState.movedPiece.color());

        this.moves.remove(moves.size()-1);

        if (moveState.moveType.equals(MoveType.CASTLE_SHORT) || moveState.moveType.equals(MoveType.CASTLE_LONG))
            undoCastling(moveState);
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

    private static Move transformDraftToMove(MoveDraft move) {
        return transformDraftToMove(move, ThreatType.NULL);
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

    private void executePromotion(Move move) {
        this.board.emptySquare(move.from());
        this.board.setPieceOn(new Piece(move.type().pieceType, move.color()), move.to());
        this.moves.add(move);
        this.board.setActive(move.color().other());
    }

    private void executeCastling(Move move) {
        this.board.removeCastlingRights(move.color());
        executeRookMoveCastling(move);
        executeNormal(move);
    }

    private void executeRookMoveCastling(Move move) {
        if (move.color().equals(Color.WHITE) && move.type().equals(MoveType.CASTLE_SHORT)) {
            Piece piece = this.board.getPieceOn(Coord.of("h1"));
            this.board.setPieceOn(piece, Coord.of("f1"));
            this.board.emptySquare(Coord.of("h1"));

        } else if (move.color().equals(Color.WHITE) && move.type().equals(MoveType.CASTLE_LONG)) {
            Piece piece = this.board.getPieceOn(Coord.of("a1"));
            this.board.setPieceOn(piece, Coord.of("d1"));
            this.board.emptySquare(Coord.of("a1"));

        } else if (move.color().equals(Color.BLACK) && move.type().equals(MoveType.CASTLE_SHORT)) {
            Piece piece = this.board.getPieceOn(Coord.of("h8"));
            this.board.setPieceOn(piece, Coord.of("f8"));
            this.board.emptySquare(Coord.of("h8"));

        } else if (move.color().equals(Color.BLACK) && move.type().equals(MoveType.CASTLE_LONG)) {
            Piece piece = this.board.getPieceOn(Coord.of("a8"));
            this.board.setPieceOn(piece, Coord.of("d8"));
            this.board.emptySquare(Coord.of("a8"));
        }
    }

    private void undoCastling(MoveState moveState) {
        if (moveState.movedPiece.color().equals(Color.WHITE) && moveState.moveType.equals(MoveType.CASTLE_SHORT)) {
            Piece piece = this.board.getPieceOn(Coord.of("f1"));
            this.board.setPieceOn(piece, Coord.of("h1"));
            this.board.emptySquare(Coord.of("f1"));

        } else if (moveState.movedPiece.color().equals(Color.WHITE) && moveState.moveType.equals(MoveType.CASTLE_LONG)) {
            Piece piece = this.board.getPieceOn(Coord.of("d1"));
            this.board.setPieceOn(piece, Coord.of("a1"));
            this.board.emptySquare(Coord.of("d1"));

        } else if (moveState.movedPiece.color().equals(Color.BLACK) && moveState.moveType.equals(MoveType.CASTLE_SHORT)) {
            Piece piece = this.board.getPieceOn(Coord.of("f8"));
            this.board.setPieceOn(piece, Coord.of("h8"));
            this.board.emptySquare(Coord.of("f8"));

        } else if (moveState.movedPiece.color().equals(Color.BLACK) && moveState.moveType.equals(MoveType.CASTLE_LONG)) {
            Piece piece = this.board.getPieceOn(Coord.of("d8"));
            this.board.setPieceOn(piece, Coord.of("a8"));
            this.board.emptySquare(Coord.of("d8"));
        }
    }

    private void executeNormal(Move move) {
        this.board.emptySquare(move.from());
        this.board.setPieceOn(move.getPiece(), move.to());
        this.moves.add(move);
        this.board.setActive(move.color().other());

        if (!this.board.getCastlingRights().isEmpty()) {
            if (move.pieceType().equals(PieceType.KING)) {
                this.board.removeCastlingRights(move.color());

            } else if (move.pieceType().equals(PieceType.ROOK)) {
                if (move.from().getX() == 0) {
                    this.board.removeCastlingRights(move.color(), MoveType.CASTLE_LONG);
                } else if (move.from().getX() == 7) {
                    this.board.removeCastlingRights(move.color(), MoveType.CASTLE_SHORT);
                }
            }
        }
    }

    public String exportPgn() {
        StringBuilder pgn = new StringBuilder();

        int fullMove = 1;
        for (Move move : this.moves) {
            pgn
                .append(Color.WHITE.equals(move.color()) ? fullMove + "." : "")
                .append(move.notation())
                .append(" ");
            if (move.color().equals(Color.BLACK))
                fullMove++;
        }

        return pgn.toString();
    }
}
