package de.fuwa.bomberman.game.appstates.visual;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.appstates.FuwaManGuiHolderAppState;
import de.fuwa.bomberman.game.gui.InGameGui;

public class VisualTimerAppState extends BaseAppState {

    private float timer;
    private InGameGui inGameGui;

    public VisualTimerAppState(float timer) {
        this.timer = timer;
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        FuwaManGuiHolderAppState guiHolderAppState = stateManager.getState(FuwaManGuiHolderAppState.class);
        this.inGameGui = guiHolderAppState.getInGameGui();
    }

    @Override
    public void update(float tpf) {
        if (this.inGameGui != null) {
            timer -= tpf;
            if (timer < 0) {
                timer = 0;
            }
            inGameGui.setRemainingTime(timer);
        }
    }
}
