package core;

import core.move.Move;
import lombok.Data;

@Data
public class GameState {
    private Player playerWhite;
    private Player playerBlack;
    private int movesWithoutCaptureAndPawnMove;

    public GameState(boolean humanOpponent) {
        playerWhite = new Player(true, true);
        playerBlack = new Player(false, humanOpponent);
        movesWithoutCaptureAndPawnMove = 0;
    }

    public void update50MoveRule(Move move) {
        if (move.getPiece() != (Pieces.PAWN & 7) || Boolean.FALSE.equals(move.getStandardCapture()) || Boolean.FALSE.equals(move.getEnPassant())) {
            movesWithoutCaptureAndPawnMove += 1;
        }
    }
}
