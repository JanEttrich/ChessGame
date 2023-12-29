package core;

import core.input.ChessNotationHandler;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Board {
    private static final String PIECE_COLOR_BLACK = "\u001B[31m";
    private static final String COLOR_RESET = "\u001B[0m";
    private Square[][] squares;

    public Board() {
        squares = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                squares[i][j] = new Square(false, i, j);
            }
        }
    }

    public void printBoard() {
        for (Square[] squareRow : squares) {
            for (int j = 0; j < squares[0].length; j++) {
                if (squareRow[j].isOccupied()) {
                    String piece = squareRow[j].getPiece().getDisplay();
                    String color = Character.isUpperCase(piece.charAt(0)) ? "" : PIECE_COLOR_BLACK;
                    System.out.print(color + squareRow[j].getPiece().getDisplay() + COLOR_RESET + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    public void placePiece(Piece piece, int rank, int file) {
        if (piece == null) {
            return;
        }
        var square = squares[rank][file];
        square.setOccupied(true);
        square.setPiece(piece);
    }

    public Piece getPieceFromSquare(int rank, int file) {
        var square = squares[rank][file];
        if (square.isOccupied()) {
            return square.getPiece();
        }
        return null;
    }

    public Square getSquareFromChessSquare(String chessSquare) {
        if (chessSquare.length() != 2) {
            return null;
        }
        int rank = 8 - Character.getNumericValue(chessSquare.charAt(1));
        int file = ChessNotationHandler.getFileFromChessFile(chessSquare.charAt(0));
        if (file == -1 || rank >= 8) {
            return null;
        }
        return squares[rank][file];
    }

    public List<Square> getPositionOfPiecesByType(String type, boolean white) {
        List<Square> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var piece = getPieceFromSquare(i, j);
                if (piece != null && piece.isWhite() == white && piece.getDisplay().equalsIgnoreCase(type)) {
                    positions.add(squares[i][j]);
                }
            }
        }
        return positions;
    }

    public List<Square> getAllPiecePositionsOfPlayer(boolean white) {
        List<Square> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                var piece = getPieceFromSquare(i, j);
                if (piece != null && piece.isWhite() == white) {
                    positions.add(squares[i][j]);
                }
            }
        }
        return positions;
    }
}
