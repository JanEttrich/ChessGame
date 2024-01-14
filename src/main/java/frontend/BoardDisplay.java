package frontend;

import core.Board;
import core.Piece;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class BoardDisplay extends JPanel {
    public static final int SQUARE_SIZE = 85;
    private final int rows;
    private final int cols;
    @Setter
    private Board board;
    private final PieceDisplay pieceDisplay;

    @Getter
    @Setter
    private Piece selectedPiece;
    @Setter
    private int selectedXPos;
    @Setter
    private int selectedYPos;

    public BoardDisplay(Board board) {
        this.board = board;
        this.pieceDisplay = new PieceDisplay();
        this.rows = board.getSquares().length;
        this.cols = board.getSquares()[0].length;

        setPreferredSize(new Dimension(cols * SQUARE_SIZE, rows * SQUARE_SIZE));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // draw square
                g2d.setColor((row + col) % 2 == 0 ? new Color(227, 197, 181) : new Color(157, 105, 53));
                g2d.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

                // draw piece
                if (board.getSquares()[row][col].isOccupied() && board.getSquares()[row][col].getPiece() != selectedPiece) {
                    pieceDisplay.paint(g2d, board.getSquares()[row][col].getPiece(), col * SQUARE_SIZE, row * SQUARE_SIZE);
                }

                // draw selected piece
                if (selectedPiece != null) {
                    pieceDisplay.paint(g2d, selectedPiece, selectedXPos, selectedYPos);
                }
            }
        }
    }
}
