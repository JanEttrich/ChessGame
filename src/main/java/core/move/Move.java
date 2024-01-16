package core.move;

import core.Board;
import core.Pieces;
import lombok.Data;

@Data
public class Move {
    private int startSquare;
    private int endSquare;
    private int piece;

    private Boolean promotion;
    private int promotionPiece;

    private Boolean castleShort;
    private Boolean castleLong;

    private Boolean enPassant;
    private int enPassantSquare;

    private Boolean standardCapture;
    private int capturedPiece;

    public Move(int startSquare, int endSquare) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.piece = Board.squares[startSquare];
        if (Board.squares[endSquare] != Pieces.NONE) {
            standardCapture = true;
            capturedPiece = Board.squares[endSquare];
        }
    }

    public Move(int startSquare, int endSquare, Boolean promotion, int promotionPiece) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.piece = Board.squares[startSquare];
        this.promotion = promotion;
        this.promotionPiece = promotionPiece;
        if (Board.squares[endSquare] != Pieces.NONE) {
            standardCapture = true;
            capturedPiece = Board.squares[endSquare];
        }
    }

    public Move(int startSquare, int endSquare, int enPassantSquare, Boolean enPassant) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.piece = Board.squares[startSquare];
        this.enPassant = enPassant;
        this.enPassantSquare = enPassantSquare;
    }

    public Move(int startSquare, int endSquare, Boolean castleShort, Boolean castleLong) {
        this.startSquare = startSquare;
        this.endSquare = endSquare;
        this.piece = Board.squares[startSquare];
        this.castleShort = castleShort;
        this.castleLong = castleLong;
    }

    @Override
    public String toString() {
        String move = "[" + Pieces.getSymbolForPiece(piece) + Board.intToChessSquare(startSquare) + "-" +
                Board.intToChessSquare(endSquare);
        if (Boolean.TRUE.equals(promotion)) {
            move += "=" + Pieces.getSymbolForPiece(promotionPiece);
        }
        move += "]";
        return move;
    }
}
