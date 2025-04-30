package knight.clubbing.data.bitboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BBoard {

    public static int rowLength = 8;

    // Using BPiece for index, contains 64 bit bitboards for each piecetype+color
    private long[] bitboards;

    // 64-size int array. Representation for board using BPiece int-structure for pieces
    public int[] pieceBoard;

    // 64 bit bitboards for white/blackpieces
    private long[] colorBoards;
    private int[] kingSquares;
    private int whiteIndex = 0;
    private int blackIndex = 0;

    // 64 bit bitboard for every piece
    private long allPiecesBoard;

    // 64 bit bitboard for different type of pieces
    private long whiteOrthogonalSliderBoard;
    private long blackOrthogonalSliderBoard;
    private long whiteDiagonalSliderBoard;
    private long blackDiagonalSliderBoard;

    private int totalPieceCountWithoutPawnsAndKings;

    private List<BMove> allGameMoves;
    public BGameState state;
    private int plyCount;
    private Stack<Long> repetitionPositionHistory;
    private Stack<BGameState> gameStateHistory;

    private boolean cachedInCheckValue;
    private boolean hasCachedInCheckValue;

    public boolean isWhiteToMove;
    private int moveColor() {
        return isWhiteToMove ? BPiece.white : BPiece.black;
    }

    private int opponentColor() {
        return isWhiteToMove ? BPiece.black : BPiece.white;
    }

    private int moveColorIndex() {
        return isWhiteToMove ? whiteIndex : blackIndex;
    }

    private int opponentColorIndex() {
        return isWhiteToMove ? blackIndex : whiteIndex;
    }

    private long opponentDiagonalSliderBoard() {
        return isWhiteToMove ? blackDiagonalSliderBoard : whiteDiagonalSliderBoard;
    }

    private long opponentOrthogonalSliderBoard() {
        return isWhiteToMove ? blackOrthogonalSliderBoard : whiteOrthogonalSliderBoard;
    }

    public BBoard() {
        bitboards = new long[12];
        pieceBoard = new int[64];
        colorBoards = new long[2];
    }

    public void makeMove(BMove move) {
        this.makeMove(move, false);
    }

    public void makeMove(BMove move, boolean inSearch) {
        int startSquare = move.startSquare();
        int targetSquare = move.targetSquare();
        int moveFlag = move.moveFlag();
        boolean isPromotion = move.isPromotion();
        boolean isEnPassant = moveFlag == BMove.enPassantCaptureFlag;

        int movedPiece = pieceBoard[startSquare];
        int movedPieceType = BPiece.getPieceType(movedPiece);
        int capturedPiece = isEnPassant ? BPiece.makePiece(BPiece.pawn, isWhiteToMove? BPiece.white : BPiece.black) : pieceBoard[targetSquare];
        int capturedPieceType = BPiece.getPieceType(capturedPiece);

        int prevCastleRights = state.getCastlingRights();
        int prevEnpassantFile = state.getEnPassantFile();
        long zobristKey = state.getZobristKey();
        int newCastleRights = state.getCastlingRights();
        int newEnpassantFile = 0;

        this.move(movedPiece, startSquare, targetSquare);

        if (capturedPieceType != BPiece.none) {
            int captureSquare = targetSquare;

            if (isEnPassant) {
                captureSquare = targetSquare + (isWhiteToMove ? -rowLength : rowLength);
                pieceBoard[captureSquare] = BPiece.none;
            }

            if (capturedPieceType != BPiece.pawn) {
                totalPieceCountWithoutPawnsAndKings--;
            }

            this.clear(capturedPiece, capturedPieceType);
            zobristKey ^= BZobrist.getPiecesArray()[capturedPiece][captureSquare];
        }

        if (movedPieceType == BPiece.king) {
            kingSquares[moveColorIndex()] = targetSquare;
            newCastleRights &= isWhiteToMove ? 0b1100 : 0b0011;

            if (moveFlag == BMove.castleFlag) {

                int rook = BPiece.makePiece(BPiece.rook, moveColor());
                boolean kingside = targetSquare == BBoardHelper.g1 || targetSquare == BBoardHelper.g8;
                int castleRookFrom = kingside ? targetSquare + 1 : targetSquare - 2;
                int castleRookTo = kingside ? targetSquare - 1 : targetSquare + 1;

                this.move(rook, castleRookFrom, castleRookTo);
                zobristKey ^= BZobrist.getPiecesArray()[rook][castleRookFrom];
                zobristKey ^= BZobrist.getPiecesArray()[rook][castleRookTo];
            }
        }

        if (isPromotion) {
            totalPieceCountWithoutPawnsAndKings++;
            int promotionPiece = BPiece.makePiece(move.promotionPieceType(), moveColor());

            this.clear(movedPiece, targetSquare);
            this.set(promotionPiece, targetSquare);
        }

        if (moveFlag == BMove.pawnTwoUpFlag) {
            int enPassantFile = BBoardHelper.fileIndex(targetSquare);
            zobristKey ^= BZobrist.getEnPassantFile()[enPassantFile];
        }

        if (prevCastleRights != 0) {
            if (targetSquare == BBoardHelper.h1 || startSquare == BBoardHelper.h1) {
                newCastleRights &= BGameState.clearWhiteKingsideMask;

            } else if (targetSquare == BBoardHelper.a1 || startSquare == BBoardHelper.a1) {
                newCastleRights &= BGameState.clearWhiteQueensideMask;

            } else if (targetSquare == BBoardHelper.h8 || startSquare == BBoardHelper.h8) {
                newCastleRights &= BGameState.clearBlackKingsideMask;

            } else if (targetSquare == BBoardHelper.a8 || startSquare == BBoardHelper.a8) {
                newCastleRights &= BGameState.clearBlackQueensideMask;
            }
        }

        zobristKey ^= BZobrist.getSideToMove();
        zobristKey ^= BZobrist.getPiecesArray()[movedPiece][startSquare];
        zobristKey ^= BZobrist.getPiecesArray()[pieceBoard[targetSquare]][targetSquare];
        zobristKey ^= BZobrist.getEnPassantFile()[prevEnpassantFile];

        if (newCastleRights != prevCastleRights) {
            zobristKey ^= BZobrist.getCastlingRights()[prevCastleRights];
            zobristKey ^= BZobrist.getCastlingRights()[newCastleRights];
        }

        isWhiteToMove = !isWhiteToMove;

        plyCount++;
        int newFiftyMoveCounter = state.getFiftyMoveCounter() + 1;

        allPiecesBoard = colorBoards[whiteIndex] | colorBoards[blackIndex];
        //updateSliderBitboards();

        if (movedPieceType == BPiece.pawn || capturedPieceType != BPiece.none) {
            if (!inSearch) {
                repetitionPositionHistory.clear();
            }
            newFiftyMoveCounter = 0;
        }

        BGameState newState = new BGameState(capturedPieceType, newEnpassantFile, newCastleRights, newFiftyMoveCounter, zobristKey);
        gameStateHistory.push(newState);
        state = newState;
        hasCachedInCheckValue = false;

        if (!inSearch) {
            repetitionPositionHistory.push(newState.getZobristKey());
            allGameMoves.add(move);
        }
    }

    public void undoMove(BMove move) {
        undoMove(move, false);
    }

    public void undoMove(BMove move, boolean inSearch) {
        isWhiteToMove = !isWhiteToMove;

        boolean undoingWhiteMove = isWhiteToMove;

        int movedFrom = move.startSquare();
        int movedTo = move.startSquare();
        int moveFlag = move.moveFlag();

        boolean undoingEnpassant = moveFlag == BMove.enPassantCaptureFlag;
        boolean undoingPromotion = move.isPromotion();
        boolean undoingCapture = state.getCapturedPiece() != BPiece.none;

        int movedPiece = undoingPromotion ? BPiece.makePiece(BPiece.pawn, moveColor()) : pieceBoard[movedTo];
        int movedPieceType = BPiece.getPieceType(movedPiece);
        int capturedPieceType = BPiece.getPieceType(state.getCapturedPiece());

        if (undoingPromotion) {
            int promotionPiece = pieceBoard[movedTo];
            totalPieceCountWithoutPawnsAndKings--;

            this.clear(promotionPiece, movedTo);
            this.set(movedPiece, movedTo);
        }

        this.move(movedPiece, movedTo, movedFrom);

        if (undoingCapture) {
            int captureSquare = movedTo;
            int capturedPiece = BPiece.makePiece(capturedPieceType, opponentColor());

            if (undoingEnpassant) {
                captureSquare = movedTo + ((undoingWhiteMove) ? -rowLength : rowLength);
            }
            if (capturedPiece != BPiece.pawn) {
                totalPieceCountWithoutPawnsAndKings++;
            }

            pieceBoard[captureSquare] = capturedPiece;
            this.set(capturedPiece, captureSquare);
        }

        if (movedPieceType == BPiece.king) {
            kingSquares[moveColorIndex()] = movedFrom;

            if (moveFlag == BMove.castleFlag) {
                int rookPiece = BPiece.makePiece(BPiece.rook, moveColor());
                boolean kingside = movedTo == BBoardHelper.g1 || movedTo == BBoardHelper.g8;
                int rookSquareBeforeCastling = kingside ? movedTo + 1 : movedTo - 2;
                int rookSquareAfterCastling = kingside ? movedTo - 1 : movedTo + 1;

                this.move(rookPiece, rookSquareAfterCastling, rookSquareBeforeCastling);
            }
        }

        allPiecesBoard = colorBoards[whiteIndex] | colorBoards[blackIndex];
        //updateSliderBitboards();


        if (!inSearch && !repetitionPositionHistory.isEmpty()) {
            repetitionPositionHistory.pop();
        }
        if (!inSearch) {
            allGameMoves.remove(allGameMoves.size() - 1);
            allGameMoves.removeLast();
        }

        gameStateHistory.pop();
        state = gameStateHistory.peek();
        plyCount--;
        hasCachedInCheckValue = false;
    }

    public void makeNullMove() {
        isWhiteToMove = !isWhiteToMove;

        plyCount++;

        long newZobristKey = state.getZobristKey();
        newZobristKey ^= BZobrist.getSideToMove();
        newZobristKey ^= BZobrist.getEnPassantFile()[state.getEnPassantFile()]; //todo hier naar kijken

        BGameState newState = new BGameState(BPiece.none, 0, state.getCastlingRights(), state.getFiftyMoveCounter() + 1, newZobristKey);
        state = newState;
        gameStateHistory.push(state);
        //updateSliderBitboards();
        hasCachedInCheckValue = true;
        cachedInCheckValue = false;
    }

    public void undoNullMove() {
        isWhiteToMove = !isWhiteToMove;
        plyCount--;
        gameStateHistory.pop();
        //updateSliderBitboards();
        hasCachedInCheckValue = true;
        cachedInCheckValue = false;
    }

    public boolean isInCheck() {
        if (hasCachedInCheckValue) {
            return cachedInCheckValue;
        }
        cachedInCheckValue = calculateInCheckState();
        hasCachedInCheckValue = true;

        return cachedInCheckValue;
    }

    public boolean calculateInCheckState() {
        int kingSquare = kingSquares[moveColorIndex()];
        long blockers = allPiecesBoard;

        if (opponentOrthogonalSliderBoard() != 0) {
            //long rookAttacks = Magic.getRookAttacks(kingSquare, blockers)
            //if ((rookAttacks & opponentOrthogonalSliderBoard() != 0))
            //    return true;
        }

        if (opponentDiagonalSliderBoard() != 0) {
            //long bishopAttacks = Magic.getBishopAttacks(kingSquare, blockers)
            //if ((bishopAttacks & opponentDiagonal SliderBoard() != 0))
            //    return true;
        }

        long enemyKnights = pieceBoard[BPiece.makePiece(BPiece.knight, opponentColor())];
        //if ((BBoardHelper.knightAttacks(kingSquare) & enemyKnights) != 0)
        //    return true;

        long enemyPawns = pieceBoard[BPiece.makePiece(BPiece.pawn, opponentColor())];
        //long pawnAttackMask = isWhiteToMove ? BBoardHelper.whitePawnAttacks(kingSquare) : BBoardHelper.blackPawnAttacks(kingSquare);
        //if ((pawnAttackMask & enemyPawns) != 0)
        //    return true;

        return false;
    }

    public void loadStartPosition() {
        loadPosition(FenHelper.startFen);
    }

    public void loadPosition(String fen) {
        FenHelper.PositionData positionData = FenHelper.loadPositionData(fen);
        loadPosition(positionData);
    }

    public void loadPosition(FenHelper.PositionData positionData) {
        //
    }

    public void initialize() {
        allGameMoves = new ArrayList<>();
        kingSquares = new int[2];
        pieceBoard = new int[64];

        repetitionPositionHistory = new Stack<>();
        gameStateHistory = new Stack<>();

        state = new BGameState();
        plyCount = 0;

        totalPieceCountWithoutPawnsAndKings = 0;

        bitboards = new long[BPiece.maxPieceIndex + 1];
        colorBoards = new long[2];
        allPiecesBoard = 0;
    }

    public void set(int piece, int squareIndex) {
        checkPiece(piece);
        checkSquareIndex(squareIndex);
        bitboards[piece] |= 1L << squareIndex;
    }

    public void clear(int piece, int squareIndex) {
        checkPiece(piece);
        checkSquareIndex(squareIndex);
        bitboards[piece] &= ~(1L << squareIndex);
    }

    public boolean get(int pieceIndex, int squareIndex) {
        checkPiece(pieceIndex);
        checkSquareIndex(squareIndex);
        return (bitboards[pieceIndex] & (1L << squareIndex)) != 0;
    }

    public long getBitboard(int piece) {
        return bitboards[piece];
    }

    public void move(int piece, int fromSquare, int toSquare) {
        if (this.get(piece, fromSquare)) {
            this.clear(piece, fromSquare);
            this.set(piece, toSquare);
        } else
            throw new IllegalArgumentException("bitboard is empty there");
    }

    private static void checkPiece(int piece) {
        if (piece <= 0 || piece >= BPiece.maxPieceIndex)
            throw new IllegalArgumentException("pieceIndex out of range: " + piece);
    }

    private static void checkSquareIndex(int square) {
        if (square < 0 || square >= 64)
            throw new IllegalArgumentException("squareIndex out of range: " + square);
    }
}
