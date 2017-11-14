package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.game.enums.MoveDirection;

public class WalkableComponent {

    private MoveDirection moveDirection;
    private float speed;

    public WalkableComponent(MoveDirection moveDirection, float speed) {
        this.moveDirection = moveDirection;
        this.speed = speed;
    }

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public float getSpeed() {
        return speed;
    }
}
