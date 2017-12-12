package de.fuwa.bomberman.game;

import de.fuwa.bomberman.game.appstates.AbstractGame;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.GameOptions;
import de.fuwa.bomberman.game.utils.GameUtils;
import de.fuwa.bomberman.game.utils.Player;

public class SingleplayerGame extends AbstractGame {

    private Player player = GameUtils.createDefaultHumanPlayer();
    private GameSessionAppState gameSessionAppState;

    @Override
    public void onInitialize() {
        this.mainGameAppState = new MainGameAppState(stateManager);
        stateManager.attachState(mainGameAppState);

        this.gameStateHandler = new GameStateHandler(stateManager);
        stateManager.attachState(gameStateHandler);
        this.mainGameAppState.addGameStateListener(gameStateHandler);
    }

    @Override
    public void onStartGame(GameOptions gameOptions) {
        if (gameOptions.getNumberOfKis() <= 0) {
            return;
        }
        System.out.println(player.getName());
        // first add the real player to the game
        mainGameAppState.addPlayer(player);

        // want to setup the game
        // listeners will be notified
        mainGameAppState.setupGame(gameOptions);

        // we create the game session app state for our player
        GameSession gameSession = mainGameAppState.getGameSession(player);
        this.gameSessionAppState = new GameSessionAppState(gameSession);
        stateManager.attachState(gameSessionAppState);

        // finally we can start the game
        mainGameAppState.startGame();
    }

    @Override
    public void onCloseGame() {

    }

    @Override
    public void cleanup() {
        stateManager.detachState(mainGameAppState);
        stateManager.detachState(gameSessionAppState);
        super.cleanup();
    }
}
