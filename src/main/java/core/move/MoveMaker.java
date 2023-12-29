package core.move;

import core.Square;
import core.pieces.Pawn;

public class MoveMaker {
    private MoveMaker() {
    }

    public static void makeMove(Move move) {
        Square sourceSquare = move.getStartSquare();
        Square targetSquare = move.getEndSquare();

        // handle promotion
        if (Boolean.TRUE.equals(move.getPromotion())) {
            targetSquare.placePiece(move.getPromotionPiece());
            sourceSquare.clearSquare();
            MoveTracker.addMove(move);
            return;
        }
        // move piece
        targetSquare.placePiece(sourceSquare.getPiece());
        sourceSquare.clearSquare();

        // handle en passant
        if (Boolean.TRUE.equals(move.getEnPassant()) && move.getEnPassantSquare() != null) {
            move.getEnPassantSquare().clearSquare();
        }

        MoveTracker.addMove(move);
    }

    public static void unmakeMove(Move move) {
        Square sourceSquare = move.getStartSquare();
        Square targetSquare = move.getEndSquare();

        // place piece back to source square
        if (Boolean.TRUE.equals(move.getPromotion())) {
            sourceSquare.placePiece(new Pawn(move.getPromotionPiece().isWhite()));
        } else {
            sourceSquare.placePiece(targetSquare.getPiece());
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

        MoveTracker.removeLastMove();
    }
}
