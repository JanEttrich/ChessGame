package core;

import core.pieces.Move;
import lombok.Getter;
import util.FenStringReader;
import util.MoveTracker;

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
        MoveTracker.resetMoves();
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
        if (move.length() != 2 && move.length() != 3 && move.length() != 4) {
            return false;
        }

        String promotionPiece = move.length() == 4 ? move.substring(3, 4) : null;

        if (move.length() == 2 || move.length() == 4) {
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
                    // handle promotion
                    if (Boolean.TRUE.equals(legalMove.getPromotion())) {
                        if (legalMove.getPromotionPiece().getDisplay().equalsIgnoreCase(promotionPiece)) {
                            sourceSquare.clearSquare();
                            targetSquare.placePiece(legalMove.getPromotionPiece());
                            MoveTracker.addMove(legalMove);
                            return true;
                        } else {
                            continue;
                        }
                    }
                    // move piece
                    sourceSquare.clearSquare();
                    targetSquare.placePiece(piece);

                    // handle en passant
                    if (Boolean.TRUE.equals(legalMove.getEnPassant()) && legalMove.getEnPassantSquare() != null) {
                        legalMove.getEnPassantSquare().clearSquare();
                    }
                    MoveTracker.addMove(legalMove);
                    return true;
                }
            }
        }

        return false;
    }
}
