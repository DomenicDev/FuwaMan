package de.fuwa.bomberman.game;

import de.fuwa.bomberman.game.appstates.AbstractGame;
import de.fuwa.bomberman.game.appstates.multiplayer.GameClient;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.utils.GameOptions;

public class RemoteMultiplayerGame extends AbstractGame {

    private GameClient gameClient;
    private GameStateHandler gameStateHandler;

    public RemoteMultiplayerGame(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void onInitialize() {
        this.gameStateHandler = new GameStateHandler();
        stateManager.attachState(gameStateHandler);
        this.gameClient.setGameStateListener(gameStateHandler);
    }

    @Override
    protected void onStartGame(GameOptions gameOptions) {

    }

    @Override
    public void onCloseGame() {

    }

    @Override
    public void cleanup() {
        super.cleanup();
        stateManager.detachState(gameClient);
        stateManager.detachState(gameStateHandler);
    }
}
