package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.game.components.PositionComponent;

public class KiAction {

    private Path path;
    private boolean placeBomb;
    private float waitTime;

    public KiAction(Path path, boolean placeBomb, float waitTime) {
        this.placeBomb = placeBomb;
        this.waitTime = waitTime;
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public boolean isPlaceBomb() {
        return placeBomb;
    }

    public float getWaitTime() {
        return waitTime;
    }
}
