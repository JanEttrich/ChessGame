package controller;

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
        frame.setVisible(false);
        new ViewController(humanOpponent);


    }

}
