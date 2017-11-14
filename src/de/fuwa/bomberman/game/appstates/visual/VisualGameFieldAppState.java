package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.gui.GameContextFrame;
import de.fuwa.bomberman.app.gui.VisualGameField;

public class VisualGameFieldAppState extends BaseAppState {

    private VisualGameField visualGameField;
    private int sizeX, sizeY;

    public VisualGameFieldAppState(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        GameContextFrame frame = stateManager.getGameApplication().getGameContext();
        this.visualGameField = frame.createAndDisplayGameField(sizeX, sizeY);
    }

    public VisualGameField getVisualGameField() {
        return visualGameField;
    }
}
