package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class GameEngine extends JPanel {

    private JFrame frame = new JFrame();

    private Color mainColor = new Color(20, 60, 100);
    private Color secondaryColor = new Color(235, 195, 155);

    private Color player1Color = new Color(100, 20, 20);
    private Color player2Color = new Color(20, 100, 20);
    private Color player3Color = new Color(100, 100, 20);
    private Color player4Color = new Color(100, 20, 100);

    private int playerWidth = 12;
    private int playerHeight = 120;

    private PlayerController player1 = new PlayerController(true);
    private int player1Position = 300;

    public GameEngine() {
        frame.setTitle("Quong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(1000, 800));
        this.setBackground(mainColor);

        JLabel label = new JLabel("CHAT");
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setFont(new Font("Mono", Font.PLAIN, 30));
        label.setForeground(secondaryColor);
        label.setVisible(true);
        frame.add(label);

        frame.addKeyListener(player1);

        frame.pack();
        frame.setVisible(true);
        frame.add(this);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
        //this.paint(this.getGraphics());
        if(player1.upPressed){
            player1Position -= 20;
        }else if(player1.downPressed){
            player1Position += 20;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);

        g.setColor(player1Color);
        g.fillRect(0, player1Position, playerWidth, playerHeight);

        g.setColor(player2Color);
        g.fillRect(800-playerWidth, 300, playerWidth, playerHeight);

        g.setColor(player3Color);
        g.fillRect(300, 0, playerHeight, playerWidth);

        g.setColor(player4Color);
        g.fillRect(300, 751, playerHeight, playerWidth);

        g.setColor(Color.white);
        g.fillRect(400, 400, 16, 16);
    }

    public static void main(String[ ] args){
        GameEngine game = new GameEngine();
        long timer = System.currentTimeMillis();

        while(true){
            if(System.currentTimeMillis()-timer > 66){
                timer = System.currentTimeMillis();
                game.update(game.getGraphics());
            }
        }

    }
}


