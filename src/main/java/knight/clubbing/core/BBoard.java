package knight.clubbing.core;

import knight.clubbing.movegen.MoveUtility;
import knight.clubbing.movegen.magic.Magic;

import java.util.*;

public class BBoard {

    // Using BPiece for index, contains 64 bit bitboards for each piecetype+color
    private long[] bitboards;

    // 64-size int array. Representation for board using BPiece int-structure for pieces
    public int[] pieceBoards;

    // 64 bit bitboards for white/blackpieces
    private long[] colorBoards;
    private int[] kingSquares;
    public static final int whiteIndex = 0;
    public static final int blackIndex = 1;

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
    private Deque<Long> repetitionPositionHistory;
    private Deque<BGameState> gameStateHistory;

    private boolean cachedInCheckValue;
    private boolean hasCachedInCheckValue;

    public boolean isWhiteToMove;
    public int moveColor() {
        return isWhiteToMove ? BPiece.white : BPiece.black;
    }

    public int opponentColor() {
        return isWhiteToMove ? BPiece.black : BPiece.white;
    }

    public int moveColorIndex() {
        return isWhiteToMove ? whiteIndex : blackIndex;
    }

    public int opponentColorIndex() {
        return isWhiteToMove ? blackIndex : whiteIndex;
    }

    private long opponentDiagonalSliderBoard() {
        return isWhiteToMove ? blackDiagonalSliderBoard : whiteDiagonalSliderBoard;
    }

    private long opponentOrthogonalSliderBoard() {
        return isWhiteToMove ? blackOrthogonalSliderBoard : whiteOrthogonalSliderBoard;
    }

    public BBoard(String fen) {
        initialize();
        loadPosition(fen);
    }

    public BBoard() {
        initialize();
        loadStartPosition();
    }

    public void makeMove(BMove move, boolean inSearch) {
        int startSquare = move.startSquare();
        int targetSquare = move.targetSquare();
        int moveFlag = move.moveFlag();
        boolean isPromotion = move.isPromotion();
        boolean isEnPassant = moveFlag == BMove.enPassantCaptureFlag;

        int movedPiece = pieceBoards[startSquare];
        int movedPieceType = BPiece.getPieceType(movedPiece);
        int targetPiece = pieceBoards[targetSquare];
        int capturedPiece = isEnPassant ? BPiece.makePiece(BPiece.pawn, opponentColor()) : targetPiece;
        int capturedPieceType = BPiece.getPieceType(capturedPiece);

        int prevCastleRights = state.getCastlingRights();
        int prevEnPassantFile = state.getEnPassantFile();
        long zobristKey = state.getZobristKey();
        int newCastleRights = state.getCastlingRights();
        int newEnPassantFile = 0;


        if (capturedPieceType != BPiece.none) {
            int captureSquare = targetSquare;

            if (isEnPassant) {
                captureSquare = targetSquare + (isWhiteToMove ? -BBoardHelper.rowLength : BBoardHelper.rowLength);
                this.clear(capturedPiece, captureSquare);
                pieceBoards[captureSquare] = BPiece.none;
                zobristKey ^= BZobrist.getPiecesArray()[capturedPiece][captureSquare];
            }

            if (capturedPieceType != BPiece.pawn) {
                totalPieceCountWithoutPawnsAndKings--;
            }

            zobristKey ^= BZobrist.getPiecesArray()[capturedPiece][captureSquare];
            this.clear(capturedPiece, captureSquare);
        }

        this.move(movedPiece, startSquare, targetSquare);
        zobristKey ^= BZobrist.getPiecesArray()[movedPiece][startSquare];
        zobristKey ^= BZobrist.getPiecesArray()[movedPiece][targetSquare];

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
            zobristKey ^= BZobrist.getPiecesArray()[movedPiece][targetSquare];
            zobristKey ^= BZobrist.getPiecesArray()[promotionPiece][targetSquare];
        }

