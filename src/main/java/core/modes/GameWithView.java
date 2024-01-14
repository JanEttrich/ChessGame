package core.modes;

import core.Game;
import core.Square;
import core.move.Move;
import core.move.MoveMaker;
import frontend.MoveResource;

public class GameWithView extends Game {
    public GameWithView(String posFen, boolean humanOpponent) {
        super(posFen, humanOpponent);
    }

    public boolean handleMove(MoveResource moveResource) {
        var legalMoves = generate(activePlayer.isWhite());
        for (Move move : legalMoves) {
            if (doesMoveMatchUiMove(move, moveResource)) {
                if ((Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) &&
                        threatenedOrCastlesThroughCheck(move)) {
                    return false;
                }

                MoveMaker.makeMove(move, activePlayer.isWhite(), board);
                if (canKingCanBeCaptured()) {
                    MoveMaker.unmakeMove(move, activePlayer.isWhite(), board);
                    return false;
                }
                updateCastlingRights(move);
                activePlayer = activePlayer == playerWhite ? playerBlack : playerWhite;
                return true;
            }
        }
        return false;
    }

    private boolean doesMoveMatchUiMove(Move move, MoveResource moveResource) {
        Square startSquare = move.getStartSquare();
        Square endSquare = move.getEndSquare();
        return startSquare.isOccupied() && startSquare.getRank() == moveResource.getStartRow() &&
                startSquare.getFile() == moveResource.getStartCol() &&
                endSquare.getRank() == moveResource.getEndRow() && endSquare.getFile() == moveResource.getEndCol();
    }
}
