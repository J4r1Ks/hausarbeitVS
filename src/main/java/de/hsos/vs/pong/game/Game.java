package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    public GameEngine game;

    public GameChat gameChat;

    private PlayerController[] players;

    public Game(int numberOfPlayers, int choosePlayer) {
        //Initialisierung der Spieler
        players = new PlayerController[numberOfPlayers];
        switch(numberOfPlayers){
            case 4: players[3] = new PlayerController(false, new Color(100, 20, 100), 12, 120, 300, 800-12);
            case 3: players[2] = new PlayerController(false, new Color(100, 100, 20), 12, 120, 300, 0);
            case 2: players[1] = new PlayerController(true, new Color(20, 100, 20), 120, 12, 800-12, 300);
            case 1: players[0] = new PlayerController(true, new Color(100, 20, 20), 120, 12, 0, 300);
        }

        //Initialisierung des Fensters
        this.setTitle("Quong");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setMinimumSize(new Dimension(1000, 863));

        //Hinzufügen von Keylisteners an den Spielern für Inputabfragen
        for(PlayerController player : players){
            this.addKeyListener(player);
        }

        //Initialisierung der GameEngine und des GameChats
        game = new GameEngine(players, numberOfPlayers, choosePlayer);
        gameChat = new GameChat(players);

        //Hinzufügen eines Pause Buttons, der GameEngine und des GameChats im Hauptfenster
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

        //Finale Initialisierung
        this.pack();
        this.setVisible(true);
    }
}
