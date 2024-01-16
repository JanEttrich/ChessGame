package core.modes;

import core.Game;
import core.Pieces;
import core.move.Move;
import core.move.MoveMaker;
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
                MoveMaker.makeMove(move);
                updateCastlingRights(move);
                activePlayer = activePlayer == playerWhite ? playerBlack : playerWhite;
                return true;
            }
        }
        return false;
    }

    private boolean doesMoveMatchUiMove(Move move, MoveResource moveResource) {
        int startSquare = moveResource.getStartRow() * 8 + moveResource.getStartCol();
        int endSquare = moveResource.getEndRow() * 8 + moveResource.getEndCol();
        return squares[startSquare] != Pieces.NONE && move.getStartSquare() == startSquare && move.getEndSquare() == endSquare;
    }
}
