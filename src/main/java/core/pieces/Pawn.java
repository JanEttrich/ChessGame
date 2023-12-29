package core.pieces;

import core.Piece;
import core.Square;
import core.move.Move;
import core.move.MoveTracker;
import core.move.PieceMovementHelper;

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
    public List<Move> getPseudoLegalMovesForPiece(Square[][] squares, Square startSquare) {
        var moves = new ArrayList<Move>();
        int startRank = startSquare.getRank();
        int startFile = startSquare.getFile();

        int direction = isWhite() ? -1 : 1;
        int doubleMoveStart = isWhite() ? 6 : 1;
        int promotionMoveStart = isWhite() ? 1 : 6;
        int enPassantMoveStart = isWhite() ? 3 : 4;

        // one-step move (and promotion)
        if (!squares[startRank + direction][startFile].isOccupied()) {
            if (startRank == promotionMoveStart) {
                moves.addAll(addPawnPromotion(squares, startSquare, direction, 0));
            } else {
                moves.add(new Move(startSquare, squares[startRank + direction][startFile]));
            }
        }
        // double move
        if (startRank == doubleMoveStart &&
                !squares[doubleMoveStart + direction][startFile].isOccupied() &&
                !squares[doubleMoveStart + 2 * direction][startFile].isOccupied()) {
            moves.add(new Move(startSquare, squares[doubleMoveStart + 2 * direction][startSquare.getFile()]));
        }
        // capture right (and promotion)
        if (PieceMovementHelper.checkIfSquareOnBoard(startRank + direction, startFile + 1) &&
                checkPawnCapture(squares, startRank, startFile, direction, 1)) {
            if (startRank == promotionMoveStart) {
                moves.addAll(addPawnPromotion(squares, startSquare, direction, 1));
            } else {
                moves.add(new Move(startSquare, squares[startRank + direction][startFile + 1]));
            }
        }
        // capture left (and promotion)
        if (PieceMovementHelper.checkIfSquareOnBoard(startRank + direction, startFile - 1) &&
                checkPawnCapture(squares, startRank, startFile, direction, -1)) {
            if (startRank == promotionMoveStart) {
                moves.addAll(addPawnPromotion(squares, startSquare, direction, -1));
            } else {
                moves.add(new Move(startSquare, squares[startRank + direction][startFile - 1]));
            }
        }

        // en passant
        if (startRank == enPassantMoveStart) {
            // check left and right en passant
            if (checkEnPassant(squares, startRank, startFile, direction, 1, enPassantMoveStart)) {
                moves.add(new Move(squares[startRank][startFile], squares[startRank + direction][startFile + 1],
                        true, squares[startRank][startFile + 1]));
            }
            if (checkEnPassant(squares, startRank, startFile, direction, -1, enPassantMoveStart)) {
                moves.add(new Move(squares[startRank][startFile], squares[startRank + direction][startFile - 1],
                        true, squares[startRank][startFile - 1]));
            }
        }
        return moves;
    }

    private boolean checkPawnCapture(Square[][] squares, int startRank, int startFile, int rankDirection, int fileDirection) {
        return squares[startRank + rankDirection][startFile + fileDirection].isOccupied() &&
                squares[startRank + rankDirection][startFile + fileDirection].getPiece().isWhite() != isWhite();
    }

    private List<Move> addPawnPromotion(Square[][] squares, Square startSquare, int direction, int fileOffset) {
        int startRank = startSquare.getRank();
        int startFile = startSquare.getFile();

        var moves = new ArrayList<Move>();
        moves.add(new Move(startSquare, squares[startRank + direction][startFile + fileOffset],
                true, new Queen(isWhite())));
        moves.add(new Move(startSquare, squares[startRank + direction][startFile + fileOffset],
                true, new Rook(isWhite())));
        moves.add(new Move(startSquare, squares[startRank + direction][startFile + fileOffset],
                true, new Bishop(isWhite())));
        moves.add(new Move(startSquare, squares[startRank + direction][startFile + fileOffset],
                true, new Knight(isWhite())));

        return moves;
    }

    private boolean checkEnPassant(Square[][] squares, int startRank, int startFile, int rankDirection, int fileDirection, int enPassantMoveStart) {
        if (!hasAdjacentPawn(squares, startRank, startFile, fileDirection)) {
            return false;
        }
        // check if the enemy pawn was moved by two squares in the last move
        Move lastMove = MoveTracker.getLastMove();
        if (!lastMove.getEndSquare().equals(squares[startRank][startFile + fileDirection]) ||
                lastMove.getStartSquare().getRank() != enPassantMoveStart + 2 * rankDirection) {
            return false;
        }

        // check if the target square is empty
        return !squares[startRank + rankDirection][startFile + 1].isOccupied();
    }

    private boolean hasAdjacentPawn(Square[][] squares, int startRank, int startFile, int fileDirection) {
        return PieceMovementHelper.checkIfSquareOnBoard(startRank, startFile + fileDirection) &&
                checkPawnCapture(squares, startRank, startFile, 0, fileDirection) &&
                squares[startRank][startFile + fileDirection].getPiece() instanceof Pawn;
    }
}
