package core;

import core.pieces.Move;
import lombok.Getter;
import util.ChessMove;
import util.Command;
import util.FenStringReader;
import util.MoveTracker;

import java.util.Scanner;

@Getter
public class Game {

    private static final String START_POS_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    private final Board board;

    public Game() {
        this.board = new Board();
    }

    public void startGame() {
        initStartingPosition();
        printBoard();
        MoveTracker.resetMoves();

        Scanner scanner = new Scanner(System.in);
        boolean whiteToMove = true;
        while (true) {
            System.out.print("Move" + (whiteToMove ? "(white)" : "(black)") + ": ");
            String move = scanner.next();
            if (move.equals("quit")) {
                break;
            }

            for (Command command : Command.values()) {
                if (move.matches(command.getPattern())) {
                    ChessMove chessMove = command.handleInput(move);
                    if (handleMove(chessMove, whiteToMove)) {
                        whiteToMove = !whiteToMove;
                    }
                    break;
                }
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

    public boolean handleMove(ChessMove chessMove, boolean white) {

        // get target square
        var targetSquare = board.getSquareFromChessSquare(chessMove.getEndSquare());

        // find piece of correct type on board
        String pieceString = chessMove.getPiece();

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
                        if (legalMove.getPromotionPiece().getDisplay().equalsIgnoreCase(chessMove.getPromotionPiece())) {
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
