package core.move;

import core.Pieces;

import static core.Board.squares;

public class MoveMaker {
    private MoveMaker() {
    }

    public static void makeMove(Move move) {
        int sourceSquare = move.getStartSquare();
        int targetSquare = move.getEndSquare();
        int piece = move.getPiece();

        // handle promotion
        if (Boolean.TRUE.equals(move.getPromotion())) {
            squares[targetSquare] = move.getPromotionPiece();
            squares[sourceSquare] = Pieces.NONE;
            MoveTracker.addMove(move);
            return;
        }
        // move piece
        squares[targetSquare] = piece;
        squares[sourceSquare] = Pieces.NONE;

        // handle en passant
        if (Boolean.TRUE.equals(move.getEnPassant())) {
            squares[move.getEnPassantSquare()] = Pieces.NONE;
        }

        // handle castle (move rook)
        if (Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) {
            int rank = sourceSquare / 8;
            int rookStartFile = Boolean.TRUE.equals(move.getCastleShort()) ? 7 : 0;
            int rookEndFile = Boolean.TRUE.equals(move.getCastleShort()) ? 5 : 3;

            int rookStartSquare = 8 * rank + rookStartFile;
            int rookEndSquare = 8 * rank + rookEndFile;
            squares[rookEndSquare] = squares[rookStartSquare];
            squares[rookStartSquare] = Pieces.NONE;
        }

        MoveTracker.addMove(move);
    }

    public static void unmakeMove(Move move) {
        int sourceSquare = move.getStartSquare();
        int targetSquare = move.getEndSquare();
        int piece = move.getPiece();

        // place piece back to source square
        if (Boolean.TRUE.equals(move.getPromotion())) {
            boolean white = Pieces.isPieceOfColor(move.getPromotionPiece(), Pieces.WHITE);
            squares[sourceSquare] = white ? Pieces.WHITE | Pieces.PAWN : Pieces.BLACK | Pieces.PAWN;
        } else {
            squares[sourceSquare] = piece;
        }

        // remove piece from target
        squares[targetSquare] = Pieces.NONE;

        // reinstate captured piece
        if (Boolean.TRUE.equals(move.getStandardCapture())) {
            squares[targetSquare] = move.getCapturedPiece();
        }

        // handle en passant
        if (Boolean.TRUE.equals(move.getEnPassant())) {
            boolean white = Pieces.isPieceOfColor(piece, Pieces.WHITE);
            squares[move.getEnPassantSquare()] = white ? Pieces.BLACK | Pieces.PAWN : Pieces.WHITE | Pieces.PAWN;
        }

        // handle castle (move rook back to source Square)
        if (Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) {
            int rank = sourceSquare / 8;
            int rookStartFile = Boolean.TRUE.equals(move.getCastleShort()) ? 7 : 0;
            int rookEndFile = Boolean.TRUE.equals(move.getCastleShort()) ? 5 : 3;

            int rookStartSquare = 8 * rank + rookStartFile;
            int rookEndSquare = 8 * rank + rookEndFile;
            squares[rookStartSquare] = squares[rookEndSquare];
            squares[rookEndSquare] = Pieces.NONE;
        }

        MoveTracker.removeLastMove();
    }
}
