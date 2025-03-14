package knight.clubbing;

import knight.clubbing.data.details.*;
import knight.clubbing.data.move.Move;
import knight.clubbing.data.move.MoveDraft;
import knight.clubbing.logic.ChessGame;

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