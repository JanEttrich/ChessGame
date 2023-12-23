package core.pieces;

import core.Piece;
import core.Square;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Move {
    private Square startSquare;
    private Square endSquare;

    private Boolean promotion;
    private Piece promotionPiece;

    private Boolean castleShort;
    private Boolean castleLong;

    public Move(Square startSquare, Square endSquare) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
    }

    public Move(Square startSquare, Square endSquare, Boolean promotion, Piece promotionPiece) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.promotion = promotion;
        this.promotionPiece = promotionPiece;
    }

    @Override
    public String toString() {
        String move = "[" + startSquare.getPiece().getDisplay() + startSquare.toChessSquare() + "-" + endSquare.toChessSquare();
        if (Boolean.TRUE.equals(promotion)) {
            move += "=" + promotionPiece.getDisplay().toUpperCase();
        }
        move += "]";

        return move;
    }
}
