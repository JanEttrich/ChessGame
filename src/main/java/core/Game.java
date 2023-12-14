package core;

import util.FenStringReader;

public class Game {
    private final Board board;
    private static final String START_POS_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    public Game() {
        this.board = new Board();
    }

    public void initStartingPosition() {
        FenStringReader.read(START_POS_FEN, board);
    }

    public void initPositionFromFen(String fen) {
        FenStringReader.read(fen, board);
    }

    public Square getSquare(int rank, int file) {
        return board.getSquares()[rank][file];
    }

    public void printBoard() {
        for (int i = 0; i < board.getSquares().length; i++) {
            for (int j = 0; j < board.getSquares()[0].length; j++) {
                if (getSquare(i, j).isOccupied()) {
                    System.out.print(getSquare(i, j).getPiece().getDisplay() + " ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }
}
