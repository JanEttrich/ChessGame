package core.pieces;

import core.Piece;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "B" : "b";
    }
}
