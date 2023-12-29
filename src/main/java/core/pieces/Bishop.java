package core.pieces;

import core.Piece;
import core.Square;
import core.move.Move;
import core.move.PieceMovementHelper;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "B" : "b";
    }

    @Override
    public List<Move> getPseudoLegalMovesForPiece(Square[][] squares, Square startSquare) {
        return new ArrayList<>(PieceMovementHelper.getDiagonalMoves(squares, startSquare, isWhite()));
    }
}