        if (moveFlag == BMove.pawnTwoUpFlag && isEnPassantPossible(targetSquare)) {
            int enPassantFile = BBoardHelper.fileIndex(targetSquare) + 1;
            newEnPassantFile = enPassantFile;
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
        zobristKey ^= BZobrist.getEnPassantFile()[prevEnPassantFile];

        if (newCastleRights != prevCastleRights) {
            zobristKey ^= BZobrist.getCastlingRights()[prevCastleRights];
            zobristKey ^= BZobrist.getCastlingRights()[newCastleRights];
        }

        isWhiteToMove = !isWhiteToMove;

        plyCount++;
        int newFiftyMoveCounter = state.getFiftyMoveCounter() + 1;


        updateOtherBoards();

        if (movedPieceType == BPiece.pawn || capturedPieceType != BPiece.none) {
            if (!inSearch) {
                repetitionPositionHistory.clear();
            }
            newFiftyMoveCounter = 0;
        }

        BGameState newState = new BGameState(capturedPieceType, newEnPassantFile, newCastleRights, newFiftyMoveCounter, zobristKey);
        gameStateHistory.push(newState);
        state = newState;
        hasCachedInCheckValue = false;

        if (!inSearch) {
            repetitionPositionHistory.push(newState.getZobristKey());
            allGameMoves.add(move);
        }
    }

