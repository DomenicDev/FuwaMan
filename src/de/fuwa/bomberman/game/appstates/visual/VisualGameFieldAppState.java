package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.GameContextFrame;
import de.fuwa.bomberman.app.gui.VisualGameField;
import de.fuwa.bomberman.game.gui.InGameGui;

public class VisualGameFieldAppState extends BaseAppState {

    private GameContextFrame frame;
    private VisualGameField visualGameField;
    private InGameGui inGameGui;
    private int sizeX, sizeY;

    public VisualGameFieldAppState(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        this.frame = stateManager.getGameApplication().getGameContext();
        this.inGameGui = new InGameGui(sizeX, sizeY);
        this.frame.setScreen(inGameGui);
        this.visualGameField = inGameGui.getVisualGameField();
    }

    public VisualGameField getVisualGameField() {
        return visualGameField;
    }

    @Override
    public void cleanup() {
        this.frame.removeCurrentGameField();
    }
}
