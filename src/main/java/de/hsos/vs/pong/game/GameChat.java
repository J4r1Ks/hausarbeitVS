package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class GameChat extends JPanel {

    private final Color secondaryColor = new Color(235, 195, 155);
    private final Color mainColor = new Color(20, 60, 100);

    public JLabel[] playerScores;

    private PlayerController[] players;

    public GameChat(PlayerController[] players) {
        this.setPreferredSize(new Dimension(186, 800));
        this.setBackground(mainColor);

        this.players = players;

        playerScores = new JLabel[players.length];
        for(int i = 0; i < players.length; i++) {
            playerScores[i] = new JLabel("0", SwingConstants.CENTER);
            playerScores[i].setFont(new Font("Mono", Font.PLAIN, 30));
            playerScores[i].setForeground(players[i].playerColor);
            playerScores[i].setVisible(true);
            playerScores[i].setPreferredSize(new Dimension(20, 30));
            this.add(playerScores[i]);
        }

        JLabel label = new JLabel("CHAT", SwingConstants.CENTER);
        label.setFont(new Font("Mono", Font.PLAIN, 30));
        label.setForeground(secondaryColor);
        label.setVisible(true);
        this.add(label, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea("Moin");
        textArea.setPreferredSize(new Dimension(186, 64));
        this.add(textArea, BorderLayout.PAGE_END);


        JPanel comments = new JPanel();
        comments.setLayout(new BoxLayout(comments, BoxLayout.PAGE_AXIS));
        comments.setBackground(mainColor);

        JButton senden = new JButton("Senden");
        senden.setPreferredSize(new Dimension(186, 20));
        senden.addActionListener(e -> {
            JLabel message = new JLabel(textArea.getText());
            message.setFont(new Font("Mono", Font.PLAIN, 16));
            message.setForeground(secondaryColor);
            message.setVisible(true);
            comments.add(message, BorderLayout.PAGE_END);
            textArea.setText(textArea.getText());
        });
        this.add(senden, BorderLayout.PAGE_END);

        JScrollPane scrollPane = new JScrollPane(comments);
        scrollPane.setPreferredSize(new Dimension(186, 620));
        this.add(scrollPane, BorderLayout.CENTER);

    }

    public void updateScore() {
        for(int i = 0; i < playerScores.length; i++) {
            playerScores[i].setText(""+players[i].playerScore);
        }
    }

}
