package core.pieces;

import core.Piece;
import core.Square;
import util.PieceMovementHelper;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "P" : "p";
    }

    @Override
    public List<Move> getLegalMovesForPiece(Square[][] squares, Square startSquare) {
        // TODO: en passant
        // TODO: promotion

        var moves = new ArrayList<Move>();
        int startRank = startSquare.getRank();
        int startFile = startSquare.getFile();

        int direction = isWhite() ? -1 : 1;
        int doubleMoveStart = isWhite() ? 6 : 1;

        // one-step move
        if (!squares[startRank + direction][startFile].isOccupied()) {
            moves.add(new Move(startSquare, squares[startRank + direction][startFile]));
        }
        // double move
        if (startRank == doubleMoveStart &&
                !squares[doubleMoveStart + direction][startFile].isOccupied() &&
                !squares[doubleMoveStart + 2 * direction][startFile].isOccupied()) {
            moves.add(new Move(startSquare, squares[doubleMoveStart + 2 * direction][startSquare.getFile()]));
        }
        // capture right
        if (PieceMovementHelper.checkIfSquareOnBoard(startRank + direction, startFile + 1) &&
                checkPawnCapture(squares, startRank, startFile, direction, 1)) {
            moves.add(new Move(startSquare, squares[startRank - 1][startFile + 1]));
        }
        // capture left
        if (PieceMovementHelper.checkIfSquareOnBoard(startRank + direction, startFile - 1) &&
                checkPawnCapture(squares, startRank, startFile, direction, -1)) {
            moves.add(new Move(startSquare, squares[startRank - 1][startFile - 1]));
        }
        return moves;
    }

    private boolean checkPawnCapture(Square[][] squares, int startRank, int startFile, int rankDirection, int fileDirection) {
        return squares[startRank + rankDirection][startFile + fileDirection].isOccupied() &&
                squares[startRank + rankDirection][startFile + fileDirection].getPiece().isWhite() != isWhite();
    }
}
