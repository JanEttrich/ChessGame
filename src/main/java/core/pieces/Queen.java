package core.pieces;

import core.Piece;
import core.Square;
import util.PieceMovementHelper;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "Q" : "q";
    }

    @Override
    public List<Move> getLegalMovesForPiece(Square[][] squares, Square startSquare) {
        var moves = new ArrayList<Move>();
        moves.addAll(PieceMovementHelper.getStraightMoves(squares, startSquare, isWhite()));
        moves.addAll(PieceMovementHelper.getDiagonalMoves(squares, startSquare, isWhite()));
        
        return moves;
    }
}
