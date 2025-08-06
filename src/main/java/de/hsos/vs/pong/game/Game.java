package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    private GameEngine game;

    private GameChat gameChat;

    private PlayerController[] players = {new PlayerController(true),
                                            new PlayerController(true),
                                            new PlayerController(false),
                                            new PlayerController(false)};

    public Game() {
        this.setTitle("Quong");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setMinimumSize(new Dimension(1000, 863));

        for(PlayerController player : players){
            this.addKeyListener(player);
        }
        game = new GameEngine(players);
        gameChat = new GameChat();

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

    public static void main(String[ ] args){
        Game game = new Game();
        game.game.start(game.gameChat);

    }
}
