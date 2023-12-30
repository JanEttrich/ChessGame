package core;

public class GameState {
    public static Player playerWhite;
    public static Player playerBlack;

    public static int movesWithoutCaptureAndPawnMove;

    public static void resetGameState(Player white, Player black) {
        playerWhite = white;
        playerBlack = black;
        movesWithoutCaptureAndPawnMove = 0;
    }
}
