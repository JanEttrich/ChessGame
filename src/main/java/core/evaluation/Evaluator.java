package core.evaluation;

import core.Pieces;

import java.util.Map;

import static core.Board.squares;

public class Evaluator {
    private Evaluator() {
    }

    private static final Map<Integer, Integer> pieceValueByType = Map.of(Pieces.PAWN, 1, Pieces.KNIGHT, 3,
            Pieces.BISHOP, 3, Pieces.ROOK, 5, Pieces.QUEEN, 9, Pieces.KING, 0);

    public static int evaluate(boolean whiteToMove) {
        int whiteScore = calculateMaterialScore(true);
        int blackScore = calculateMaterialScore(false);

        int evaluation = whiteScore - blackScore;

        return whiteToMove ? evaluation : -evaluation;
    }

    protected static int calculateMaterialScore(boolean white) {
        int sum = 0;
        int color = white ? Pieces.WHITE : Pieces.BLACK;
        for (int i = 0; i < 64; i++) {
            if (squares[i] != Pieces.NONE && Pieces.isPieceOfColor(squares[i], color)) {
                sum += pieceValueByType.get(squares[i] & 7);
            }
        }
        return sum;
    }
}
