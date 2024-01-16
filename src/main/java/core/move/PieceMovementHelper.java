package core.move;

import core.GameState;
import core.Pieces;
import core.Player;

import java.util.ArrayList;
import java.util.List;

import static core.Board.squares;

public class PieceMovementHelper {
    private PieceMovementHelper() {
    }

    public static List<Move> getStraightMoves(int startSquare, int color) {
        List<Move> moves = new ArrayList<>();
        // up
        for (int endSquare = startSquare + 8; endSquare < 64; endSquare += 8) {
            if (!processSingleMove(startSquare, endSquare, color, moves)) {
                break;
            }
        }

        // down
        for (int endSquare = startSquare - 8; endSquare >= 0; endSquare -= 8) {
            if (!processSingleMove(startSquare, endSquare, color, moves)) {
                break;
            }
        }

        // left
        int leftBound = startSquare % 8;
        for (int endSquare = startSquare - 1; endSquare >= startSquare - leftBound; endSquare -= 1) {
            if (!processSingleMove(startSquare, endSquare, color, moves)) {
                break;
            }
        }


        int rightBound = 8 - leftBound;
        // right
        for (int endSquare = startSquare + 1; endSquare < startSquare + rightBound; endSquare++) {
            if (!processSingleMove(startSquare, endSquare, color, moves)) {
                break;
            }
        }
        return moves;
    }

    public static List<Move> getDiagonalMoves(int startSquare, int color) {
        List<Move> moves = new ArrayList<>();

        int leftBound = startSquare % 8;
        int rightBound = 8 - leftBound - 1;

        // left up
        for (int endSquare = startSquare + 7, maxToLeft = leftBound - 1; endSquare < 64 && maxToLeft >= 0; endSquare += 7, maxToLeft--) {
            if (!processSingleMove(startSquare, endSquare, color, moves)) {
                break;
            }
        }

        // right up
        for (int endSquare = startSquare + 9, maxToRight = rightBound - 1; endSquare < 64 && maxToRight >= 0; endSquare += 9, maxToRight--) {
            if (!processSingleMove(startSquare, endSquare, color, moves)) {
                break;
            }
        }

        // right down
        for (int endSquare = startSquare - 7, maxToRight = rightBound - 1; endSquare >= 0 && maxToRight >= 0; endSquare -= 7, maxToRight--) {
            if (!processSingleMove(startSquare, endSquare, color, moves)) {
                break;
            }
        }

        // left down
        for (int endSquare = startSquare - 9, maxToLeft = leftBound - 1; endSquare >= 0 && maxToLeft >= 0; endSquare -= 9, maxToLeft--) {
            if (!processSingleMove(startSquare, endSquare, color, moves)) {
                break;
            }
        }

        return moves;
    }

    public static List<Move> getKnightMoves(int startSquare, int color) {
        List<Move> moves = new ArrayList<>();

        int[] knightDirections;
        int leftBound = startSquare % 8;
        if (leftBound == 0) {
            knightDirections = new int[]{17, 10, -6, -15};
        } else if (leftBound == 1) {
            knightDirections = new int[]{17, 10, -6, -15, -17, 15};
        } else if (leftBound == 6) {
            knightDirections = new int[]{17, -15, -17, -10, 6, 15};
        } else if (leftBound == 7) {
            knightDirections = new int[]{-17, -10, 6, 15};
        } else {
            knightDirections = new int[]{17, 10, -6, -15, -17, -10, 6, 15};
        }

        for (int direction : knightDirections) {
            int endSquare = startSquare + direction;
            if (endSquare >= 0 && endSquare < 64 &&
                    (squares[endSquare] == Pieces.NONE ||
                            !Pieces.isPieceOfColor(squares[endSquare], color))) {
                moves.add(new Move(startSquare, endSquare));
            }
        }
        return moves;
    }

    public static List<Move> getKingMoves(int startSquare, int color) {
        List<Move> moves = new ArrayList<>();

        // one step in each direction
        int[] kingDirections;
        int leftBound = startSquare % 8;
        if (leftBound == 0) {
            kingDirections = new int[]{8, 9, 1, -7, -8};
        } else if (leftBound == 7) {
            kingDirections = new int[]{-8, -9, -1, 7, 8};
        } else {
            kingDirections = new int[]{7, 8, 9, 1, -7, -8, -9, -1};
        }

        for (int direction : kingDirections) {
            int endSquare = startSquare + direction;
            if (endSquare > 0 && endSquare < 64 &&
                    (squares[endSquare] == Pieces.NONE ||
                            !Pieces.isPieceOfColor(squares[endSquare], color))) {
                moves.add(new Move(startSquare, endSquare));
            }
        }

        // castling
        int requiredCastleRank = color == Pieces.WHITE ? 0 : 7;
        if (startSquare / 8 != requiredCastleRank || startSquare % 8 != 4) {
            return moves;
        }

        Player activePlayer = color == Pieces.WHITE ? GameState.playerWhite : GameState.playerBlack;
        if (activePlayer.isCastleShortAllowed() && squares[startSquare + 1] == Pieces.NONE && squares[startSquare + 2] == Pieces.NONE &&
                squares[startSquare + 3] != Pieces.NONE && squares[startSquare + 3] == (Pieces.ROOK | color)) {
            moves.add(new Move(startSquare, startSquare + 2, true, false));

        }
        if (activePlayer.isCastleLongAllowed() && squares[startSquare - 1] == Pieces.NONE && squares[startSquare - 2] == Pieces.NONE &&
                squares[startSquare - 3] == Pieces.NONE && squares[startSquare - 4] != Pieces.NONE && squares[startSquare - 4] == (Pieces.ROOK | color)) {
            moves.add(new Move(startSquare, startSquare - 2, false, true));
        }

        return moves;
    }

