package core.pieces;

import core.Piece;
import core.Square;
import lombok.Data;

@Data
public class Move {
    private Square startSquare;
    private Square endSquare;

    private Boolean promotion;
    private Piece promotionPiece;

    private Boolean castleShort;
    private Boolean castleLong;

    private Boolean enPassant;
    private Square enPassantSquare;

    private Boolean standardCapture;
    private Piece capturedPiece;

    public Move(Square startSquare, Square endSquare) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        if (endSquare.isOccupied()) {
            standardCapture = true;
            capturedPiece = endSquare.getPiece();
        }
    }

    public Move(Square startSquare, Square endSquare, Boolean promotion, Piece promotionPiece) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.promotion = promotion;
        this.promotionPiece = promotionPiece;
        if (endSquare.isOccupied()) {
            standardCapture = true;
            capturedPiece = endSquare.getPiece();
        }
    }

    public Move(Square startSquare, Square endSquare, Boolean enPassant, Square enPassantSquare) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.enPassant = enPassant;
        this.enPassantSquare = enPassantSquare;
    }

    @Override
    public String toString() {
        String move = "[" + startSquare.getPiece().getDisplay() + startSquare.toChessSquare() + "-" +
                endSquare.toChessSquare();
        if (Boolean.TRUE.equals(promotion)) {
            move += "=" + promotionPiece.getDisplay().toUpperCase();
        }
        move += "]";

        return move;
    }
}
