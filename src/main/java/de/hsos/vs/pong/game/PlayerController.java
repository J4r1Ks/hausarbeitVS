package de.hsos.vs.pong.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PlayerController extends KeyAdapter {

    private boolean vertical;
    public boolean upPressed = false;
    public boolean downPressed = false;
    public boolean leftPressed = false;
    public boolean rightPressed = false;

    public PlayerController(boolean vertical) {
        this.vertical = vertical;
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
