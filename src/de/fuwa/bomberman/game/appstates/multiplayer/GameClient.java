package de.fuwa.bomberman.game.appstates.multiplayer;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.net.RemoteEntityData;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.multiplayer.messages.*;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.state.GameStateListener;
import de.fuwa.bomberman.game.utils.GameInitializer;
import de.fuwa.bomberman.game.utils.GameUtils;
import de.fuwa.bomberman.network.Client;
import de.fuwa.bomberman.network.ClientStateListener;
import de.fuwa.bomberman.network.MessageListener;
import de.fuwa.bomberman.network.messages.AbstractMessage;

public class GameClient extends BaseAppState implements MessageListener<Client>, ClientStateListener {

    private Client client;
    private String hostIp;
    private int port;
    private GameStateListener gameStateListener;
    private AppStateManager stateManager;

    public GameClient() {
    }

    public GameClient(String hostIp, int port) {
        this.hostIp = hostIp;
        this.port = port;
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
//        this.gameStateListener = stateManager.getState(GameStateHandler.class);

//        this.client = new Client(hostIp, port);
//        this.client.addMessageListener(this);
//        this.client.addClientStateListener(this);
//        this.client.start();
    }

    public boolean connect(String hostIp, int port) {
        this.client = new Client(hostIp, port);
        this.client.addMessageListener(this);
        this.client.addClientStateListener(this);
        return client.connectToServer();
    }

    public void start() {
        this.client.start();
    }

    public void setGameStateListener(GameStateHandler listener) {
        this.gameStateListener = listener;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public void onMessageReceived(Client source, AbstractMessage m) {
        if (m instanceof JoinGameMessage) {
            // we have been accepted so we can create
            // our game session

        }

        if (m instanceof OnSetupGameMessage) {
            stateManager.getGameApplication().addCallable(() -> {
                OnSetupGameMessage sm = (OnSetupGameMessage) m;
                stateManager.attachState(new EntityDataState(new RemoteEntityData(source)));
                stateManager.attachState(new GameSessionAppState(new RemoteGameSession(source)));
                gameStateListener.onSetupGame(sm.getSetting(), sm.getSizeX(), sm.getSizeY());
                GameInitializer.initClientAppStates(stateManager);
            });

            // at this point we setup our game, so we tell the server that we are ready
            source.send(new ReadyForGameStartMessage());
        } else if (m instanceof OnGameStartMessage) {
            stateManager.getGameApplication().addCallable(() -> gameStateListener.onStartGame(((OnGameStartMessage) m).getMatchDuration()));

        } else if (m instanceof OnCloseGameMessage) {
            stateManager.getGameApplication().addCallable(() -> gameStateListener.onCloseGame());
        } else if (m instanceof OnGameDecidedMessage) {
            stateManager.getGameApplication().addCallable(() -> gameStateListener.onGameDecided(((OnGameDecidedMessage) m).getWinnerName()));
        }
    }

    @Override
    public void onClientConnected(Client c) {
        System.out.println("connected");
        c.send(new JoinGameMessage(GameUtils.createDefaultHumanPlayer()));
    }

    @Override
    public void onClientDisconnected() {

    }

    private class RemoteGameSession implements GameSession {

        private Client client;

        public RemoteGameSession(Client client) {
            this.client = client;
        }

        @Override
        public void placeBomb() {
            client.send(new PlaceBombMessage());
        }

        @Override
        public void applyMoveDirection(MoveDirection direction) {
            client.send(new ApplyMoveDirectionMessage(direction));
        }
    }

}
