package core;

import core.input.ChessMove;
import core.input.Command;
import core.move.Move;
import core.move.MoveMaker;
import core.move.MoveTracker;
import core.pieces.King;
import core.pieces.Rook;
import lombok.Getter;
import lombok.Setter;
import util.FenStringReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private static final String START_POS_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    @Getter
    private final Board board;
    private final Player playerWhite;
    private final Player playerBlack;
    private final Random random = new Random();
    @Getter
    @Setter
    private Player activePlayer;

    public Game(String posFen) {
        this.board = new Board();
        this.playerWhite = new Player(true, true);
        this.playerBlack = new Player(false, true);
        this.activePlayer = playerWhite;
        if (posFen == null) {
            initStartingPosition();
        } else {
            initPositionFromFen(posFen);
        }
        GameState.resetGameState(playerWhite, playerBlack);
        MoveTracker.resetMoves();
    }

    public void initStartingPosition() {
        FenStringReader.read(START_POS_FEN, board);
    }

    public void initPositionFromFen(String fen) {
        FenStringReader.read(fen, board);
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!canPlayerMove(activePlayer.isWhite())) {
                if (canKingCanBeCaptured(activePlayer.isWhite())) {
                    System.out.println("Checkmate, " + (activePlayer.isWhite() ? "black" : "white") + " wins");
                } else {
                    System.out.println("Draw by Stalemate");
                }
                break;
            }
            if (activePlayer.isHuman()) {
                System.out.print("Move" + (activePlayer.isWhite() ? "(white)" : "(black)") + ": ");
                String move = scanner.next();
                for (Command command : Command.values()) {
                    if (move.matches(command.getPattern())) {
                        ChessMove chessMove = command.handleInput(move);
                        if (handleChessMove(chessMove, activePlayer.isWhite())) {
                            activePlayer = activePlayer == playerWhite ? playerBlack : playerWhite;
                        }
                        break;
                    }
                }
                board.printBoard();
            } else {
                var move = generateMove(activePlayer.isWhite());
                System.out.println("Move" + (activePlayer.isWhite() ? "(white)" : "(black)") + ": " + move);
                MoveMaker.makeMove(move, activePlayer.isWhite(), board);
                updateCastlingRights(move);
                activePlayer = activePlayer == playerWhite ? playerBlack : playerWhite;
                board.printBoard();
            }
        }
    }

    private boolean canPlayerMove(boolean white) {
        return !generate(white).isEmpty();
    }

    public boolean handleChessMove(ChessMove chessMove, boolean white) {
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
                    if (canKingCanBeCaptured(white)) {
                        MoveMaker.unmakeMove(move, white, board);
                        return false;
                    }
                    updateCastlingRights(move);
                    return true;

                }
            }
        }
        return false;
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

    public boolean updateCastlingRights(Move move) {
        Piece piece = move.getEndSquare().getPiece();
        Square sourceSquare = move.getStartSquare();

        // handle rook/king move for castling rights
        if (piece instanceof King && activePlayer.canCastleOnAtLeastOneSide()) {
            activePlayer.disallowCastle();
            return true;
        } else if (piece instanceof Rook rook) {
            if (sourceSquare.getFile() == 0 && activePlayer.isCastleLongAllowed() && !rook.isMoved()) {
                activePlayer.setCastleLongAllowed(false);
                rook.setMoved(true);
                return true;
            } else if (sourceSquare.getFile() == 7 && activePlayer.isCastleShortAllowed() && !rook.isMoved()) {
                activePlayer.setCastleShortAllowed(false);
                rook.setMoved(true);
                return true;
            }
        }
        return false;
    }

    public void unmakeCastlingRightUpdate(Move move) {
        Piece piece = move.getPiece();
        Square sourceSquare = move.getStartSquare();

        // handle rook/king move for castling rights
        if (piece instanceof King) {
            activePlayer.reAllowedCastle();
        } else if (piece instanceof Rook rook) {
            if (sourceSquare.getFile() == 0) {
                activePlayer.setCastleLongAllowed(true);
                rook.setMoved(false);
            } else if (sourceSquare.getFile() == 7) {
                activePlayer.setCastleShortAllowed(true);
                rook.setMoved(false);
            }
        }
    }

    // Returns all legal moves of a player
    public List<Move> generate(boolean white) {
        List<Move> pseudoLegalMoves = generatePseudoLegalMoves(white);
        return filterMoves(pseudoLegalMoves, white);
    }

    // returns a random legal move
    public Move generateMove(boolean white) {
        List<Move> legalMoves = generate(white);

        int randomIndex = random.nextInt(legalMoves.size());
        return legalMoves.get(randomIndex);
    }

    private List<Move> filterMoves(List<Move> pseudoLegalMoves, boolean white) {
        List<Move> legalMoves = new ArrayList<>();
        for (Move move : pseudoLegalMoves) {
            // do not allow king to castle through check
            if ((Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) &&
                    threatenedOrCastlesThroughCheck(move)) {
                continue;
            }
            MoveMaker.makeMove(move, white, board);
            if (!canKingCanBeCaptured(white)) {
                legalMoves.add(move);
            }
            MoveMaker.unmakeMove(move, white, board);
        }
        return legalMoves;
    }

    private boolean canKingCanBeCaptured(boolean white) {
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

    // check if king is in check or adjacent square in direction of castle is threatened by opponent
    // target square of king is checked later
    private boolean threatenedOrCastlesThroughCheck(Move move) {
        if (canKingCanBeCaptured(activePlayer.isWhite())) {
            return true;
        }
        int direction = Boolean.TRUE.equals(move.getCastleShort()) ? 1 : -1;
        // check if horizontal squares are threatened by the opponents pieces
        Move moveToAdjacentSquare = new Move(move.getStartSquare(),
                board.getSquares()[move.getStartSquare().getRank()][move.getStartSquare().getFile() + direction]);

        MoveMaker.makeMove(moveToAdjacentSquare, activePlayer.isWhite(), board);
        if (canKingCanBeCaptured(activePlayer.isWhite())) {
            MoveMaker.unmakeMove(moveToAdjacentSquare, activePlayer.isWhite(), board);
            return true;
        }
        MoveMaker.unmakeMove(moveToAdjacentSquare, activePlayer.isWhite(), board);
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
