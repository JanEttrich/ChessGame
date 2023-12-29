package core;

import core.pieces.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class Piece {
    private boolean white;

    public static Piece getPieceForSymbol(char symbol) {
        return switch (symbol) {
            case 'R' -> new Rook(true);
            case 'r' -> new Rook(false);
            case 'N' -> new Knight(true);
            case 'n' -> new Knight(false);
            case 'B' -> new Bishop(true);
            case 'b' -> new Bishop(false);
            case 'Q' -> new Queen(true);
            case 'q' -> new Queen(false);
            case 'K' -> new King(true);
            case 'k' -> new King(false);
            case 'P' -> new Pawn(true);
            case 'p' -> new Pawn(false);
            default -> null;
        };
    }

    public abstract String getDisplay();

    public abstract List<Move> getPseudoLegalMovesForPiece(Square[][] squares, Square startSquare);
}
