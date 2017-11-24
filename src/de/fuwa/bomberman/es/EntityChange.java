package de.fuwa.bomberman.es;

import java.io.Serializable;

/**
 * An EntityChange is created when an entity has been modified.
 * (For example by adding, updating, or removing components)
 */
public class EntityChange implements Serializable {

    private EntityId entityId;
    private EntityComponent component;
    private Class compClass;

    /**
     * Creates a new entity change with the specified parameters.
     * @param entityId the entity id of the affected entity
     * @param component the new component which changed (or null if component has been removed)
     * @param c the component type (this always should have a valid value)
     */
    public EntityChange(EntityId entityId, EntityComponent component, Class c) {
        this.entityId = entityId;
        this.component = component;
        this.compClass = c;
    }

    public EntityComponent getComponent() {
        return component;
    }

    public EntityId getEntityId() {
        return entityId;
    }

    public Class getComponentClass() {
        return compClass;
    }

}
