package knight.clubbing.data.bitboard.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FenHelper {

    public static final String startFen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static PositionData loadPositionData(String fen) {
        return new PositionData(fen);
    }

    public static String exportFen(BBoard board) {
        StringBuilder piecePlacement = new StringBuilder();

        for (int rank = 7; rank >= 0; rank--) {
            int empty = 0;
            for (int file = 0; file < 8; file++) {
                int index = rank * 8 + file;
                int piece = board.pieceBoards[index];

                if (piece == BPiece.none) {
                    empty++;
                } else {
                    if (empty > 0) {
                        piecePlacement.append(empty);
                        empty = 0;
                    }

                    char symbol = switch (BPiece.getPieceType(piece)) {
                        case BPiece.king -> 'k';
                        case BPiece.queen -> 'q';
                        case BPiece.rook -> 'r';
                        case BPiece.bishop -> 'b';
                        case BPiece.knight -> 'n';
                        case BPiece.pawn -> 'p';
                        default -> '?';
                    };

                    if (BPiece.isWhite(piece)) {
                        symbol = Character.toUpperCase(symbol);
                    }

                    piecePlacement.append(symbol);
                }
            }

            if (empty > 0) {
                piecePlacement.append(empty);
            }

            if (rank > 0) {
                piecePlacement.append('/');
            }
        }

        // Side to move
        String side = board.isWhiteToMove ? "w" : "b";

        // Castling rights
        StringBuilder castling = new StringBuilder();
        if (board.state.hasKingSideCastleRight(true)) castling.append("K");
        if (board.state.hasQueenSideCastleRight(true)) castling.append("Q");
        if (board.state.hasKingSideCastleRight(false)) castling.append("k");
        if (board.state.hasQueenSideCastleRight(false)) castling.append("q");
        if (castling.isEmpty()) castling.append("-");

        // En passant
        String ep = "-";
        int epFile = board.state.getEnPassantFile();
        if (epFile > 0 && epFile <= 8) {
            int rankOffset = board.isWhiteToMove ? 5 : 2;
            ep = BBoardHelper.fileChars.charAt(epFile - 1) + Integer.toString(rankOffset + 1);
        }

        int halfMoveClock = board.state.getFiftyMoveCounter();
        int fullMoveNumber = 1 + (board.getPlyCount() / 2);

        return String.format("%s %s %s %s %d %d",
                piecePlacement,
                side,
                castling,
                ep,
                halfMoveClock,
                fullMoveNumber
        );
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
            List<Integer> tempSquares = new ArrayList<>(Collections.nCopies(64, BPiece.none));

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
                    tempEpFile = BBoardHelper.fileChars.indexOf(enPassantFileName) + 1;
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

        public List<Integer> getSquares() {
            return squares;
        }

        public boolean isWhiteCastlingKingside() {
            return whiteCastlingKingside;
        }

        public boolean isWhiteCastlingQueenside() {
            return whiteCastlingQueenside;
        }

        public boolean isBlackCastlingKingside() {
            return blackCastlingKingside;
        }

        public boolean isBlackCastlingQueenside() {
            return blackCastlingQueenside;
        }

        public int getEpFile() {
            return epFile;
        }

        public boolean isWhiteToMove() {
            return whiteToMove;
        }

        public int getFiftyMovePlyCount() {
            return fiftyMovePlyCount;
        }

        public int getMoveCount() {
            return moveCount;
        }

        public String getFen() {
            return fen;
        }
    }
}
