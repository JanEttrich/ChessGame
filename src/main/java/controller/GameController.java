package controller;

import core.Game;
import frontend.BoardDisplay;
import frontend.ContainerDisplay;
import frontend.MoveResource;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.System.exit;

public class GameController extends MouseAdapter {
    private Game game;
    private BoardDisplay boardDisplay;
    private final ContainerDisplay containerDisplay;

    // record mouse press
    private int startRow = 0;
    private int startCol = 0;

    public GameController() {
        game = new Game(null);
        boardDisplay = new BoardDisplay(game.getBoard());

        // register controller as observer
        boardDisplay.addMouseListener(this);
        boardDisplay.addMouseMotionListener(this);

        // add board to background container
        containerDisplay = new ContainerDisplay(boardDisplay);
        containerDisplay.getFrame().setVisible(true);
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
        game.handleMove(new MoveResource(startRow, startCol, endRow, endCol));

        boardDisplay.repaint();
        boardDisplay.setSelectedPiece(null);

        if (game.canPlayerMove()) {
            return;
        }
        showGameEndDialog();
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
        game = new Game(null);
        boardDisplay.setBoard(game.getBoard());
        boardDisplay.repaint();
    }
}
