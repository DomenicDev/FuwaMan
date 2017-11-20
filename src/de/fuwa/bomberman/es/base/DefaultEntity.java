package de.fuwa.bomberman.es.base;

import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityComponent;
import de.fuwa.bomberman.es.EntityId;

import java.io.Serializable;

public class DefaultEntity implements Entity, Serializable {

    private EntityId entityId;
    private EntityComponent[] components;
    private Class[] componentTypes;

    DefaultEntity(EntityId entityId, Class[] componentTypes) {
        this(entityId, componentTypes, new EntityComponent[componentTypes.length]);
    }

    DefaultEntity(EntityId entityId, Class[] componentTypes, EntityComponent[] components) {
        this.entityId = entityId;
        this.componentTypes = componentTypes;
        this.components = components;
    }

    @Override
    public EntityId getId() {
        return entityId;
    }

    @Override
    public <T extends EntityComponent> T get(Class<T> componentType) {
        for (int i = 0; i < componentTypes.length; i++) {
            if (componentTypes[i] == componentType) {
                return (T) componentTypes[i].cast(components[i]);
            }
        }
        return null;
    }

    void changeComponent(Class type, EntityComponent component) {
        for (int i = 0; i < componentTypes.length; i++) {
            if (componentTypes[i] == type) {
                this.components[i] = component;
                return;
            }
        }
    }

    /**
     * This method is package-private and only used
     * by the DefaultEntitySet class.
     *
     * It changes the local component.
     */
    void removeComponent(Class c) {
        if (c == null) {
            return;
        }

        for (int i = 0; i < components.length; i++) {
            if (components[i] != null && components[i].getClass() == c) {
                components[i] = null;
            }
        }
    }

}
