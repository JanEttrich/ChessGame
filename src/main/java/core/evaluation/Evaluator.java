package core.evaluation;

import core.Board;
import core.Square;

import java.util.Map;

public class Evaluator {
    private static final Map<String, Integer> pieceValueByType = Map.of("P", 1, "N", 3, "B", 3, "R", 5, "Q", 9, "K", 0);

    public static int evaluate(Board board, boolean whiteToMove) {
        int whiteScore = calculateMaterialScore(board, true);
        int blackScore = calculateMaterialScore(board, false);

        int evaluation = whiteScore - blackScore;

        return whiteToMove ? evaluation : -evaluation;
    }

    protected static int calculateMaterialScore(Board board, boolean white) {
        int sum = 0;
        Square[][] squares = board.getSquares();
        for (Square[] square : squares) {
            for (int j = 0; j < squares[0].length; j++) {
                if (square[j].isOccupied() && square[j].getPiece().isWhite() == white) {
                    sum += pieceValueByType.get(square[j].getPiece().getDisplay().toUpperCase());
                }
            }
        }
        return sum;
    }
}
