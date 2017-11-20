package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;

public class BombComponent implements EntityComponent{

    private int radius;
    private float timer;

    public BombComponent(float timer, int radius) {
        this.radius = radius;
        this.timer = timer;
    }

    public int getRadius() {
        return radius;
    }

    public float getTimer() {
        return timer;
    }

}
