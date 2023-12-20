package core;

import core.pieces.Move;
import lombok.Getter;
import util.FenStringReader;

@Getter
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

    // move = Se4-d6
    public boolean handleMove(String move, boolean white) {
        if (move.length() != 6 && move.length() != 5) {
            return false;
        }

        if (move.length() == 5) {
            String pawn = white ? "P" : "p";
            move = pawn + move;
        }

        // get source square
        var sourceSquare = board.getSquareFromChessSquare(move.substring(1, 3));

        // get target square
        var targetSquare = board.getSquareFromChessSquare(move.substring(4, 6));

        // get piece to be moved
        String pieceString = move.substring(0, 1);
        if (!sourceSquare.isOccupied() || !sourceSquare.getPiece().getDisplay().equals(pieceString)) {
            return false;
        }
        var piece = sourceSquare.getPiece();

        // check if move is legal
        var legalMoves = piece.getLegalMovesForPiece(board.getSquares(), sourceSquare);
        System.out.println(legalMoves);
        for (Move legalMove : legalMoves) {
            if (legalMove.getStartSquare() == sourceSquare && legalMove.getEndSquare() == targetSquare) {
                // move piece
                sourceSquare.clearSquare();
                targetSquare.placePiece(piece);
                return true;
            }
        }

        return false;
    }
}
