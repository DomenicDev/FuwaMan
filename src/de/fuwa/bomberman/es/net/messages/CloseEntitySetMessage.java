package de.fuwa.bomberman.es.net.messages;

import de.fuwa.bomberman.network.messages.AbstractMessage;

public class CloseEntitySetMessage extends AbstractMessage {

    private int id;

    public CloseEntitySetMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
