package de.fuwa.bomberman.game.appstates.state;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.appstates.visual.SettingsAppState;
import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.state.GameStateListener;
import de.fuwa.bomberman.game.utils.GameInitializer;

/**
 * A GameStateHandler receives events from the overall game state logic.
 * So the game can be properly prepared, started and stopped.
 * <p>
 * In a networked environment this class will run on client side
 * and will receive those events from the server.
 */
public class GameStateHandler extends BaseAppState implements GameStateListener {

    private AppStateManager stateManager;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void onSetupGame(Setting setting, int gameFieldSizeX, int gameFieldSizeY) {
        stateManager.attachState(new SettingsAppState(setting));

        // create visual and sound app states
        GameInitializer.initVisualAndSoundAppStates(stateManager, gameFieldSizeX, gameFieldSizeY);
    }

    @Override
    public void onStartGame() {
        // finally enable input --> will call game session methods
        GameInitializer.initInputAppStates(stateManager);
    }
}
