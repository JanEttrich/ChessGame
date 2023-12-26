package util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChessMove {
    private String piece;
    private String endSquare;
    private boolean capture;

    private String rankStart;
    private String fileStart;

    private boolean pawnPromotion;
    private String promotionPiece;

    private boolean castleShort;
    private boolean castleLong;

    public static ChessMove getStandardMove(String piece, String endSquare, boolean capture) {
        return new ChessMove(piece, endSquare, capture, null, null, false, null, false, false);
    }

    public static ChessMove getExtendedMove(String piece, String endSquare, boolean capture, String rankStart, String fileStart) {
        return new ChessMove(piece, endSquare, capture, rankStart, fileStart, false, null, false, false);
    }

    public static ChessMove getPawnCapture(String piece, String endSquare, String fileStart) {
        return new ChessMove(piece, endSquare, false, null, fileStart, false, null, false, false);
    }


    public static ChessMove getPawnPromotion(String piece, String endSquare, String promotionPiece) {
        return new ChessMove(piece, endSquare, false, null, null, true, promotionPiece, false, false);
    }

    public static ChessMove getPawnCaptureWithPromotion(String piece, String endSquare, String fileStart, String promotionPiece) {
        return new ChessMove(piece, endSquare, true, null, fileStart, true, promotionPiece, false, false);
    }

    public static ChessMove getCastle(boolean castleShort) {
        return new ChessMove(null, null, false, null, null, false, null, castleShort, !castleShort);
    }
}
