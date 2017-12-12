package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.utils.GameOptions;

public abstract class AbstractGame extends BaseAppState {

    protected AppStateManager stateManager;
    protected MainGameAppState mainGameAppState;
    protected GameStateHandler gameStateHandler;


    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
        onInitialize();
    }

    public void startGame(GameOptions gameOptions) {
//        this.mainGameAppState = new MainGameAppState(stateManager);
//        stateManager.attachState(mainGameAppState);
//
//        this.gameStateHandler = new GameStateHandler(stateManager);
//        stateManager.attachState(gameStateHandler);
//        this.mainGameAppState.addGameStateListener(gameStateHandler);

        onStartGame(gameOptions);
    }

    public abstract void onInitialize();

    protected abstract void onStartGame(GameOptions gameOptions);

    public void closeGame() {
        if (mainGameAppState != null) {
            mainGameAppState.closeGame();
            stateManager.detachState(mainGameAppState);
        }

        stateManager.detachState(gameStateHandler);

        onCloseGame();
    }

    public abstract void onCloseGame();

    @Override
    public void cleanup() {
        stateManager.detachState(mainGameAppState);
        stateManager.detachState(gameStateHandler);
    }
}
