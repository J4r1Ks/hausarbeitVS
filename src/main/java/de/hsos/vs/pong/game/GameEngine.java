package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class GameEngine extends JFrame {

    private Color mainColor = new Color(20, 60, 100);
    private Color secondaryColor = new Color(100, 60, 20);

    private Color player1Color = new Color(100, 20, 20);
    private Color player2Color = new Color(20, 100, 20);
    private Color player3Color = new Color(100, 100, 20);
    private Color player4Color = new Color(100, 20, 100);

    private int playerWidth = 12;
    private int playerHeight = 120;

    private PlayerController player1 = new PlayerController(true);
    private int player1Velocity = 0;
    private int player1Position = 300;

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

        this.addKeyListener(player1);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
        //this.paint(this.getGraphics());
        if(player1.upPressed){
            player1Velocity = 1;
            player1Position -= 20;
        }else if(player1.downPressed){
            player1Velocity = -1;
            player1Position += 20;
        }else{
            player1Velocity = 0;
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 800, 800);

        g.setColor(player1Color);
        g.fillRect(playerWidth-5, player1Position, playerWidth, playerHeight);

        g.setColor(player2Color);
        g.fillRect(800-playerWidth, 300, playerWidth, playerHeight);

        g.setColor(player3Color);
        g.fillRect(300, 30, playerHeight, playerWidth);

        g.setColor(player4Color);
        g.fillRect(300, 800-19, playerHeight, playerWidth);

        g.setColor(Color.white);
        g.fillRect(400, 400, 16, 16);
    }

    public static void main(String[ ] args){
        GameEngine game = new GameEngine();
        long timer = System.currentTimeMillis();

        while(true){
            if(System.currentTimeMillis()-timer > 200){
                timer =  System.currentTimeMillis();
                game.update(game.getGraphics());
            }
        }

    }
}


