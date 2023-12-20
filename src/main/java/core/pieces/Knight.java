package core.pieces;

import core.Piece;
import core.Square;
import util.PieceMovementHelper;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "N" : "n";
    }

    @Override
    public List<Move> getLegalMovesForPiece(Square[][] squares, Square startSquare) {
        var moves = new ArrayList<Move>();
        int startRank = startSquare.getRank();
        int startFile = startSquare.getFile();

        for (var entry : PieceMovementHelper.movementLShaped) {
            int targetRow = startRank + entry.getKey();
            int targetCol = startFile + entry.getValue();
            if (PieceMovementHelper.checkIfSquareOnBoard(targetRow, targetCol) &&
                    PieceMovementHelper.checkSquareAvailability(squares, targetRow, targetCol, isWhite())) {
                moves.add(new Move(startSquare, squares[targetRow][targetCol]));
            }
        }

        return moves;
    }
}
