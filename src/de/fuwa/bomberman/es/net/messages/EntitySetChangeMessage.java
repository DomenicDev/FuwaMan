package de.fuwa.bomberman.es.net.messages;

import de.fuwa.bomberman.es.EntityChange;
import de.fuwa.bomberman.network.messages.AbstractMessage;

/**
 * Used to transmit changes for a specific entity set from the server to the client.
 */
public class EntitySetChangeMessage extends AbstractMessage {

    private int id;
    private EntityChange[] changes;

    public EntitySetChangeMessage(int id, EntityChange[] changes) {
        this.id = id;
        this.changes = changes;
    }

    public int getId() {
        return id;
    }

    public EntityChange[] getChanges() {
        return changes;
    }
}
