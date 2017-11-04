package de.fuwa.bomberman.es.net;

import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityChange;
import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.es.base.DefaultEntitySet;
import de.fuwa.bomberman.es.net.messages.EntitySetChangeMessage;
import de.fuwa.bomberman.es.net.messages.EntitySetInitialDataMessage;
import de.fuwa.bomberman.es.net.messages.GetEntitiesMessage;
import de.fuwa.bomberman.network.HostedConnection;

import java.util.HashMap;
import java.util.Map;

public class HostedEntityData {

    private HostedConnection connection;

    private Map<Integer, DefaultEntitySet> activeEntitySets = new HashMap<>();

    private DefaultEntityData entityData;

    public HostedEntityData(HostedConnection connection, DefaultEntityData entityData) {
        this.connection = connection;
        this.entityData = entityData;
    }

    public void getEntities(GetEntitiesMessage message) {
        int setId = message.getId();
        Class[] types = message.getTypes();

        DefaultEntitySet entitySet = (DefaultEntitySet) entityData.getEntities(types);
        Entity[] entities = entitySet.toArray(new Entity[entitySet.size()]);

        // send back initial set of data
        EntitySetInitialDataMessage m = new EntitySetInitialDataMessage(setId, entities);
        // send it
        connection.send(m);

        // put set into activeEntitySets
        activeEntitySets.put(setId, entitySet);
    }

    public void sendUpdates() {
        // send updates for EntitySets
        for (Map.Entry<Integer, DefaultEntitySet> e : activeEntitySets.entrySet()) {
            int id = e.getKey();
            DefaultEntitySet set = e.getValue();
            EntityChange[] changes = set.getChanges().toArray(new EntityChange[set.getChanges().size()]);

            // apply changes to clear the change buffer within the EntitySet
            set.applyChanges();

            EntitySetChangeMessage m = new EntitySetChangeMessage(id, changes);
            connection.send(m);
        }

    }

}
