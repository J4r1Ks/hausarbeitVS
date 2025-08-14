package de.hsos.vs.pong.game;

import jakarta.websocket.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class PongClient {

    private Game game;
    private Thread gameThread;
    private static boolean running = true;

    @OnOpen
    public void onOpen(Session session) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "giveValues");
            json.put("sessionID", session.getId());
            session.getBasicRemote().sendText(json.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if(message.startsWith("{")) {
            JSONObject json = new JSONObject(message);
            if(json.getString("type").equals("startGame") && json.getInt("sessionID") == Integer.parseInt(session.getId())) {
                gameThread = new Thread(() -> {
                    game = new Game(json.getInt("numberOfPlayers"), json.getInt("playerID"));
                    game.game.start(game.gameChat, session);
                });
                gameThread.start();
                running = false;
            } else if(json.getString("type").equals("getData")){
                GameDataPackage gameDataPackage = new GameDataPackage();
                gameDataPackage.setValues(json, Integer.parseInt(session.getId()));
                game.game.setGameData(gameDataPackage);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(new PongClient(), URI.create("ws://192.168.178.47:8080/quong"));
        while (running) {}
    }
}