    public static List<Move> getPawnMoves(int startSquare, int color) {
        List<Move> moves = new ArrayList<>();

        int direction = color == Pieces.WHITE ? 8 : -8;
        int doubleMoveStart = color == Pieces.WHITE ? 1 : 6;
        int promotionMoveStart = color == Pieces.WHITE ? 6 : 1;
        int enPassantMoveStart = color == Pieces.WHITE ? 4 : 3;

        int startRank = startSquare / 8;
        int startFile = startSquare % 8;

        // one-step move (and promotion)
        if (squares[startSquare + direction] == Pieces.NONE) {
            if (startRank == promotionMoveStart) {
                moves.addAll(addPawnPromotion(startSquare, direction, 0, color));
            } else {
                moves.add(new Move(startSquare, startSquare + direction));
            }
        }
        // double move
        if (startRank == doubleMoveStart &&
                squares[startSquare + direction] == Pieces.NONE &&
                squares[startSquare + 2 * direction] == Pieces.NONE) {
            moves.add(new Move(startSquare, startSquare + 2 * direction));
        }
        // capture left (and promotion)
        if (startFile != 0 && squares[startSquare + direction - 1] != Pieces.NONE && !Pieces.isPieceOfColor(squares[startSquare + direction - 1], color)) {
            if (startRank == promotionMoveStart) {
                moves.addAll(addPawnPromotion(startSquare, direction, -1, color));
            } else {
                moves.add(new Move(startSquare, startSquare + direction - 1));
            }
        }

        // capture right (and promotion)
        if (startFile != 7 && squares[startSquare + direction + 1] != Pieces.NONE && !Pieces.isPieceOfColor(squares[startSquare + direction + 1], color)) {
            if (startRank == promotionMoveStart) {
                moves.addAll(addPawnPromotion(startSquare, direction, 1, color));
            } else {
                moves.add(new Move(startSquare, startSquare + direction + 1));
            }
        }

        // en passant
        if (startRank == enPassantMoveStart) {
            Move lastMove = MoveTracker.getLastMove();
            int enemyPawn = color == Pieces.WHITE ? Pieces.PAWN | Pieces.BLACK : Pieces.PAWN | Pieces.WHITE;
            // en passant right
            if (startFile != 7 &&
                    squares[startSquare + 1] == enemyPawn &&
                    lastMove.getEndSquare() == startSquare + 1 &&
                    lastMove.getStartSquare() == startSquare + 1 + 2 * direction) {
                moves.add(new Move(startSquare, startSquare + direction + 1, startSquare + 1, true));
                return moves;
            }

            if (startFile != 0 &&
                    squares[startSquare - 1] == enemyPawn &&
                    lastMove.getEndSquare() == startSquare - 1 &&
                    lastMove.getStartSquare() == startSquare - 1 + 2 * direction) {
                moves.add(new Move(startSquare, startSquare + direction - 1, startSquare - 1, true));
            }
        }
        return moves;
    }

    public static List<Move> addPawnPromotion(int startSquare, int direction, int fileOffset, int color) {
        var moves = new ArrayList<Move>();
        moves.add(new Move(startSquare, startSquare + direction + fileOffset,
                true, Pieces.QUEEN | color));
        moves.add(new Move(startSquare, startSquare + direction + fileOffset,
                true, Pieces.ROOK | color));
        moves.add(new Move(startSquare, startSquare + direction + fileOffset,
                true, Pieces.BISHOP | color));
        moves.add(new Move(startSquare, startSquare + direction + fileOffset,
                true, Pieces.KNIGHT | color));

        return moves;
    }

    public static boolean processSingleMove(int startSquare, int endSquare, int color, List<Move> moves) {
        if (squares[endSquare] != Pieces.NONE) {
            if (!Pieces.isPieceOfColor(squares[endSquare], color)) {
                moves.add(new Move(startSquare, endSquare));
            }
            return false;
        }
        moves.add(new Move(startSquare, endSquare));
        return true;
    }
}
