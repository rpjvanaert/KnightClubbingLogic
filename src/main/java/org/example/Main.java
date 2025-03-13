package org.example;

import org.example.data.details.*;
import org.example.data.move.Move;
import org.example.data.move.MoveDraft;
import org.example.logic.ChessGame;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int depth = Integer.parseInt(args[0]);
        String fen = args[1];

        ChessGame game = new ChessGame(fen);

        System.out.println(game.perft(depth));

        //java -jar KnightClubbingLogic.jar 3 "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
    }
}