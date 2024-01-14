package controller;

import core.Game;
import frontend.BoardDisplay;
import frontend.ContainerDisplay;
import frontend.MoveResource;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameController extends MouseAdapter {
    private final Game game;
    private final BoardDisplay boardDisplay;

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
        ContainerDisplay containerDisplay = new ContainerDisplay(boardDisplay);
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
    }
}
