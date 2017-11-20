package de.fuwa.bomberman.game.appstates.multiplayer.messages;

import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.network.messages.AbstractMessage;

public class ApplyMoveDirectionMessage extends AbstractMessage {

    private MoveDirection moveDirection;

    public ApplyMoveDirectionMessage(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }
}
