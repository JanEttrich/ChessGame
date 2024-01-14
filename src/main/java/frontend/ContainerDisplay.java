package frontend;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class ContainerDisplay {
    private final JFrame frame;

    public ContainerDisplay(BoardDisplay boardDisplay) {
        frame = new JFrame();
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.add(boardDisplay);
    }
}
