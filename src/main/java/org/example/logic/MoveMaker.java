package org.example.logic;

import org.example.data.Board;
import org.example.data.details.*;
import org.example.data.move.MoveDraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MoveMaker {

    public static List<MoveDraft> generatePieceMoves(Board board, Coord coord) {
        return generatePieceMoves(board, board.getPieceOn(coord), coord);
    }
    public static List<MoveDraft> generatePieceMoves(Board board, Piece piece, Coord coord) {
        if (piece == null)
            return List.of();

        return switch (piece.pieceType()) {
            case PAWN -> generatePawnMoves(board, coord, piece);
            case KNIGHT -> generateKnightMoves(board, coord, piece);
            case BISHOP -> generateBishopMoves(board, coord, piece);
            case ROOK -> generateRookMoves(board, coord, piece);
            case QUEEN -> generateQueenMoves(board, coord, piece);
            case KING -> generateKingMoves(board, coord, piece);
        };
    }

    public static List<MoveDraft> generatePawnMoves(Board board, Coord from) {
        return generatePawnMoves(board, from, board.getPieceOn(from));
    }

    public static List<MoveDraft> generatePawnMoves(Board board, Coord from, Piece piece) {
        List<MoveDraft> moves = new ArrayList<>();
        if (!PieceType.PAWN.equals(piece.pieceType()))
            throw new IllegalArgumentException("Not a pawn given to generate pawn moves.");

        int direction = piece.color().direction;
        
        Coord oneForward = from.getAdjacent(0, direction);
        if (board.isEmpty(oneForward)) {

            if (isPromotionSquare(oneForward)) {
                moves.addAll(createEveryPromotionMove(from, oneForward, piece));
            } else {
                moves.add(new MoveDraft(
                        piece.pieceType(),
                        piece.color(),
                        from,
                        oneForward
                ));
            }

            if (isPawnFromStart(from, piece)) {
                Coord twoForward = from.getAdjacent(0, 2 * direction);
                if (board.isEmpty(twoForward)) {
                    moves.add(new MoveDraft(
                            piece.pieceType(),
                            piece.color(),
                            from,
                            twoForward
                    ));
                }
            }
        }

        moves.addAll(createPawnTakeMove(board, from, piece, from.getAdjacent(-1, direction)).stream().filter(Objects::nonNull).toList());
        moves.addAll(createPawnTakeMove(board, from, piece, from.getAdjacent(1, direction)).stream().filter(Objects::nonNull).toList());
        moves = moves.stream().filter(Objects::nonNull).collect(Collectors.toList());

        return moves;
    }

    private static List<MoveDraft> createEveryPromotionMove(Coord from, Coord target, Piece piece) {
        List<MoveDraft> moves = new ArrayList<>(List.of());

        moves.add(createPromotionMove(from, target, piece.color(), Promotion.PROMOTION_QUEEN));
        moves.add(createPromotionMove(from, target, piece.color(), Promotion.PROMOTION_ROOK));
        moves.add(createPromotionMove(from, target, piece.color(), Promotion.PROMOTION_BISHOP));
        moves.add(createPromotionMove(from, target, piece.color(), Promotion.PROMOTION_KNIGHT));

        return moves;
    }

    private static MoveDraft createPromotionMove(Coord from, Coord target, Color color, Promotion promotion) {
        return new MoveDraft(
                PieceType.PAWN,
                color,
                from,
                target,
                promotion
        );
    }

    private static List<MoveDraft> createPawnTakeMove(Board board, Coord from, Piece piece, Coord toCapture) {
        if (toCapture == null)
            return List.of();

        boolean isEnPassant = isEnPassantMove(board.getEnPassantSquare(), toCapture);
        boolean isPromotion = isPromotionSquare(toCapture);

        if (isPromotion && board.isEnemy(toCapture, piece.color())) {
            return createEveryPromotionMove(from, toCapture, piece);
        }
        if (board.isEnemy(toCapture, piece.color()) || isEnPassant) {
            return List.of(new MoveDraft(
                    piece.pieceType(),
                    piece.color(),
                    from,
                    toCapture
            ));
        }
        return List.of();
    }

    private static boolean isEnPassantMove(Coord enPassantSquare, Coord toCapture) {
        return toCapture.equals(enPassantSquare);
    }

    private static boolean isPawnFromStart(Coord from, Piece piece) {
        return (piece.color() == Color.WHITE && from.getY() == 1) || (piece.color() == Color.BLACK && from.getY() == 6);
    }

    public static List<MoveDraft> generateKnightMoves(Board board, Coord coord) {
        return generateKnightMoves(board, coord, board.getPieceOn(coord));
    }

    public static List<MoveDraft> generateKnightMoves(Board board, Coord coord, Piece piece) {
        List<MoveDraft> moves = new ArrayList<>();
        if (!piece.pieceType().equals(PieceType.KNIGHT))
            throw new IllegalArgumentException("Not a pawn given to generate pawn moves.");

        for (int x = -2; x <=2; x++) {
            for (int y = -2; y <=2; y++) {
                if (Math.abs(x * y) == 2){

                    Coord target = coord.getAdjacent(x, y);
                    if (target != null && board.isValid(target) && !board.isFriendly(target, piece.color())) {
                        moves.add(new MoveDraft(piece.pieceType(), piece.color(), coord, target));
                    }

                }
            }
        }

        return moves;
    }

    private static List<MoveDraft> generateSlidingMoves(Board board, Coord coord, Piece piece) {
        List<MoveDraft> moves = new ArrayList<>();

        int[][] directions = getDirectionsOf(piece);
        if (directions == null)
            return moves;

        for (int[] direction : directions) {
            Coord target = coord;
            while (true) {
                target = target.getAdjacent(direction[0], direction[1]);
                if (target == null || !board.isValid(target)) break;

                if (board.isEmpty(target)) {
                    moves.add(new MoveDraft(piece.pieceType(), piece.color(), coord, target));
                } else if (board.isEnemy(target, piece.color())) {
                    moves.add(new MoveDraft(piece.pieceType(), piece.color(), coord, target));
                    break;
                } else {
                    break;
                }

            }
        }

        return moves;
    }

    private static int[][] getDirectionsOf(Piece piece) {
        if (PieceType.BISHOP.equals(piece.pieceType()))
            return new int [][] {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        if (PieceType.ROOK.equals(piece.pieceType()))
            return new int [][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        if (PieceType.QUEEN.equals(piece.pieceType()))
            return new int [][] {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        return null;
    }

    public static List<MoveDraft> generateBishopMoves(Board board, Coord coord) {
        return generateBishopMoves(board, coord, board.getPieceOn(coord));
    }

    public static List<MoveDraft> generateBishopMoves(Board board, Coord coord, Piece piece) {
        return generateSlidingMoves(board, coord, piece);
    }
    public static List<MoveDraft> generateRookMoves(Board board, Coord coord) {
        return generateRookMoves(board, coord, board.getPieceOn(coord));
    }

    public static List<MoveDraft> generateRookMoves(Board board, Coord coord, Piece piece) {
        return generateSlidingMoves(board, coord, piece);
    }

    public static List<MoveDraft> generateQueenMoves(Board board, Coord coord) {
        return generateQueenMoves(board, coord, board.getPieceOn(coord));
    }

    public static List<MoveDraft> generateQueenMoves(Board board, Coord coord, Piece piece) {
        return generateSlidingMoves(board, coord, piece);
    }

    public static List<MoveDraft> generateKingMoves(Board board, Coord coord, Piece piece) {
        List<MoveDraft> moves = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                Coord target = coord.getAdjacent(dx, dy);
                if (target != null && board.isValid(target) && !board.isFriendly(coord, piece.color())) {
                    moves.add(new MoveDraft(piece.pieceType(), piece.color(), coord, target));
                }
            }
        }

        if (RuleChecker.isCastlingPossible(board, piece.color(), Castling.KING)) {
            moves.add(new MoveDraft(
                    PieceType.KING,
                    piece.color(),
                    coord,
                    coord.getAdjacent(2,0)
            ));
        }

        if (RuleChecker.isCastlingPossible(board, piece.color(), Castling.QUEEN)) {
            moves.add(new MoveDraft(
                    PieceType.KING,
                    piece.color(),
                    coord,
                    coord.getAdjacent(-2,0)
            ));
        }

        return moves;
    }



    public static boolean isPromotionSquare(Coord to) {
        return (to.getY() == 0 || to.getY() == 7);
    }
}
