package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;

public class PositionComponent implements EntityComponent {

    private float x, y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
