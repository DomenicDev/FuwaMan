package de.fuwa.bomberman.game;

import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.game.appstates.AbstractGame;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.multiplayer.GameServer;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.GameConstants;
import de.fuwa.bomberman.game.utils.GameOptions;
import de.fuwa.bomberman.game.utils.GameUtils;
import de.fuwa.bomberman.game.utils.Player;

public class MultiPlayerGame extends AbstractGame {

    private GameServer server;
    private Player player;
    private GameSessionAppState gameSessionAppState;
    private boolean waitingForGameStart;

    @Override
    public void onInitialize() {
        this.mainGameAppState = new MainGameAppState(stateManager);
        stateManager.attachState(mainGameAppState);

        this.gameStateHandler = new GameStateHandler(stateManager);
        stateManager.attachState(gameStateHandler);
        this.mainGameAppState.addGameStateListener(gameStateHandler);


        this.server = new GameServer(GameConstants.PORT);
        this.server.setMainGameAppState(mainGameAppState);
        this.server.setEntityData((DefaultEntityData) mainGameAppState.getEntityData());
        stateManager.attachState(server);

        // we add the host
        this.player = GameUtils.createDefaultHumanPlayer();
        mainGameAppState.addPlayer(player);
    }

    @Override
    protected void onStartGame(GameOptions gameOptions) {
        this.waitingForGameStart = false;
        //   this.server.setMainGameAppState(mainGameAppState);
        System.out.println("hallo " + mainGameAppState.sizeOfAddedPlayers());
        if (!(mainGameAppState.sizeOfAddedPlayers() > 1)) {
            return;
        }


        // we setup the game
        mainGameAppState.setupGame(gameOptions);

        // we create the game session app state for our player (host)
        GameSession gameSession = mainGameAppState.getGameSession(player);
        this.gameSessionAppState = new GameSessionAppState(gameSession);
        stateManager.attachState(gameSessionAppState);

        // now we have to wait until every player is ready
        waitingForGameStart = true;
    }

    @Override
    public void onCloseGame() {

    }

    @Override
    public void update(float tpf) {
        // we start the game if every player is ready
        if (waitingForGameStart && server.areAllPlayersReady()) {
            mainGameAppState.startGame();
            waitingForGameStart = false;
        }
    }

    @Override
    public void cleanup() {
        System.out.println("cleanup");
        stateManager.detachState(gameSessionAppState);
        stateManager.detachState(mainGameAppState);
        //   stateManager.detachState(gameStateHandler);
        stateManager.detachState(server);
        super.cleanup();
        System.out.println("cleanup ende");

    }
}
