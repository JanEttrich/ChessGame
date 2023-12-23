package util;

import core.pieces.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveTracker {
    private MoveTracker() {
    }

    protected static List<Move> moves = new ArrayList<>();

    public static void resetMoves() {
        moves = new ArrayList<>();
    }

    public static void addMove(Move move) {
        moves.add(move);
    }

    public static Move getLastMove() {
        if (moves.isEmpty()) {
            return null;
        }
        return moves.get(moves.size() - 1);
    }
}
