package core.move;

import core.Board;
import core.Piece;
import core.Player;
import core.Square;
import core.pieces.Pawn;

public class MoveMaker {
    private MoveMaker() {
    }

    public static void makeMove(Move move, Player player, Board board) {
        Square sourceSquare = move.getStartSquare();
        Square targetSquare = move.getEndSquare();
        Piece piece = sourceSquare.getPiece();

        // handle promotion
        if (Boolean.TRUE.equals(move.getPromotion())) {
            targetSquare.placePiece(move.getPromotionPiece());
            sourceSquare.clearSquare();
            MoveTracker.addMove(move);
            return;
        }
        // move piece
        targetSquare.placePiece(piece);
        sourceSquare.clearSquare();

        // handle en passant
        if (Boolean.TRUE.equals(move.getEnPassant()) && move.getEnPassantSquare() != null) {
            move.getEnPassantSquare().clearSquare();
        }

        // handle castle (move rook)
        if (Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) {
            int rank = player.isWhite() ? 7 : 0;
            int rookStartFile = Boolean.TRUE.equals(move.getCastleShort()) ? 7 : 0;
            int rookEndFile = Boolean.TRUE.equals(move.getCastleShort()) ? 5 : 3;
            Square rookStartSquare = board.getSquares()[rank][rookStartFile];
            Square rookEndSquare = board.getSquares()[rank][rookEndFile];
            rookEndSquare.placePiece(rookStartSquare.getPiece());
            rookStartSquare.clearSquare();

            player.disallowCastle();
        }

        MoveTracker.addMove(move);
    }

    public static void unmakeMove(Move move, Player player, Board board) {
        Square sourceSquare = move.getStartSquare();
        Square targetSquare = move.getEndSquare();
        Piece piece = targetSquare.getPiece();

        // place piece back to source square
        if (Boolean.TRUE.equals(move.getPromotion())) {
            sourceSquare.placePiece(new Pawn(move.getPromotionPiece().isWhite()));
        } else {
            sourceSquare.placePiece(piece);
        }

        // remove piece from target
        targetSquare.clearSquare();

        // reinstate captured piece
        if (Boolean.TRUE.equals(move.getStandardCapture()) && move.getCapturedPiece() != null) {
            targetSquare.placePiece(move.getCapturedPiece());
        }

        // handle en passant
        if (Boolean.TRUE.equals(move.getEnPassant()) && move.getEnPassantSquare() != null) {
            move.getEnPassantSquare().placePiece(new Pawn(!sourceSquare.getPiece().isWhite()));
        }

        // handle castle (move rook back to source Square)
        if (Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) {
            int rank = player.isWhite() ? 7 : 0;
            int rookStartFile = Boolean.TRUE.equals(move.getCastleShort()) ? 7 : 0;
            int rookEndFile = Boolean.TRUE.equals(move.getCastleShort()) ? 5 : 3;
            Square rookStartSquare = board.getSquares()[rank][rookStartFile];
            Square rookEndSquare = board.getSquares()[rank][rookEndFile];
            rookStartSquare.placePiece(rookEndSquare.getPiece());
            rookEndSquare.clearSquare();

            player.reAllowCastle();
        }

        MoveTracker.removeLastMove();
    }
}
