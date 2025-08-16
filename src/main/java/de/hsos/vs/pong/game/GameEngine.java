package de.hsos.vs.pong.game;

import jakarta.websocket.Session;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameEngine extends JPanel {

    public boolean paused = true;
    public int choosePlayer;
    private int numberOfPlayers;

    private PlayerController[] players;

    private final int ballSize = 16;
    public float ballX = 400;
    public float ballY = 400;
    public float dirX = -1;
    public float dirY = -0.5f;
    public float ballSpeed = 8;

    public GameEngine(PlayerController[] players, int numberOfPlayers, int choosePlayer) {
        this.setPreferredSize(new Dimension(800, 800));
        this.players = players;
        this.numberOfPlayers = numberOfPlayers;
        this.choosePlayer = choosePlayer;
    }

    public void start(GameChat gameChat, Session session){
        long timer = System.currentTimeMillis();

        while(winner()){
            if(System.currentTimeMillis()-timer > 66 && !paused){
                timer = System.currentTimeMillis();
                this.update(this.getGraphics());
                gameChat.updateScore();
                try {
                    JSONObject gameData = getGameData();
                    session.getBasicRemote().sendText(gameData.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private JSONObject getGameData() {
        JSONObject gameData = new JSONObject();
        gameData.put("type", "getData");
        if(choosePlayer == 0){
            gameData.put("player1Pos", players[0].playerPositionY);
            gameData.put("ballX", ballX);
            gameData.put("ballY", ballY);
            gameData.put("dirX", dirX);
            gameData.put("dirY", dirY);
            gameData.put("ballSpeed", ballSpeed);
        }
        if(choosePlayer == 1)
            gameData.put("player2Pos", players[1].playerPositionY);
        if(choosePlayer == 2)
            gameData.put("player3Pos", players[2].playerPositionX);
        if(choosePlayer == 3)
            gameData.put("player4Pos", players[3].playerPositionX);
        return gameData;
    }
    public void setGameData(JSONObject json){
        if(json.has("player1Pos") && choosePlayer != 0){
            ballX = json.getFloat("ballX");
            ballY = json.getFloat("ballY");
            dirX = json.getFloat("dirX");
            dirY = json.getFloat("dirY");
            ballSpeed = json.getFloat("ballSpeed");
            players[0].playerPositionY = json.getInt("player1Pos");
        }
        if(json.has("player2Pos") && choosePlayer != 1)
            players[1].playerPositionY = json.getInt("player2Pos");
        if(json.has("player3Pos") && choosePlayer != 2)
            players[2].playerPositionX = json.getInt("player3Pos");
        if(json.has("player4Pos") && choosePlayer != 3)
            players[3].playerPositionX = json.getInt("player4Pos");
    }

    @Override
    public void update(Graphics g) {
        this.repaint();

        players[choosePlayer].changePlayerPosition();

        if(choosePlayer == 0)
            ballMovement();
    }

    private void ballMovement(){
        ballX += dirX * ballSpeed;
        ballY += dirY * ballSpeed;

        for(int i = 0; i < players.length; ++i){
            if(players[i].vertical && players[i].collision(ballX, ballY, ballSize)){
                dirX *= -1;
                ballSpeed += 0.5f;
            }else if (players[i].collision(ballX, ballY, ballSize)){
                dirY *= -1;
                ballSpeed += 0.5f;
            }
        }

        if(ballX + ballSize < 0){
            resetBall(0);
        }
        if(numberOfPlayers < 2 && ballX + ballSize > 800){
            dirX *= -1;
        }else if(ballX + ballSize > 800){
            resetBall(1);
        }
        if(numberOfPlayers < 3 && ballY < 0){
            dirY *= -1;
        }else if(ballY < 0){
            resetBall(2);
        }
        if(numberOfPlayers < 4 && ballY + ballSize > 800){
            dirY *= -1;
        }else if(ballY > 800){
            resetBall(3);
        }
    }

    private void resetBall(int i){
        int skip = i;
        ballX = 400;
        ballY = 400;
        ballSpeed = 8;
        ++i;
        for(; i % numberOfPlayers != skip; ++i){
            players[i % numberOfPlayers].playerScore += 1;
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


