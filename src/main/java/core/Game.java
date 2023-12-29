package core;

import core.input.ChessMove;
import core.input.Command;
import core.move.Move;
import core.move.MoveMaker;
import core.move.MoveTracker;
import lombok.Getter;
import util.FenStringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private static final String START_POS_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    @Getter
    private final Board board;
    private final Random random = new Random();

    public Game() {
        this.board = new Board();
    }

    public void startGame() {
        initStartingPosition();
        printBoard();
        MoveTracker.resetMoves();
        Player playerWhite = new Player(true, true);
        Player playerBlack = new Player(false, false);
        Player activePlayer = playerWhite;

        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (activePlayer.isHuman()) {
                System.out.print("Move" + (activePlayer.isWhite() ? "(white)" : "(black)") + ": ");
                String move = scanner.next();
                if (move.equals("quit")) {
                    break;
                }
                for (Command command : Command.values()) {
                    if (move.matches(command.getPattern())) {
                        ChessMove chessMove = command.handleInput(move);
                        if (handleChessMove(chessMove, activePlayer.isWhite())) {
                            activePlayer = activePlayer == playerWhite ? playerBlack : playerWhite;
                        }
                        break;
                    }
                }

            } else {
                var move = generateMove(activePlayer.isWhite());
                MoveMaker.makeMove(move);
                activePlayer = activePlayer == playerWhite ? playerBlack : playerWhite;
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

    public boolean handleChessMove(ChessMove chessMove, boolean white) {
        // get target square
        Square targetSquare = board.getSquareFromChessSquare(chessMove.getEndSquare());

        // find piece of correct type on board
        String pieceString = chessMove.getPiece();

        List<Square> squaresWithPiece = board.getPositionOfPiecesByType(pieceString, white);
        for (Square sourceSquare : squaresWithPiece) {
            Piece piece = sourceSquare.getPiece();
            List<Move> pseudoLegalMovesForPiece = piece.getPseudoLegalMovesForPiece(board.getSquares(), sourceSquare);

            // match move input to legal move
            for (Move move : pseudoLegalMovesForPiece) {
                if (doesMoveMatchInput(move, sourceSquare, targetSquare, chessMove)) {
                    MoveMaker.makeMove(move);
                    if (checkIfKingCanBeCaptured(white)) {
                        MoveMaker.unmakeMove(move);
                        return false;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean doesMoveMatchInput(Move move, Square sourceSquare, Square targetSquare, ChessMove chessMove) {
        if (move.getStartSquare() == sourceSquare && move.getEndSquare() == targetSquare) {
            if (chessMove.isPawnPromotion()) {
                return Boolean.TRUE.equals(move.getPromotion()) &&
                        move.getPromotionPiece().getDisplay().equalsIgnoreCase(chessMove.getPromotionPiece());
            }
            return true;
        }
        return false;

    }

    public Move generateMove(boolean white) {
        List<Move> pseudoLegalMoves = generatePseudoLegalMoves(white);
        List<Move> legalMoves = filterMoves(pseudoLegalMoves, white);

        int randomIndex = random.nextInt(legalMoves.size());
        return legalMoves.get(randomIndex);
    }

    private List<Move> filterMoves(List<Move> pseudoLegalMoves, boolean white) {
        List<Move> legalMoves = new ArrayList<>();
        for (Move move : pseudoLegalMoves) {
            MoveMaker.makeMove(move);
            if (!checkIfKingCanBeCaptured(white)) {
                legalMoves.add(move);
            }
            MoveMaker.unmakeMove(move);
        }
        return legalMoves;
    }

    private boolean checkIfKingCanBeCaptured(boolean white) {
        // get all possible moves of opponent
        List<Move> pseudoLegalMoves = generatePseudoLegalMoves(!white);
        for (Move move : pseudoLegalMoves) {
            if (move.getEndSquare().isOccupied() && move.getEndSquare().getPiece().getDisplay().equalsIgnoreCase("K")) {
                // if king can be captured directly, last move was illegal, regardless of if the capturing move is illegal
                return true;
            }
        }
        return false;
    }

    private List<Move> generatePseudoLegalMoves(boolean white) {
        // get pseudo-legal moves
        List<Square> positions = board.getAllPiecePositionsOfPlayer(white);
        List<Move> pseudoLegalMoves = new ArrayList<>();
        for (Square sourceSquare : positions) {
            pseudoLegalMoves.addAll(sourceSquare.getPiece().getPseudoLegalMovesForPiece(board.getSquares(),
                    sourceSquare));
        }
        return pseudoLegalMoves;
    }
}
