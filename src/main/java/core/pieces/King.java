package core.pieces;

import core.Piece;
import core.Square;
import core.move.Move;
import core.move.PieceMovementHelper;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "K" : "k";
    }

    @Override
    public List<Move> getPseudoLegalMovesForPiece(Square[][] squares, Square startSquare) {
        // TODO: Castle
        // TODO: Checkmate
        var moves = new ArrayList<Move>();
        int startRank = startSquare.getRank();
        int startFile = startSquare.getFile();

        // Test all one step in each direction
        for (int i = startRank - 1; i <= startRank + 1; i++) {
            for (int j = startFile - 1; j <= startFile + 1; j++) {
                if (PieceMovementHelper.checkIfSquareOnBoard(i, j)
                        && PieceMovementHelper.checkSquareAvailability(squares, i, j, isWhite())) {
                    moves.add(new Move(startSquare, squares[i][j]));
                }
            }
        }
        return moves;
    }
}
