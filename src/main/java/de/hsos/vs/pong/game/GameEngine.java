package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class GameEngine extends JPanel {

    private byte choosePlayer = 0;

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
    private PlayerController player2 = new PlayerController(true);
    private PlayerController player3 = new PlayerController(false);
    private PlayerController player4 = new PlayerController(false);
    private int player1Position = 300;
    private int player2Position = 300;
    private int player3Position = 300;
    private int player4Position = 300;

    private float ballX = 400;
    private float ballY = 400;
    private float dirX = -1;
    private float dirY = -0.5f;
    private float ballSpeed = 12;

    public GameEngine() {
        frame.setTitle("Quong");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setMinimumSize(new Dimension(1000, 800));
        this.setBackground(mainColor);

        //Kaputt
        /*JLabel label = new JLabel("CHAT");
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setFont(new Font("Mono", Font.PLAIN, 30));
        label.setForeground(secondaryColor);
        label.setVisible(true);
        frame.add(label);*/

        frame.addKeyListener(player1);
        frame.addKeyListener(player2);
        frame.addKeyListener(player3);
        frame.addKeyListener(player4);

        frame.pack();
        frame.setVisible(true);
        frame.add(this);
    }

    @Override
    public void update(Graphics g) {
        this.repaint();
        //this.paint(this.getGraphics());
        switch (choosePlayer) {
            case 0:
                player1Position = playerMovementVertical(player1, player1Position); break;
            case 1:
                player2Position = playerMovementVertical(player2, player2Position); break;
            case 2:
                player3Position = playerMovementHorizontal(player3, player3Position); break;
            case 3:
                player4Position = playerMovementHorizontal(player4, player4Position); break;
        }
        ballMovement();
    }

    private int playerMovementVertical(PlayerController player, int playerPosition) {
        if(player.upPressed){
            if(playerPosition-20 >= 0){
                return playerPosition -= 20;
            }
        }else if(player.downPressed){
            if(playerPosition+20 < 763-playerHeight){
                return playerPosition += 20;
            }
        }
        return playerPosition;
    }

    private int playerMovementHorizontal(PlayerController player, int playerPosition) {
        if(player.leftPressed){
            if(playerPosition-20 >= 0){
                return playerPosition -= 20;
            }
        }else if(player.rightPressed){
            if(playerPosition+20 <= 800-playerHeight){
                return playerPosition += 20;
            }
        }
        return playerPosition;
    }

    private void ballMovement(){
        ballX += dirX * ballSpeed;
        ballY += dirY * ballSpeed;
        if(ballX <= 0 || ballX >= 800 - 16){
            //dirX *= (float) Math.random() * 0.5f + 0.5f;
            dirX *= -1;
            ballSpeed += 1f;
        }else if(ballY < 0 || ballY > 763 - 16){
            //dirY *= (float) Math.random() * 0.5f + 0.5f;
            dirY *= -1;
            ballSpeed += 1f;
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
        g.fillRect(800-playerWidth, player2Position, playerWidth, playerHeight);

        g.setColor(player3Color);
        g.fillRect(player3Position, 0, playerHeight, playerWidth);

        g.setColor(player4Color);
        g.fillRect(player4Position, 751, playerHeight, playerWidth);

        g.setColor(Color.white);
        g.fillRect((int)ballX, (int) ballY, 16, 16);
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