    public void undoMove(BMove move, boolean inSearch) {
        isWhiteToMove = !isWhiteToMove;

        boolean undoingWhiteMove = isWhiteToMove;

        int movedFrom = move.startSquare();
        int movedTo = move.targetSquare();
        int moveFlag = move.moveFlag();

        boolean undoingEnpassant = moveFlag == BMove.enPassantCaptureFlag;
        boolean undoingPromotion = move.isPromotion();
        boolean undoingCapture = state.getCapturedPiece() != BPiece.none;

        int movedPiece = undoingPromotion ? BPiece.makePiece(BPiece.pawn, moveColor()) : pieceBoards[movedTo];
        int movedPieceType = BPiece.getPieceType(movedPiece);
        int capturedPieceType = BPiece.getPieceType(state.getCapturedPiece());

        if (undoingPromotion) {
            int promotionPiece = pieceBoards[movedTo];
            totalPieceCountWithoutPawnsAndKings--;

            this.clear(promotionPiece, movedTo);
            this.set(movedPiece, movedTo);
        }

        this.move(movedPiece, movedTo, movedFrom);

        if (undoingCapture) {
            int captureSquare = movedTo;
            int capturedPiece = BPiece.makePiece(capturedPieceType, opponentColor());

            if (undoingEnpassant) {
                captureSquare = movedTo + ((undoingWhiteMove) ? -BBoardHelper.rowLength : BBoardHelper.rowLength);
            }
            if (capturedPiece != BPiece.pawn) {
                totalPieceCountWithoutPawnsAndKings++;
            }

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
        updateOtherBoards();


        if (!inSearch && !repetitionPositionHistory.isEmpty()) {
            repetitionPositionHistory.pop();
        }
        if (!inSearch) {
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

        state = new BGameState(BPiece.none, 0, state.getCastlingRights(), state.getFiftyMoveCounter() + 1, newZobristKey);
        gameStateHistory.push(state);
        updateOtherBoards();
        hasCachedInCheckValue = true;
        cachedInCheckValue = false;
    }

    public void undoNullMove() {
        isWhiteToMove = !isWhiteToMove;
        plyCount--;
        gameStateHistory.pop();
        updateOtherBoards();
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
            long rookAttacks = Magic.getRookAttacks(kingSquare, blockers);
            if ((rookAttacks & opponentOrthogonalSliderBoard()) != 0L)
                return true;
        }

        if (opponentDiagonalSliderBoard() != 0) {
            long bishopAttacks = Magic.getBishopAttacks(kingSquare, blockers);
            if ((bishopAttacks & opponentDiagonalSliderBoard()) != 0L)
                return true;
        }

        long enemyKnights = bitboards[BPiece.makePiece(BPiece.knight, opponentColor())];
        if ((MoveUtility.KnightAttacks[kingSquare] & enemyKnights) != 0)
            return true;

        long enemyPawns = bitboards[BPiece.makePiece(BPiece.pawn, opponentColor())];
        long pawnAttackMask = isWhiteToMove ? MoveUtility.WhitePawnAttacks[kingSquare] : MoveUtility.BlackPawnAttacks[kingSquare];
        if ((pawnAttackMask & enemyPawns) != 0)
            return true;

        return false;
    }

    private void updateOtherBoards() {
        colorBoards[whiteIndex] = 0L;
        colorBoards[blackIndex] = 0L;
        for (int piece : BPiece.pieceIndices) {
            if (BPiece.isWhite(piece)) {
                colorBoards[whiteIndex] |= bitboards[piece];
            } else {
                colorBoards[blackIndex] |= bitboards[piece];
            }
        }

        allPiecesBoard = colorBoards[whiteIndex] | colorBoards[blackIndex];

        whiteOrthogonalSliderBoard = bitboards[BPiece.whiteRook] | bitboards[BPiece.whiteQueen];
        whiteDiagonalSliderBoard = bitboards[BPiece.whiteBishop] | bitboards[BPiece.whiteQueen];

        blackOrthogonalSliderBoard = bitboards[BPiece.blackRook] | bitboards[BPiece.blackQueen];
        blackDiagonalSliderBoard = bitboards[BPiece.blackBishop] | bitboards[BPiece.blackQueen];
    }

    public void loadStartPosition() {
        loadPosition(FenHelper.startFen);
    }

    public void loadPosition(String fen) {
        FenHelper.PositionData positionData = FenHelper.loadPositionData(fen);
        loadPosition(positionData);
    }

    public void loadPosition(FenHelper.PositionData posData) {
        for (int index = 0; index < 64; index++) {
            int piece = posData.getSquares().get(index);
            int pieceType = BPiece.getPieceType(piece);
            int colorIndex = BPiece.isWhite(piece) ? whiteIndex : blackIndex;
            pieceBoards[index] = piece;

            if (piece != BPiece.none) {
                this.set(piece, index);

                if (pieceType == BPiece.king) {
                    kingSquares[colorIndex] = index;
                } else if (pieceType != BPiece.pawn) {
                    totalPieceCountWithoutPawnsAndKings++;
                }
            }
        }

        isWhiteToMove = posData.isWhiteToMove();

        allPiecesBoard = colorBoards[whiteIndex] | colorBoards[blackIndex];
        updateOtherBoards();

        int whiteCastle = (posData.isWhiteCastlingKingside() ? 1 << 0 : 0) | (posData.isWhiteCastlingQueenside() ? 1 << 1 : 0);
        int blackCastle = (posData.isBlackCastlingKingside() ? 1 << 2 : 0) | (posData.isBlackCastlingQueenside() ? 1 << 3 : 0);
        int castlingRights = whiteCastle | blackCastle;

        plyCount = (posData.getMoveCount() - 1) * 2 + (isWhiteToMove ? 0 : 1);

        state = new BGameState(BPiece.none, posData.getEpFile(), castlingRights, posData.getFiftyMovePlyCount(), 0);
        long zobristKey = BZobrist.CalculateZobristKey(this);
        state = new BGameState(BPiece.none, posData.getEpFile(), castlingRights, posData.getFiftyMovePlyCount(), zobristKey);

        repetitionPositionHistory.push(zobristKey);

        gameStateHistory.push(state);
    }

    public String exportFen() {
        return FenHelper.exportFen(this);
    }

    private void initialize() {
        allGameMoves = new ArrayList<>();
        kingSquares = new int[2];
        pieceBoards = new int[64];

        repetitionPositionHistory = new ArrayDeque<>();
        gameStateHistory = new ArrayDeque<>();

        state = new BGameState();
        plyCount = 0;

        totalPieceCountWithoutPawnsAndKings = 0;

        bitboards = new long[BPiece.maxPieceIndex + 1];
        colorBoards = new long[2];
        allPiecesBoard = 0;
    }

    private boolean isEnPassantPossible(int targetSquare) {
        int file = BBoardHelper.fileIndex(targetSquare);
        int epCaptureRank = isWhiteToMove ? 3 : 4;

        int fileLeft = file - 1;
        int fileRight = file + 1;
        if (fileLeft >= 0) {
            int opponentSquare = BBoardHelper.indexFromCoord(fileLeft, epCaptureRank);
            if (get(BPiece.makePiece(BPiece.pawn, opponentColor()), opponentSquare)) {
                return true;
            }
        }

        if (fileRight <= 7) {
            int opponentSquare = BBoardHelper.indexFromCoord(fileRight, epCaptureRank);
            if (get(BPiece.makePiece(BPiece.pawn, opponentColor()), opponentSquare)) {
                return true;
            }
        }
        return false;
    }


    public void set(int piece, int squareIndex) { //add colorbitboards?
        checkPiece(piece);
        checkSquareIndex(squareIndex);
        bitboards[piece] |= 1L << squareIndex;
        pieceBoards[squareIndex] = piece;
    }

    public void clear(int piece, int squareIndex) { //add colorbitboards?
        checkPiece(piece);
        checkSquareIndex(squareIndex);
        bitboards[piece] &= ~(1L << squareIndex);
        pieceBoards[squareIndex] = BPiece.none;
    }

    public boolean get(int piece, int squareIndex) { //add colorbitboards?
        checkPiece(piece);
        checkSquareIndex(squareIndex);
        return (bitboards[piece] & (1L << squareIndex)) != 0;
    }

    public long getBitboard(int piece) {
        return bitboards[piece];
    }

    public long[] getBitboards() {
        return bitboards;
    }

    public long[] getCopyBitboards() {
        return Arrays.copyOf(bitboards, bitboards.length);
    }

    public int[] getPieceBoards() {
        return pieceBoards;
    }

    public int[] getCopyPieceBoards() {
        return Arrays.copyOf(pieceBoards, pieceBoards.length);
    }

    public int getPlyCount() {
        return plyCount;
    }

    public int getKingSquare(int colorIndex) {
        if (colorIndex != whiteIndex && colorIndex != blackIndex)
            throw new IndexOutOfBoundsException();
        return kingSquares[colorIndex];
    }

    public long getColorBitboard(int colorIndex) {
        if (colorIndex != whiteIndex && colorIndex != blackIndex)
            throw new IndexOutOfBoundsException();
        return colorBoards[colorIndex];
    }

    public long getAllPiecesBoard() {
        return allPiecesBoard;
    }

    public List<BMove> getAllGameMoves() {
        return allGameMoves;
    }

    public long getDiagonalSliders(int colorIndex) {
        if (colorIndex == whiteIndex)
            return whiteDiagonalSliderBoard;

        if (colorIndex == blackIndex)
            return blackDiagonalSliderBoard;

        throw new IndexOutOfBoundsException();
    }

    public long getOrthogonalSliders(int colorIndex) {
        if (colorIndex == whiteIndex)
            return whiteOrthogonalSliderBoard;

        if (colorIndex == blackIndex)
            return blackOrthogonalSliderBoard;

        throw new IndexOutOfBoundsException();
    }

    public void move(int piece, int fromSquare, int toSquare) {
        if (this.get(piece, fromSquare)) {
            this.clear(piece, fromSquare);
            this.set(piece, toSquare);
        } else
            throw new IllegalArgumentException("bitboard is empty there");
    }

    private static void checkPiece(int piece) {
        if (piece <= 0 || piece > BPiece.maxPieceIndex)
            throw new IllegalArgumentException("pieceIndex out of range (of " + BPiece.maxPieceIndex + "): " + piece);
    }

    private static void checkSquareIndex(int square) {
        if (square < 0 || square >= 64)
            throw new IllegalArgumentException("squareIndex out of range: " + square);
    }

    public String getDisplay() {
        StringBuilder sb = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                int index = rank * 8 + file;
                int piece = pieceBoards[index];
                if (piece == BPiece.none) {
                    sb.append("-  ");
                } else {
                    if (BPiece.isWhite(piece)) {
                        sb.append(Character.toUpperCase(BPiece.getChar(piece))).append("  ");
                    } else {
                        sb.append(BPiece.getChar(piece)).append("  ");
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public BBoard(BBoard other) {
        this.bitboards = Arrays.copyOf(other.bitboards, other.bitboards.length);
        this.pieceBoards = Arrays.copyOf(other.pieceBoards, other.pieceBoards.length);
        this.colorBoards = Arrays.copyOf(other.colorBoards, other.colorBoards.length);
        this.kingSquares = Arrays.copyOf(other.kingSquares, other.kingSquares.length);

        this.allPiecesBoard = other.allPiecesBoard;
        this.whiteOrthogonalSliderBoard = other.whiteOrthogonalSliderBoard;
        this.blackOrthogonalSliderBoard = other.blackOrthogonalSliderBoard;
        this.whiteDiagonalSliderBoard = other.whiteDiagonalSliderBoard;
        this.blackDiagonalSliderBoard = other.blackDiagonalSliderBoard;
        this.totalPieceCountWithoutPawnsAndKings = other.totalPieceCountWithoutPawnsAndKings;

        this.allGameMoves = new ArrayList<>(other.allGameMoves);

        this.state = new BGameState(other.state);
        this.plyCount = other.plyCount;

        this.repetitionPositionHistory = new ArrayDeque<>(other.repetitionPositionHistory);
        this.gameStateHistory = new ArrayDeque<>();
        for (BGameState s : other.gameStateHistory) {
            this.gameStateHistory.push(new BGameState(s));
        }

        this.cachedInCheckValue = other.cachedInCheckValue;
        this.hasCachedInCheckValue = other.hasCachedInCheckValue;
        this.isWhiteToMove = other.isWhiteToMove;
    }

    public BBoard copy() {
        return new BBoard(this);
    }
}
