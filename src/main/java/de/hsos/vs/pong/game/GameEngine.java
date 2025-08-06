package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLOutput;
import java.util.Arrays;

public class GameEngine extends JPanel {

    public boolean paused = true;
    private final byte choosePlayer = 0;

    private final Color player1Color = new Color(100, 20, 20);
    private final Color player2Color = new Color(20, 100, 20);
    private final Color player3Color = new Color(100, 100, 20);
    private final Color player4Color = new Color(100, 20, 100);

    private final int playerWidth = 12;
    private final int playerHeight = 120;

    private final int playerSpeed = 8;
    private final PlayerController player1;
    private final PlayerController player2;
    private final PlayerController player3;
    private final PlayerController player4;
    private int player1Position = 300;
    private int player2Position = 300;
    private int player3Position = 300;
    private int player4Position = 300;
    public int player1Score = 0;
    public int player2Score = 0;
    public int player3Score = 0;
    public int player4Score = 0;

    private final int ballSize = 16;
    private float ballX = 400;
    private float ballY = 400;
    private float dirX = -1;
    private float dirY = -0.5f;
    private float ballSpeed = 3;

    public GameEngine(PlayerController[] players) {
        this.setPreferredSize(new Dimension(800, 800));
        player1 = players[0];
        player2 = players[1];
        player3 = players[2];
        player4 = players[3];
    }

    public void start(GameChat gameChat){
        long timer = System.currentTimeMillis();

        while(winner()){
            System.out.flush();
            if(System.currentTimeMillis()-timer > 12 && !paused){
                timer = System.currentTimeMillis();
                this.update(this.getGraphics());
                gameChat.updateScore(player1Score, player2Score, player3Score, player4Score);
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
            if(playerPosition-playerSpeed >= 0){
                return playerPosition -= playerSpeed;
            }
        }else if(player.downPressed){
            if(playerPosition+playerSpeed < 800-playerHeight){
                return playerPosition += playerSpeed;
            }
        }
        return playerPosition;
    }

    private int playerMovementHorizontal(PlayerController player, int playerPosition) {
        if(player.leftPressed){
            if(playerPosition-playerSpeed >= 0){
                return playerPosition -= playerSpeed;
            }
        }else if(player.rightPressed){
            if(playerPosition+playerSpeed <= 800-playerHeight){
                return playerPosition += playerSpeed;
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
                ballSpeed += 0.2f;
            }
        }if(collisionY(player2Position)){
            if(ballX >= 800 - playerWidth - ballSize){
                dirX *= -1;
                ballSpeed += 0.2f;
            }
        }if(collisionX(player3Position)){
            if(ballY < playerWidth){
                dirY *= -1;
                ballSpeed += 0.2f;
            }
        }if(collisionX(player4Position)){
            if(ballY > 800 - playerWidth - ballSize){
                dirY *= -1;
                ballSpeed += 0.2f;
            }
        }
        if(ballX + ballSize < 0){
            resetBall();
            player2Score += 1;
            player3Score += 1;
            player4Score += 1;
        }
        if(ballX + ballSize > 800){
            resetBall();
            player1Score += 1;
            player3Score += 1;
            player4Score += 1;
        }
        if(ballY < 0){
            resetBall();
            player1Score += 1;
            player2Score += 1;
            player4Score += 1;
        }
        if(ballY > 800){
            resetBall();
            player1Score += 1;
            player2Score += 1;
            player3Score += 1;
        }
    }
    private boolean collisionY(int playerPosition){
        return ballY + ballSize >= playerPosition && ballY <= playerPosition + playerHeight;
    }
    private boolean collisionX(int playerPosition){
        return ballX + ballSize >= playerPosition && ballX <= playerPosition + playerHeight;
    }
    private void resetBall(){
        ballX = 400;
        ballY = 400;
        ballSpeed = 3;
    }

    private boolean winner(){
        if(player1Score > 9){
            System.out.println("Player 1 won!");
            return false;
        }else if(player2Score > 9){
            System.out.println("Player 2 won!");
            return false;
        }else if(player3Score > 9){
            System.out.println("Player 3 won!");
            return false;
        }else if(player4Score > 9){
            System.out.println("Player 4 won!");
            return false;
        }
        return true;
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
        g.fillRect(player4Position, 800-playerWidth, playerHeight, playerWidth);

        g.setColor(Color.white);
        g.fillRect((int)ballX, (int) ballY, ballSize, ballSize);
    }
}


