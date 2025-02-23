package org.example.data;

import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.Piece;
import org.example.data.details.PieceType;

import java.util.Arrays;

public class Board {

    private Piece[][] board;

    private Color active;
    private String castlingRights;
    private String enPassantSquare;
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
                    Color color = Character.isLowerCase(c) ? Color.WHITE : Color.BLACK;
                    board[rankIndex][fileIndex] = new Piece(pieceType, color);
                    fileIndex++;
                }
            }
        }

        this.active = fields[1].equals("w") ? Color.WHITE : Color.BLACK;
        this.castlingRights = fields[2];
        this.enPassantSquare = fields[3];
        this.halfmoveClock = Integer.parseInt(fields[4]);
        this.fullmoveNumber = Integer.parseInt(fields[5]);
    }

    public String exportFEN() {
        StringBuilder fenBuilder = new StringBuilder();

        for (int rankIndex = 0; rankIndex < board.length; rankIndex++) {
            int emptyCount = 0;
            for (int fileIndex = 0; fileIndex < board[rankIndex].length; fileIndex++) {
                Piece piece = board[rankIndex][fileIndex];
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
        fenBuilder.append(" ").append(enPassantSquare);
        fenBuilder.append(" ").append(halfmoveClock);
        fenBuilder.append(" ").append(fullmoveNumber);

        return fenBuilder.toString();
    }

    public String getDisplay() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Piece[] rank : board) {
            for (Piece piece : rank) {
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

    public void setEnPassantSquare(String enPassantSquare) {
        this.enPassantSquare = enPassantSquare;
    }

    public String getEnPassantSquare() {
        return enPassantSquare;
    }

    public int tickHalfmoveClock() {
        return ++this.halfmoveClock;
    }

    public void resetHalfmoveClock() {
        this.halfmoveClock = 1;
    }

    public int getHalfmoveClock() {
        return halfmoveClock;
    }

    public int tickFullmoveNumber() {
        return ++this.fullmoveNumber;
    }

    public int getFullmoveNumber() {
        return fullmoveNumber;
    }
}
