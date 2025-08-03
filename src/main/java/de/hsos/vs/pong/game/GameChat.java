package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class GameChat extends JPanel {

    private final Color secondaryColor = new Color(235, 195, 155);
    private final Color mainColor = new Color(20, 60, 100);

    public GameChat() {
        this.setPreferredSize(new Dimension(200, 800));
        this.setBackground(mainColor);

        JLabel label = new JLabel("CHAT");
        label.setFont(new Font("Mono", Font.PLAIN, 30));
        label.setForeground(secondaryColor);
        label.setVisible(true);
        this.add(label);
        JTextArea textArea = new JTextArea("CHAT");
        this.add(textArea);

    }
}
