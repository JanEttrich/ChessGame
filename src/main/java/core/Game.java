package core;

import core.evaluation.Evaluator;
import core.move.Move;
import core.move.MoveMaker;
import core.move.MoveTracker;
import lombok.Getter;
import lombok.Setter;
import util.FenStringReader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static core.Board.*;
import static core.evaluation.Evaluator.pieceValueByType;

public class Game {

    private static final String START_POS_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
    protected final Player playerWhite;
    protected final Player playerBlack;
    @Getter
    @Setter
    protected Player activePlayer;
    private final Random random = new Random();

    public Game(String posFen, boolean humanOpponent) {
        Board.resetBoard();
        this.playerWhite = new Player(true, true);
        this.playerBlack = new Player(false, humanOpponent);
        this.activePlayer = playerWhite;
        if (posFen == null) {
            initStartingPosition();
        } else {
            initPositionFromFen(posFen);
        }
        piecesPosWhite = Board.getAllPiecePositionsOfPlayer(true);
        piecePosBlack = Board.getAllPiecePositionsOfPlayer(false);
        GameState.resetGameState(playerWhite, playerBlack);
        MoveTracker.resetMoves();
    }

    // TODO: Draw by repetition
    // TODO: Draw by 50 move rule

    public void initStartingPosition() {
        FenStringReader.read(START_POS_FEN);
    }

    public void initPositionFromFen(String fen) {
        FenStringReader.read(fen);
    }

    public boolean canPlayerMove() {
        return !generate().isEmpty();
    }

    // Returns true if both players only have a king left
    public boolean isInsufficientMaterial() {
        return checkInsufficientMaterialForPlayer(true) && checkInsufficientMaterialForPlayer(false);
    }

    public boolean checkInsufficientMaterialForPlayer(boolean white) {
        int knightCount = 0;
        int bishopCount = 0;
        var piecePositions = Board.getAllPiecePositionsOfPlayer(white);
        if (piecePositions.size() > 2) {
            return false;
        }
        for (Integer square : piecePositions) {
            if ((squares[square] & 7) == Pieces.KNIGHT) {
                knightCount += 1;
            } else if ((squares[square] & 7) == Pieces.BISHOP) {
                bishopCount += 1;
            } else if ((squares[square] & 7) != Pieces.KING) {
                return false;
            }
        }
        return knightCount + bishopCount <= 1;
    }

    // Returns all legal moves of a player
    public List<Move> generate() {
        List<Move> pseudoLegalMoves = generatePseudoLegalMoves(activePlayer.isWhite());
        return filterMoves(pseudoLegalMoves);
    }

    public Move findRandomMove() {
        List<Move> legalMoves = generate();
        int randomIndex = random.nextInt(legalMoves.size());

        return legalMoves.get(randomIndex);
    }

    static Move bestMove;

    public void searchAndMakeMove() {
        long start = System.currentTimeMillis();
        search(3, 3, -100, 100);
        if (bestMove == null) {
            bestMove = findRandomMove();
        }
        System.out.println("Found move " + bestMove.toString() + " in " + (System.currentTimeMillis() - start) + "ms");
        MoveMaker.makeMove(bestMove);
        updateCastlingRights(bestMove);
        setActivePlayer(activePlayer == GameState.playerWhite ? GameState.playerBlack : GameState.playerWhite);
        bestMove = null;
    }

    public int search(int depth, int initialDepth, int alpha, int beta) {
        if (depth == 0) {
            return Evaluator.evaluate(activePlayer == GameState.playerWhite);
        }
        List<Move> moves = generate();
        if (moves.isEmpty()) {
            if (canKingCanBeCaptured()) {
                return -100;
            }
            return 0;
        }
        moves = orderMoves(moves);

        // order moves

        for (Move move : moves) {
            MoveMaker.makeMove(move);
            boolean updated = updateCastlingRights(move);
            setActivePlayer(activePlayer == GameState.playerWhite ? GameState.playerBlack : GameState.playerWhite);

            int eval = -search(depth - 1, initialDepth, -beta, -alpha);
            if (eval > beta) {
                MoveMaker.unmakeMove(move);
                setActivePlayer(activePlayer == GameState.playerWhite ? GameState.playerBlack : GameState.playerWhite);
                if (updated) {
                    unmakeCastlingRightUpdate(move);
                }
                return beta;
            }
            // alpha
            if (eval > alpha) {
                alpha = eval;
                if (depth == initialDepth) {
                    bestMove = move;
                }
            }

            MoveMaker.unmakeMove(move);
            setActivePlayer(activePlayer == GameState.playerWhite ? GameState.playerBlack : GameState.playerWhite);
            if (updated) {
                unmakeCastlingRightUpdate(move);
            }
        }
        return alpha;
    }

