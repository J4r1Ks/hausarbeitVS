package de.hsos.vs.pong.game;

import jakarta.websocket.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class PongClient {

    private Game game;

    @OnOpen
    public void onOpen(Session session) {
        try {
            session.getBasicRemote().sendText("giveValues");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> {
            game = new Game(4, 0);
            game.game.start(game.gameChat, session);
        }).start();
    }

    @OnMessage
    public void onMessage(String message) {
        /*System.out.println("Message received: "+message);
        JSONObject data = new JSONObject(message);
        GameDataPackage gameDataPackage = new GameDataPackage();
        gameDataPackage.setValues(data);
        game.game.setGameData(gameDataPackage);*/
    }

    public static void main(String[] args) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(new PongClient(), URI.create("ws://192.168.178.47:8080/quong"));
    }
}


