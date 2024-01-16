package core.move;

import core.Pieces;

import static core.Board.*;

public class MoveMaker {
    private MoveMaker() {
    }

    public static void makeMove(Move move) {
        Integer sourceSquare = move.getStartSquare();
        Integer targetSquare = move.getEndSquare();
        int piece = move.getPiece();

        var piecePosList = Pieces.isPieceOfColor(move.getPiece(), Pieces.WHITE) ? piecesPosWhite : piecePosBlack;
        var piecePosListOpponent = Pieces.isPieceOfColor(move.getPiece(), Pieces.WHITE) ? piecePosBlack : piecesPosWhite;

        // handle promotion
        if (Boolean.TRUE.equals(move.getPromotion())) {
            squares[targetSquare] = move.getPromotionPiece();
            squares[sourceSquare] = Pieces.NONE;
            piecePosList.add(targetSquare);
            piecePosList.remove(sourceSquare);
            piecePosListOpponent.remove(targetSquare);
            MoveTracker.addMove(move);
            return;
        }
        // move piece
        squares[targetSquare] = piece;
        squares[sourceSquare] = Pieces.NONE;
        piecePosList.add(targetSquare);
        piecePosList.remove(sourceSquare);
        piecePosListOpponent.remove(targetSquare);

        // handle en passant
        if (Boolean.TRUE.equals(move.getEnPassant())) {
            squares[move.getEnPassantSquare()] = Pieces.NONE;
            piecePosListOpponent.remove(Integer.valueOf(move.getEnPassantSquare()));
        }

        // handle castle (move rook)
        if (Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) {
            int rank = sourceSquare / 8;
            int rookStartFile = Boolean.TRUE.equals(move.getCastleShort()) ? 7 : 0;
            int rookEndFile = Boolean.TRUE.equals(move.getCastleShort()) ? 5 : 3;

            Integer rookStartSquare = 8 * rank + rookStartFile;
            Integer rookEndSquare = 8 * rank + rookEndFile;
            squares[rookEndSquare] = squares[rookStartSquare];
            squares[rookStartSquare] = Pieces.NONE;
            piecePosList.add(rookEndSquare);
            piecePosList.remove(rookStartSquare);
        }

        MoveTracker.addMove(move);
    }

    public static void unmakeMove(Move move) {
        Integer sourceSquare = move.getStartSquare();
        Integer targetSquare = move.getEndSquare();
        int piece = move.getPiece();

        var piecePosList = Pieces.isPieceOfColor(move.getPiece(), Pieces.WHITE) ? piecesPosWhite : piecePosBlack;
        var piecePosListOpponent = Pieces.isPieceOfColor(move.getPiece(), Pieces.WHITE) ? piecePosBlack : piecesPosWhite;

        // place piece back to source square
        if (Boolean.TRUE.equals(move.getPromotion())) {
            boolean white = Pieces.isPieceOfColor(move.getPromotionPiece(), Pieces.WHITE);
            squares[sourceSquare] = white ? Pieces.WHITE | Pieces.PAWN : Pieces.BLACK | Pieces.PAWN;
        } else {
            squares[sourceSquare] = piece;
        }
        piecePosList.remove(targetSquare);

        // remove piece from target
        squares[targetSquare] = Pieces.NONE;
        piecePosList.add(sourceSquare);

        // reinstate captured piece
        if (Boolean.TRUE.equals(move.getStandardCapture())) {
            squares[targetSquare] = move.getCapturedPiece();
            piecePosListOpponent.add(targetSquare);
        }

        // handle en passant
        if (Boolean.TRUE.equals(move.getEnPassant())) {
            boolean white = Pieces.isPieceOfColor(piece, Pieces.WHITE);
            squares[move.getEnPassantSquare()] = white ? Pieces.BLACK | Pieces.PAWN : Pieces.WHITE | Pieces.PAWN;
            piecePosListOpponent.add(move.getEnPassantSquare());
        }

        // handle castle (move rook back to source Square)
        if (Boolean.TRUE.equals(move.getCastleShort()) || Boolean.TRUE.equals(move.getCastleLong())) {
            int rank = sourceSquare / 8;
            int rookStartFile = Boolean.TRUE.equals(move.getCastleShort()) ? 7 : 0;
            int rookEndFile = Boolean.TRUE.equals(move.getCastleShort()) ? 5 : 3;

            Integer rookStartSquare = 8 * rank + rookStartFile;
            Integer rookEndSquare = 8 * rank + rookEndFile;
            squares[rookStartSquare] = squares[rookEndSquare];
            squares[rookEndSquare] = Pieces.NONE;

            piecePosList.add(rookStartSquare);
            piecePosList.remove(rookEndSquare);
        }

        MoveTracker.removeLastMove();
    }
}
