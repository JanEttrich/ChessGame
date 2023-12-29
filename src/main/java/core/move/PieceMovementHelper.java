package core.move;

import core.Square;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PieceMovementHelper {
    public static final List<Map.Entry<Integer, Integer>> movementLShaped = new ArrayList<>();

    static {
        movementLShaped.add(new AbstractMap.SimpleEntry<>(2, 1));
        movementLShaped.add(new AbstractMap.SimpleEntry<>(1, 2));
        movementLShaped.add(new AbstractMap.SimpleEntry<>(2, -1));
        movementLShaped.add(new AbstractMap.SimpleEntry<>(1, -2));
        movementLShaped.add(new AbstractMap.SimpleEntry<>(-2, -1));
        movementLShaped.add(new AbstractMap.SimpleEntry<>(-2, 1));
        movementLShaped.add(new AbstractMap.SimpleEntry<>(-1, 2));
        movementLShaped.add(new AbstractMap.SimpleEntry<>(-1, -2));
    }

    private PieceMovementHelper() {
    }

    public static List<Move> getStraightMoves(Square[][] squares, Square startSquare, boolean white) {
        List<Move> moves = new ArrayList<>();
        int startRank = startSquare.getRank();
        int startFile = startSquare.getFile();

        // up
        for (int rank = startRank - 1; rank >= 0; rank--) {
            var endSquare = squares[rank][startFile];
            if (!processSingleMove(startSquare, endSquare, white, moves)) {
                break;
            }
        }

        // down
        for (int rank = startRank + 1; rank < 8; rank++) {
            var endSquare = squares[rank][startFile];
            if (!processSingleMove(startSquare, endSquare, white, moves)) {
                break;
            }
        }

        // left
        for (int file = startFile - 1; file >= 0; file--) {
            var endSquare = squares[startRank][file];
            if (!processSingleMove(startSquare, endSquare, white, moves)) {
                break;
            }
        }

        // right
        for (int file = startFile + 1; file < 8; file++) {
            var endSquare = squares[startRank][file];
            if (!processSingleMove(startSquare, endSquare, white, moves)) {
                break;
            }
        }

        return moves;
    }

    public static List<Move> getDiagonalMoves(Square[][] squares, Square startSquare, boolean white) {
        List<Move> moves = new ArrayList<>();
        int startRank = startSquare.getRank();
        int startFile = startSquare.getFile();

        // left up
        for (int rank = startRank - 1, file = startFile - 1; rank >= 0 && file >= 0; rank--, file--) {
            var endSquare = squares[rank][file];
            if (!processSingleMove(startSquare, endSquare, white, moves)) {
                break;
            }
        }

        // right up
        for (int rank = startRank - 1, file = startFile + 1; rank >= 0 && file < 8; rank--, file++) {
            var endSquare = squares[rank][file];
            if (!processSingleMove(startSquare, endSquare, white, moves)) {
                break;
            }
        }

        // right down
        for (int rank = startRank + 1, file = startFile + 1; rank < 8 && file < 8; rank++, file++) {
            var endSquare = squares[rank][file];
            if (!processSingleMove(startSquare, endSquare, white, moves)) {
                break;
            }
        }

        // left down
        for (int rank = startRank + 1, file = startFile - 1; rank < 8 && file >= 0; rank++, file--) {
            var endSquare = squares[rank][file];
            if (!processSingleMove(startSquare, endSquare, white, moves)) {
                break;
            }
        }

        return moves;
    }

    public static boolean processSingleMove(Square startSquare, Square endSquare, boolean white, List<Move> moves) {
        if (endSquare.isOccupied()) {
            var colorMatch = endSquare.getPiece().isWhite() == white;
            if (!colorMatch) {
                moves.add(new Move(startSquare, endSquare));
            }
            return false;
        }
        moves.add(new Move(startSquare, endSquare));
        return true;
    }

    public static boolean checkSquareAvailability(Square[][] squares, int row, int col, boolean white) {
        return !squares[row][col].isOccupied() || !squares[row][col].getPiece().isWhite() == white;
    }


    public static boolean checkIfSquareOnBoard(int targetRow, int targetCol) {
        return targetRow < 8 && targetRow >= 0 && targetCol < 8 && targetCol >= 0;
    }
}
