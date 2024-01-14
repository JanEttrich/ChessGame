package controller;

import core.modes.GameInTerminal;

import javax.swing.*;

import static java.lang.System.exit;

public class MenuController {
    public MenuController() {
        JFrame frame = FrameInitializer.initFrame();
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
