package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;

public class ScoreComponent implements EntityComponent{
    private int score;

    public ScoreComponent(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
