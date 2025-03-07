package org.example.data;

import org.example.data.details.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private final Piece[][] board;

    private Color active;
    private String castlingRights;
    private Coord enPassantSquare;
    private int halfmoveClock;
    private int fullmoveNumber;

    public Board() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    public Board(String FEN) {
        this.board = new Piece[8][8];
        this.loadFEN(FEN);
    }

    private void loadFEN(String FEN) {
        String[] fields = FEN.split(" ");
        String[] ranks = fields[0].split("/");

        for (int rankIndex = 0; rankIndex < ranks.length; rankIndex++) {
            String rank = ranks[rankIndex];
            int fileIndex = 0;
            for (int i = 0; i < rank.length(); i++) {
                char c = rank.charAt(i);
                if (Character.isDigit(c)) {
                    fileIndex += Character.getNumericValue(c);
                } else {
                    PieceType pieceType = PieceType.fromFEN(c);
                    Color color = Character.isUpperCase(c) ? Color.WHITE : Color.BLACK;
                    board[7 - rankIndex][fileIndex] = new Piece(pieceType, color);
                    fileIndex++;
                }
            }
        }

        this.active = fields[1].equals("w") ? Color.WHITE : Color.BLACK;
        this.castlingRights = fields[2];

        this.enPassantSquare = null;
        if (!fields[3].equals("-") && fields[3].length() == 2)
            this.enPassantSquare = Coord.of(fields[3]);

        this.halfmoveClock = Integer.parseInt(fields[4]);
        this.fullmoveNumber = Integer.parseInt(fields[5]);
    }

    public String exportFEN() {
        StringBuilder fenBuilder = new StringBuilder();

        for (int rankIndex = 0; rankIndex < board.length; rankIndex++) {
            int emptyCount = 0;
            for (int fileIndex = 0; fileIndex < board[rankIndex].length; fileIndex++) {
                Piece piece = board[7 - rankIndex][fileIndex];
                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fenBuilder.append(emptyCount);
                        emptyCount = 0;
                    }
                    fenBuilder.append(piece.getChar());
                }
            }
            if (emptyCount > 0) {
                fenBuilder.append(emptyCount);
            }
            if (rankIndex < (board.length - 1)) {
                fenBuilder.append("/");
            }
        }

        fenBuilder.append(" ").append(active == Color.WHITE ? "w" : "b");
        fenBuilder.append(" ").append(castlingRights);
        fenBuilder.append(" ").append(enPassantSquare == null ? "-" : enPassantSquare.getNotation());
        fenBuilder.append(" ").append(halfmoveClock);
        fenBuilder.append(" ").append(fullmoveNumber);

        return fenBuilder.toString();
    }

    public String getDisplay() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = 0; j < board[i].length; j++) {
                Piece piece = board[i][j];
                if (piece == null) {
                    stringBuilder.append("-");
                } else {
                    stringBuilder.append(piece.getChar());
                }
                stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public String getColorDisplay() {
        final String RESET = "\u001B[0m";
        final String WHITE_PIECE = "\u001B[97m";
        final String BLACK_PIECE = "\u001B[34m";
        final String EMPTY = "\u001B[90m";

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = 0; j < board[i].length; j++) {
                Piece piece = board[i][j];
                if (piece == null) {
                    stringBuilder.append(EMPTY).append("-").append(RESET);
                } else {
                    String color = piece.color().equals(Color.WHITE) ? WHITE_PIECE : BLACK_PIECE;
                    stringBuilder.append(color).append(Character.toUpperCase(piece.getChar())).append(RESET);
                }
                stringBuilder.append("  ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }


    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPieceOn(Coord coord) {
        return this.board[coord.getY()][coord.getX()];
    }

    public void setPieceOn(Piece piece, Coord coord) {
        if (piece == null) return;
        this.board[coord.getY()][coord.getX()] = piece;
    }

    public int countPieces(Piece piece) {
        if (piece == null)
            return 0;

        return (int) Arrays.stream(this.board)
                .flatMap(Arrays::stream)
                .filter(piece::equals)
                .count();
    }

    public List<Coord> searchPieces(PieceType pieceType, Color color) {
        List<Coord> result = new ArrayList<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                Piece current = board[y][x];
                if (current != null &&
                        (pieceType == null || pieceType == current.pieceType()) &&
                        (color == null || color == current.color())) {
                    result.add(new Coord(x, y));
                }
            }
        }
        return result;
    }

    public boolean isValid(Coord coord) {
        return coord.getX() >= 0 && coord.getX() < 8 && coord.getY() >= 0 && coord.getY() < 8;
    }

    public boolean isEmpty(Coord coord) {
        return isValid(coord) && board[coord.getY()][coord.getX()] == null;
    }

    public boolean isFriendly(Coord coord, Color color) {
        return isValid(coord) && board[coord.getY()][coord.getX()] != null &&
                board[coord.getY()][coord.getX()].color() == color;
    }

    public boolean isEnemy(Coord coord, Color color) {
        return isValid(coord) && board[coord.getY()][coord.getX()] != null &&
                board[coord.getY()][coord.getX()].color() != color;
    }


    public void emptySquare(Coord coord) {
        this.board[coord.getY()][coord.getX()] = null;
    }

    public void setActive(Color color) {
        this.active = color;
    }

    public Color getActive() {
        return active;
    }

    public void setCastlingRights(String castlingRights) {
        this.castlingRights = castlingRights;
    }

    public String getCastlingRights() {
        return castlingRights;
    }

    public List<Castling> getCastlingRights(Color color) {
        List<Castling> moveTypes = new ArrayList<>();

        if (this.castlingRights.contains("-"))
            return moveTypes;

        if (Color.WHITE.equals(color)) {
            if (this.castlingRights.contains("K"))
                moveTypes.add(Castling.KING);

            if (this.castlingRights.contains("Q"))
                moveTypes.add(Castling.QUEEN);

        } else if (Color.BLACK.equals(color)) {
            if (this.castlingRights.contains("k"))
                moveTypes.add(Castling.KING);

            if (this.castlingRights.contains("q"))
                moveTypes.add(Castling.QUEEN);
        }

        return moveTypes;
    }

    public void removeCastlingRights(Color color, Castling castling) {
        String side = "";

        if (castling.equals(Castling.KING)) {
            side = "k";
        } else if (castling.equals(Castling.QUEEN)) {
            side = "q";
        }

        removeCastlingRights(color, side);
    }

    private void removeCastlingRights(Color color, String side) {
        if (color.equals(Color.WHITE))
            side = side.toUpperCase();

        removeCastlingRights(side);
    }

    private void removeCastlingRights(String side) {
        this.castlingRights = this.castlingRights.replace(side, "");
    }

    public void removeCastlingRights(Color color) {
        removeCastlingRights(color, Castling.KING);
        removeCastlingRights(color, Castling.QUEEN);
    }

    public void setEnPassantSquare(Coord enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public Coord getEnPassantSquare() {
        return enPassantSquare;
    }

    public int tickHalfmoveClock() {
        return ++this.halfmoveClock;
    }

    public void resetHalfmoveClock() {
        this.halfmoveClock = 1;
    }

    public void setHalfmoveClock(int i) {
        this.halfmoveClock = i;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int tickFullmoveNumber() {
        return ++this.fullmoveNumber;
    }

    public void setFullmoveNumber(int i ) {
        this.fullmoveNumber = i;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }
}
