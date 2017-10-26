package de.fuwa.bomberman.es;

/**
 * An EntityId mostly acts as a key and identifies an entity.
 * At the beginning an entity is nothing more than an id.
 */
public class EntityId {

    private int id; // an int should be enough for us

    /**
     * Creates a new EntityId object with the specified id
     * @param id the if of the entity
     */
    public EntityId(int id) {
        this.id = id;
    }

    /**
     * @return the id of that entity
     */
    public int getId() {
        return id;
    }

    /**
     * Equals will return true if the specified object is an EntityId and
     * has the same ID as this object.
     * @param obj the object to check for equality
     * @return true if the two object have the same id
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof EntityId && ((EntityId) obj).getId() == this.id;
    }

}
