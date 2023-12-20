package core.pieces;

import core.Square;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Move {
    private Square startSquare;
    private Square endSquare;

    @Override
    public String toString() {
        return "[" + startSquare.getPiece().getDisplay() + startSquare.toChessSquare() + "-" + endSquare.toChessSquare() + "]";
    }
}
