package de.fuwa.bomberman.game.appstates.multiplayer.messages;

import de.fuwa.bomberman.network.messages.AbstractMessage;

public class OnGameStartMessage extends AbstractMessage {

    private float matchDuration;

    public OnGameStartMessage(float matchDuration) {
        this.matchDuration = matchDuration;
    }

    public float getMatchDuration() {
        return matchDuration;
    }
}
