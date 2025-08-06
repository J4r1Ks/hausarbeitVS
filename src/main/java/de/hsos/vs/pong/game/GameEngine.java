package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLOutput;
import java.util.Arrays;

public class GameEngine extends JPanel {

    public boolean paused = true;
    private final byte choosePlayer = 0;

    private final int playerWidth = 12;
    private final int playerHeight = 120;

    private final int playerSpeed = 8;
    private PlayerController[] players;

    private final int ballSize = 16;
    private float ballX = 400;
    private float ballY = 400;
    private float dirX = -1;
    private float dirY = -0.5f;
    private float ballSpeed = 3;

    public GameEngine(PlayerController[] players) {
        this.setPreferredSize(new Dimension(800, 800));
        this.players = players;
    }

    public void start(GameChat gameChat){
        long timer = System.currentTimeMillis();

        while(winner()){
            System.out.flush();
            if(System.currentTimeMillis()-timer > 12 && !paused){
                timer = System.currentTimeMillis();
                this.update(this.getGraphics());
                gameChat.updateScore();
            }
        }
    }

    @Override
    public void update(Graphics g) {
        this.repaint();

        for(int i = 0; i < players.length; i++){
            players[i].playerPosition = players[i].vertical ? playerMovementVertical(players[i]) : playerMovementHorizontal(players[i]);
        }

        ballMovement();
    }

    private int playerMovementVertical(PlayerController player) {
        if(player.upPressed){
            if(player.playerPosition-playerSpeed >= 0){
                return player.playerPosition -= playerSpeed;
            }
        }else if(player.downPressed){
            if(player.playerPosition+playerSpeed < 800-playerHeight){
                return player.playerPosition += playerSpeed;
            }
        }
        return player.playerPosition;
    }

    private int playerMovementHorizontal(PlayerController player) {
        if(player.leftPressed){
            if(player.playerPosition-playerSpeed >= 0){
                return player.playerPosition -= playerSpeed;
            }
        }else if(player.rightPressed){
            if(player.playerPosition+playerSpeed <= 800-playerHeight){
                return player.playerPosition += playerSpeed;
            }
        }
        return player.playerPosition;
    }

    private void ballMovement(){
        ballX += dirX * ballSpeed;
        ballY += dirY * ballSpeed;
        if(collisionY(players[0].playerPosition)){
            if(ballX < playerWidth){
                dirX *= -1;
                ballSpeed += 0.2f;
            }
        }if(collisionY(players[1].playerPosition)){
            if(ballX >= 800 - playerWidth - ballSize){
                dirX *= -1;
                ballSpeed += 0.2f;
            }
        }if(collisionX(players[2].playerPosition)){
            if(ballY < playerWidth){
                dirY *= -1;
                ballSpeed += 0.2f;
            }
        }if(collisionX(players[3].playerPosition)){
            if(ballY > 800 - playerWidth - ballSize){
                dirY *= -1;
                ballSpeed += 0.2f;
            }
        }
        if(ballX + ballSize < 0){
            resetBall(0);
        }
        if(ballX + ballSize > 800){
            resetBall(1);
        }
        if(ballY < 0){
            resetBall(2);
        }
        if(ballY > 800){
            resetBall(3);
        }
    }
    private boolean collisionY(int playerPosition){
        return ballY + ballSize >= playerPosition && ballY <= playerPosition + playerHeight;
    }
    private boolean collisionX(int playerPosition){
        return ballX + ballSize >= playerPosition && ballX <= playerPosition + playerHeight;
    }
    private void resetBall(int i){
        int skip = i;
        ballX = 400;
        ballY = 400;
        ballSpeed = 3;
        ++i;
        for(; i % 4 != skip; ++i){
            players[i % 4].playerScore += 1;
        }
    }

    private boolean winner(){
        for(int i = 0; i < players.length; i++){
            if(players[i].playerScore > 9){
                System.out.println("Player "+i+" won!");
                return false;
            }
        }
        return true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, 800, 800);

        g.setColor(players[0].playerColor);
        g.fillRect(0, players[0].playerPosition, playerWidth, playerHeight);

        g.setColor(players[1].playerColor);
        g.fillRect(800-playerWidth, players[1].playerPosition, playerWidth, playerHeight);

        g.setColor(players[2].playerColor);
        g.fillRect(players[2].playerPosition, 0, playerHeight, playerWidth);

        g.setColor(players[3].playerColor);
        g.fillRect(players[3].playerPosition, 800-playerWidth, playerHeight, playerWidth);

        g.setColor(Color.white);
        g.fillRect((int)ballX, (int) ballY, ballSize, ballSize);
    }
}


