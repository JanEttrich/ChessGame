package core.evaluation;

import core.Pieces;

import java.util.List;
import java.util.Map;

import static core.Board.*;

public class Evaluator {
    private Evaluator() {
    }

    public static final Map<Integer, Integer> pieceValueByType = Map.of(Pieces.PAWN, 1, Pieces.KNIGHT, 3,
            Pieces.BISHOP, 3, Pieces.ROOK, 5, Pieces.QUEEN, 9, Pieces.KING, 0);

    public static int evaluate(boolean whiteToMove) {
        int whiteScore = calculateMaterialScore(true);
        int blackScore = calculateMaterialScore(false);

        int evaluation = whiteScore - blackScore;

        return whiteToMove ? evaluation : -evaluation;
    }

    protected static int calculateMaterialScore(boolean white) {
        int sum = 0;
        List<Integer> pieceList = white ? piecesPosWhite : piecePosBlack;
        for (Integer pos : pieceList) {
            sum += pieceValueByType.get(squares[pos] & 7);
        }
        return sum;
    }
}
