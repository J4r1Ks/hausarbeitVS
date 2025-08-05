package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class GameChat extends JPanel {

    private final Color secondaryColor = new Color(235, 195, 155);
    private final Color mainColor = new Color(20, 60, 100);

    private final Color player1Color = new Color(100, 20, 20);
    private final Color player2Color = new Color(20, 100, 20);
    private final Color player3Color = new Color(100, 100, 20);
    private final Color player4Color = new Color(100, 20, 100);

    public JLabel player1Score = new JLabel("0", SwingConstants.CENTER);
    public JLabel player2Score = new JLabel("0", SwingConstants.CENTER);
    public JLabel player3Score = new JLabel("0", SwingConstants.CENTER);
    public JLabel player4Score = new JLabel("0", SwingConstants.CENTER);

    public GameChat() {
        this.setPreferredSize(new Dimension(186, 800));
        this.setBackground(mainColor);

        player1Score.setFont(new Font("Mono", Font.PLAIN, 30));
        player1Score.setForeground(player1Color);
        player1Score.setVisible(true);
        player1Score.setPreferredSize(new Dimension(20, 30));
        this.add(player1Score);

        player2Score.setFont(new Font("Mono", Font.PLAIN, 30));
        player2Score.setForeground(player2Color);
        player2Score.setVisible(true);
        player2Score.setPreferredSize(new Dimension(20, 30));
        this.add(player2Score);

        player3Score.setFont(new Font("Mono", Font.PLAIN, 30));
        player3Score.setForeground(player3Color);
        player3Score.setVisible(true);
        player3Score.setPreferredSize(new Dimension(20, 30));
        this.add(player3Score);

        player4Score.setFont(new Font("Mono", Font.PLAIN, 30));
        player4Score.setForeground(player4Color);
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

    public void updateScore(int player1ScoreInt, int player2ScoreInt, int player3ScoreInt, int player4ScoreInt) {
        player1Score.setText(""+player1ScoreInt);
        player2Score.setText(""+player2ScoreInt);
        player3Score.setText(""+player3ScoreInt);
        player4Score.setText(""+player4ScoreInt);
    }

}
