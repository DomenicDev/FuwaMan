package de.fuwa.bomberman.game.components;

import de.fuwa.bomberman.es.EntityComponent;

/**
 * A simple Name component which holds the name of an entity.
 */
public class NameComponent implements EntityComponent {

    private final String name;

    /**
     * Creates a new NameComponent with the specified name
     * @param name the name of the entity
     */
    public NameComponent(String name) {
        this.name = name;
    }

    /**
     * @return the name of the entity
     */
    public String getName() {
        return name;
    }
}
