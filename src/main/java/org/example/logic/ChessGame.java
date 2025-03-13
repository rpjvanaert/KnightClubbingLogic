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

    protected MoveState applyMoveTemporarily(Move move) {
        Coord enPassantTarget = this.board.getEnPassantSquare();
        Piece capturedPiece = this.board.getPieceOn(move.to());

        if (move.pieceType() == PieceType.PAWN &&
                enPassantTarget != null &&
                move.to().equals(enPassantTarget)) {

            capturedPiece = this.board.getPieceOn(enPassantTarget.getAdjacent(0, move.color().other().direction));
            this.board.emptySquare(enPassantTarget.getAdjacent(0, move.color().other().direction));
        }

        MoveState moveState = new MoveState(
                move.from(),
                move.to(),
                move.getPiece(),
                capturedPiece,
                this.board.getCastlingRights(),
                enPassantTarget,
                this.board.getHalfmoveClock(),
                this.board.getFullmoveNumber(),
                move.promotion()
        );

        this.executeMove(move);

        return moveState;
    }

    protected void undoMove(MoveState moveState) {
        board.setPieceOn(moveState.movedPiece, moveState.from);

        if (moveState.capturedPiece == null) {
            board.emptySquare(moveState.to);
        } else {
            if (moveState.movedPiece.pieceType() == PieceType.PAWN &&
                    moveState.to.equals(moveState.previousEnPassantTarget)) {
                board.setPieceOn(moveState.capturedPiece, moveState.to.getAdjacent(0, moveState.movedPiece.color().other().direction));
            } else {
                board.setPieceOn(moveState.capturedPiece, moveState.to);
            }
        }

        board.setCastlingRights(moveState.previousCastlingRights);
        board.setEnPassantSquare(moveState.previousEnPassantTarget);
        board.setHalfmoveClock(moveState.previousHalfMoveClock);
        board.setFullmoveNumber(moveState.previousFullMoveNumber);
        board.setActive(moveState.movedPiece.color());

        if (moveState.movedPiece.pieceType() == PieceType.KING
                && Math.abs(moveState.from.getX() - moveState.to.getX()) == 2) {
            undoCastling(moveState);
        }

        this.moves.remove(moves.size() - 1);
    }


    private static Move transformDraftToMove(MoveDraft move, ThreatType threat) {
        return new Move(
                getNotation(move, threat),
                move.pieceType(),
                move.color(),
                move.from(),
                move.to(),
                move.promotion()
        );
    }

    private static Move transformDraftToMove(MoveDraft move) {
        return transformDraftToMove(move, ThreatType.NULL);
    }

    private static String getNotation(MoveDraft move, ThreatType threat) {
        return (move.getPiece().pieceType().fen() + move.to().getNotation() + threat.getSign()).trim();
    }

    private void executeMove(Move move) {
        if (move.promotion() != null) {
            executePromotion(move);
            return;
        }

        if (move.pieceType().equals(PieceType.KING) && Math.abs(move.from().getX() - move.to().getX()) == 2) {
            executeCastling(move);
            return;
        }

        executeNormal(move);
    }

    private void executePromotion(Move move) {
        this.board.emptySquare(move.from());
        this.board.setPieceOn(new Piece(move.promotion().pieceType, move.color()), move.to());
        this.moves.add(move);
        this.board.setActive(move.color().other());
    }

    private void executeCastling(Move move) {
        this.board.removeCastlingRights(move.color());

        if (move.color().equals(Color.WHITE) && move.to().equals(Coord.of("g1"))) {
            executeMoveRookOver("h1", "f1");
            executeNormal(move);

        } else if (move.color().equals(Color.WHITE) && move.to().equals(Coord.of("c1"))) {
            executeMoveRookOver("a1", "d1");
            executeNormal(move);

        } else if (move.color().equals(Color.BLACK) && move.to().equals(Coord.of("g8"))) {
            executeMoveRookOver("h8", "f8");
            executeNormal(move);

        } else if (move.color().equals(Color.BLACK) && move.to().equals(Coord.of("c8"))) {
            executeMoveRookOver("a8", "d8");
            executeNormal(move);
        }
    }

    private void executeMoveRookOver(String from, String to) {
        Piece piece = this.board.getPieceOn(Coord.of(from));
        this.board.setPieceOn(piece, Coord.of(to));
        this.board.emptySquare(Coord.of(from));
    }

    private void undoCastling(MoveState moveState) {
        if (moveState.movedPiece.color().equals(Color.WHITE) && moveState.to.equals(Coord.of("f1"))) {
            executeMoveRookOver("f1", "h1");

        } else if (moveState.movedPiece.color().equals(Color.WHITE) && moveState.to.equals(Coord.of("c1"))) {
            executeMoveRookOver("d1", "a1");

        } else if (moveState.movedPiece.color().equals(Color.BLACK) && moveState.to.equals(Coord.of("f8"))) {
            executeMoveRookOver("f8", "h8");

        } else if (moveState.movedPiece.color().equals(Color.BLACK) && moveState.to.equals(Coord.of("c8"))) {
            executeMoveRookOver("d8", "a8");
        }
    }

    private void executeNormal(Move move) {
        this.board.emptySquare(move.from());
        this.board.setPieceOn(move.getPiece(), move.to());
        this.moves.add(move);
        this.board.setActive(move.color().other());

        if (move.pieceType() == PieceType.PAWN && Math.abs(move.from().getY() - move.to().getY()) == 2) {
            this.board.setEnPassantSquare(new Coord(move.from().getX(), (move.from().getY() + move.to().getY()) / 2));
        } else if (
                Math.abs(move.from().getX() - move.to().getX()) == 1 &&
                Math.abs(move.from().getY() - move.to().getY()) == 1 &&
                this.board.getEnPassantSquare() != null &&
                this.board.getEnPassantSquare().equals(move.to())
        ) {
            this.board.emptySquare(move.to().getAdjacent(0, move.color().other().direction));
        } else {
            this.board.setEnPassantSquare(null);
        }

        if (!this.board.getCastlingRights().isEmpty()) {
            if (move.pieceType().equals(PieceType.KING)) {
                this.board.removeCastlingRights(move.color());

            } else if (move.pieceType().equals(PieceType.ROOK)) {
                if (move.from().getX() == 0) {
                    this.board.removeCastlingRights(move.color(), Castling.QUEEN);
                } else if (move.from().getX() == 7) {
                    this.board.removeCastlingRights(move.color(), Castling.KING);
                }
            }
        }
    }

    public String exportPgn() { //todo fully if possible
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

    public long perft(int depth) {
        if (depth == 0) return 1;

        long nodes = 0;
        for (Move move : determineAllLegalMoves()) {
            MoveState state = applyMoveTemporarily(move);
            nodes += perft(depth - 1);
            undoMove(state);
        }
        return nodes;
    }

    public long perftDivide(int depth) {
        long totalNodes = 0;

        for (Move move : determineAllLegalMoves()) {
            MoveState state = applyMoveTemporarily(move);
            long moveNodes = perft(depth - 1);
            undoMove(state);

            System.out.println(move.notation() + ": " + moveNodes);
            totalNodes += moveNodes;
        }

        System.out.println("Total: " + totalNodes);
        return totalNodes;
    }

}
