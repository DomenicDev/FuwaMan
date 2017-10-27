package de.fuwa.bomberman.es;

import java.util.Set;

/**
 * An EntitySet watches entities with certain components.
 * The EntitySet will be kept up-to-date.
 * It will receive added, updated and removed entities for this type of set.
 */
public interface EntitySet extends Set<Entity> {

    /**
     * Will return the local entity view for the specified entity id
     * @param entityId the entity id of the desired entity
     * @return the entity object with the specified id or null if entity does not exist-
     */
    Entity getEntity(EntityId entityId);

    /**
     * Will return true if the specified entity id is part of this set.
     * @param entityId the entity id to check
     * @return true if this set contains the specified entity id
     */
    boolean containsId(EntityId entityId);

    /**
     * @return the component types this set is interested in
     */
    Class[] getComponentTypes();

    /**
     * This method will apply the changes since the last call.
     * It will refresh the added, changed and removed entity set list
     * @return true if there were any chances, otherwise false
     */
    boolean applyChanges();

    /**
     * @return the added entities since the last <code>applyChanges()</code> call
     */
    Set<Entity> getAddedEntities();

    /**
     * @return the changed entities since the last <code>applyChanges()</code> call
     */
    Set<Entity> getChangedEntities();

    /**
     * @return the removed entities since the last <code>applyChanges()</code> call
     */
    Set<Entity> getRemovedEntities();

    /**
     * This method release this set from EntityData.
     * It then will no longer receive updates.
     * Use when set is not needed anymore.
     */
    void close();

}
