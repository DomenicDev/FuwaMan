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
import de.fuwa.bomberman.game.utils.Player;
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
        this.gameStateListener = stateManager.getState(GameStateHandler.class);

        this.client = new Client(hostIp, port);
        this.client.addMessageListener(this);
        this.client.addClientStateListener(this);
        this.client.start();
    }


    @Override
    public void onMessageReceived(Client source, AbstractMessage m) {
        if (m instanceof JoinGameMessage) {
            // we have been accepted so we can create
            // our game session

        }

        if (m instanceof OnSetupGameMessage) {
            OnSetupGameMessage sm = (OnSetupGameMessage) m;
            stateManager.attachState(new EntityDataState(new RemoteEntityData(source)));
            stateManager.attachState(new GameSessionAppState(new RemoteGameSession(source)));
            this.gameStateListener.onSetupGame(sm.getSetting(), sm.getSizeX(), sm.getSizeY());
            // at this point we setup our game, so we tell the server that we are ready
            source.send(new ReadyForGameStartMessage());
        } else if (m instanceof OnGameStartMessage) {
            this.gameStateListener.onStartGame();
        }
    }

    @Override
    public void onClientConnected(Client c) {
        System.out.println("connected");
        c.send(new JoinGameMessage(new Player("TestClient")));
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