    private List<Move> orderMoves(List<Move> moves) {

        return moves.stream().sorted(Comparator.comparingInt(move -> {
            if (Boolean.TRUE.equals(move.getStandardCapture())) {
                return pieceValueByType.get(move.getCapturedPiece() & 7) - pieceValueByType.get(move.getPiece() & 7);
            }
            return 0;
        })).toList();
    }

    public List<Move> filterMoves(List<Move> pseudoLegalMoves) {
        List<Move> legalMoves = new ArrayList<>();
        for (Move move : pseudoLegalMoves) {
            // do not allow king to castle through check
            if ((Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) &&
                    threatenedOrCastlesThroughCheck(move)) {
                continue;
            }
            MoveMaker.makeMove(move);
            if (!canKingCanBeCaptured()) {
                legalMoves.add(move);
            }
            MoveMaker.unmakeMove(move);
        }
        return legalMoves;
    }

    public boolean canKingCanBeCaptured() {
        // get all possible moves of opponent
        List<Move> pseudoLegalMoves = generatePseudoLegalMoves(!activePlayer.isWhite());
        for (Move move : pseudoLegalMoves) {
            if (squares[move.getEndSquare()] != Pieces.NONE && (squares[move.getEndSquare()] & 7) == Pieces.KING) {
                // if king can be captured directly, last move was illegal, regardless of if the capturing move is illegal
                return true;
            }
        }
        return false;
    }

    private List<Move> generatePseudoLegalMoves(boolean white) {
        // get pseudo-legal moves
        List<Integer> positions = white ? piecesPosWhite : piecePosBlack;
        List<Move> pseudoLegalMoves = new ArrayList<>();
        for (Integer sourceSquare : positions) {
            pseudoLegalMoves.addAll(Pieces.generatePseudoLegalMoves(sourceSquare, white ? Pieces.WHITE : Pieces.BLACK));
        }
        return pseudoLegalMoves;
    }

    // check if king is in check or adjacent square in direction of castle is threatened by opponent
    // target square of king is checked later
    protected boolean threatenedOrCastlesThroughCheck(Move move) {
        if (canKingCanBeCaptured()) {
            return true;
        }
        int direction = Boolean.TRUE.equals(move.getCastleShort()) ? 1 : -1;
        // check if horizontal squares are threatened by the opponents pieces
        Move moveToAdjacentSquare = new Move(move.getStartSquare(),
                move.getStartSquare() + direction);

        MoveMaker.makeMove(moveToAdjacentSquare);
        if (canKingCanBeCaptured()) {
            MoveMaker.unmakeMove(moveToAdjacentSquare);
            return true;
        }
        MoveMaker.unmakeMove(moveToAdjacentSquare);
        return false;
    }

    public boolean updateCastlingRights(Move move) {
        int piece = move.getPiece() & 7;
        int sourceFile = move.getStartSquare() % 8;

        // handle rook/king move for castling rights
        if (piece == Pieces.KING && activePlayer.canCastleOnAtLeastOneSide()) {
            activePlayer.disallowCastle();
            return true;
        } else if (piece == Pieces.ROOK) {
            if (sourceFile % 8 == 0 && activePlayer.isCastleLongAllowed()) {
                activePlayer.setCastleLongAllowed(false);
                return true;
            } else if (sourceFile % 8 == 7 && activePlayer.isCastleShortAllowed()) {
                activePlayer.setCastleShortAllowed(false);
                return true;
            }
        }
        return false;
    }

    public void unmakeCastlingRightUpdate(Move move) {
        int piece = move.getPiece() & 7;
        int sourceFile = move.getStartSquare() % 8;

        // handle rook/king move for castling rights
        if (piece == Pieces.KING) {
            activePlayer.reAllowedCastle();
        } else if (piece == Pieces.ROOK) {
            if (sourceFile == 0) {
                activePlayer.setCastleLongAllowed(true);
            } else if (sourceFile == 7) {
                activePlayer.setCastleShortAllowed(true);
            }
        }
    }
}
