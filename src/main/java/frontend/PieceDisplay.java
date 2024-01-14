package frontend;

import core.Piece;

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
    private Map<String, Image> pieceImageMap;

    public PieceDisplay() {
        try {
            pieceImages = ImageIO.read(new File("src/main/resources/pieces.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        singlePieceSize = pieceImages.getWidth() / 6;
        initializeMap();
    }

    public void paint(Graphics2D g2d, Piece piece, int x, int y) {
        Image pieceImage = pieceImageMap.get(piece.getDisplay());
        g2d.drawImage(pieceImage, x, y, null);
    }

    private void initializeMap() {
        pieceImageMap = new HashMap<>();
        pieceImageMap.put("K", pieceImages.getSubimage(0, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("Q", pieceImages.getSubimage(singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("B", pieceImages.getSubimage(2 * singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("N", pieceImages.getSubimage(3 * singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("R", pieceImages.getSubimage(4 * singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("P", pieceImages.getSubimage(5 * singlePieceSize, 0, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("k", pieceImages.getSubimage(0, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("q", pieceImages.getSubimage(singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("b", pieceImages.getSubimage(2 * singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("n", pieceImages.getSubimage(3 * singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("r", pieceImages.getSubimage(4 * singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
        pieceImageMap.put("p", pieceImages.getSubimage(5 * singlePieceSize, singlePieceSize, singlePieceSize, singlePieceSize)
                .getScaledInstance(BoardDisplay.SQUARE_SIZE, BoardDisplay.SQUARE_SIZE, Image.SCALE_SMOOTH));
    }
}
