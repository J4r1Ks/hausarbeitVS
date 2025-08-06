package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class GameChat extends JPanel {

    private final Color secondaryColor = new Color(235, 195, 155);
    private final Color mainColor = new Color(20, 60, 100);

    public JLabel player1Score = new JLabel("0", SwingConstants.CENTER);
    public JLabel player2Score = new JLabel("0", SwingConstants.CENTER);
    public JLabel player3Score = new JLabel("0", SwingConstants.CENTER);
    public JLabel player4Score = new JLabel("0", SwingConstants.CENTER);

    private PlayerController[] players;

    public GameChat(PlayerController[] players) {
        this.players = players;

        this.setPreferredSize(new Dimension(186, 800));
        this.setBackground(mainColor);

        player1Score.setFont(new Font("Mono", Font.PLAIN, 30));
        player1Score.setForeground(players[0].playerColor);
        player1Score.setVisible(true);
        player1Score.setPreferredSize(new Dimension(20, 30));
        this.add(player1Score);

        player2Score.setFont(new Font("Mono", Font.PLAIN, 30));
        player2Score.setForeground(players[1].playerColor);
        player2Score.setVisible(true);
        player2Score.setPreferredSize(new Dimension(20, 30));
        this.add(player2Score);

        player3Score.setFont(new Font("Mono", Font.PLAIN, 30));
        player3Score.setForeground(players[2].playerColor);
        player3Score.setVisible(true);
        player3Score.setPreferredSize(new Dimension(20, 30));
        this.add(player3Score);

        player4Score.setFont(new Font("Mono", Font.PLAIN, 30));
        player4Score.setForeground(players[3].playerColor);
        player4Score.setVisible(true);
        player4Score.setPreferredSize(new Dimension(20, 30));
        this.add(player4Score);


        JLabel label = new JLabel("CHAT", SwingConstants.CENTER);
        label.setFont(new Font("Mono", Font.PLAIN, 30));
        label.setForeground(secondaryColor);
        label.setVisible(true);
        this.add(label, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea("CHAT");
        textArea.setPreferredSize(new Dimension(186, 64));
        this.add(textArea, BorderLayout.PAGE_END);

    }

    public void updateScore() {
        player1Score.setText(""+players[0].playerScore);
        player2Score.setText(""+players[1].playerScore);
        player3Score.setText(""+players[2].playerScore);
        player4Score.setText(""+players[3].playerScore);
    }

}
