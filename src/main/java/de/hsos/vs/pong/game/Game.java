package de.hsos.vs.pong.game;

import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    private final Color mainColor = new Color(20, 60, 100);

    private GameEngine game;

    private PlayerController[] players = {new PlayerController(true),
                                            new PlayerController(true),
                                            new PlayerController(false),
                                            new PlayerController(false)};

    public Game() {
        this.setTitle("Quong");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setMinimumSize(new Dimension(1000, 800));
        this.setBackground(mainColor);

        for(PlayerController player : players){
            this.addKeyListener(player);
        }
        game = new GameEngine(players);

        this.pack();
        this.setVisible(true);
        this.add(game);
    }

    public static void main(String[ ] args){
        Game game = new Game();
        game.game.start();

    }
}
