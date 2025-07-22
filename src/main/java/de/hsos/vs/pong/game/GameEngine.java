package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class GameEngine extends JFrame {

    private Color mainColor = new Color(20, 60, 100);
    private Color secondaryColor = new Color(100, 60, 20);

    private Color player1 = new Color(100, 20, 20);
    private Color player2 = new Color(20, 100, 20);
    private Color player3 = new Color(100, 100, 20);
    private Color player4 = new Color(100, 20, 100);

    private int playerWidth = 12;
    private int playerHeight = 120;

    public GameEngine() {
        this.setTitle("Quong");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setMinimumSize(new Dimension(1000, 800));
        this.getContentPane().setBackground(mainColor);

        JLabel label = new JLabel("CHAT");
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setFont(new Font("Mono", Font.PLAIN, 30));
        label.setForeground(secondaryColor);
        label.setVisible(true);
        this.add(label);


        this.pack();
        this.setVisible(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 800, 800);

        g.setColor(player1);
        g.fillRect(playerWidth-5, 300, playerWidth, playerHeight);

        g.setColor(player2);
        g.fillRect(800-playerWidth, 300, playerWidth, playerHeight);

        g.setColor(player3);
        g.fillRect(300, 30, playerHeight, playerWidth);

        g.setColor(player4);
        g.fillRect(300, 800-19, playerHeight, playerWidth);

        g.setColor(Color.white);
        g.fillRect(400, 400, 16, 16);
    }

    public static void main(String[ ] args){
        GameEngine game = new GameEngine();
        game.paint(game.getGraphics());
    }
}


