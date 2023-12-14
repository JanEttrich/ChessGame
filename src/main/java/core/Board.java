package core;

import lombok.Data;

@Data
public class Board {
    private Square[][] squares;

    public Board() {
        squares = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new Square(false);
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
}
