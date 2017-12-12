package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.AssetLoader;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.DrawableObject;
import de.fuwa.bomberman.app.gui.DrawingLayer;
import de.fuwa.bomberman.app.gui.StaticImageObject;
import de.fuwa.bomberman.app.gui.VisualGameField;
import de.fuwa.bomberman.game.enums.Setting;

public class VisualBackgroundAppState extends BaseAppState {

    private DrawableObject background;
    private VisualGameField visualGameField;

    @Override
    public void initialize(AppStateManager stateManager) {
        SettingsAppState settingsAppState = stateManager.getState(SettingsAppState.class);
        Setting setting = settingsAppState.getCurrentSetting();

        VisualGameFieldAppState visualGameFieldAppState = stateManager.getState(VisualGameFieldAppState.class);
        this.visualGameField = visualGameFieldAppState.getVisualGameField();

        AssetLoader assetLoader = stateManager.getGameApplication().getAssetLoader();

        String path = "assets/Textures/Settings/" + setting + "/ground.jpg";

        this.background = new StaticImageObject(assetLoader.loadSingleImage(path));
        background.setLayer(DrawingLayer.Background);
        visualGameField.setBackground(background);
    }

    @Override
    public void cleanup() {
        this.visualGameField.removeGameObject(background);
    }
}
