package core;

import lombok.Data;
import util.ChessNotationHandler;

import java.util.ArrayList;
import java.util.List;

@Data
public class Board {
    private Square[][] squares;

    public Board() {
        squares = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new Square(false, i, j);
            }
        }
    }

    public void placePiece(Piece piece, int rank, int file) {
        if (piece == null) {
            return;
        }
        var square = squares[rank][file];
        square.setOccupied(true);
        square.setPiece(piece);
    }

    public Piece getPieceFromSquare(int rank, int file) {
        var square = squares[rank][file];
        if (square.isOccupied()) {
            return square.getPiece();
        }
        return null;
    }

    public Square getSquareFromChessSquare(String chessSquare) {
        if (chessSquare.length() != 2) {
            return null;
        }
        int rank = 8 - Character.getNumericValue(chessSquare.charAt(1));
        int file = ChessNotationHandler.getFileFromChessFile(chessSquare.charAt(0));
        if (file == -1 || rank >= 8) {
            return null;
        }
        return squares[rank][file];
    }

    public List<Square> getPositionOfPiecesByType(String type, boolean white) {
        List<Square> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var piece = getPieceFromSquare(i, j);
                if (piece != null && piece.isWhite() == white && piece.getDisplay().equalsIgnoreCase(type)) {
                    positions.add(squares[i][j]);
                }
            }
        }
        return positions;
    }
}
