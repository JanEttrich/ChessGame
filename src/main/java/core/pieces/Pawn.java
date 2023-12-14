package core.pieces;

import core.Piece;

public class Pawn extends Piece {
    public Pawn(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "P" : "p";
    }
}
