package knight.clubbing.data.bitboard;

public class BBoardHelper {

    public static final String fileChars = "abcdefgh";
    public static final String rankChars = "12345678";

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

    public static int rankIndex(int squareIndex) {
        return squareIndex >> 3;
    }

    public static int fileIndex(int squareIndex) {
        return squareIndex & 0b000111;
    }
}
