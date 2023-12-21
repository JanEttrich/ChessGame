package core;

import core.pieces.Move;
import lombok.Getter;
import util.FenStringReader;

import java.util.Scanner;

@Getter
public class Game {

    private final Board board;
    private static final String START_POS_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    public Game() {
        this.board = new Board();
    }

    public void startGame() {
        initStartingPosition();
        printBoard();
        // Player playerWhite = new Player(true, true, true, true);
        // Player playerBlack = new Player(false, true, true, true);
        Scanner scanner = new Scanner(System.in);
        boolean whiteOneMove = true;
        while (true) {
            System.out.print("Move" + (whiteOneMove ? "(white)" : "(black)") + ": ");
            String move = scanner.next();
            if (move.equals("quit")) {
                break;
            }
            if (handleMove(move, whiteOneMove)) {
                whiteOneMove = !whiteOneMove;
            }
            printBoard();

        }

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
        if (move.length() != 2 && move.length() != 3) {
            return false;
        }

        if (move.length() == 2) {
            String pawn = white ? "P" : "p";
            move = pawn + move;
        }

        // get target square
        var targetSquare = board.getSquareFromChessSquare(move.substring(1, 3));

        // find piece of correct type on board
        String pieceString = move.substring(0, 1);
        var squaresWithPiece = board.getPositionOfPiecesByType(pieceString, white);
        for (Square sourceSquare : squaresWithPiece) {
            Piece piece = sourceSquare.getPiece();

            var legalMoves = piece.getLegalMovesForPiece(board.getSquares(), sourceSquare);
            System.out.println(legalMoves);

            // check if move to target square from this piece is legal
            for (Move legalMove : legalMoves) {
                if (legalMove.getStartSquare() == sourceSquare && legalMove.getEndSquare() == targetSquare) {
                    // move piece
                    sourceSquare.clearSquare();
                    targetSquare.placePiece(piece);
                    return true;
                }
            }
        }

        return false;
    }
}
