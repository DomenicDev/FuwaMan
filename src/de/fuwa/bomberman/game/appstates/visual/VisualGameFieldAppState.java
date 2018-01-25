package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.GameContextFrame;
import de.fuwa.bomberman.app.gui.VisualGameField;
import de.fuwa.bomberman.game.appstates.FuwaManGuiHolderAppState;

public class VisualGameFieldAppState extends BaseAppState {

    private GameContextFrame frame;
    private VisualGameField visualGameField;
    private int sizeX, sizeY;

    public VisualGameFieldAppState(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        this.frame = stateManager.getGameApplication().getGameContext();
        FuwaManGuiHolderAppState guiHolder = stateManager.getState(FuwaManGuiHolderAppState.class);
        guiHolder.getInGameGui().getVisualGameField().setSize(sizeX, sizeY);
        this.frame.setScreen(guiHolder.getInGameGui());
        this.visualGameField = guiHolder.getInGameGui().getVisualGameField();
    }

    public VisualGameField getVisualGameField() {
        return visualGameField;
    }

    @Override
    public void cleanup() {
        if (visualGameField != null) {
            visualGameField.clearAll();
        }
    }
}
