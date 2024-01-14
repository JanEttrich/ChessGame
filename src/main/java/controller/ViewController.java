package controller;

import core.modes.GameWithView;
import frontend.BoardDisplay;
import frontend.MoveResource;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.System.exit;

public class ViewController extends MouseAdapter {
    private GameWithView game;
    private final BoardDisplay boardDisplay;
    private final boolean humanOpponent;

    // record mouse press
    private int startRow = 0;
    private int startCol = 0;

    public ViewController(boolean humanOpponent) {
        this.humanOpponent = humanOpponent;
        game = new GameWithView(null, humanOpponent);
        boardDisplay = new BoardDisplay(game.getBoard());

        // register controller as observer
        boardDisplay.addMouseListener(this);
        boardDisplay.addMouseMotionListener(this);

        // add board to background container
        JFrame frame = FrameInitializer.initFrame();
        frame.add(boardDisplay);
        frame.setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startRow = e.getY() / BoardDisplay.SQUARE_SIZE;
        startCol = e.getX() / BoardDisplay.SQUARE_SIZE;
        boardDisplay.setSelectedPiece(game.getBoard().getPieceFromSquare(startRow, startCol));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (boardDisplay.getSelectedPiece() != null) {
            // fix offset of selected piece to mouse
            boardDisplay.setSelectedXPos(e.getX() - BoardDisplay.SQUARE_SIZE / 2);
            boardDisplay.setSelectedYPos(e.getY() - BoardDisplay.SQUARE_SIZE / 2);
            boardDisplay.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int endRow = e.getY() / BoardDisplay.SQUARE_SIZE;
        int endCol = e.getX() / BoardDisplay.SQUARE_SIZE;
        boolean moved = game.handleMove(new MoveResource(startRow, startCol, endRow, endCol));

        boardDisplay.repaint();
        boardDisplay.setSelectedPiece(null);

        if (!moved) {
            return;
        }

        if (!game.canPlayerMove()) {
            showGameEndDialog();
            return;
        }

        if (!humanOpponent) {
            game.makeRandomMove(game.getActivePlayer().isWhite());
            if (!game.canPlayerMove()) {
                showGameEndDialog();
            }
        }
    }

    public void showGameEndDialog() {
        String title;
        String message;
        if (game.canKingCanBeCaptured()) {
            title = "Checkmate";
            message = "Checkmate, " + (game.getActivePlayer().isWhite() ? "Black" : "White") + " wins";
        } else {
            title = "Stalemate";
            message = "Draw by Stalemate";
        }

        int option = JOptionPane.showOptionDialog(null, message, title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"restart"}, null);
        if (option == 0) {
            resetGame();
        } else {
            exit(0);
        }
    }

    public void resetGame() {
        game = new GameWithView(null, humanOpponent);
        boardDisplay.setBoard(game.getBoard());
        boardDisplay.repaint();
    }
}
