package de.fuwa.bomberman.game.appstates.Ki;

import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.MoveDirection;

public class Move {
    private PositionComponent pos;
    private MoveDirection dir;

    public Move(PositionComponent pos, MoveDirection dir) {
        this.pos = pos;
        this.dir = dir;
    }

    public PositionComponent getPos() {
        return pos;
    }

    public MoveDirection getDir() {
        return dir;
    }
}
