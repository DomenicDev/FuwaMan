package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.AssetLoader;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.AnimatedImageObject;
import de.fuwa.bomberman.app.gui.DrawableObject;
import de.fuwa.bomberman.app.gui.StaticImageObject;
import de.fuwa.bomberman.app.gui.VisualGameField;
import de.fuwa.bomberman.es.Entity;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.components.ModelComponent;
import de.fuwa.bomberman.game.components.PositionComponent;

import java.util.HashMap;
import java.util.Map;


public class VisualAppState extends BaseAppState {

    private EntitySet visualEntities;
    private VisualGameField visualGameField;
    private AssetLoader assetLoader;

    private Map<EntityId, DrawableObject> visualGameObjectMap = new HashMap<>();

    @Override
    public void initialize(AppStateManager stateManager) {
        EntityData entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.visualEntities = entityData.getEntities(ModelComponent.class, PositionComponent.class);
        this.visualGameField = stateManager.getState(VisualGameFieldAppState.class).getVisualGameField();
        this.assetLoader = stateManager.getGameApplication().getAssetLoader();
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
        ModelComponent model = entity.get(ModelComponent.class);
        PositionComponent position = entity.get(PositionComponent.class);

        DrawableObject drawableObject;

        String path = "assets/Textures/" + model.getModelType().getFilename();

        if (model.isAnimated()) {
            // load animated gif
            drawableObject = new AnimatedImageObject(position.getX(), position.getY(), assetLoader.loadAnimatedGif(path), true);
        } else {
            // load static image
            drawableObject = new StaticImageObject(position.getX(), position.getY(), assetLoader.loadSingleImage("assets/Textures/" + model.getModelType().getFilename()));
        }

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
