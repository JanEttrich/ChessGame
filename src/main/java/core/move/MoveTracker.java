package core.move;

import java.util.ArrayList;
import java.util.List;

public class MoveTracker {
    protected static List<Move> moves = new ArrayList<>();

    private MoveTracker() {
    }

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

    public static boolean removeLastMove() {
        if (moves.isEmpty()) {
            return false;
        }
        moves.remove(moves.size() - 1);
        return true;
    }
}
