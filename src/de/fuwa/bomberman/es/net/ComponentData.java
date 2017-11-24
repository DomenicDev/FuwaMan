package de.fuwa.bomberman.es.net;

import de.fuwa.bomberman.es.EntityComponent;

/**
 * Holds the components of one entity.
 */
public class ComponentData {

    private EntityComponent[] components;

    public ComponentData(EntityComponent[] components) {
        this.components = components;
    }

    public EntityComponent[] getComponents() {
        return components;
    }
}
