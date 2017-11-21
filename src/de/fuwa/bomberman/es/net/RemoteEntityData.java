package de.fuwa.bomberman.es.net;

import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityChange;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.es.base.DefaultEntitySet;
import de.fuwa.bomberman.es.net.messages.EntitySetChangeMessage;
import de.fuwa.bomberman.es.net.messages.EntitySetInitialDataMessage;
import de.fuwa.bomberman.es.net.messages.GetEntitiesMessage;
import de.fuwa.bomberman.network.Client;
import de.fuwa.bomberman.network.MessageListener;
import de.fuwa.bomberman.network.messages.AbstractMessage;

import java.util.*;

public class RemoteEntityData extends DefaultEntityData {

    private Map<Integer, RemoteEntitySet> entitySets = new HashMap<>();
    private Client client;
    private int entitySetCounter = 0;

    public RemoteEntityData(Client client) {
        this.client = client;
        this.client.addMessageListener(new EntityMessageListener());
    }

    @Override
    public EntitySet getEntities(Class... componentTypes) {
        client.send(new GetEntitiesMessage(entitySetCounter, componentTypes));
        RemoteEntitySet set = new RemoteEntitySet(this, componentTypes);
        entitySets.put(entitySetCounter, set);
        entitySetCounter++;
        return set;
    }

    private class EntityMessageListener implements MessageListener<Client> {

        @Override
        public void onMessageReceived(Client source, AbstractMessage m) {
            if (m instanceof EntitySetInitialDataMessage) {
                EntitySetInitialDataMessage im = (EntitySetInitialDataMessage) m;
                RemoteEntitySet set = entitySets.get(im.getSetId());
                set.loadEntities(new ArrayList<>(Arrays.asList(im.getEntities())));
            } else if (m instanceof EntitySetChangeMessage) {
                EntitySetChangeMessage cm = (EntitySetChangeMessage) m;
                RemoteEntitySet set = entitySets.get(cm.getId());
                for (EntityChange change : cm.getChanges()) {
                    set.onRelevantEntityChange(change);
                }
            }
        }
    }

    private class RemoteEntitySet extends DefaultEntitySet {

        RemoteEntitySet(EntityData entityData, Class[] types) {
            super(entityData, types);
        }

        @Override
        protected void loadEntities(List<Entity> entities) {
            super.loadEntities(entities);
        }

        @Override
        protected void onRelevantEntityChange(EntityChange change) {
            super.onRelevantEntityChange(change);
        }
    }
}
