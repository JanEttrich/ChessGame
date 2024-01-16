package core;

import core.move.Move;
import core.move.PieceMovementHelper;

import java.util.ArrayList;
import java.util.List;

import static core.Board.squares;

public class Pieces {
    public static final int NONE = 0;
    public static final int PAWN = 1;
    public static final int KNIGHT = 2;
    public static final int BISHOP = 3;
    public static final int ROOK = 4;
    public static final int QUEEN = 5;
    public static final int KING = 6;

    public static final int WHITE = 8;
    public static final int BLACK = 16;

    public static int getPieceForSymbol(char symbol) {
        return switch (symbol) {
            case 'R' -> ROOK | WHITE;
            case 'r' -> ROOK | BLACK;
            case 'N' -> KNIGHT | WHITE;
            case 'n' -> KNIGHT | BLACK;
            case 'B' -> BISHOP | WHITE;
            case 'b' -> BISHOP | BLACK;
            case 'Q' -> QUEEN | WHITE;
            case 'q' -> QUEEN | BLACK;
            case 'K' -> KING | WHITE;
            case 'k' -> KING | BLACK;
            case 'P' -> PAWN | WHITE;
            case 'p' -> PAWN | BLACK;
            default -> NONE;
        };
    }

    public static char getSymbolForPiece(int piece) {
        int type = piece & 7;
        return switch (type) {
            case ROOK -> 'R';
            case KNIGHT -> 'N';
            case BISHOP -> 'B';
            case QUEEN -> 'Q';
            case KING -> 'K';
            case PAWN -> 'P';
            default -> '\0';
        };
    }

    public static boolean isPieceOfColor(int piece, int color) {
        return (piece & color) == 0;
    }

    public static List<Move> generatePseudoLegalMoves(int pos, int color) {
        switch (squares[pos]) {
            case ROOK -> PieceMovementHelper.getStraightMoves(pos, color);
            case QUEEN -> {
                List<Move> moves = PieceMovementHelper.getStraightMoves(pos, color);
                moves.addAll(PieceMovementHelper.getDiagonalMoves(pos, color));
            }
            case KNIGHT -> PieceMovementHelper.getKnightMoves(pos, color);
            case BISHOP -> PieceMovementHelper.getDiagonalMoves(pos, color);
            case PAWN -> PieceMovementHelper.getPawnMoves(pos, color);
            case KING -> PieceMovementHelper.getKingMoves(pos, color);
        }
        return new ArrayList<>();
    }

}
