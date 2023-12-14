package core.pieces;

import core.Piece;

public class Knight extends Piece {
    public Knight(boolean white) {
        super(white);
    }

    @Override
    public String getDisplay() {
        return isWhite() ? "N" : "n";
    }
}
