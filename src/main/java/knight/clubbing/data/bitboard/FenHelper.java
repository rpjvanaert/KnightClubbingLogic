package knight.clubbing.data.bitboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FenHelper {

    public static final String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static PositionData loadPositionData(String fen) {
        return new PositionData(fen);
    }



    public static class PositionData {
        private final String fen;
        private final List<Integer> squares;

        private final boolean whiteCastlingKingside;
        private final boolean whiteCastlingQueenside;
        private final boolean blackCastlingKingside;
        private final boolean blackCastlingQueenside;

        private final int epFile;
        private final boolean whiteToMove;

        private final int fiftyMovePlyCount;
        private final int moveCount;

        public PositionData(String fen) {
            this.fen = fen;
            List<Integer> tempSquares = new ArrayList<>(64);

            String[] sections = fen.split(" ");

            int file = 0;
            int rank = 7;

            for (char symbol : sections[0].toCharArray()) {

                if (symbol == '/') {
                    file = 0;
                    rank--;
                } else {
                    if (Character.isDigit(symbol)) {
                        file += Character.getNumericValue(symbol);
                    } else {
                        int pieceColor = Character.isUpperCase(symbol) ? BPiece.white : BPiece.black;
                        int pieceType = switch (Character.toLowerCase(symbol)) {
                            case 'k'-> BPiece.king;
                            case 'p'-> BPiece.pawn;
                            case 'n'-> BPiece.knight;
                            case 'b'-> BPiece.bishop;
                            case 'r'-> BPiece.rook;
                            case 'q'-> BPiece.queen;
                            default -> BPiece.none;
                        };

                        tempSquares.set(rank * 8 + file, BPiece.makePiece(pieceColor, pieceType));
                        file++;
                    }
                }
            }

            this.squares = Collections.unmodifiableList(tempSquares);

            whiteToMove = Objects.equals(sections[1], "w");

            String castlingRights = sections[2];
            whiteCastlingKingside = castlingRights.contains("K");
            whiteCastlingQueenside = castlingRights.contains("Q");
            blackCastlingKingside = castlingRights.contains("k");
            blackCastlingQueenside = castlingRights.contains("q");

            int tempEpFile = 0;
            int tempFiftyMovePlyCount = 0;
            int tempMoveCount = 0;

            if (sections.length > 3) {
                String enPassantFileName = sections[3].charAt(0) + "";
                if (BBoardHelper.fileChars.contains(enPassantFileName)) {
                    tempEpFile = BBoardHelper.fileChars.indexOf(enPassantFileName);
                }
            }

            if (sections.length > 4) {
                tempFiftyMovePlyCount = Integer.parseInt(sections[4]);
            }

            if (sections.length > 5) {
                tempMoveCount = Integer.parseInt(sections[5]);
            }

            epFile = tempEpFile;
            fiftyMovePlyCount = tempFiftyMovePlyCount;
            moveCount = tempMoveCount;
        }
    }
}
