package de.fuwa.bomberman.es.net.messages;

import de.fuwa.bomberman.network.messages.AbstractMessage;


public class GetEntitiesMessage extends AbstractMessage {

    private Class[] types;
    private int id;

    public GetEntitiesMessage(int id, Class[] types) {
        this.types = types;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Class[] getTypes() {
        return types;
    }
}
