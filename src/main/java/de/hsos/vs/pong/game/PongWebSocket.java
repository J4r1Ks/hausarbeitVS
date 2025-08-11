package de.hsos.vs.pong.game;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/quong/{userID}")
public class PongWebSocket {

    private static final List<Session> sessions = new ArrayList<>();
    private Game game;

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
        //Spielinitialisierung
        if(game == null){
            //Ganz ganz wichtig, damit das Pong Fenster sich oeffnet
            System.setProperty("java.awt.headless", "false");
            if (!GraphicsEnvironment.isHeadless()) {
                game = new Game(Integer.parseInt(message));
                game.game.choosePlayer = userID;
                game.game.start(game.gameChat);
            }
        }else{



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
