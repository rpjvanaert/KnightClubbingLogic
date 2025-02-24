package org.example;

import org.example.data.details.Color;
import org.example.data.details.Coord;
import org.example.data.details.MoveType;
import org.example.data.details.PieceType;
import org.example.data.move.MoveDraft;
import org.example.logic.ChessGame;

public class Main {
    public static void main(String[] args) {
        ChessGame chessGame = new ChessGame();

        System.out.println(chessGame.getBoard().getDisplay());

        System.out.println(chessGame.submitMove(new MoveDraft(
                MoveType.NORMAL,
                PieceType.PAWN,
                Color.WHITE,
                new Coord(1, 2),
                new Coord(1, 2),
                ""
        )));

        System.out.println(chessGame.getBoard().getDisplay());
        System.out.println(chessGame.getBoard().getPieceOn(new Coord(0, 0)).getChar());
        System.out.println(chessGame.getBoard().getPieceOn(new Coord(3, 0)).getChar());
    }
}