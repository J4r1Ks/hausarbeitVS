package de.hsos.vs.pong.game;

import org.json.JSONObject;

public class GameDataPackage {
    public float ballX;
    public float ballY;
    public float dirX;
    public float dirY;
    public float ballSpeed;

    public int[] playerPos =  new int[4];

    public void setValues(JSONObject json){
        ballX = json.getFloat("ballX");
        ballY = json.getFloat("ballY");
        dirX = json.getFloat("dirX");
        dirY = json.getFloat("dirY");
        ballSpeed = json.getFloat("ballSpeed");
        playerPos[0] = json.getInt("player1Pos");
        playerPos[1] = json.getInt("player2Pos");
        playerPos[2] = json.getInt("player3Pos");
        playerPos[3] = json.getInt("player4Pos");
    }
}
