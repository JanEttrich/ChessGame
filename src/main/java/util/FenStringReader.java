package util;

import core.Board;
import core.Piece;

public class FenStringReader {
    private FenStringReader() {
    }

    private static final String PIECE_SYMBOLS = "RNBQKPrnbqkp";
    private static final String ALLOWED_NUMBERS = "12345678";

    public static void read(String fen, Board board) {
        int rank = 0;
        int file = 0;
        for (int i = 0; i < fen.length(); i++) {
            var symbol = fen.charAt(i);
            if (symbol == '/') {
                rank += 1;
                file = 0;
            } else if (PIECE_SYMBOLS.indexOf(symbol) != -1) {
                var piece = Piece.getPieceForSymbol(symbol);
                board.placePiece(piece, rank, file);
                file += 1;
            } else if (ALLOWED_NUMBERS.indexOf(symbol) != -1) {
                var offset = Character.getNumericValue(symbol);
                if (file + offset < 8) {
                    file += offset;
                }
            }
        }
    }
}
