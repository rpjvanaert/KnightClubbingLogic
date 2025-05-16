package knight.clubbing.data.bitboard.moveGeneration;

import knight.clubbing.data.bitboard.core.*;
import knight.clubbing.data.bitboard.moveGeneration.magic.Magic;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    public enum PromotionMode { All, Queen, QueenAndKnight }

    public static final int MAX_MOVES = 218;
    public PromotionMode promotionMode = PromotionMode.All;

    private boolean isWhiteToMove;
    private int friendlyColor;
    private int opponentColor;
    private int friendlyKingSquare;
    private int friendlyIndex;
    private int enemyIndex;

    private boolean inCheck;
    private boolean inDoubleCheck;

    private long checkRayBitmask;

    private long pinRays;
    private long notPinRays;
    private long opponentAttackMapNoPawns;
    private long opponentAttackMap;
    private long opponentPawnAttackMap;
    private long opponentSlidingAttackMap;

    private boolean generateQuietMoves;
    private BBoard board;
    private int currentMoveIndex;

    private long enemyPieces;
    private long friendlyPieces;
    private long allPieces;
    private long emptySquares;
    private long emptyOrEnemySquares;

    private long moveTypeMask;

    public MoveGenerator(BBoard board) {
        this.board = board;
    }

    public BMove[] generateMoves(boolean capturesOnly) {
        generateQuietMoves = !capturesOnly;

        init();

        BMove[] kingMoves;
        BMove[] slidingMoves = new BMove[0];
        BMove[] knightMoves = new BMove[0];
        BMove[] pawnMoves = new BMove[0];

        kingMoves = generateKingMoves();

        if (!inDoubleCheck) {
            slidingMoves = generateSlidingMoves();
            knightMoves = generateKnightMoves();
            pawnMoves = generatePawnMoves();
        }

        BMove[] combined = new BMove[kingMoves.length + slidingMoves.length + knightMoves.length + pawnMoves.length];

        return combined;
    }

    private void init() {
        currentMoveIndex = 0;
        inCheck = false;
        inDoubleCheck = false;
        checkRayBitmask = 0;
        pinRays = 0;

        isWhiteToMove = board.isWhiteToMove;
        friendlyColor = board.moveColor();
        opponentColor = board.opponentColor();
        friendlyKingSquare = board.getKingSquare(board.moveColorIndex());
        friendlyIndex = board.moveColorIndex();
        enemyIndex = 1 - friendlyIndex;

        enemyPieces = board.getColorBitboard(enemyIndex);
        friendlyPieces = board.getColorBitboard(friendlyIndex);
        allPieces = board.getAllPiecesBoard();
        emptySquares = ~allPieces;
        emptyOrEnemySquares = emptySquares | enemyPieces;
        moveTypeMask = generateQuietMoves ? Long.MAX_VALUE : enemyPieces;

        calculateAttackData();
    }

    private void calculateAttackData() {

        generateSlidingAttackMap();
        PrecomputedMoveData.getInstance();

        int startDirIndex = 0;
        int endDirIndex = 8;

        if (board.getBitboard(BPiece.makePiece(BPiece.queen, opponentColor)) == 0) {
            startDirIndex = board.getBitboard(BPiece.makePiece(BPiece.rook, opponentColor)) > 0 ? 0 : 4;
            endDirIndex = board.getBitboard(BPiece.makePiece(BPiece.bishop, opponentColor)) > 0 ? 8 : 4;
        }

        for (int dir = startDirIndex; dir < endDirIndex; dir++) {
            boolean isDiagonal = dir > 3;

            long slider = isDiagonal ? board.getDiagonalSliders(enemyIndex) : board.getOrthogonalSliders(enemyIndex);

            if ((PrecomputedMoveData.getInstance().getDirRayMask()[dir][friendlyKingSquare] & slider) == 0) {
                continue;
            }

            int n = PrecomputedMoveData.getInstance().getNumSquaresToEdge()[friendlyKingSquare][dir];
            int directionOffset = PrecomputedMoveData.getInstance().getDirectionOffsets()[dir];
            boolean isFriendlyPieceAlongRay = false;
            long rayMask = 0;

            for (int i = 0; i < n; i++) {
                int squareIndex = friendlyKingSquare + directionOffset * (i + 1);
                rayMask |= 1L << squareIndex;
                int piece = board.getPieceBoards()[squareIndex];

                if (piece != BPiece.none) {
                    if (BPiece.isColor(piece, friendlyColor)) {
                        if (!isFriendlyPieceAlongRay) {
                            isFriendlyPieceAlongRay = true;
                        } else {
                            break;
                        }
                    } else {
                        int pieceType = BPiece.getPieceType(piece);

                        if (isDiagonal && BPiece.isDiagonalMover(pieceType) || !isDiagonal && BPiece.isOrthogonalMover(pieceType)) {

                            if (isFriendlyPieceAlongRay) {
                                pinRays |= rayMask;
                            } else {
                                checkRayBitmask |= rayMask;
                                inDoubleCheck = inCheck;
                                inCheck = true;
                            }
                            break;
                        } else {
                            break;
                        }
                    }
                }
            }

            if (inDoubleCheck) {
                break;
            }
        }

        notPinRays = ~pinRays;

        long opponentKnightAttacks = 0;
        long knights = board.getBitboard(BPiece.makePiece(BPiece.knight, opponentColor));
        long friendlyKingBoard = board.getBitboard(BPiece.makePiece(BPiece.king, friendlyColor));

        while (knights != 0) {
            PopLsbResult lsbResult = PopLsbResult.popLsb(knights);
            int knightSquare = lsbResult.index;
            knights = lsbResult.remaining;
            long knightAttacks = MoveUtility.KnightAttacks[knightSquare];
            opponentKnightAttacks |= knightAttacks;

            if ((knightAttacks & friendlyKingBoard) != 0) {
                inDoubleCheck = inCheck;
                inCheck = true;
                checkRayBitmask |= 1L << knightSquare;
            }
        }

        long opponentPawns = board.getBitboard(BPiece.makePiece(BPiece.pawn, opponentColor));
        opponentPawnAttackMap = MoveUtility.pawnAttacks(opponentPawns, !isWhiteToMove);

        if (MoveUtility.containsSquare(opponentPawnAttackMap, friendlyKingSquare)) {
            inDoubleCheck = inCheck;

            long possiblePawnAttackOrigins = board.isWhiteToMove ? MoveUtility.WhitePawnAttacks[friendlyKingSquare] : MoveUtility.BlackPawnAttacks[friendlyKingSquare];
            long pawnCheckMap = opponentPawns & possiblePawnAttackOrigins;
            checkRayBitmask |= pawnCheckMap;
        }

        int enemyKingSquare = board.getKingSquare(enemyIndex);

        opponentAttackMapNoPawns = opponentSlidingAttackMap | opponentKnightAttacks | MoveUtility.KingMoves[enemyKingSquare];
        opponentAttackMap = opponentAttackMapNoPawns | opponentKnightAttacks;

        if (!inCheck) {
            checkRayBitmask = Long.MAX_VALUE;
        }
    }

    private void generateSlidingAttackMap() {

        opponentSlidingAttackMap = 0;

        updateSlideAttack(board.getOrthogonalSliders(opponentColor), true);
        updateSlideAttack(board.getDiagonalSliders(opponentColor), false);

    }

    private void updateSlideAttack(long pieceBoard, boolean orthogonal) {
        long blockers = board.getAllPiecesBoard() & ~(1L << friendlyKingSquare);

        while(pieceBoard != 0) {
            PopLsbResult popLsbResult = PopLsbResult.popLsb(pieceBoard);
            int startSquare = popLsbResult.index;
            pieceBoard = popLsbResult.remaining;

            long moveBoard = Magic.getSliderAttacks(startSquare, blockers, orthogonal);

            opponentSlidingAttackMap |= moveBoard;
        }
    }

    private BMove[] generateKingMoves() {
        List<BMove> moves = new ArrayList<>();

        long legalMask = ~(opponentAttackMap | friendlyPieces);
        long kingMoves = MoveUtility.KingMoves[friendlyKingSquare] & legalMask & moveTypeMask;

        while (kingMoves != 0) {
            PopLsbResult popLsbResult = PopLsbResult.popLsb(kingMoves);
            int targetSquare = popLsbResult.index;
            kingMoves = popLsbResult.remaining;
            moves.add(new BMove(friendlyKingSquare, targetSquare));
            currentMoveIndex++;
        }

        if (!inCheck && generateQuietMoves) {
            long castleBlockers = opponentAttackMap | board.getAllPiecesBoard();

            if (board.state.hasKingSideCastleRight(board.isWhiteToMove)) {
                long castleMask = board.isWhiteToMove ? MoveUtility.WHITE_KINGSIDE_MASK : MoveUtility.BLACK_KINGSIDE_MASK;
                if ((castleMask & castleBlockers) == 0) {
                    int targetSquare = board.isWhiteToMove ? BBoardHelper.g1 : BBoardHelper.g8;
                    moves.add(new BMove(friendlyKingSquare, targetSquare, BMove.castleFlag));
                }
            }

            if (board.state.hasQueenSideCastleRight(board.isWhiteToMove)) {
                long castleMask = board.isWhiteToMove ? MoveUtility.WHITE_QUEENSIDE_MASK2 : MoveUtility.BLACK_QUEENSIDE_MASK2;
                long castleBlockMask = board.isWhiteToMove ? MoveUtility.WHITE_QUEENSIDE_MASK : MoveUtility.BLACK_QUEENSIDE_MASK;
                if ((castleMask & castleBlockers) == 0 && (castleBlockMask & board.getAllPiecesBoard()) == 0) {
                    int targetSquare = board.isWhiteToMove ? BBoardHelper.c1 : BBoardHelper.c8;
                    moves.add(new BMove(friendlyKingSquare, targetSquare, BMove.castleFlag));
                }
            }
        }

        return moves.toArray(new BMove[0]);
    }

    private BMove[] generateSlidingMoves() {
        List<BMove> moves = new ArrayList<>();

        long moveMask = emptyOrEnemySquares & checkRayBitmask & moveTypeMask;

        long orthogonalSliders = board.getOrthogonalSliders(friendlyIndex);
        long diagonalSliders = board.getDiagonalSliders(friendlyIndex);

        if (inCheck) {
            orthogonalSliders &= ~pinRays;
            diagonalSliders &= ~pinRays;
        }

        while (orthogonalSliders != 0) {
            PopLsbResult popLsbResult = PopLsbResult.popLsb(orthogonalSliders);
            int startSquare = popLsbResult.index;
            orthogonalSliders = popLsbResult.remaining;
            long moveSquares = Magic.getRookAttacks(startSquare, allPieces) & moveMask;

            if (isPinned(startSquare)) {
                moveSquares &= PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare];
            }

            while (moveSquares != 0) {
                PopLsbResult moveResult = PopLsbResult.popLsb(moveSquares);
                int targetSquare = moveResult.index;
                moveSquares = moveResult.remaining;
                moves.add(new BMove(startSquare, targetSquare));
            }
        }

        while (diagonalSliders != 0) {
            PopLsbResult startResult = PopLsbResult.popLsb(diagonalSliders);
            int startSquare = startResult.index;
            diagonalSliders = startResult.remaining;
            long moveSquares = Magic.getBishopAttacks(startSquare, allPieces) & moveMask;

            if (isPinned(startSquare)) {
                moveSquares &= PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare];
            }

            while (moveSquares != 0) {
                PopLsbResult targetResult = PopLsbResult.popLsb(moveSquares);
                int targetSquare = targetResult.index;
                moveSquares = targetResult.remaining;
                moves.add(new BMove(startSquare, targetSquare));
            }
        }

        return moves.toArray(new BMove[0]);
    }

    private BMove[] generateKnightMoves() {
        List<BMove> moves = new ArrayList<>();

        int friendlyKnightPiece = BPiece.makePiece(BPiece.knight, friendlyColor);
        long knights = board.getPieceBoards()[friendlyKnightPiece] & notPinRays;
        long moveMask = emptyOrEnemySquares & checkRayBitmask & moveTypeMask;

        while (knights != 0) {

            PopLsbResult startResult = PopLsbResult.popLsb(knights);
            int knightSquare = startResult.index;
            knights = startResult.remaining;
            long moveSquares = MoveUtility.KnightAttacks[knightSquare] & moveMask;

            while (knights != 0) {
                PopLsbResult targetResult = PopLsbResult.popLsb(moveSquares);
                int targetSquare = targetResult.index;
                moveSquares = targetResult.remaining;

                moves.add(new BMove(knightSquare, targetSquare));
            }
        }

        return moves.toArray(new BMove[0]);
    }

    private BMove[] generatePawnMoves() {
        List<BMove> moves = new ArrayList<>();

        int pushDir = board.isWhiteToMove ? 1 : -1;
        int pushOffset = pushDir * 8;

        int friendlyPawnPiece = BPiece.makePiece(BPiece.pawn, friendlyColor);
        long pawns = board.getPieceBoards()[friendlyPawnPiece];

        long promotionRankMask = board.isWhiteToMove ? MoveUtility.Rank8 : MoveUtility.Rank1;
        long singlePush = (MoveUtility.shift(pawns, pushOffset)) & emptySquares;
        long pushPromotions = singlePush & promotionRankMask & checkRayBitmask;

        long captureEdgeFileMask = board.isWhiteToMove ? MoveUtility.notAFile : MoveUtility.notHFile;
        long captureEdgeFileMask2 = board.isWhiteToMove ? MoveUtility.notHFile : MoveUtility.notAFile;
        long captureA = MoveUtility.shift(pawns & captureEdgeFileMask, pushDir * 7) & enemyPieces;
        long captureB = MoveUtility.shift(pawns & captureEdgeFileMask2, pushDir * 9) & enemyPieces;

        long singlePushNoPromotions = singlePush & ~promotionRankMask & checkRayBitmask;
        long capturePromotionsA = captureA & promotionRankMask & checkRayBitmask;
        long capturePromotionsB = captureB & promotionRankMask & checkRayBitmask;

        captureA &= checkRayBitmask & ~promotionRankMask;
        captureB &= checkRayBitmask & ~promotionRankMask;

        if (generateQuietMoves) {

            while (singlePushNoPromotions != 0) {

                PopLsbResult targetResult = PopLsbResult.popLsb(singlePush);
                int targetSquare = targetResult.index;
                singlePush = targetResult.remaining;
                int startSquare = targetSquare - pushOffset;

                if (!isPinned(startSquare) || PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare] == PrecomputedMoveData.getInstance().getAlignMask()[targetSquare][friendlyKingSquare]) {
                    moves.add(new BMove(startSquare, targetSquare));
                }
            }

            long doublePushTargetRankMask = board.isWhiteToMove ? MoveUtility.Rank4: MoveUtility.Rank5;
            long doublePush = MoveUtility.shift(singlePush, pushOffset) & emptySquares & doublePushTargetRankMask & checkRayBitmask;

            while (doublePush != 0) {

                PopLsbResult targetResult = PopLsbResult.popLsb(doublePush);
                int targetSquare = targetResult.index;
                doublePush = targetResult.remaining;
                int startSquare = targetSquare - pushOffset * 2;

                if (!isPinned(startSquare) || PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare] == PrecomputedMoveData.getInstance().getAlignMask()[targetSquare][friendlyKingSquare]) {
                    moves.add(new BMove(startSquare, targetSquare, BMove.pawnTwoUpFlag));
                }
            }
        }

        while (captureA != 0) {

            PopLsbResult targetResult = PopLsbResult.popLsb(captureA);
            int targetSquare = targetResult.index;
            captureA = targetResult.remaining;
            int startSquare = targetSquare - pushOffset * 7;

            if (!isPinned(startSquare) || PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare] == PrecomputedMoveData.getInstance().getAlignMask()[targetSquare][friendlyKingSquare]) {
                moves.add(new BMove(startSquare, targetSquare));
            }
        }

        while (captureB != 0) {

            PopLsbResult targetResult = PopLsbResult.popLsb(captureB);
            int targetSquare = targetResult.index;
            captureB = targetResult.remaining;
            int startSquare = targetSquare - pushOffset * 9;

            if (!isPinned(startSquare) || PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare] == PrecomputedMoveData.getInstance().getAlignMask()[targetSquare][friendlyKingSquare]) {
                moves.add(new BMove(startSquare, targetSquare));
            }
        }

        while (pushPromotions != 0) {

            PopLsbResult targetResult = PopLsbResult.popLsb(pushPromotions);
            int targetSquare = targetResult.index;
            pushPromotions = targetResult.remaining;
            int startSquare = targetSquare - pushOffset;
            if (!isPinned(startSquare)) {
                moves.addAll(generatePromotions(startSquare, targetSquare));
            }
        }

        while (capturePromotionsA != 0) {

            PopLsbResult targetResult = PopLsbResult.popLsb(capturePromotionsA);
            int targetSquare = targetResult.index;
            capturePromotionsA = targetResult.remaining;
            int startSquare = targetSquare - pushDir * 7;

            if (!isPinned(startSquare) || PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare] == PrecomputedMoveData.getInstance().getAlignMask()[targetSquare][friendlyKingSquare]) {
                generatePromotions(startSquare, targetSquare);
            }
        }

        while (capturePromotionsB != 0) {

            PopLsbResult targetResult = PopLsbResult.popLsb(capturePromotionsB);
            int targetSquare = targetResult.index;
            capturePromotionsB = targetResult.remaining;
            int startSquare = targetSquare - pushDir * 7;

            if (!isPinned(startSquare) || PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare] == PrecomputedMoveData.getInstance().getAlignMask()[targetSquare][friendlyKingSquare]) {
                generatePromotions(startSquare, targetSquare);
            }
        }

        if (board.state.getEnPassantFile() > 0) {
            int epFileIndex = board.state.getEnPassantFile() - 1;
            int epRankIndex = board.isWhiteToMove ? 5 : 2;
            int targetSquare = epRankIndex * 8 + epFileIndex;
            int capturedPawnSquare = targetSquare - pushOffset;

            if (MoveUtility.containsSquare(checkRayBitmask, capturedPawnSquare)) {

                long pawnsThatCanCaptureEp = pawns & MoveUtility.pawnAttacks(1L << targetSquare, !board.isWhiteToMove);

                while (pawnsThatCanCaptureEp != 0) {

                    PopLsbResult startResult = PopLsbResult.popLsb(pawnsThatCanCaptureEp);
                    int startSquare = startResult.index;
                    pawnsThatCanCaptureEp = startResult.remaining;

                    if (!isPinned(startSquare) || PrecomputedMoveData.getInstance().getAlignMask()[startSquare][friendlyKingSquare] == PrecomputedMoveData.getInstance().getAlignMask()[targetSquare][friendlyKingSquare]) {

                        if (!inCheckAfterEnPassant(startSquare, targetSquare, capturedPawnSquare)) {
                            moves.add(new BMove(startSquare, targetSquare, BMove.enPassantCaptureFlag));
                        }
                    }
                }
            }
        }

        return moves.toArray(new BMove[0]);
    }

    private boolean inCheckAfterEnPassant(int startSquare, int targetSquare, int capturedPawnSquare) {
        long enemyOrthogonal = board.getOrthogonalSliders(enemyIndex);

        if (enemyOrthogonal != 0) {
            long maskedBlockers = (allPieces ^ (1L << capturedPawnSquare | 1L << startSquare | 1L << targetSquare));
            long rookAttacks = Magic.getRookAttacks(friendlyKingSquare, maskedBlockers);
            return (rookAttacks & enemyOrthogonal) != 0;
        }

        return false;
    }

    private List<BMove> generatePromotions(int startSquare, int targetSquare) {
        List<BMove> moves = new ArrayList<>();

        moves.add(new BMove(startSquare, targetSquare, BMove.promoteToQueenFlag));

        if (generateQuietMoves) {
            if (promotionMode == PromotionMode.All) {
                moves.add(new BMove(startSquare, targetSquare, BMove.promoteToKnightFlag));
                moves.add(new BMove(startSquare, targetSquare, BMove.promoteToRookFlag));
                moves.add(new BMove(startSquare, targetSquare, BMove.promoteToBishopFlag));

            } else if (promotionMode == PromotionMode.QueenAndKnight) {
                moves.add(new BMove(startSquare, targetSquare, BMove.promoteToKnightFlag));
            }
        }

        return moves;
    }

    private boolean isPinned(int square) {
        return ((pinRays >> square) & 1) != 0;
    }
}
