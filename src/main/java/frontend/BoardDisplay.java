package frontend;

import core.Pieces;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static core.Board.squares;

public class BoardDisplay extends JPanel {
    public static final int SQUARE_SIZE = 85;
    private final int rows;
    private final int cols;
    private final PieceDisplay pieceDisplay;

    // fields changing on mouse actions
    @Getter
    @Setter
    private Integer selectedSquare;
    @Getter
    private final List<Integer> availableSquares = new ArrayList<>();
    @Setter
    private int selectedXPos;
    @Setter
    private int selectedYPos;

    // marker for reachable square
    private final Image squareMarker;

    public BoardDisplay() {
        this.pieceDisplay = new PieceDisplay();
        this.rows = 8;
        this.cols = 8;

        setPreferredSize(new Dimension(cols * SQUARE_SIZE, rows * SQUARE_SIZE));

        try {
            squareMarker = ImageIO.read(new File("src/main/resources/marker.png"))
                    .getScaledInstance(BoardDisplay.SQUARE_SIZE / 3, BoardDisplay.SQUARE_SIZE / 3, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int piece = squares[row * 8 + col];

                // highlight square with selected piece or if it is reachable with capture
                if (piece != Pieces.NONE && selectedSquare != null && (piece == squares[selectedSquare] || availableSquares.contains(selectedSquare))) {
                    g2d.setColor(new Color(100, 111, 64));
                } else {
                    g2d.setColor((row + col) % 2 == 0 ? new Color(227, 197, 181) : new Color(157, 105, 53));
                }
                // draw square
                g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);


                // draw circle for reachable square
                if (availableSquares.contains(selectedSquare)) {
                    g2d.drawImage(squareMarker, col * SQUARE_SIZE + SQUARE_SIZE / 3, row * SQUARE_SIZE + SQUARE_SIZE / 3, null);
                }

                // draw piece
                if (piece != Pieces.NONE && selectedSquare != null && row * 8 + col != selectedSquare) {
                    pieceDisplay.paint(g2d, piece, col * SQUARE_SIZE, row * SQUARE_SIZE);
                }

                // draw selected piece
                if (selectedSquare != null && squares[selectedSquare] != Pieces.NONE) {
                    pieceDisplay.paint(g2d, squares[selectedSquare], selectedXPos, selectedYPos);
                }
            }
        }
    }
}
