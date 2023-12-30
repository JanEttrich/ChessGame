package core.pieces;

import core.GameState;
import core.Piece;
import core.Player;
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
        var moves = new ArrayList<Move>();
        int startRank = startSquare.getRank();
        int startFile = startSquare.getFile();

        // Test one step in each direction
        for (int i = startRank - 1; i <= startRank + 1; i++) {
            for (int j = startFile - 1; j <= startFile + 1; j++) {
                if (PieceMovementHelper.checkIfSquareOnBoard(i, j)
                        && PieceMovementHelper.checkSquareAvailability(squares, i, j, isWhite())) {
                    moves.add(new Move(startSquare, squares[i][j]));
                }
            }
        }

        // Test castling
        Player activePlayer = startSquare.getPiece().isWhite() ? GameState.playerWhite : GameState.playerBlack;
        int requiredCastleRank = activePlayer.isWhite() ? 7 : 0;
        if (startRank != requiredCastleRank || startFile != 4) {
            return moves;
        }

        if (activePlayer.isCastleShortAllowed()) {
            Square rookSquare = squares[requiredCastleRank][7];
            if (checkRookForCastle(rookSquare, activePlayer) && checkHorizontalOccupationsForCastle(squares,
                    requiredCastleRank, 1)) {
                moves.add(new Move(startSquare, squares[requiredCastleRank][6], true, false));
            }
        }
        if (activePlayer.isCastleLongAllowed()) {
            Square rookSquare = squares[requiredCastleRank][0];
            if (checkRookForCastle(rookSquare, activePlayer) && checkHorizontalOccupationsForCastle(squares,
                    requiredCastleRank, -1)) {
                moves.add(new Move(startSquare, squares[requiredCastleRank][2], false, true));
            }

        }
        return moves;
    }

    private boolean checkHorizontalOccupationsForCastle(Square[][] squares, int rank, int direction) {
        return !squares[rank][4 + direction].isOccupied() && !squares[rank][4 + 2 * direction].isOccupied();
    }

    private boolean checkRookForCastle(Square rookSquare, Player activePlayer) {
        return rookSquare.isOccupied() && rookSquare.getPiece() instanceof Rook rook &&
                rook.isWhite() == activePlayer.isWhite() && !rook.isMoved();
    }
}
