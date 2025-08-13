package de.hsos.vs.pong.game;

import jakarta.websocket.*;
import org.json.JSONObject;

import java.net.URI;

@ClientEndpoint
public class PongClient {

    private Game game;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("SessionID: "+session.getId());
        game = new Game(4, 0);
        game.game.start(game.gameChat, session);
    }

    @OnMessage
    public void onMessage(String message) {
        JSONObject data = new JSONObject(message);
        GameDataPackage gameDataPackage = new GameDataPackage();
        gameDataPackage.setValues(data);
        game.game.setGameData(gameDataPackage);
    }

    public static void main(String[] args) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.connectToServer(new PongClient(), URI.create("ws://192.168.178.47:8080/quong"));
    }
}


