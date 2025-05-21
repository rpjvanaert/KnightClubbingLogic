package knight.clubbing;

import java.util.*;

public class PerftDiff {

    public static void main(String[] args) {
        String yourOutput = """
            a5a4: 1
             a5a6: 1
             a5b6: 1
             b4b1: 1
             b4b2: 1
             b4b3: 1
             b4a4: 1
             b4c4: 1
             b4d4: 1
             b4e4: 1
             b4f4: 1
             e2e3: 1
             g2g3: 1
             e2e4: 1
             g2g4: 1
        """;

        String stockfishOutput = """
            e2e3: 1
                    g2g3: 1
                    a5a6: 1
                    e2e4: 1
                    g2g4: 1
                    b4b1: 1
                    b4b2: 1
                    b4b3: 1
                    b4a4: 1
                    b4c4: 1
                    b4d4: 1
                    b4e4: 1
                    b4f4: 1
                    a5a4: 1
        """;

        Map<String, Integer> yours = parsePerftOutput(yourOutput);
        Map<String, Integer> stockfish = parsePerftOutput(stockfishOutput);

        Set<String> allMoves = new TreeSet<>();
        allMoves.addAll(yours.keySet());
        allMoves.addAll(stockfish.keySet());
        int totalMissing = 0;
        int totalTooMuch = 0;

        for (String move : allMoves) {
            Integer y = yours.get(move);
            Integer s = stockfish.get(move);

            if (s != null && !Objects.equals(y, s)) {
                int diff = y - s;

                if (diff > 0) {
                    totalTooMuch += diff;
                } else {
                    totalMissing -= diff;
                }

                System.out.printf("%s => Yours: %s, Stockfish: %s -- Diff: %s%n", move, y, s, diff);
            } else if (s == null && y != null) {
                totalTooMuch += y;

                System.out.printf("%s => Yours: %s, Stockfish: %s -- Diff: %s%n", move, y, 0, y);
            }
        }

        System.out.printf("Too much: %s. Missing: %s%n", totalTooMuch, totalMissing);
    }

    static Map<String, Integer> parsePerftOutput(String raw) {
        Map<String, Integer> map = new HashMap<>();
        for (String line : raw.strip().split("\n")) {
            String[] parts = line.trim().split(":");
            if (parts.length == 2) {
                map.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
            }
        }
        return map;
    }
}

