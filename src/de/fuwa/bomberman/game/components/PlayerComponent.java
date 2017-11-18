package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;

public class PlayerComponent implements EntityComponent {

    private int bombStrength;
    private int bombAmount;

    public PlayerComponent( int bombStrength, int bombAmount) {
        this.bombStrength = bombStrength;
        this.bombAmount = bombAmount;
    }

    public int getBombAmount() { return bombAmount; }

    public int getBombStrength() {
        return bombStrength;
    }
}
