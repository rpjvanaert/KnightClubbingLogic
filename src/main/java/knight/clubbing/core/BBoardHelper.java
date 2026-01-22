package knight.clubbing.core;

public class BBoardHelper {

    public static final String fileChars = "abcdefgh";
    public static final String rankChars = "12345678";

    public static final BCoord[] ROOK_DIRECTIONS = { new BCoord(-1, 0), new BCoord(1, 0), new BCoord(0, 1), new BCoord(0, -1) };
    public static final BCoord[] BISHOP_DIRECTIONS = { new BCoord(-1, 1), new BCoord(1, 1), new BCoord(1, -1), new BCoord(-1, -1) };

    public static int rowLength = 8;

    public static final int a1 = 0;
    public static final int b1 = 1;
    public static final int c1 = 2;
    public static final int d1 = 3;
    public static final int e1 = 4;
    public static final int f1 = 5;
    public static final int g1 = 6;
    public static final int h1 = 7;

    public static final int a8 = 56;
    public static final int b8 = 57;
    public static final int c8 = 58;
    public static final int d8 = 59;
    public static final int e8 = 60;
    public static final int f8 = 61;
    public static final int g8 = 62;
    public static final int h8 = 63;

    public static final long allBitsSet = 0xFFFFFFFFFFFFFFFFL;

    public static final long FILE_A = 0x0101010101010101L;
    public static final long FILE_B = 0x0202020202020202L;
    public static final long FILE_C = 0x0404040404040404L;
    public static final long FILE_D = 0x0808080808080808L;
    public static final long FILE_E = 0x1010101010101010L;
    public static final long FILE_F = 0x2020202020202020L;
    public static final long FILE_G = 0x4040404040404040L;
    public static final long FILE_H = 0x8080808080808080L;

    public static final long[] FILE_MASKS = {
            FILE_A, FILE_B, FILE_C, FILE_D,
            FILE_E, FILE_F, FILE_G, FILE_H
    };

    public static int rankIndex(int squareIndex) {
        return squareIndex >> 3;
    }

    public static int fileIndex(int squareIndex) {
        return squareIndex & 0b000111;
    }

    public static int stringCoordToIndex(String stringCoord) {
        if (stringCoord == null || stringCoord.length() != 2) {
            throw new IllegalArgumentException("Invalid coordinate: " + stringCoord);
        }

        char fileChar = stringCoord.charAt(0);
        char rankChar = stringCoord.charAt(1);

        int file = fileChars.indexOf(fileChar);
        int rank = rankChars.indexOf(rankChar);

        if (file == -1 || rank == -1) {
            throw new IllegalArgumentException("Invalid coordinate: " + stringCoord);
        }

        return rank * 8 + file;
    }

    public static String indexToStringCoord(int index) {
        if (index < 0 || index > 63) {
            throw new IllegalArgumentException("Invalid square index: " + index);
        }

        int file = index % 8;
        int rank = index / 8;

        return "" + fileChars.charAt(file) + rankChars.charAt(rank);
    }

    public static int indexFromCoord(int fileIndex, int rankIndex) {
        return rankIndex * 8 + fileIndex;
    }

    public static int indexFromCoord(BCoord coord) {
        return indexFromCoord(coord.getFileIndex(), coord.getRankIndex());
    }

    public static BCoord coordFromIndex(int squareIndex) {
        return new BCoord(fileIndex(squareIndex), rankIndex(squareIndex));
    }

    public static boolean containsSquare(long bitboard, int squareIndex) {
        return ((bitboard >> squareIndex) & 1) != 0;
    }

    public static long setSquare(long bitboard, int squareIndex) {
        return bitboard | 1L << squareIndex;
    }

    public static String bitboardToString(long bitboard) {
        return String.format("%64s", Long.toBinaryString(bitboard)).replace(' ', '0');
    }

    public static String bitboardToDisplay(long bitboard) {
        StringBuilder sb = new StringBuilder();

        String binary = String.format("%64s", Long.toBinaryString(bitboard)).replace(' ', '0');
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                int index = rank * 8 + file;
                sb.append(binary.charAt(index)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
