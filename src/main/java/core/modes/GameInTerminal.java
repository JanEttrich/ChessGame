package core.modes;

import core.Game;
import core.Piece;
import core.Square;
import core.input.ChessMove;
import core.input.Command;
import core.move.Move;
import core.move.MoveMaker;

import java.util.List;
import java.util.Scanner;

public class GameInTerminal extends Game {

    public GameInTerminal(String posFen, boolean humanOpponent) {
        super(posFen, humanOpponent);
    }

    public void startGameOnTerminal() {
        board.printBoard();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!canPlayerMove()) {
                if (canKingCanBeCaptured()) {
                    System.out.println("Checkmate, " + (activePlayer.isWhite() ? "Black" : "White") + " wins");
                } else {
                    System.out.println("Draw by Stalemate");
                }
                break;
            }
            if (activePlayer.isHuman()) {
                System.out.print("Move" + (activePlayer.isWhite() ? "(White)" : "(Black)") + ": ");
                String move = scanner.next();
                handleChessMoveString(move, activePlayer.isWhite());
                board.printBoard();
            } else {
                var move = makeRandomMove(activePlayer.isWhite());
                System.out.println("Move" + (activePlayer.isWhite() ? "(White)" : "(Black)") + ": " + move);
                board.printBoard();
            }
        }
        System.exit(0);
    }


    // move String handling
    public boolean handleChessMoveString(String moveString, boolean white) {
        ChessMove chessMove = getMoveFromString(moveString);
        if (chessMove == null) {
            return false;
        }

        // get target square
        Square targetSquare;
        if (chessMove.isCastleShort() || chessMove.isCastleLong()) {
            targetSquare = getTargetSquareForCastling(chessMove);
        } else {
            targetSquare = board.getSquareFromChessSquare(chessMove.getEndSquare());
        }

        // find piece of correct type on board
        String pieceString = chessMove.getPiece();

        List<Square> squaresWithPiece = board.getPositionOfPiecesByType(pieceString, white);
        for (Square sourceSquare : squaresWithPiece) {
            Piece piece = sourceSquare.getPiece();
            List<Move> pseudoLegalMovesForPiece = piece.getPseudoLegalMovesForPiece(board.getSquares(), sourceSquare);

            // match move input to legal move
            for (Move move : pseudoLegalMovesForPiece) {
                if (doesMoveMatchInput(move, sourceSquare, targetSquare, chessMove)) {
                    if ((Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) &&
                            threatenedOrCastlesThroughCheck(move)) {
                        return false;
                    }
                    MoveMaker.makeMove(move, white, board);
                    if (canKingCanBeCaptured()) {
                        MoveMaker.unmakeMove(move, white, board);
                        return false;
                    }
                    updateCastlingRights(move);
                    activePlayer = activePlayer == playerWhite ? playerBlack : playerWhite;
                    return true;
                }
            }
        }
        return false;
    }

    private ChessMove getMoveFromString(String move) {
        for (Command command : Command.values()) {
            if (move.matches(command.getPattern())) {
                return command.handleInput(move);
            }
        }
        return null;
    }

    private Square getTargetSquareForCastling(ChessMove chessMove) {
        if (chessMove.isCastleShort() && activePlayer.isWhite()) {
            return board.getSquares()[7][6];
        } else if (chessMove.isCastleShort()) {
            return board.getSquares()[0][6];
        } else if (chessMove.isCastleLong() && activePlayer.isWhite()) {
            return board.getSquares()[7][2];
        } else {
            return board.getSquares()[0][2];
        }
    }

    private boolean doesMoveMatchInput(Move move, Square sourceSquare, Square targetSquare, ChessMove chessMove) {
        if (chessMove.isCastleShort() && Boolean.TRUE.equals(move.getCastleShort()) ||
                chessMove.isCastleLong() && Boolean.TRUE.equals(move.getCastleLong())) {
            return true;
        }

        if (move.getStartSquare() == sourceSquare && move.getEndSquare() == targetSquare) {
            if (chessMove.isPawnPromotion()) {
                return Boolean.TRUE.equals(move.getPromotion()) &&
                        move.getPromotionPiece().getDisplay().equalsIgnoreCase(chessMove.getPromotionPiece());
            }
            return true;
        }
        return false;
    }
}
