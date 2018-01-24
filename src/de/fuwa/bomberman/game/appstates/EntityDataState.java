package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.base.DefaultEntityData;

public class EntityDataState extends BaseAppState {

    private EntityData entityData;

    public EntityDataState() {
        this.entityData = new DefaultEntityData();
    }

    public EntityDataState(EntityData entityData) {
        this.entityData = entityData;
    }

    public EntityData getEntityData() {
        return entityData;
    }

    @Override
    public void cleanup() {
        if (entityData != null) {
            entityData.cleanup();
            this.entityData = null;
        }
    }
}
