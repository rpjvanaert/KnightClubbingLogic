package knight.clubbing.moveGeneration;

import knight.clubbing.FileUtil;
import knight.clubbing.core.BBoard;
import knight.clubbing.core.BBoardHelper;
import knight.clubbing.core.BCoord;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class PrecomputedMoveData implements Serializable{

    public static void main(String[] args) {
        PrecomputedMoveData.getInstance();
    }

    @Serial
    private static final long serialVersionUID = 1L;
    private static final String FILE_NAME = "precomputed_data.bin";
    private static final Path PATH = Path.of(FILE_NAME);

    private long[][] alignMask;
    private long[][] dirRayMask;

    private final int[] directionOffsets = { 8, -8, -1, 1, 7, -7, 9, -9 };

    private final BCoord[] dirOffsets2D = {
            new BCoord(0, 1),
            new BCoord(0, -1),
            new BCoord(-1, 0),
            new BCoord(1, 0),
            new BCoord(-1, 1),
            new BCoord(1, -1),
            new BCoord(1, 1),
            new BCoord(-1, -1)
    };

    private int[][] numSquaresToEdge;

    private byte[][] knightMoves;
    private byte[][] kingMoves;

    private final byte[][] pawnAttackDirections = {
            new byte[] { 4, 6 },
            new byte[] { 7, 5 }
    };

    private int[][] pawnAttacksWhite;
    private int[][] pawnAttacksBlack;
    private int[] directionLookup;

    private long[] kingAttackBitboards;
    private long[] knightAttackBitboards;
    private long[][] pawnAttackBitboards;

    private long[] rookMoves;
    private long[] bishopMoves;
    private long[] queenMoves;

    private int[][] orthogonalDistance;
    private int[][] kingDistance;
    private int[] centreManhattanDistance;

    private static PrecomputedMoveData instance;

    public static PrecomputedMoveData getInstance() {
        if (instance == null) {
            instance = loadOrGenerate();
        }

        return instance;
    }

    public static PrecomputedMoveData loadOrGenerate() {
        PrecomputedMoveData data;
        if ((data = FileUtil.load(PrecomputedMoveData.class, PATH)) == null) {
            data = new PrecomputedMoveData();
            data.computeAll();
            FileUtil.save(data, PATH);
        }

        return data;
    }

    private void computeAll() {
        pawnAttacksWhite = new int[64][];
        pawnAttacksBlack = new int[64][];
        numSquaresToEdge = new int[8][];
        knightMoves = new byte[64][];
        kingMoves = new byte[64][];
        numSquaresToEdge = new int[64][];

        rookMoves = new long[64];
        bishopMoves = new long[64];
        queenMoves = new long[64];

        // Calculate knight jumps and available squares for each square on the board.
        // See comments by variable definitions for more info.
        int[] allKnightJumps = { 15, 17, -17, -15, 10, -6, 6, -10 };
        knightAttackBitboards = new long[64];
        kingAttackBitboards = new long[64];
        pawnAttackBitboards = new long[64][];

        for (int squareIndex = 0; squareIndex < 64; squareIndex++) {

            int y = squareIndex / 8;
            int x = squareIndex - y * 8;

            int north = 7 - y;
            int south = y;
            int west = x;
            int east = 7 - x;
            numSquaresToEdge[squareIndex] = new int[8];
            numSquaresToEdge[squareIndex][0] = north;
            numSquaresToEdge[squareIndex][1] = south;
            numSquaresToEdge[squareIndex][2] = west;
            numSquaresToEdge[squareIndex][3] = east;
            numSquaresToEdge[squareIndex][4] = Math.min(north, west);
            numSquaresToEdge[squareIndex][5] = Math.min(south, east);
            numSquaresToEdge[squareIndex][6] = Math.min(north, east);
            numSquaresToEdge[squareIndex][7] = Math.min(south, west);

            List<Byte> knightJumps = new ArrayList<Byte>();
            long knightBitboard = 0;
            for (int knightJumpDelta : allKnightJumps) {
                int knightJumpSquare = squareIndex + knightJumpDelta;
                if (knightJumpSquare >= 0 && knightJumpSquare < 64) {
                    int knightSquareY = knightJumpSquare / 8;
                    int knightSquareX = knightJumpSquare - knightSquareY * 8;
                    int maxCoordDist = Math.max(Math.abs(x - knightSquareX), Math.abs(y - knightSquareY));
                    if (maxCoordDist == 2) {
                        knightJumps.add((byte) knightJumpSquare);
                        knightBitboard |= 1L << knightJumpSquare;
                    }
                }
            }

            knightMoves[squareIndex] = toByteArray(knightJumps);
            knightAttackBitboards[squareIndex] = knightBitboard;

            List<Byte> legalKingMoves = new ArrayList<>();

            for (int kingMoveDelta : directionOffsets) {
                int kingMoveSquare = squareIndex + kingMoveDelta;
                if (kingMoveSquare >= 0 && kingMoveSquare < 64) {
                    int kingSquareY = kingMoveSquare / 8;
                    int kingSquareX = kingMoveSquare - kingSquareY * 8;
                    int maxCoordDist = Math.max(Math.abs(x - kingSquareX), Math.abs(y - kingSquareY));
                    if (maxCoordDist == 1) {
                        legalKingMoves.add((byte) kingMoveSquare);
                        kingAttackBitboards[squareIndex] |= 1L << kingMoveSquare;
                    }
                }
            }

            kingMoves[squareIndex] = toByteArray(legalKingMoves);

            List<Integer> pawnCapturesWhite = new ArrayList<>();
            List<Integer> pawnCapturesBlack = new ArrayList<>();
            pawnAttackBitboards[squareIndex] = new long[2];

            if (x > 0) {
                if (y < 7) {
                    pawnCapturesWhite.add(squareIndex + 7);
                    pawnAttackBitboards[squareIndex][BBoard.whiteIndex] |= 1L << (squareIndex + 7);
                }
                if (y > 0) {
                    pawnCapturesBlack.add(squareIndex - 9);
                    pawnAttackBitboards[squareIndex][BBoard.blackIndex] |= 1L << (squareIndex - 9);
                }
            }

            if (x < 7) {
                if (y < 7) {
                    pawnCapturesWhite.add(squareIndex + 9);
                    pawnAttackBitboards[squareIndex][BBoard.whiteIndex] |= 1L << (squareIndex + 9);
                }
                if (y > 0) {
                    pawnCapturesBlack.add(squareIndex - 7);
                    pawnAttackBitboards[squareIndex][BBoard.blackIndex] |= 1L << (squareIndex - 7);
                }
            }

            pawnAttacksWhite[squareIndex] = pawnCapturesWhite.stream().mapToInt(i -> i).toArray();
            pawnAttacksBlack[squareIndex] = pawnCapturesBlack.stream().mapToInt(i -> i).toArray();

            for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
                int currentDirOffset = directionOffsets[directionIndex];

                for (int n = 0; n < numSquaresToEdge[squareIndex][directionIndex]; n++) {
                    int targetSquare = squareIndex + currentDirOffset * (n + 1);
                    rookMoves[squareIndex] |= 1L << targetSquare;
                }
            }

            for (int directionIndex = 4; directionIndex < 8; directionIndex++) {
                int currentDirOffset = directionOffsets[directionIndex];

                for (int n = 0; n < numSquaresToEdge[squareIndex][directionIndex]; n++) {
                    int targetSquare = squareIndex + currentDirOffset * (n + 1);
                    bishopMoves[squareIndex] |= 1L << targetSquare;
                }
            }
            queenMoves[squareIndex] = rookMoves[squareIndex] | bishopMoves[squareIndex];
        }

        directionLookup = new int[127];
        for (int i = 0; i < 127; i++) {

            int offset = i - 63;
            int absOffset = Math.abs(offset);
            int absDir = 1;

            if (absOffset % 9 == 0) {
                absDir = 9;

            } else if (absOffset % 8 == 0) {
                absDir = 8;

            } else if (absOffset % 7 == 0) {
                absDir = 7;
            }

            directionLookup[i] = absDir * Integer.signum(offset);
        }

        orthogonalDistance = new int[64][64];
        kingDistance = new int[64][64];
        centreManhattanDistance = new int[64];

        for (int squareA = 0; squareA < 64; squareA++) {
            BCoord coordA = BBoardHelper.coordFromIndex(squareA);
            int fileDstFromCentre = Math.max(3 - coordA.getFileIndex(), coordA.getFileIndex() - 4);
            int rankDstFromCentre = Math.max(3 - coordA.getRankIndex(), coordA.getRankIndex() - 4);
            centreManhattanDistance[squareA] = fileDstFromCentre + rankDstFromCentre;

            for (int squareB = 0; squareB < 64; squareB++) {
                BCoord coordB = BBoardHelper.coordFromIndex(squareB);
                int rankDistance = Math.abs(coordA.getRankIndex() - coordB.getRankIndex());
                int fileDistance = Math.abs(coordA.getFileIndex() - coordB.getFileIndex());
                orthogonalDistance[squareA][squareB] = fileDistance + rankDistance;
                kingDistance[squareA][squareB] = Math.max(fileDistance, rankDistance);
            }
        }

        calculateAlignMask();

        dirRayMask = new long[8][64];
        for (int dirIndex = 0; dirIndex < dirOffsets2D.length; dirIndex++) {
            for (int squareIndex = 0; squareIndex < 64; squareIndex++) {

                BCoord square = BBoardHelper.coordFromIndex(squareIndex);

                for (int i = 0; i < 8; i++) {
                    BCoord coord = square.add(dirOffsets2D[dirIndex].multiply(i));
                    if (coord.isValidSquare()) {
                        dirRayMask[dirIndex][squareIndex] |= 1L << BBoardHelper.indexFromCoord(coord);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void calculateAlignMask() {
        alignMask = new long[64][64];
        for (int squareA = 0; squareA < 64; squareA++) {
            for (int squareB = 0; squareB < 64; squareB++) {

                if (squareA == squareB) {
                    alignMask[squareA][squareB] = 1L << squareA;
                    continue;
                }

                BCoord coordA = BBoardHelper.coordFromIndex(squareA);
                BCoord coordB = BBoardHelper.coordFromIndex(squareB);
                BCoord delta = coordB.subtract(coordA);

                if (
                        delta.getFileIndex() != 0 &&
                        delta.getRankIndex() != 0 &&
                        Math.abs(delta.getRankIndex()) != Math.abs(delta.getFileIndex())
                ) {
                    alignMask[squareA][squareB] = 0;
                    continue;
                }

                BCoord dir = new BCoord(Integer.signum(delta.getFileIndex()), Integer.signum(delta.getRankIndex()));


                for (int i = -8; i < 8; i++) {

                    if (i == 0)
                        continue;

                    BCoord coord = BBoardHelper.coordFromIndex(squareA).add(dir.multiply(i));
                    if (coord.isValidSquare()) {
                        alignMask[squareA][squareB] |= 1L << BBoardHelper.indexFromCoord(coord);
                    }
                }

                alignMask[squareA][squareB] |= (1L << squareA) | (1L << squareB);
            }
        }
    }

    private byte[] toByteArray(List<Byte> byteList) {
        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = byteList.get(i);
        }
        return byteArray;
    }

    public long[][] getAlignMask() {
        return alignMask;
    }

    public long[][] getDirRayMask() {
        return dirRayMask;
    }

    public BCoord[] getDirOffsets2D() {
        return dirOffsets2D;
    }

    public int[][] getNumSquaresToEdge() {
        return numSquaresToEdge;
    }

    public byte[][] getKnightMoves() {
        return knightMoves;
    }

    public byte[][] getKingMoves() {
        return kingMoves;
    }

    public byte[][] getPawnAttackDirections() {
        return pawnAttackDirections;
    }

    public int[][] getPawnAttacksWhite() {
        return pawnAttacksWhite;
    }

    public int[][] getPawnAttacksBlack() {
        return pawnAttacksBlack;
    }

    public int[] getDirectionLookup() {
        return directionLookup;
    }

    public long[] getKingAttackBitboards() {
        return kingAttackBitboards;
    }

    public long[] getKnightAttackBitboards() {
        return knightAttackBitboards;
    }

    public long[][] getPawnAttackBitboards() {
        return pawnAttackBitboards;
    }

    public long[] getRookMoves() {
        return rookMoves;
    }

    public long[] getBishopMoves() {
        return bishopMoves;
    }

    public long[] getQueenMoves() {
        return queenMoves;
    }

    public int[][] getOrthogonalDistance() {
        return orthogonalDistance;
    }

    public int[][] getKingDistance() {
        return kingDistance;
    }

    public int[] getCentreManhattanDistance() {
        return centreManhattanDistance;
    }

    public int[] getDirectionOffsets() {
        return directionOffsets;
    }
}
