package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.utils.GameUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This app state creates a logical view on the current game field
 * and does provide methods to access entities at specific positions
 * of the game field.
 */
public class LogicalGameFieldAppState extends BaseAppState {

    private EntitySet positionalEntities; // contains all entities with a PositionComponent
    private int gameFieldSizeX;
    private int gameFieldSizeY;
    private Cell[][] gameField; // represents the logical game field with its entities
    private Map<EntityId, Cell> entityIdCellMap = new HashMap<>();

    /**
     * Creates a new logical game field app state with the specified parameters.
     *
     * @param sizeX the size of the game field in x-direction (width)
     * @param sizeY the size of the game field in y-direction (height)
     */
    public LogicalGameFieldAppState(int sizeX, int sizeY) {
        this.gameFieldSizeX = sizeX;
        this.gameFieldSizeY = sizeY;
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        EntityData entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.positionalEntities = entityData.getEntities(PositionComponent.class);

        // we create our game field array
        this.gameField = new Cell[this.gameFieldSizeY][this.gameFieldSizeX];
        for (int y = 0; y < this.gameFieldSizeY; y++) {
            for (int x = 0; x < this.gameFieldSizeX; x++) {
                gameField[y][x] = new Cell();
            }
        }
    }

    @Override
    public void update(float tpf) {
        if (positionalEntities.applyChanges()) {

            for (Entity entity : positionalEntities.getAddedEntities()) {
                add(entity);
            }

            for (Entity entity : positionalEntities.getChangedEntities()) {
                update(entity);
            }

            for (Entity entity : positionalEntities.getRemovedEntities()) {
                remove(entity);
            }

        }
    }

    public List<EntityId> getEntitiesAt(int x, int y) {
        Cell cell = gameField[y][x];
        if (cell != null) {
            return cell.entities;
        }
        return null;
    }

    public int getGameFieldSizeX() {
        return gameFieldSizeX;
    }

    public int getGameFieldSizeY() {
        return gameFieldSizeY;
    }

    private void add(Entity entity) {
        PositionComponent pos = entity.get(PositionComponent.class);
        pos = GameUtils.getCellPosition(pos);
        int y = (int) pos.getY();
        int x = (int) pos.getX();
        // we check if the values are valid (should always be the case)
        if (areValidValues(x, y)) {
            gameField[y][x].addEntity(entity.getId());
            this.entityIdCellMap.put(entity.getId(), gameField[y][x]);
        }
    }

    private void update(Entity entity) {
        Cell cell = entityIdCellMap.get(entity.getId());
        if (cell == null) return;
        PositionComponent pos = entity.get(PositionComponent.class);
        pos = GameUtils.getCellPosition(pos);
        int y = (int) pos.getY();
        int x = (int) pos.getX();
        if (areValidValues(x, y)) {
            // now we check if the cell would change
            if (cell != gameField[y][x]) {
                // it is a different one
                // so we remove the entity from the current cell
                cell.removeEntity(entity.getId());
                // now we have to add it to the new cell
                gameField[y][x].addEntity(entity.getId());
                // we also have to refresh the map
                this.entityIdCellMap.put(entity.getId(), gameField[y][x]);
            }
        }
    }

    private void remove(Entity entity) {
        Cell cell = entityIdCellMap.remove(entity.getId());
        if (cell != null) {
            cell.removeEntity(entity.getId());
        }
    }

    /**
     * Returns true if the specified x and y values are valid (within the game field)
     *
     * @param x the x value to check
     * @param y the y value to check
     * @return true if both values are within the game field.
     */
    private boolean areValidValues(int x, int y) {
        return isValid(x, gameFieldSizeX) && isValid(y, gameFieldSizeY);
    }

    private boolean isValid(int v, int max) {
        return v >= 0 && v < max;
    }

    @Override
    public void cleanup() {
        for (Entity e : positionalEntities) {
            remove(e);
        }
        positionalEntities.close();
        positionalEntities.clear();
        positionalEntities = null;

        this.entityIdCellMap.clear();
    }

    /**
     * A Cell represents a logical cell and can contain
     * a bunch of entities positioned in this cell.
     */
    private class Cell {

        // the entities contained in this cell
        private List<EntityId> entities;

        Cell() {
            this.entities = new ArrayList<>();
        }

        void addEntity(EntityId entityId) {
            if (!entities.contains(entityId)) {
                entities.add(entityId);
            }
        }

        void removeEntity(EntityId entityId) {
            if (entities.contains(entityId)) {
                entities.remove(entityId);
            }
        }
    }
}
