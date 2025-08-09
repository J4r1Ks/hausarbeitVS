package de.hsos.vs.pong.game;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerController extends KeyAdapter {

    public boolean vertical;
    public boolean upPressed = false;
    public boolean downPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;
    public int playerPositionX;
    public int playerPositionY;
    public final int playerWidth;
    public final int playerHeight;
    public int playerScore = 0;
    public Color playerColor;
    private final int playerSpeed = 8;

    public PlayerController(boolean vertical, Color playerColor, int playerHeight, int playerWidth, int playerPositionX, int playerPositionY) {
        this.vertical = vertical;
        this.playerColor = playerColor;
        this.playerWidth =  playerWidth;
        this.playerHeight = playerHeight;
        this.playerPositionX = playerPositionX;
        this.playerPositionY = playerPositionY;
    }

    public void changePlayerPosition(){
        if(vertical){
            playerPositionY = setPosition(playerPositionY);
        }else{
            playerPositionX = setPosition(playerPositionX);
        }

    }
    private int setPosition(int playerPosition){
        if(vertical ? upPressed : leftPressed){
            if(playerPosition-playerSpeed > 0){
                return playerPosition - playerSpeed;
            }
        }else if(vertical ? downPressed : rightPressed){
            if(playerPosition+playerSpeed < 800-(vertical ? playerHeight : playerWidth)){
                return playerPosition + playerSpeed;
            }
        }
        return playerPosition;
    }

    public boolean collision(float ballX, float ballY, int ballSize){
        return ballX + ballSize >= playerPositionX && ballX <= playerPositionX + playerWidth && ballY + ballSize >= playerPositionY && ballY <= playerPositionY + playerHeight;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if(vertical){
            if ((key == KeyEvent.VK_UP) && !upPressed) {
                upPressed = true;
            }
            if ((key == KeyEvent.VK_DOWN) && !downPressed) {
                downPressed = true;
            }
        }else{
            if ((key == KeyEvent.VK_LEFT)  && !leftPressed) {
                leftPressed = true;
            }
            if ((key == KeyEvent.VK_RIGHT)   && !rightPressed) {
                rightPressed = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if ((key == KeyEvent.VK_UP)) {
            upPressed = false;
        }
        if ((key == KeyEvent.VK_DOWN)) {
            downPressed = false;
        }
        if ((key == KeyEvent.VK_LEFT)) {
            leftPressed = false;
        }
        if ((key == KeyEvent.VK_RIGHT)) {
            rightPressed = false;
        }
    }

}
