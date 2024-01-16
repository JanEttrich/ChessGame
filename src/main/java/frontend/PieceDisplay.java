package frontend;

import core.Pieces;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PieceDisplay {
    private final BufferedImage pieceImages;
    private final int singlePieceSize;
    private Map<Integer, Image> pieceImageMap;

    public PieceDisplay() {
        try {
            pieceImages = ImageIO.read(new File("src/main/resources/pieces.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        singlePieceSize = pieceImages.getWidth() / 6;
        initializeMap();
    }

    public void paint(Graphics2D g2d, int piece, int x, int y) {
        Image pieceImage = pieceImageMap.get(piece);
        g2d.drawImage(pieceImage, x, y, null);
    }

    private void initializeMap() {
        pieceImageMap = new HashMap<>();
        pieceImageMap.put(Pieces.KING | Pieces.WHITE, pieceImages.getSubimage(0, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.QUEEN | Pieces.WHITE, pieceImages.getSubimage(singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.BISHOP | Pieces.WHITE, pieceImages.getSubimage(2 * singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.KNIGHT | Pieces.WHITE, pieceImages.getSubimage(3 * singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.ROOK | Pieces.WHITE, pieceImages.getSubimage(4 * singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.PAWN | Pieces.WHITE, pieceImages.getSubimage(5 * singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.KING | Pieces.BLACK, pieceImages.getSubimage(0, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.QUEEN | Pieces.BLACK, pieceImages.getSubimage(singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.BISHOP | Pieces.BLACK, pieceImages.getSubimage(2 * singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.KNIGHT | Pieces.BLACK, pieceImages.getSubimage(3 * singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.ROOK | Pieces.BLACK, pieceImages.getSubimage(4 * singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put(Pieces.PAWN | Pieces.BLACK, pieceImages.getSubimage(5 * singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
    }
}
