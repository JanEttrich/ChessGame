package core.pieces;

import core.Piece;
import core.Square;
import core.move.Move;
import core.move.PieceMovementHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class Rook extends Piece {

    // field to track if a rook has moved, for castling rights
    private boolean moved;

    public Rook(boolean white, boolean moved) {
        super(white);
        this.moved = moved;
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
