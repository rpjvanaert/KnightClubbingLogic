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

    private static final int[][] BISHOP_DIRECTIONS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    private static final int[][] ROOK_DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final int[][] QUEEN_DIRECTIONS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};


    private final List<MoveListener> listeners;

    public ChessGame() {
        this.board = new Board();
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
            case CASTLING -> {
                if (!isValidCastling(move)) throw new ChessException(("Not valid castling move"));
            }
            case PROMOTION -> {
                if (!isValidNormal(move) && !isValidPromotion(move)) throw new ChessException("Not valid promotion move");
            }
            case NORMAL -> {
                if (!isValidNormal(move)) throw new ChessException("Not valid move");
            }
        }
    }

    private List<MoveDraft> determineAllPseudoLegalMoves() {
        List<MoveDraft> pseudoLegalMoves = new ArrayList<>();

        for (Coord coord : board.searchPieces(null, board.getActive())) {
            Piece piece = board.getPieceOn(coord);
            pseudoLegalMoves.addAll(generatePieceMoves(piece, coord));
        }

        return pseudoLegalMoves;
    }

    private List<MoveDraft> generatePieceMoves(Piece piece, Coord position) {
        List<MoveDraft> moves = new ArrayList<>();
        PieceType type = piece.pieceType();
        Color color = piece.color();

        switch (type) {
            case PAWN -> moves.addAll(generatePawnMoves(position, color));
            case KNIGHT -> moves.addAll(generateKnightMoves(position, color));
            case BISHOP -> moves.addAll(generateSlidingMoves(position, color, BISHOP_DIRECTIONS));
            case ROOK -> moves.addAll(generateSlidingMoves(position, color, ROOK_DIRECTIONS));
            case QUEEN -> moves.addAll(generateSlidingMoves(position, color, QUEEN_DIRECTIONS));
            case KING -> moves.addAll(generateKingMoves(position, color));
        }

        return moves;
    }

    private List<MoveDraft> generatePawnMoves(Coord position, Color color) {
        List<MoveDraft> moves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? 1 : -1;
        Piece piece = board.getPieceOn(position);

        Coord forward = position.getAdjacent(0, direction);
        if (board.isEmpty(forward)) {
            moves.add(new MoveDraft(
                    isPromotionMove(position, forward) ? MoveType.PROMOTION : MoveType.NORMAL,
                    piece.pieceType(),
                    color,
                    position,
                    forward,
                    ""
            ));

            if ((color == Color.WHITE && position.getY() == 1) || (color == Color.BLACK && position.getY() == 6)) {
                Coord doubleMove = position.getAdjacent(0, 2 * direction);
                if (board.isEmpty(doubleMove)) {
                    moves.add(new MoveDraft(MoveType.NORMAL, piece.pieceType(), color, position, doubleMove, ""));
                }
            }
        }

        for (int dx : new int[]{-1, 1}) {
            Coord capture = position.getAdjacent(dx, direction);
            if (board.isEnemy(capture, color) || isEnPassantMove(position, capture)) {
                moves.add(new MoveDraft(
                        isPromotionMove(position, capture) ? MoveType.PROMOTION : MoveType.NORMAL,
                        piece.pieceType(),
                        color,
                        position,
                        capture,
                        isEnPassantMove(position, capture) ? "en passant" : ""
                ));
            }
        }

        return moves;
    }

    private List<MoveDraft> generateKnightMoves(Coord position, Color color) {
        List<MoveDraft> moves = new ArrayList<>();
        Piece piece = board.getPieceOn(position);
        int[][] knightOffsets = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

        for (int[] offset : knightOffsets) {
            Coord target = position.getAdjacent(offset[0], offset[1]);
            if (board.isValid(target) && !board.isFriendly(target, color)) {
                moves.add(new MoveDraft(MoveType.NORMAL, piece.pieceType(), color, position, target, ""));
            }
        }

        return moves;
    }

    private List<MoveDraft> generateSlidingMoves(Coord position, Color color, int[][] directions) {
        List<MoveDraft> moves = new ArrayList<>();
        Piece piece = board.getPieceOn(position);

        for (int[] direction : directions) {
            Coord target = position;
            while (true) {
                target = target.getAdjacent(direction[0], direction[1]);
                if (!board.isValid(target)) break;

                if (board.isEmpty(target)) {
                    moves.add(new MoveDraft(MoveType.NORMAL, piece.pieceType(), color, position, target, ""));
                } else {
                    if (board.isEnemy(target, color)) {
                        moves.add(new MoveDraft(MoveType.NORMAL, piece.pieceType(), color, position, target, "capture"));
                    }
                    break;
                }
            }
        }

        return moves;
    }

    private List<MoveDraft> generateKingMoves(Coord position, Color color) {
        List<MoveDraft> moves = new ArrayList<>();
        Piece piece = board.getPieceOn(position);

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Coord target = position.getAdjacent(dx, dy);
                if (board.isValid(target) && !board.isFriendly(target, color)) {
                    moves.add(new MoveDraft(
                            isCastlingMove(position, target) ? MoveType.CASTLING : MoveType.NORMAL,
                            piece.pieceType(),
                            color,
                            position,
                            target,
                            isCastlingMove(position, target) ? "castling" : ""
                    ));
                }
            }
        }

        return moves;
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

        PieceType promotionPieceType = PieceType.fromFEN(move.special().charAt(0));

        return
                promotionPieceType != null
                && promotionPieceType != PieceType.PAWN
                && promotionPieceType != PieceType.KING;
    }

    private boolean isValidNormal(MoveDraft move) {
        return switch (move.pieceType()) {
            case KING -> isValidKingMove(move);
            case QUEEN -> isValidQueenMove(move);
            case ROOK -> isValidRookMove(move);
            case BISHOP -> isValidBishopMove(move);
            case KNIGHT -> isValidKnightMove(move);
            case PAWN -> isValidPawnMove(move);
        };
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

            if (move.special().equals("ep")) {
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

    private boolean isValidCastling(MoveDraft move) {
        String side = String.valueOf(move.special().charAt(0));
        if (move.color() == Color.WHITE) side = side.toUpperCase();

        return this.board.getCastlingRights().contains(side);
    }

    private Move getLegalMove(MoveDraft moveDraft) throws ChessException {
        Move move = transformDraftToMove(moveDraft, ThreatType.NULL);
        MoveState moveState = applyMoveTemporarily(move);

        if (isKingInCheck(move.color())) {
            undoMove(moveState);
            throw new ChessException("Move leaves king in check.");
        }

        ThreatType threat = assessThreat(move.color().other());

        undoMove(moveState);

        return transformDraftToMove(moveDraft, threat);
    }

    public boolean isKingInCheck(Color color) {
        Coord kingPosition = this.board.searchPieces(PieceType.KING, color).get(0);
        if (kingPosition == null) {
            return false;
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Coord from = new Coord(row, col);
                Piece piece = board.getPieceOn(from);

                if (piece != null && piece.color() != color) {
                    List<MoveDraft> possibleMoves = generatePieceMoves(piece, from);
                    for (MoveDraft move : possibleMoves) {
                        if (move.to().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ThreatType assessThreat(Color opponentColor) {
        Coord kingPosition = board.searchPieces(PieceType.KING, opponentColor).get(0);
        boolean inCheck = isUnderAttack(kingPosition);

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
        for (MoveDraft moveDraft : moves) {
            MoveState tempState = applyMoveTemporarily(transformDraftToMove(moveDraft, ThreatType.NULL));
            boolean safe = !isKingInCheck(color);
            undoMove(tempState);
            if (safe) return true;
        }
        return false;
    }



    private MoveState applyMoveTemporarily(Move move) {
        MoveState moveState = new MoveState(this, move); // Save state

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
                move.type(),
                move.pieceType(),
                move.color(),
                move.from(),
                move.to(),
                move.special()
        );
    }

    private boolean isUnderAttack(Coord coord) {

        Color opponentColor = this.board.getActive().other();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Coord from = new Coord(row, col);
                Piece piece = board.getPieceOn(from);

                if (piece != null && piece.color() == opponentColor) {
                    List<MoveDraft> possibleMoves = generatePieceMoves(piece, from);
                    for (MoveDraft move : possibleMoves) {
                        if (move.to().equals(coord)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static String getNotation(MoveDraft move, ThreatType threat) {
        return (move.getPiece().pieceType().fen() + move.to().getNotation() + threat.getSign()).trim();
    }

    private void executeMove(Move move) {
        switch (move.type()) {
            case NORMAL -> executeNormal(move);
            case CASTLING -> executeCastling(move);
            case PROMOTION -> executePromotion(move);
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
        /*
        switch (move.pieceType()) {
            case PAWN -> movePawnNormal(move);
            case KING -> moveKingNormal(move);
            case QUEEN -> moveQueenNormal(move);
            case ROOK -> moveRookNormal(move);
            case BISHOP -> moveBishopNormal(move);
            case KNIGHT -> moveKnightNormal(move);
        }
         */
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
