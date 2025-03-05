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

    private boolean isPromotionMove(Coord from, Coord to) {
        Piece piece = board.getPieceOn(from);
        return piece.pieceType() == PieceType.PAWN && (to.getY() == 0 || to.getY() == 7);
    }

    private boolean isCastlingMove(Coord from, Coord to) {
        Piece piece = board.getPieceOn(from);
        return piece.pieceType() == PieceType.KING && Math.abs(from.getX() - to.getX()) == 2;
    }

    private boolean isEnPassantMove(Coord from, Coord to) {
        Piece piece = board.getPieceOn(from);

        if (piece == null || piece.pieceType() != PieceType.PAWN) {
            return false;
        }

        return board.getEnPassantSquare() != null && board.getEnPassantSquare().equals(to);
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

    private boolean isValidPawnMove(MoveDraft move) {

        int direction = move.color() == Color.WHITE ? 1 : -1;
        int rankDiff = move.to().getY() - move.from().getY();
        int fileDiff = move.to().getX() - move.from().getX();

        if (rankDiff * direction <= 0) return false;

        if (fileDiff == 0) {

            if (targetOccupied(move)) return false;

            if (rankDiff * direction > 1 && move.from().getY() != (move.color() == Color.WHITE ? 1 : 6)) return false;
            if (rankDiff > 2) return false;

            if (rankDiff == 2) {
                Coord intermediate = move.to().getAdjacent(0, -direction);

                return this.board.getPieceOn(intermediate) == null;
            }
        } else {

            if (fileDiff != 1 || rankDiff != 1) return false;

            Piece targetPiece = this.board.getPieceOn(move.to());

            if (move.type().equals("ep")) {
                //TODO en passant
            }

            return targetOccupiedByOtherColor(move);


        }

        return true;
    }

    private boolean isValidKnightMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        if (!((rankDiff == 2 && fileDiff == 1) || (rankDiff == 1 && fileDiff == 2))) return false;

        return targetNotOccupiedByOwnColor(move);
    }

    private boolean isValidBishopMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        if (rankDiff != fileDiff) return false;

        int rankDirection = Integer.compare(move.to().getY(), move.from().getY());
        int fileDirection = Integer.compare(move.to().getX(), move.from().getX());

        int currentRank = move.from().getY() + rankDirection;
        int currentFile = move.from().getX() + fileDirection;

        while (currentFile != move.to().getX() || currentRank != move.to().getY()) {
            if (this.board.getPieceOn(new Coord(currentFile, currentRank)) != null) return false;

            currentFile += fileDirection;
            currentRank += rankDirection;
        }

        return targetNotOccupiedByOwnColor(move);
    }

    private boolean isValidRookMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        if (!(rankDiff == 0 && fileDiff > 0) && !(rankDiff > 0 && fileDiff == 0)) return false;

        int rankDirection = Integer.compare(move.to().getY(), move.from().getY());
        int fileDirection = Integer.compare(move.to().getX(), move.from().getX());

        int currentRank = move.from().getY() + rankDirection;
        int currentFile = move.from().getX() + fileDirection;

        while (currentFile != move.to().getX() || currentRank != move.to().getY()) {
            if (this.board.getPieceOn(new Coord(currentFile, currentRank)) != null) return false;

            currentFile += fileDirection;
            currentRank += rankDirection;
        }

        return targetNotOccupiedByOwnColor(move);
    }

    private boolean isValidQueenMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        boolean isRookMove = (rankDiff == 0 && fileDiff > 0) || (rankDiff > 0 && fileDiff == 0);
        boolean isBishopMove = (rankDiff == fileDiff);

        if (!isRookMove && !isBishopMove) return false;

        int rankDirection = Integer.compare(move.to().getY(), move.from().getY());
        int fileDirection = Integer.compare(move.to().getX(), move.from().getX());

        int currentRank = move.from().getY() + rankDirection;
        int currentFile = move.from().getX() + fileDirection;

        while (currentFile != move.to().getX() || currentRank != move.to().getY()) {
            if (board.getPieceOn(new Coord(currentFile, currentRank)) != null) return false;

            currentRank += rankDirection;
            currentFile += fileDirection;
        }

        return targetNotOccupiedByOwnColor(move);
    }

    private boolean isValidKingMove(MoveDraft move) {
        int rankDiff = Math.abs(move.to().getY() - move.from().getY());
        int fileDiff = Math.abs(move.to().getX() - move.from().getX());

        if (rankDiff > 1 || fileDiff > 1) return false;

        return targetNotOccupiedByOwnColor(move);
    }

    private boolean isValidCastling(MoveDraft move) { //todo
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
            MoveState tempState = applyMoveTemporarily(transformDraftToMove(moveDraft, ThreatType.NULL)); //todo here is issue
            safe = !RuleChecker.isKingInCheck(board, color);
            undoMove(tempState); //todo here is issue or here
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

    private void executeMove(Move move) {//todo every moveType.
        switch (move.type()) {
            case NORMAL -> executeNormal(move);
            case CASTLE_LONG -> executeCastling(move);
            case CASTLE_SHORT -> executeCastling(move);
            case PROMOTION_QUEEN -> executePromotion(move);
        }
    }

    private void executePromotion(Move move) {
    }

    private void executeCastling(Move move) {

    }

    private void executeNormal(Move move) {
        this.board.emptySquare(move.from());
        this.board.setPieceOn(move.getPiece(), move.to());
        this.moves.add(move);
        this.board.setActive(move.color().other());
    }

    private boolean targetNotOccupiedByOwnColor(MoveDraft move) {
        Piece target = board.getPieceOn(move.to());
        return target == null || target.color() != move.color();
    }

    private boolean targetNotOccupied(MoveDraft move) {
        return board.getPieceOn(move.to()) == null;
    }

    private boolean targetOccupied(MoveDraft move) {
        return board.getPieceOn(move.to()) != null;
    }

    private boolean targetOccupiedByOtherColor(MoveDraft move) {
        Piece piece = board.getPieceOn(move.to());
        return piece != null && piece.color() == move.color().other();
    }
}
