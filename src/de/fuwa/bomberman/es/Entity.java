package de.fuwa.bomberman.es;

/**
 * A local view of an entity.
 * An Entity may not necessarily have every component really added to that entity
 * but only the ones the EntitySet is interested in.
 */
public interface Entity {

    /**
     * @return the EntityId of this entity object
     */
    EntityId getId();

    /**
     * Will return the component of the specified type
     * @param componentType the component Type
     * @param <T> the type of the component
     * @return the desired component of the specified type or null if that type does not exist.
     */
    <T extends EntityComponent> T get(Class<T> componentType);

}
