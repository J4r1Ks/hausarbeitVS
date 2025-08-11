package de.hsos.vs.pong.game;

import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/quong/{userID}")
public class PongWebSocket {

    private static final List<Session> sessions = new ArrayList<>();
    private Game game;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
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
        }


    }
}
