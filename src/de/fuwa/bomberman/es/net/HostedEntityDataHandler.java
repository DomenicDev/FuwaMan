package de.fuwa.bomberman.es.net;

import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.es.net.messages.CloseEntitySetMessage;
import de.fuwa.bomberman.es.net.messages.GetEntitiesMessage;
import de.fuwa.bomberman.network.ConnectionListener;
import de.fuwa.bomberman.network.HostedConnection;
import de.fuwa.bomberman.network.MessageListener;
import de.fuwa.bomberman.network.messages.AbstractMessage;

import java.util.HashMap;
import java.util.Map;

public class HostedEntityDataHandler implements MessageListener<HostedConnection>, ConnectionListener {

    private DefaultEntityData entityData;
    private Map<HostedConnection, HostedEntityData> hostedEntityDataMap = new HashMap<>();

    public HostedEntityDataHandler(DefaultEntityData entityData) {
        this.entityData = entityData;
    }

    @Override
    public void onMessageReceived(HostedConnection source, AbstractMessage m) {
        if (!hostedEntityDataMap.containsKey(source)) {
            return;
        }
        if (m instanceof GetEntitiesMessage) {
            GetEntitiesMessage gm = (GetEntitiesMessage) m;
            hostedEntityDataMap.get(source).getEntities(gm);
        } else if (m instanceof CloseEntitySetMessage) {
            CloseEntitySetMessage cm = (CloseEntitySetMessage) m;
            hostedEntityDataMap.get(source).closeEntitySet(cm);
        }
    }

    public void sendUpdates() {
        for (HostedEntityData hostedEntityData : hostedEntityDataMap.values()) {
            hostedEntityData.sendUpdates();
        }
    }

    @Override
    public void onClientConnected(HostedConnection connection) {
        this.hostedEntityDataMap.put(connection, new HostedEntityData(connection, entityData));
    }

    @Override
    public void onClientDisconnected(HostedConnection connection) {
        this.hostedEntityDataMap.remove(connection);
    }
}
