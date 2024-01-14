package controller;

import core.modes.GameInTerminal;

import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;

public class MenuController {
    public MenuController() {
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setVisible(true);

        int gameModeOption = JOptionPane.showOptionDialog(null, "Wähle deinen Gegner aus", "Gegner",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Maschine", "Mensch"}, null);
        if (gameModeOption == JOptionPane.CLOSED_OPTION) {
            exit(0);
        }
        boolean humanOpponent = gameModeOption == 1;

        int inputOption = JOptionPane.showOptionDialog(null, "Wähle deine Eingabeart aus", "Eingabe",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null,
                new String[]{"Board", "Terminal"}, null);
        if (inputOption == 0) {
            frame.setVisible(false);
            new ViewController(humanOpponent);
        } else if (inputOption == 1) {
            frame.setVisible(false);
            new GameInTerminal(null, humanOpponent).startGameOnTerminal();
        } else {
            exit(0);
        }
    }

}
