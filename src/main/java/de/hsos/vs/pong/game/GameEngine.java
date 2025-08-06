package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLOutput;
import java.util.Arrays;

public class GameEngine extends JPanel {

    public boolean paused = true;
    private final byte choosePlayer = 0;

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
            players[i].changePlayerPosition();
        }

        ballMovement();
    }

    private void ballMovement(){
        ballX += dirX * ballSpeed;
        ballY += dirY * ballSpeed;

        for(int i = 0; i < players.length; ++i){
            if(players[i].vertical && players[i].collision(ballX, ballY, ballSize)){
                dirX *= -1;
                ballSpeed += 0.2f;
            }else if (players[i].collision(ballX, ballY, ballSize)){
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

        for(int i = 0; i < players.length; i++){
            g.setColor(players[i].playerColor);
            g.fillRect(players[i].playerPositionX, players[i].playerPositionY, players[i].playerWidth, players[i].playerHeight);
        }

        g.setColor(Color.white);
        g.fillRect((int)ballX, (int) ballY, ballSize, ballSize);
    }
}


