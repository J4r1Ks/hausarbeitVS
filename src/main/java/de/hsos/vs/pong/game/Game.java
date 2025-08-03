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
        this.setMinimumSize(new Dimension(1000, 800));

        for(PlayerController player : players){
            this.addKeyListener(player);
        }
        game = new GameEngine(players);
        gameChat = new GameChat();

        this.pack();
        this.setVisible(true);

        this.getContentPane().add(game, BorderLayout.WEST);
        this.getContentPane().add(gameChat, BorderLayout.EAST);
    }

    public static void main(String[ ] args){
        Game game = new Game();
        game.game.start();

    }
}
