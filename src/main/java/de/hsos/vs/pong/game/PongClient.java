package de.hsos.vs.pong.game;

import jakarta.websocket.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class PongClient {

    private Game game;
    private Thread gameThread;
    private static CountDownLatch latch = new CountDownLatch(1);

    @OnOpen
    public void onOpen(Session session) {
        try {
            JSONObject json = new JSONObject();
            json.put("type", "giveValues");
            session.getBasicRemote().sendText(json.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if(message.startsWith("{")) {
            JSONObject json = new JSONObject(message);
            if(json.getString("type").equals("startGame") && gameThread == null) {
                gameThread = new Thread(() -> {
                    game = new Game(json.getInt("numberOfPlayers"), json.getInt("playerID"));
                    game.game.start(game.gameChat, session);
                    latch.countDown();
                });
                gameThread.start();
            } else if(json.getString("type").equals("setData")){
                game.game.setGameData(json);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Gebe IP-Adresse und Port vom Server ein: ");
        container.connectToServer(new PongClient(), URI.create("ws://"+scanner.nextLine()+"/quong"));
        scanner.close();
        latch.await();
    }
}


