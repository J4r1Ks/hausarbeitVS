package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class GameChat extends JPanel {

    private final Color secondaryColor = new Color(235, 195, 155);

    public GameChat() {
        JLabel label = new JLabel("CHAT");
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setFont(new Font("Mono", Font.PLAIN, 30));
        label.setForeground(secondaryColor);
        label.setVisible(true);
        this.add(label);
        /*JTextArea textArea = new JTextArea("CHAT");
        add(textArea);*/

    }
}
