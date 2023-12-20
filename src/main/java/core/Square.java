package core;

import lombok.AllArgsConstructor;
import lombok.Data;
import util.ChessNotationHandler;

@Data
@AllArgsConstructor
public class Square {
    private boolean occupied;
    private Piece piece;
    private int rank;
    private int file;

    public Square(boolean occupied, int rank, int file) {
        this.occupied = occupied;
        this.rank = rank;
        this.file = file;
    }

    public void clearSquare() {
        this.occupied = false;
        this.piece = null;
    }

    public void placePiece(Piece piece) {
        if (piece == null) {
            return;
        }
        this.occupied = true;
        this.piece = piece;
    }

    public String toChessSquare() {
        var chessFile = ChessNotationHandler.getChessFileFromFile(file);
        var chessRank = String.valueOf(8 - rank);

        return chessFile + chessRank;
    }
}
