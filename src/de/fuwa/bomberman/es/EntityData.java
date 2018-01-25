package de.fuwa.bomberman.es;

import java.util.List;

/**
 * Entity Data is the main access point when it comes to
 * creating, modifying and removing entities.
 */
public interface EntityData {

    /**
     * This will create a new (empty) entity.
     * @return the newly created entity
     */
    EntityId createEntity();

    /**
     * This method allows you to set components to the specified entity
     * @param entityId the entity the components shall be added to
     * @param components the components to add
     */
    void setComponents(EntityId entityId, EntityComponent... components);

    /**
     * This method allows you to set the specified component to the specified entity.
     * @param entityId the entity the component shall be added to
     * @param component the component to add
     */
    void setComponent(EntityId entityId, EntityComponent component);

    /**
     * This method will remove the specified component type from the specified entity.
     * If the entity does not have that type, nothing happens.
     * @param entityId the entity whose component shall be removed
     * @param component the component to remove
     * @param <T> the type of the component to remove
     */
    <T extends EntityComponent> void removeComponent(EntityId entityId, Class<T> component);

    /**
     * Will return the desired component for the specified entity or null if that component
     * does not exist.
     * @param entityId the entity you want the component from
     * @param componentType the desired component type
     * @param <T> the desired component type
     * @return the specified component for that entity or null if not existent
     */
    <T extends EntityComponent> T getComponent(EntityId entityId, Class<T> componentType);

    /**
     * This method will return an entity set which watches entities with the specified component type.
     * This set will receive updates of new added or changed entities.
     * Use these entity sets long term.
     * @param componentTypes the component types you are interested in
     * @return an entity set with the desired component types
     */
    EntitySet getEntities(Class... componentTypes);

    /**
     * This method just returns entities which fit the desired components.
     * @param componentTypes the components you are interested in
     * @return a list containing all entities which have the specified components added.
     */
    List<Entity> findEntities(Class... componentTypes);

    /**
     * This method will remove an entity.
     * In other words, every single component of that entity is going to be removed from that entity.
     * @param entityId the entity to remove
     */
    void removeEntity(EntityId entityId);

    void cleanup();

}
