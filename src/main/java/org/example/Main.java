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
        Scanner scanner = new Scanner(System.in);
        ChessGame game = new ChessGame();
        Random random = new Random();

        while (true) {
            System.out.println("Current board:");
            System.out.println(game.getBoard().getColorDisplay());

            // Player move
            System.out.print("Enter your move (e.g., e2e4): ");
            String inputMove = scanner.nextLine();

            if (inputMove.equalsIgnoreCase("exit")) {
                System.out.println("Game exited.");
                break;
            }

            MoveDraft playerMove = parseMove(inputMove, game);
            if (playerMove == null || !game.submitMove(playerMove)) {
                System.out.println("Invalid move, try again.");
                continue;
            }

            // Check game state
            //if (isGameOver(game)) break;

            // Random AI move
            List<Move> possibleMoves = game.determineAllLegalMoves();
            if (possibleMoves.isEmpty()) {
                System.out.println("No legal moves for AI. Game over.");
                break;
            }
            Move aiMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
            System.out.println("AI moves: " + aiMove.from().getNotation() + aiMove.to().getNotation());
            game.submitMove(new MoveDraft(aiMove.pieceType(), aiMove.color(), aiMove.from(), aiMove.to(), aiMove.type()));

            // Check game state
            //if (isGameOver(game)) break;
        }
        scanner.close();
    }

    private static MoveDraft parseMove(String input, ChessGame game) {
        if (input.length() != 4) return null;
        try {
            Coord from = Coord.of(input.substring(0, 2));
            Coord to = Coord.of(input.substring(2, 4));
            Piece piece = game.getBoard().getPieceOn(from);
            return new MoveDraft(piece.pieceType(), piece.color(),from, to);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isGameOver(ChessGame game) {
        /*
        if (!game.hasLegalMoves(game.getBoard().getActive())) {
            System.out.println("Checkmate or Stalemate! Game Over.");
            return true;
        }
         */
        return false;
    }
}