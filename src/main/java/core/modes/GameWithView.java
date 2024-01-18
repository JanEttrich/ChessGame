package core.modes;

import core.Game;
import core.Pieces;
import core.move.Move;
import frontend.MoveResource;

import static core.Board.squares;

public class GameWithView extends Game {
    public GameWithView(String posFen, boolean humanOpponent) {
        super(posFen, humanOpponent);
    }

    public boolean handleMove(MoveResource moveResource) {
        var legalMoves = generate();
        for (Move move : legalMoves) {
            if (doesMoveMatchUiMove(move, moveResource)) {
                makeMoveAndUpdate(move);
                getGameState().update50MoveRule(move);
                return true;
            }
        }
        return false;
    }

    private boolean doesMoveMatchUiMove(Move move, MoveResource moveResource) {
        int startSquare = (7 - moveResource.getStartRow()) * 8 + moveResource.getStartCol();
        int endSquare = (7 - moveResource.getEndRow()) * 8 + moveResource.getEndCol();
        return squares[startSquare] != Pieces.NONE && move.getStartSquare() == startSquare && move.getEndSquare() == endSquare;
    }
}
