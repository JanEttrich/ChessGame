package core;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static int[] squares;
    public static List<Integer> piecesPosWhite;
    public static List<Integer> piecePosBlack;

    public static final String RANKS = "12345678";
    public static final String FILES = "abcdefgh";

    public static void resetBoard() {
        squares = new int[64];
        for (int index = 0; index < 64; index++) {
            squares[index] = Pieces.NONE;
        }
    }

    public static void resetPieceLists() {
        piecesPosWhite = Board.getAllPiecePositionsOfPlayer(true);
        piecePosBlack = Board.getAllPiecePositionsOfPlayer(false);
    }

    public static String intToChessSquare(int index) {
        int rank = index / 8;
        int file = index % 8;
        return Character.toString(FILES.charAt(file)) + RANKS.charAt(rank);
    }

    public static List<Integer> getAllPiecePositionsOfPlayer(boolean white) {
        int color = white ? Pieces.WHITE : Pieces.BLACK;
        List<Integer> positions = new ArrayList<>();
        for (int index = 0; index < 64; index++) {
            if (squares[index] != 0 && (squares[index] & color) > 0) {
                positions.add(index);
            }
        }
        return positions;
    }
}
