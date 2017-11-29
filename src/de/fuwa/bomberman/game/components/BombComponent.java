package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;
import de.fuwa.bomberman.es.EntityId;

public class BombComponent implements EntityComponent{

    private int radius;
    private float timer;
    private EntityId creator;

    public BombComponent(float timer, int radius, EntityId creator) {
        this.radius = radius;
        this.timer = timer;
        this.creator = creator;
    }

    public int getRadius() {
        return radius;
    }

    public float getTimer() {
        return timer;
    }

    public EntityId getCreator() { return creator; }
}
