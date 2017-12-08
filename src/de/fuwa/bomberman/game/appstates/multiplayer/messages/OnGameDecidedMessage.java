package de.fuwa.bomberman.game.appstates.multiplayer.messages;

import de.fuwa.bomberman.network.messages.AbstractMessage;

public class OnGameDecidedMessage extends AbstractMessage {

    private String winnerName;

    public OnGameDecidedMessage(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerName() {
        return winnerName;
    }
}
