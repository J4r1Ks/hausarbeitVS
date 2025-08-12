package de.hsos.vs.pong.game;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/quong/{userID}")
public class PongWebSocket {

    private static final List<Session> sessions = new ArrayList<>();
    private static final Map<Integer, Game> games = new ConcurrentHashMap<>();

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
            //sendAllEntriesTo(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("userID") int userID) {
        try {
            //Spielinitialisierung
            if(games.get(userID) == null){
                //Ganz ganz wichtig, damit das Pong Fenster sich oeffnet
                System.setProperty("java.awt.headless", "false");
                if (!GraphicsEnvironment.isHeadless()) {
                    new Thread(() -> {
                        games.put(userID, new Game(Integer.parseInt(message)));
                        games.get(userID).game.choosePlayer = userID;
                        games.get(userID).game.start(games.get(userID).gameChat, session);
                    }).start();
                }
            }else{
                games.get(userID).game.setJsonObject(new JSONObject(message));
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
