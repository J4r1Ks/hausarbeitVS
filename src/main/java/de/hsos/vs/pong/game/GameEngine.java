package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class GameEngine extends JPanel {

    private byte choosePlayer = 0;

    private final Color player1Color = new Color(100, 20, 20);
    private final Color player2Color = new Color(20, 100, 20);
    private final Color player3Color = new Color(100, 100, 20);
    private final Color player4Color = new Color(100, 20, 100);

    private int playerWidth = 12;
    private int playerHeight = 120;

    private PlayerController player1;
    private PlayerController player2;
    private PlayerController player3;
    private PlayerController player4;
    private int player1Position = 300;
    private int player2Position = 300;
    private int player3Position = 300;
    private int player4Position = 300;

    private int ballSize = 16;
    private float ballX = 400;
    private float ballY = 400;
    private float dirX = -1;
    private float dirY = -0.5f;
    private float ballSpeed = 12;

    public GameEngine(PlayerController[] players) {
        player1 = players[0];
        player2 = players[1];
        player3 = players[2];
        player4 = players[3];

        JLabel label = new JLabel("CHAT");
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setFont(new Font("Mono", Font.PLAIN, 30));
        label.setForeground(Color.BLUE);
        label.setVisible(true);
        this.add(label);
    }

    public void start(){
        long timer = System.currentTimeMillis();

        while(true){
            if(System.currentTimeMillis()-timer > 66){
                timer = System.currentTimeMillis();
                this.update(this.getGraphics());
            }
        }
    }

    @Override
    public void update(Graphics g) {
        this.repaint();
        //switch (choosePlayer) {
            //case 0:
                player1Position = playerMovementVertical(player1, player1Position); //break;
           // case 1:
                player2Position = playerMovementVertical(player2, player2Position);// break;
            //case 2:
                player3Position = playerMovementHorizontal(player3, player3Position);// break;
           // case 3:
                player4Position = playerMovementHorizontal(player4, player4Position); //break;
       // }
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
        if(collisionY(player1Position)){
            if(ballX < playerWidth){
                dirX *= -1;
            }
        }if(collisionY(player2Position)){
            if(ballX >= 800 - playerWidth - ballSize){
                dirX *= -1;
            }
        }if(collisionX(player3Position)){
            if(ballY < playerWidth){
                dirY *= -1;
            }
        }if(collisionX(player4Position)){
            if(ballY > 763 - playerWidth - ballSize){
                dirY *= -1;
            }
        }
        if(ballX < 0){
            resetBall();
        }
        if(ballX > 800){
            resetBall();
        }
        if(ballY < 0){
            resetBall();
        }
        if(ballY > 763){
            resetBall();
        }
    }
    private boolean collisionY(int playerPosition){
        if(ballY+ballSize >= playerPosition && ballY <= playerPosition+playerHeight){
            return true;
        }
        return false;
    }
    private boolean collisionX(int playerPosition){
        if(ballX+ballSize >= playerPosition && ballX <= playerPosition+playerHeight){
            return true;
        }
        return false;
    }
    private void resetBall(){
        ballX = 400;
        ballY = 400;
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
        g.fillRect((int)ballX, (int) ballY, ballSize, ballSize);
    }
}


