package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.SpriteSheetImageObject;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.components.ModelComponent;
import de.fuwa.bomberman.game.components.WalkableComponent;

import java.util.HashMap;
import java.util.Map;

public class VisualPlayerAppState extends BaseAppState {

    private EntitySet players;
    private VisualAppState visualAppState;
    private Map<EntityId, PlayerSpriteSheetControl> playerSpriteSheetControlMap = new HashMap<>();

    @Override
    public void initialize(AppStateManager stateManager) {
        EntityData entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.visualAppState = stateManager.getState(VisualAppState.class);
        this.players = entityData.getEntities(ModelComponent.class, WalkableComponent.class);
    }

    @Override
    public void update(float tpf) {
        if (players.applyChanges()) {

            for (Entity entity : players.getAddedEntities()) {
                addControl(entity);
            }

            for (Entity entity : players.getChangedEntities()) {
                updateControl(entity);
            }

            for (Entity entity : players.getRemovedEntities()) {
                removeControl(entity);
            }
        }

        for (PlayerSpriteSheetControl control : playerSpriteSheetControlMap.values()) {
            control.update(tpf);
        }
    }

    private void addControl(Entity entity) {
        SpriteSheetImageObject spriteSheet = visualAppState.getDrawableObject(entity.getId(), SpriteSheetImageObject.class);
        PlayerSpriteSheetControl control = new PlayerSpriteSheetControl();
        control.setupControl(spriteSheet);
        control.setMoveDirection(entity.get(WalkableComponent.class).getMoveDirection());
        playerSpriteSheetControlMap.put(entity.getId(), control);
    }

    private void updateControl(Entity entity) {
        PlayerSpriteSheetControl control = playerSpriteSheetControlMap.get(entity.getId());
        control.setMoveDirection(entity.get(WalkableComponent.class).getMoveDirection());
    }

    private void removeControl(Entity entity) {
        this.playerSpriteSheetControlMap.remove(entity.getId());
    }

    @Override
    public void cleanup() {
        this.playerSpriteSheetControlMap.clear();
        this.players.close();
        this.players.clear();
        this.players = null;
    }
}
