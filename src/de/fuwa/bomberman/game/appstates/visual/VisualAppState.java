package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.AssetLoader;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.DrawableObject;
import de.fuwa.bomberman.app.gui.SpriteSheetImageObject;
import de.fuwa.bomberman.app.gui.StaticImageObject;
import de.fuwa.bomberman.app.gui.VisualGameField;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.components.ModelComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.ModelType;

import java.util.HashMap;
import java.util.Map;


public class VisualAppState extends BaseAppState {

    private EntitySet visualEntities;
    private VisualGameField visualGameField;
    private AssetLoader assetLoader;
    private SettingsAppState settingsAppState;
    private Map<EntityId, DrawableObject> visualGameObjectMap = new HashMap<>();

    @Override
    public void initialize(AppStateManager stateManager) {
        EntityData entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.visualEntities = entityData.getEntities(ModelComponent.class, PositionComponent.class);
        this.visualGameField = stateManager.getState(VisualGameFieldAppState.class).getVisualGameField();
        this.settingsAppState = stateManager.getState(SettingsAppState.class);
        this.assetLoader = stateManager.getGameApplication().getAssetLoader();
    }

    public DrawableObject getDrawableObject(EntityId entityId) {
        return visualGameObjectMap.get(entityId);
    }

    public <T extends DrawableObject> T getDrawableObject(EntityId entityId, Class<T> c) {
        DrawableObject drawable = visualGameObjectMap.get(entityId);
        if (drawable != null && drawable.getClass().isAssignableFrom(c)) {
            return c.cast(visualGameObjectMap.get(entityId));
        }
        return null;
    }

    @Override
    public void update(float tpf) {
        if (visualEntities.applyChanges()) {

            for (Entity entity : visualEntities.getAddedEntities()) {
                addEntity(entity);
            }

            for (Entity entity : visualEntities.getChangedEntities()) {
                updateEntity(entity);
            }

            for (Entity entity : visualEntities.getRemovedEntities()) {
                removeEntity(entity);
            }
        }

    }

    private void addEntity(Entity entity) {
        ModelType modelType = entity.get(ModelComponent.class).getModelType();
        PositionComponent position = entity.get(PositionComponent.class);

        DrawableObject drawableObject = createDrawableObject(modelType);
        drawableObject.set(position.getX(), position.getY());
        visualGameField.addGameObject(drawableObject);
        this.visualGameObjectMap.put(entity.getId(), drawableObject);
    }

    private void updateEntity(Entity entity) {
        DrawableObject object = visualGameObjectMap.get(entity.getId());
        PositionComponent pos = entity.get(PositionComponent.class);
        object.set(pos.getX(), pos.getY());
    }

    private void removeEntity(Entity entity) {
        DrawableObject object = visualGameObjectMap.remove(entity.getId());
        visualGameField.removeGameObject(object);
    }

    private DrawableObject createDrawableObject(ModelType modelType) {
        final String TEXTURE_PATH = "assets/Textures/";
        final String PLAYER_PATH = TEXTURE_PATH + "Player/";
        final String SETTINGS_PATH = TEXTURE_PATH + "Settings/" + settingsAppState.getCurrentSetting() + "/";

        DrawableObject drawableObject = null;
        if (modelType == ModelType.Player) {
            drawableObject = new SpriteSheetImageObject(assetLoader.loadSingleImage(PLAYER_PATH + "player_green.png"), 4, 4);
        } else if (modelType == ModelType.UndestroyableTile) {
            drawableObject = new StaticImageObject(assetLoader.loadSingleImage(SETTINGS_PATH + "undes_block_01.png"));
        } else if(modelType == ModelType.DestroyableTile){
            drawableObject = new StaticImageObject(assetLoader.loadSingleImage(SETTINGS_PATH + "des_block_01.png"));
        }

        else if (modelType == ModelType.Bomb) {
            drawableObject = new StaticImageObject(assetLoader.loadSingleImage(SETTINGS_PATH + "bomb_01.png"));
        } else if(modelType == ModelType.Explosion){
            drawableObject = new StaticImageObject(assetLoader.loadSingleImage(TEXTURE_PATH + "Explosion.png"));
        } else if(modelType == ModelType.BombAmountUp){
            drawableObject = new StaticImageObject(assetLoader.loadSingleImage(TEXTURE_PATH + "BombAmountUp.png"));
        } else if(modelType == ModelType.BombStrengthUp){
            drawableObject = new StaticImageObject(assetLoader.loadSingleImage(TEXTURE_PATH + "BombStrengthUp.png"));
        } else if(modelType == ModelType.SpeedUp){
            drawableObject = new StaticImageObject(assetLoader.loadSingleImage(TEXTURE_PATH + "SpeedUp.png"));
        }

        if (drawableObject == null) {
            System.out.println("WARNUNG: drawableobject is not set!");
        }

        return drawableObject;
    }

    @Override
    public void cleanup() {
        if (visualGameField != null) {
            for (DrawableObject o : visualGameObjectMap.values()) {
                visualGameField.removeGameObject(o);
            }
        }
        this.visualEntities.close();
        this.visualEntities.clear();
        this.visualEntities = null;

        this.visualGameObjectMap.clear();
    }
}
