package de.fuwa.bomberman.game.appstates.multiplayer;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.DrawableObject;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.visual.VisualAppState;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.components.WalkableComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This app state interpolates the positions of dynamic game objects such as characters.
 * It updates the position of the specific drawable objects for each dynamic entity in update().
 */
public class ClientSideInterpolationAppState extends BaseAppState {

    private EntitySet dynamics;
    private VisualAppState visualAppState;
    private Map<EntityId, Position> current = new ConcurrentHashMap<>();

    @Override
    public void initialize(AppStateManager stateManager) {
        EntityData entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.dynamics = entityData.getEntities(PositionComponent.class, WalkableComponent.class);
        this.visualAppState = stateManager.getState(VisualAppState.class);
    }

    @Override
    public void update(float tpf) {

        if (dynamics.applyChanges()) {

            for (Entity entity : dynamics.getAddedEntities()) {
                PositionComponent pos = entity.get(PositionComponent.class);
                this.current.put(entity.getId(), new Position(pos.getX(), pos.getY()));
            }

            for (Entity entity : dynamics.getRemovedEntities()) {
                this.current.remove(entity.getId());
            }

        }

        for (Map.Entry<EntityId, Position> e : current.entrySet()) {
            EntityId entityId = e.getKey();
            Position currentPos = e.getValue();
            PositionComponent goalPos = dynamics.getEntity(entityId).get(PositionComponent.class);

            float factor = 0.15f;
            float x = currentPos.getX() + ((goalPos.getX() - currentPos.getX()) * factor);
            float y = currentPos.getY() + ((goalPos.getY() - currentPos.getY()) * factor);

            current.get(entityId).set(x, y);
            // apply interpolated pos
            DrawableObject drawableObject = visualAppState.getDrawableObject(entityId);
            if (drawableObject != null) {
                drawableObject.set(x, y);
            }
        }

    }

    @Override
    public void cleanup() {
        this.dynamics.close();
        this.dynamics.clear();
        this.dynamics = null;

        this.current.clear();
    }

    private class Position {

        private float x, y;

        Position(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void set(float x, float y) {
            setX(x);
            setY(y);
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }
    }
}
