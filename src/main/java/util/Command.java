package util;

import lombok.Getter;

@Getter
public enum Command {
    SIMPLE_PIECE_MOVE("[RNBQK](x)?[a-h][1-8]") {
        @Override
        public ChessMove handleInput(String input) {
            String piece = input.substring(0, 1);
            if (input.contains("x")) {
                String endSquare = input.substring(2, 4);
                return ChessMove.getStandardMove(piece, endSquare, true);
            }

            String endSquare = input.substring(1, 3);
            return ChessMove.getStandardMove(piece, endSquare, false);
        }
    },
    EXTENDED_PIECE_MOVE("[RNB]([a-h]|[1-8])(x)?[a-h][1-8]") {
        @Override
        public ChessMove handleInput(String input) {
            String piece = input.substring(0, 1);
            String endSquare = "";
            boolean capture = input.contains("x");
            if (capture) {
                endSquare = input.substring(3, 5);
            } else {
                endSquare = input.substring(2, 4);
            }

            String start = input.substring(1, 2);
            if (start.matches("[a-h]")) {
                return ChessMove.getExtendedMove(piece, endSquare, capture, start, null);
            }
            return ChessMove.getExtendedMove(piece, endSquare, capture, null, start);
        }
    },
    PAWN_MOVE("([a-h]x)?[a-h][1-8](=[RBNQ])?") {
        @Override
        public ChessMove handleInput(String input) {
            String piece = "P";
            // standard move
            if (input.length() == 2) {
                return ChessMove.getStandardMove(piece, input, false);
            }
            // only capture
            if (input.length() == 4 && input.contains("x")) {
                String fileStart = input.substring(0, 1);
                String endSquare = input.substring(2, 4);
                return ChessMove.getPawnCapture(piece, endSquare, fileStart);
            }

            // only promotion
            if (input.length() == 4 && input.contains("=")) {
                String endSquare = input.substring(0, 2);
                String promotionPiece = input.substring(3, 4);
                return ChessMove.getPawnPromotion(piece, endSquare, promotionPiece);
            }

            // capture with promotion
            if (input.length() == 6) {
                String fileStart = input.substring(0, 1);
                String endSquare = input.substring(2, 4);
                String promotionPiece = input.substring(5, 6);
                return ChessMove.getPawnCaptureWithPromotion(piece, endSquare, fileStart, promotionPiece);
            }

            return null;
        }
    },
    CASTLE("(0-0-0|0-0)") {
        @Override
        public ChessMove handleInput(String input) {
            return input.matches("0-0-0") ? ChessMove.getCastle(false) : ChessMove.getCastle(true);
        }
    };

    private final String pattern;

    Command(String pattern) {
        this.pattern = pattern;
    }

    public abstract ChessMove handleInput(String input);

}
