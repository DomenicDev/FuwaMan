package de.fuwa.bomberman.game.appstates.multiplayer.messages;

import de.fuwa.bomberman.game.utils.Player;
import de.fuwa.bomberman.network.messages.AbstractMessage;

public class JoinGameMessage extends AbstractMessage {

    private Player player;

    public JoinGameMessage(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
