package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;
import de.fuwa.bomberman.es.EntityId;

public class ExplosionComponent implements EntityComponent {

    private float timer;
    private EntityId creator;

    public ExplosionComponent(float timer, EntityId creator) {
        this.timer = timer;
        this.creator = creator;
    }

    public EntityId getCreator() {
        return creator;
    }

    public float getTimer() {
        return timer;
    }
}
