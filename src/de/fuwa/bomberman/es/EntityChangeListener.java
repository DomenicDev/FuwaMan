package de.fuwa.bomberman.es;

/**
 * Informs about entity changes.
 */
public interface EntityChangeListener {

    /**
     * Is called when there was a change of an entity.
     * @param change the detailed entity change object
     */
    void onEntityChange(EntityChange change);

}
