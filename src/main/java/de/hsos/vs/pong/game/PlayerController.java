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
    public int playerPosition = 300;
    public int playerScore = 0;
    public Color playerColor;
    private final int playerSpeed = 8;

    public PlayerController(boolean vertical, Color playerColor) {
        this.vertical = vertical;
        this.playerColor = playerColor;
    }

    public void changePlayerPosition(int playerHeight){
        if(upPressed || leftPressed){
            if(playerPosition-playerSpeed > 0){
                playerPosition -= playerSpeed;
            }
        }else if(downPressed || rightPressed){
            if(playerPosition+playerSpeed < 800-playerHeight){
                playerPosition += playerSpeed;
            }
        }
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
