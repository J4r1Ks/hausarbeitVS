package de.hsos.vs.pong.game;


import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint(value = "/quong/{userID}")
public class Game extends JFrame {

    private static final List<Session> sessions = new ArrayList<>();


    public int numberOfPlayers = 4;

    private GameEngine game;

    private GameChat gameChat;

    private PlayerController[] players;

    public Game() {
        players = new PlayerController[numberOfPlayers];
        switch(numberOfPlayers){
            case 4: players[3] = new PlayerController(false, new Color(100, 20, 100), 12, 120, 300, 800-12);
            case 3: players[2] = new PlayerController(false, new Color(100, 100, 20), 12, 120, 300, 0);
            case 2: players[1] = new PlayerController(true, new Color(20, 100, 20), 120, 12, 800-12, 300);
            case 1: players[0] = new PlayerController(true, new Color(100, 20, 20), 120, 12, 0, 300);
        }

        this.setTitle("Quong");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setMinimumSize(new Dimension(1000, 863));

        for(PlayerController player : players){
            this.addKeyListener(player);
        }
        game = new GameEngine(players, numberOfPlayers);
        gameChat = new GameChat(players);

        JButton playButton = new JButton("Unpause");
        playButton.addActionListener(e -> {
            this.requestFocus();
            game.paused = !game.paused;
            if(game.paused){
                playButton.setText("Unpause");
            } else {
                playButton.setText("Pause");
            }
        });
        this.getContentPane().add(playButton, BorderLayout.NORTH);
        this.getContentPane().add(game, BorderLayout.WEST);
        this.getContentPane().add(gameChat, BorderLayout.EAST);

        this.pack();
        this.setVisible(true);
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userID") int userID) {
        game.choosePlayer = userID;
        game.start(gameChat);
    }

    public static void main(String[ ] args){
        //Game game = new Game();
        //game.game.start(game.gameChat);
    }
}
