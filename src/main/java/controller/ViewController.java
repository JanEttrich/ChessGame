package controller;

import core.Board;
import core.Pieces;
import core.evaluation.Evaluator;
import core.modes.GameWithView;
import core.move.Move;
import frontend.BoardDisplay;
import frontend.MoveResource;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static core.Board.squares;
import static java.lang.System.exit;

public class ViewController extends MouseAdapter {
    private GameWithView game;
    private final BoardDisplay boardDisplay;
    private final boolean humanOpponent;

    // record mouse press
    private int startRow = 0;
    private int startCol = 0;

    private JLabel evalLabel;

    public ViewController(boolean humanOpponent) {
        this.humanOpponent = humanOpponent;
        game = new GameWithView(null, humanOpponent);
        boardDisplay = new BoardDisplay();

        // register controller as observer
        boardDisplay.addMouseListener(this);
        boardDisplay.addMouseMotionListener(this);

        // add board to background container
        JFrame frame = FrameInitializer.initFrame();
        frame.add(boardDisplay);

        int evaluation = Evaluator.evaluate(game.getActivePlayer().isWhite());

        evalLabel = new JLabel("Eval: " + evaluation);

        JPanel panel = new JPanel();
        panel.add(evalLabel);
        frame.add(panel);

        frame.setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startRow = e.getY() / BoardDisplay.SQUARE_SIZE;
        startCol = e.getX() / BoardDisplay.SQUARE_SIZE;
        boardDisplay.setSelectedSquare(startRow * 8 + startCol);
        if (squares[boardDisplay.getSelectedSquare()] != Pieces.NONE) {
            int color = game.getActivePlayer().isWhite() ? Pieces.WHITE : Pieces.BLACK;
            var legalMovesForPiece = game.filterMoves(Pieces.generatePseudoLegalMoves(boardDisplay.getSelectedSquare(), color));
            var availableSquares = legalMovesForPiece.stream().map(Move::getEndSquare).toList();
            boardDisplay.getAvailableSquares().addAll(availableSquares);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (boardDisplay.getSelectedSquare() != null && squares[boardDisplay.getSelectedSquare()] != Pieces.NONE) {
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
        boardDisplay.setSelectedSquare(null);
        boardDisplay.getAvailableSquares().clear();

        if (!moved) {
            return;
        }

        if (!game.canPlayerMove() || game.isInsufficientMaterial()) {
            showGameEndDialog();
            return;
        }

        if (!humanOpponent) {
            game.makeRandomMove();
            if (!game.canPlayerMove() || game.isInsufficientMaterial()) {
                showGameEndDialog();
            }
            evalLabel.setText("Eval: " + Evaluator.evaluate(game.getActivePlayer().isWhite()));
        }
    }

    public void showGameEndDialog() {
        String title;
        String message;
        if (game.canKingCanBeCaptured()) {
            title = "Checkmate";
            message = "Checkmate, " + (game.getActivePlayer().isWhite() ? "Black" : "White") + " wins";
        } else if (game.isInsufficientMaterial()) {
            title = "Draw";
            message = "Draw by insufficient material";
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
        Board.resetBoard();
        boardDisplay.repaint();
    }
}
