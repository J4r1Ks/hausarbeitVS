package de.hsos.vs.pong.game;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ServerEndpoint(value = "/quong")
public class PongServer {

    private static final List<Session> sessions = new ArrayList<>();
    private static final GameDataPackage gameDataPackage = new GameDataPackage();
    private static int numberOfPlayers;

    @OnOpen
    public void onOpen(Session session) {
        try {
            String ip = session.getRequestURI().toString();
            if (session.getUserProperties().get("javax.websocket.endpoint.remoteAddress") != null) {
                ip = session.getUserProperties().get("javax.websocket.endpoint.remoteAddress").toString();
            } else if (session.getRequestParameterMap().containsKey("ip")) {
                ip = session.getRequestParameterMap().get("ip").get(0);
            } else {
                ip = session.getRequestURI().getHost();
            }
            session.getUserProperties().put("ip", ip);
            System.out.println("IP: " + ip);
            synchronized (sessions) {
                sessions.add(session);
            }
            System.out.println("new WebSocket-Session: " + session.getId());
            session.getBasicRemote().sendText(numberOfPlayers+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            if(message.startsWith("{")){
                JSONObject data = new JSONObject(message);
                if(data.getString("type").equals("getData")){
                    if(data.has("player1Pos")){
                        gameDataPackage.ballX = data.getFloat("ballX");
                        gameDataPackage.ballY = data.getFloat("ballY");
                        gameDataPackage.dirX = data.getFloat("dirX");
                        gameDataPackage.dirY = data.getFloat("dirY");
                        gameDataPackage.ballSpeed = data.getFloat("ballSpeed");
                        gameDataPackage.playerPos[0] = data.getInt("player1Pos");
                    }
                    if(data.has("player2Pos"))
                        gameDataPackage.playerPos[1] = data.getInt("player2Pos");
                    if(data.has("player3Pos"))
                        gameDataPackage.playerPos[2] = data.getInt("player3Pos");
                    if(data.has("player4Pos"))
                        gameDataPackage.playerPos[3] = data.getInt("player4Pos");
                    for(Session s : sessions){
                        s.getBasicRemote().sendText(data.toString());
                    }
                    return;
                }
            }
            for(Session s : sessions){
                s.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @OnClose
    public void onClose(Session session) {
        synchronized (sessions) {
            sessions.remove(session);
        }
        System.out.println("WebSocket-Session closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
        sendError(session, "Internal Error: " + error.getMessage());

    }
    private void sendError(Session session, String message) {
        JSONObject error = new JSONObject();
        error.put("type", "error");
        error.put("message", message);
        session.getAsyncRemote().sendText(error.toString());
    }

}
