package core;

import core.move.Move;

public class GameState {
    public static Player playerWhite;
    public static Player playerBlack;

    public static int movesWithoutCaptureAndPawnMove;

    public static void resetGameState(Player white, Player black) {
        playerWhite = white;
        playerBlack = black;
        movesWithoutCaptureAndPawnMove = 0;
    }

    public static void update50MoveRule(Move move) {
        if (move.getPiece() != (Pieces.PAWN & 7) || Boolean.FALSE.equals(move.getStandardCapture()) || Boolean.FALSE.equals(move.getEnPassant())) {
            movesWithoutCaptureAndPawnMove += 1;
        }
    }
}
