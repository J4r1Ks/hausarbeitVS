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
        if(json.has("player1Pos")){
            ballX = json.getFloat("ballX");
            ballY = json.getFloat("ballY");
            dirX = json.getFloat("dirX");
            dirY = json.getFloat("dirY");
            ballSpeed = json.getFloat("ballSpeed");
            playerPos[0] = json.getInt("player1Pos");
        }
        if(json.has("player2Pos"))
            playerPos[1] = json.getInt("player2Pos");
        if(json.has("player3Pos"))
            playerPos[2] = json.getInt("player3Pos");
        if(json.has("player4Pos"))
            playerPos[3] = json.getInt("player4Pos");
    }
    public JSONObject getValues(){
        JSONObject json = new JSONObject();
        json.put("type", "setData");
        json.put("ballX", ballX);
        json.put("ballY", ballY);
        json.put("dirX", dirX);
        json.put("dirY", dirY);
        json.put("ballSpeed", ballSpeed);
        json.put("player1Pos", playerPos[0]);
        json.put("player2Pos", playerPos[1]);
        json.put("player3Pos", playerPos[2]);
        json.put("player4Pos", playerPos[3]);
        return json;
    }
}
