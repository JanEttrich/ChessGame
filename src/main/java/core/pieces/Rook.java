package core.pieces;

import core.Piece;
import core.Square;
import core.move.Move;
import core.move.PieceMovementHelper;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "R" : "r";
    }

    @Override
    public List<Move> getPseudoLegalMovesForPiece(Square[][] squares, Square startSquare) {
        return new ArrayList<>(PieceMovementHelper.getStraightMoves(squares, startSquare, isWhite()));
    }
}
