package de.fuwa.bomberman.es.base;

import de.fuwa.bomberman.es.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultEntitySet extends AbstractSet<Entity> implements EntitySet {

    private Map<EntityId, Entity> entities = new HashMap<>();

    private Set<Entity> addedEntities = new HashSet<>();
    private Set<Entity> changedEntities = new HashSet<>();
    private Set<Entity> removedEntities = new HashSet<>();

    private Class[] types;

    private Map<EntityId, Map<Class, EntityChange>> changes = new ConcurrentHashMap<>();

    private EntityData entityData;

    // the following lists is used to add entities which are added directly
    private List<Entity> directAdded = new ArrayList<>();


    public DefaultEntitySet(EntityData entityData, Class[] types) {
        this.types = types;
        this.entityData = entityData;
    }

    @Override
    public Entity getEntity(EntityId entityId) {
        return entities.get(entityId);
    }

    @Override
    public boolean containsId(EntityId entityId) {
        return entities.containsKey(entityId);
    }

    @Override
    public Class[] getComponentTypes() {
        return types;
    }

    @Override
    public boolean applyChanges() {
        // first of all we want to clear the last changed entity sets
        addedEntities.clear();
        changedEntities.clear();
        removedEntities.clear();

        // next we add directly added entities to the list
        addDirectlyAddedEntities();

        // then we refresh our change sets
        refreshChangeSets(getChanges());
        changes.clear();

        // if there were any changes we return true
        return !addedEntities.isEmpty() || !changedEntities.isEmpty() || !removedEntities.isEmpty();
    }

    @Override
    public Set<Entity> getAddedEntities() {
        return addedEntities;
    }

    @Override
    public Set<Entity> getChangedEntities() {
        return changedEntities;
    }

    @Override
    public Set<Entity> getRemovedEntities() {
        return removedEntities;
    }

    @Override
    public void close() {
        if (entityData instanceof DefaultEntityData) {
            ((DefaultEntityData) entityData).releaseEntitySet(this);
        }
    }

    @Override
    public Iterator<Entity> iterator() {
        return new EntityIterator();
    }

    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public boolean isEmpty() {
        return entities.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Entity) {
            Entity e = (Entity) o;
            return containsId(e.getId());
        }
        return false;
    }

    private class EntityIterator implements Iterator<Entity> {

        private final Iterator<Map.Entry<EntityId, Entity>> iterator = entities.entrySet().iterator();

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Entity next() {
            return iterator.next().getValue();
        }
    }

    protected void loadEntities(List<Entity> entities) {
        for (Entity e : entities) {
            this.entities.put(e.getId(), e);
        }
        directAdded.addAll(entities);
    }

    private void addDirectlyAddedEntities() {
        if (!directAdded.isEmpty()) {
            for (Entity e : directAdded) {
                entities.put(e.getId(), e);
            }
            addedEntities.addAll(directAdded);
            directAdded.clear();
        }
    }

    protected void onRelevantEntityChange(EntityChange change) {
        EntityId changedEntityId = change.getEntityId();
        if (changes.get(changedEntityId) == null) {
            this.changes.put(changedEntityId, new HashMap<>());
        }
        this.changes.get(changedEntityId).put(change.getComponentClass(), change);
    }

    public Collection<EntityChange> getChanges() {
        List<EntityChange> changeList = new ArrayList<>();
        for (Map<Class, EntityChange> changeMap : this.changes.values()) {
            changeList.addAll(changeMap.values());
        }
        return changeList;
    }

    private void refreshChangeSets(Collection<EntityChange> changeList) {
        for (EntityChange change : changeList) {
            EntityId entityId = change.getEntityId();
            Class c = change.getComponentClass();
            EntityComponent component = change.getComponent();


            if (component == null) {
                // this is a remove here
                DefaultEntity entity = (DefaultEntity) entities.remove(entityId);
                if (entity != null) {
                    entity.removeComponent(c);
                    this.removedEntities.add(entity);
                }
            } else {
                // we either have an add or a change

                if (this.entities.containsKey(entityId)) {

                    // we already know about this entity, so lets change its component
                    DefaultEntity entity = (DefaultEntity) entities.get(entityId);
                    entity.changeComponent(c, component);

                    // we check if that entity is already part of the changedEntities list
                    // because it could be that there are several changed for that same entity
                    // so it would be in the list more than once which is not what we want
                    // note that the changes are still applied above, though!
                    if (!changedEntities.contains(entity)) {
                        changedEntities.add(entity);
                    }
                } else {
                    // we have to create a new entity
                    DefaultEntity entity = new DefaultEntity(entityId, getComponentTypes());
                    entity.changeComponent(c, component);

                    // we still need to get the other components
                    for (Class type : getComponentTypes()) {
                        if (type == c) continue;
                        EntityComponent entityComponent = entityData.getComponent(entityId, type);
                        if (entityComponent != null) {
                            entity.changeComponent(type, entityComponent);
                        }
                    }
                    // we add the entity to the addedEntities list, so systems will get notified about the change
                    this.addedEntities.add(entity);

                    // finally we add the newly created entity to our list
                    this.entities.put(entityId, entity);
                }
            }
        }
    }

}
