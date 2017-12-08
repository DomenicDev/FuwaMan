package de.fuwa.bomberman.game.appstates.state;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.appstates.FuwaManGuiHolderAppState;
import de.fuwa.bomberman.game.appstates.visual.SettingsAppState;
import de.fuwa.bomberman.game.appstates.visual.VisualTimerAppState;
import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.gui.InGameGui;
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
    private InGameGui inGameGui;

    public GameStateHandler() {
    }

    public GameStateHandler(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void onSetupGame(Setting setting, int gameFieldSizeX, int gameFieldSizeY) {
        stateManager.attachState(new SettingsAppState(setting));
        this.inGameGui = stateManager.getState(FuwaManGuiHolderAppState.class).getInGameGui();

        // create visual and sound app states
        GameInitializer.initVisualAndSoundAppStates(stateManager, gameFieldSizeX, gameFieldSizeY);

        // update status text
        inGameGui.setStatusText("GAME IS BEING SETUP");
    }

    @Override
    public void onStartGame(float matchDuration) {
        // create game start related app states
        stateManager.attachState(new VisualTimerAppState(matchDuration));

        // finally enable input --> will call game session methods
        GameInitializer.initInputAppStates(stateManager);

        // update gui text
        inGameGui.setStatusText("GAME RUNNING");
    }

    @Override
    public void onGameDecided(String winnerName) {
        inGameGui.setStatusText("Player " + winnerName + " won the game!");
    }

    @Override
    public void onCloseGame() {
        stateManager.detachState(stateManager.getState(SettingsAppState.class));
        stateManager.detachState(stateManager.getState(VisualTimerAppState.class));
        GameInitializer.removeVisualAndSoundAppStates(stateManager);
        GameInitializer.removeInputAppStates(stateManager);
    }
}
