package core.pieces;

import core.Piece;

public class Rook extends Piece {

    public Rook(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "R" : "r";
    }
}
