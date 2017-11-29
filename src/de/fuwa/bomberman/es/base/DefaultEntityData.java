package de.fuwa.bomberman.es.base;

import de.fuwa.bomberman.es.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultEntityData implements EntityData {

    private int idCounter;

    private ConcurrentMap<EntityId, Map<Class, EntityComponent>> entityDataBase = new ConcurrentHashMap<>();

    private EntitySetChangeListener entitySetHandler = new EntitySetChangeListener();

    private List<EntityChangeListener> entityChangeListeners = new ArrayList<>();

    public DefaultEntityData() {
        addEntityChangeListener(entitySetHandler);
    }

    @Override
    public EntityId createEntity() {
        EntityId entityId = new EntityId(idCounter++);
        entityDataBase.put(entityId, new ConcurrentHashMap<>());
        return entityId;
    }

    @Override
    public void setComponents(EntityId entityId, EntityComponent... components) {
        for (EntityComponent comp : components) {
            setComponent(entityId, comp);
        }
    }

    @Override
    public void setComponent(EntityId entityId, EntityComponent component) {
        if (entityDataBase.containsKey(entityId)) {
            Map<Class, EntityComponent> data = entityDataBase.get(entityId);
            if (component != null) {
                data.put(component.getClass(), component);
                fireEntityChange(new EntityChange(entityId, component, component.getClass()));
            }
        }
    }

    @Override
    public void removeComponent(EntityId entityId, Class component) {
        if (component == null) {
            return;
        }

        Map<Class, EntityComponent> data = entityDataBase.get(entityId);
        if (data.remove(component) != null) {
            fireEntityChange(new EntityChange(entityId, null, component));
        }

    }

    @Override
    public <T extends EntityComponent> T getComponent(EntityId entityId, Class<T> componentType) {
        if (entityDataBase.containsKey(entityId)) {
            Map<Class, EntityComponent> data = entityDataBase.get(entityId);
            if (data.containsKey(componentType)) {
                return componentType.cast(data.get(componentType));
            }
        }
        return null;
    }

    @Override
    public EntitySet getEntities(Class... componentTypes) {
        DefaultEntitySet entitySet = new DefaultEntitySet(this, componentTypes);
        List<Entity> list = findEntities(componentTypes);
        entitySet.loadEntities(list);
        this.entitySetHandler.addEntitySet(entitySet);
        return entitySet;
    }

    @Override
    public List<Entity> findEntities(Class... componentTypes) {
        List<Entity> result = new ArrayList<>();

        List<Class> types = Arrays.asList(componentTypes);

        for (Map.Entry<EntityId, Map<Class, EntityComponent>> e : entityDataBase.entrySet()) {
            Map<Class, EntityComponent> map = e.getValue();
            if (map.keySet().containsAll(types)) {
                DefaultEntity entity = new DefaultEntity(e.getKey(), componentTypes);
                for (Class c : componentTypes) {
                    entity.changeComponent(c, map.get(c));
                }
                result.add(entity);
            }
        }

        return result;
    }

    @Override
    public void removeEntity(EntityId entityId) {
        Set<Class> types = entityDataBase.get(entityId).keySet();
        for (Class type : types) {
            //removeComponent(entityId, type);
            fireEntityChange(new EntityChange(entityId, null, type));
        }
        entityDataBase.get(entityId).clear();
    }

    protected void fireEntityChange(EntityChange entityChange) {
        for (EntityChangeListener l : entityChangeListeners) {
            l.onEntityChange(entityChange);
        }
    }

    protected void releaseEntitySet(EntitySet entitySet) {
        entitySetHandler.releaseEntitySet(entitySet);
    }

    public void addEntityChangeListener(EntityChangeListener listener) {
        this.entityChangeListeners.add(listener);
    }

    /**
     * This private class implements the {@link EntityChangeListener}
     * and thus listens for entity changes.
     * This class will care about every entity set added and only sends changes
     * to sets which actually care about that change.
     */
    private class EntitySetChangeListener implements EntityChangeListener {

        private Map<ComponentCombination, List<EntitySet>> entitySetsMap = new ConcurrentHashMap<>();

        /**
         * The specified EntitySet will now receive updates.
         * @param entitySet the set to add
         */
        public void addEntitySet(EntitySet entitySet) {

            boolean componentCombinationDoesExist = false;

            for (Map.Entry<ComponentCombination, List<EntitySet>> e : entitySetsMap.entrySet()) {
                if (e.getKey().hasSameTypes(entitySet.getComponentTypes())) {
                    e.getValue().add(entitySet);
                    componentCombinationDoesExist = true;
                    break;
                }
            }

            if (!componentCombinationDoesExist) {
                ComponentCombination combination = new ComponentCombination(entitySet.getComponentTypes());
                List<EntitySet> list = new ArrayList<>();
                list.add(entitySet);
                entitySetsMap.put(combination, list);
            }
        }

        private void releaseEntitySet(EntitySet entitySet) {
            if (entitySet == null) return;
            for (List<EntitySet> list : entitySetsMap.values()) {
                for (EntitySet set : list) {
                    if (entitySet.equals(set)) {
                        list.remove(set);
                        return;
                    }
                }
            }
        }

        @Override
        public void onEntityChange(EntityChange change) {
            EntityId entityId = change.getEntityId();
            Class componentType = change.getComponentClass();
            EntityComponent component = change.getComponent();

            for (Map.Entry<ComponentCombination, List<EntitySet>> e : entitySetsMap.entrySet()) {
                ComponentCombination combination = e.getKey();

                // we check if that combination contains the changed component type
                if (combination.containsType(componentType)) {

                    if (component == null) {
                        // this was a remove

                        boolean justRemoved = true;
                        for (Class<? extends EntityComponent> type : combination.getComponentTypes()) {
                            if (type == componentType) continue;

                            if (getComponent(entityId, type) == null) {
                                justRemoved = false;
                                break;
                            }
                        }

                        // if justRemoved is true we know that just one of the relevant types have been removed
                        // the entity sets will remove the entity from their list as soon as one required
                        // component is removed.
                        // So there is no need to send that information again if justRemoved is false.
                        if (justRemoved) {
                            for (EntitySet entitySet : e.getValue()) {
                                ((DefaultEntitySet)entitySet).onRelevantEntityChange(change);
                            }
                        }
                    } else {
                        // the component is not null, so we check if that entity has all other
                        // needed components of that combination
                        boolean hasAllNeededComponents = true;
                        for (Class<? extends EntityComponent> type : combination.getComponentTypes()) {
                            if (getComponent(entityId, type) == null) {
                                hasAllNeededComponents = false;
                                break;
                            }
                        }

                        if (hasAllNeededComponents) {
                            // the entity actually does have all required components of that combination
                            // so we inform the entity sets about the update
                           for (EntitySet entitySet : e.getValue()) {
                               ((DefaultEntitySet)entitySet).onRelevantEntityChange(change);
                           }
                        }
                    }
                }
            }
        }

        /**
         * This class just collects various types of components.
         * Used to gather entity sets which are interested in the exact same components.
         */
        private class ComponentCombination {

            private Class<? extends EntityComponent>[] componentTypes;

            ComponentCombination(Class<? extends EntityComponent>[] componentTypes) {
                this.componentTypes = componentTypes;
            }

            Class<? extends EntityComponent>[] getComponentTypes() {
                return componentTypes;
            }

            boolean hasSameTypes(Class<? extends EntityComponent>[] components) {
                if (componentTypes.length != components.length) {
                    return false;
                }

                for (Class c : componentTypes) {
                    boolean found = false;
                    for (Class c2 : components) {
                        if (c == c2) {
                            found = true;
                        }
                    }
                    if (!found) return false;
                }
                return true;
            }

            boolean containsType(Class type) {
                for (Class<? extends EntityComponent> c : componentTypes) {
                    if (c == type) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == this) {
                    return true;
                } else if (obj instanceof ComponentCombination) {
                    ComponentCombination comb = (ComponentCombination) obj;
                    return hasSameTypes(comb.getComponentTypes());
                }
                return false;
            }
        }
    }

}
