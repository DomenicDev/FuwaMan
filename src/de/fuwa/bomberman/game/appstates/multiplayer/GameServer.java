package de.fuwa.bomberman.game.appstates.multiplayer;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.es.net.HostedEntityData;
import de.fuwa.bomberman.es.net.messages.CloseEntitySetMessage;
import de.fuwa.bomberman.es.net.messages.GetEntitiesMessage;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.multiplayer.messages.*;
import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.state.GameStateListener;
import de.fuwa.bomberman.game.utils.GameUtils;
import de.fuwa.bomberman.game.utils.Player;
import de.fuwa.bomberman.network.ConnectionListener;
import de.fuwa.bomberman.network.HostedConnection;
import de.fuwa.bomberman.network.MessageListener;
import de.fuwa.bomberman.network.Server;
import de.fuwa.bomberman.network.messages.AbstractMessage;

import java.util.HashMap;
import java.util.Map;

public class GameServer extends BaseAppState implements ConnectionListener, MessageListener<HostedConnection> {

    int playerCounter = 0;
    int readyCounter = 0;
    private AppStateManager stateManager;
    private Server server;
    private int port;
    private MainGameAppState mainGameAppState;
    private Map<HostedConnection, Player> connectionPlayerMap = new HashMap<>();
    private Map<HostedConnection, HostedEntityData> hostedEntityDataMap = new HashMap<>();
    private float updateInterval = 0.1f; // 10 updates per second
    private float updateTimer = 0;

    /**
     * Will create a FuwaMan server at the specified port
     *
     * @param port the port of the server
     */
    public GameServer(int port) {
        this.port = port;
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
        this.mainGameAppState = stateManager.getState(MainGameAppState.class);

        // this will inform all players about in-game events
        this.mainGameAppState.addGameStateListener(new ServerSideGameStateListener());

        // init server
        this.server = new Server(this.port);
        this.server.addConnectionListener(this);
        this.server.addMessageListener(this);
        this.server.start();
    }

    @Override
    public void update(float tpf) {
        if ((updateTimer += tpf) >= updateInterval) {
            updateTimer = 0;
            for (HostedEntityData hostedEntityData : hostedEntityDataMap.values()) {
                hostedEntityData.sendUpdates();
            }
        }
    }

    @Override
    public void onClientConnected(HostedConnection connection) {
        this.hostedEntityDataMap.put(connection, new HostedEntityData(connection, (DefaultEntityData) stateManager.getState(EntityDataState.class).getEntityData()));
    }

    @Override
    public void onClientDisconnected(HostedConnection connection) {

    }

    @Override
    public void onMessageReceived(HostedConnection source, AbstractMessage m) {
        if (m instanceof JoinGameMessage) {
            // we add the player object contained in the message
            JoinGameMessage jm = (JoinGameMessage) m;
            Player player = jm.getPlayer();
            this.connectionPlayerMap.put(source, player);

            // we add the player to our MainGameAppState instance
            // this will create a game session for that player
            // we later will call the messages of that interface
            // from incoming messages
            this.mainGameAppState.addPlayer(player);

            // to inform the client about the add we send the message back
            source.send(m);

            if (++playerCounter >= 2) {
                mainGameAppState.setupGame(GameUtils.createSimpleGameField(), Setting.Classic);
            }
        } else if (m instanceof ReadyForGameStartMessage) {
            readyCounter++;
            if (readyCounter >= 2) {
                System.out.println("start game");
                mainGameAppState.startGame();
            }
        }

        // -----------------------------------
        // Entity System Messages
        // ---------------------------

        if (m instanceof GetEntitiesMessage) {
            GetEntitiesMessage gm = (GetEntitiesMessage) m;
            hostedEntityDataMap.get(source).getEntities(gm);
        } else if (m instanceof CloseEntitySetMessage) {
            CloseEntitySetMessage cm = (CloseEntitySetMessage) m;
            hostedEntityDataMap.get(source).closeEntitySet(cm);
        }

        // -------------------------------------
        // game session messages
        // -------------------------------------
        if (m instanceof ApplyMoveDirectionMessage) {
            ApplyMoveDirectionMessage am = (ApplyMoveDirectionMessage) m;
            getGameSession(source).applyMoveDirection(am.getMoveDirection());
        }


    }

    private GameSession getGameSession(HostedConnection connection) {
        return this.mainGameAppState.getGameSession(connectionPlayerMap.get(connection));
    }

    @Override
    public void cleanup() {
        this.server.close();
        this.connectionPlayerMap.clear();
    }

    private class ServerSideGameStateListener implements GameStateListener {

        private void sendToAllPlayers(AbstractMessage m) {
            for (HostedConnection connection : connectionPlayerMap.keySet()) {
                connection.send(m);
            }
        }

        @Override
        public void onSetupGame(Setting setting, int gameFieldSizeX, int gameFieldSizeY) {
            sendToAllPlayers(new OnSetupGameMessage(setting, gameFieldSizeX, gameFieldSizeY));
        }

        @Override
        public void onStartGame() {
            sendToAllPlayers(new OnGameStartMessage());
        }
    }
}
