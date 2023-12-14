package core.pieces;

import core.Piece;

public class Queen extends Piece {
    public Queen(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "Q" : "q";
    }
}
