package rpjvanaert;

import java.util.*;

public class PerftDiff {

    public static void main(String[] args) {
        String yourOutput = """
            e8e7: 40
                    e8d8: 45
                    e8f8: 45
                    a8b8: 45
                    a8c8: 45
                    a8d8: 45
                    h8h2: 44
                    h8h3: 43
                    h8h4: 44
                    h8h5: 45
                    h8h6: 45
                    h8h7: 45
                    h8f8: 45
                    h8g8: 45
                    a6e2: 40
                    a6d3: 43
                    a6c4: 43
                    a6b5: 44
                    a6b7: 45
                    a6c8: 45
                    g7h6: 45
                    g7f8: 45
                    b6a4: 44
                    b6c4: 43
                    b6d5: 46
                    b6c8: 45
                    f6e4: 47
                    f6g4: 45
                    f6d5: 47
                    f6h5: 46
                    f6h7: 46
                    f6g8: 46
                    b4b3: 46
                    e6e5: 43
                    c7c6: 46
                    d7d6: 44
                    c7c5: 46
                    b4c3: 45
                    e6d5: 46
                    g2g1q: 2
                    g2g1n: 45
                    g2g1r: 42
                    g2g1b: 45
                    g2h1q: 2
                    g2h1n: 45
                    g2h1r: 42
                    g2h1b: 45
        """;

        String stockfishOutput = """
            b4b3: 46
                               e6e5: 43
                               c7c6: 46
                               d7d6: 44
                               c7c5: 46
                               g2h1q: 2
                               g2h1r: 2
                               g2h1b: 45
                               g2h1n: 45
                               g2g1q: 2
                               g2g1r: 2
                               g2g1b: 45
                               g2g1n: 45
                               e6d5: 46
                               b4c3: 45
                               b6a4: 44
                               b6c4: 43
                               b6d5: 46
                               b6c8: 45
                               f6e4: 47
                               f6g4: 45
                               f6d5: 47
                               f6h5: 46
                               f6h7: 46
                               f6g8: 46
                               a6e2: 40
                               a6d3: 43
                               a6c4: 43
                               a6b5: 44
                               a6b7: 45
                               a6c8: 45
                               g7h6: 45
                               g7f8: 45
                               a8b8: 45
                               a8c8: 45
                               a8d8: 45
                               h8h2: 44
                               h8h3: 43
                               h8h4: 44
                               h8h5: 45
                               h8h6: 45
                               h8h7: 45
                               h8f8: 45
                               h8g8: 45
                               e8e7: 40
                               e8d8: 45
                               e8f8: 45
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

            if (s != null && y != null && !Objects.equals(y, s)) {
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
            } else if (y == null && s != null) {
                totalMissing += s;
                System.out.printf("%s => Yours: %s, Stockfish: %s -- Diff: -%s%n", move, 0, s, s);
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

