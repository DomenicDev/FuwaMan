package de.fuwa.bomberman.es.net.messages;

import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.network.messages.AbstractMessage;


public class EntitySetInitialDataMessage extends AbstractMessage {

    private int setId;
    private Entity[] entities;

    public EntitySetInitialDataMessage(int setId, Entity[] entities) {
        this.setId = setId;
        this.entities = entities;
    }

    public int getSetId() {
        return setId;
    }

    public Entity[] getEntities() {
        return entities;
    }
}
