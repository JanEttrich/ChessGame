package core.pieces;

import core.Piece;

public class King extends Piece {
    public King(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "K" : "k";
    }
}
