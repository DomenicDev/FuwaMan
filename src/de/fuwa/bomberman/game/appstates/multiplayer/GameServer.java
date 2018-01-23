package de.fuwa.bomberman.game.appstates.multiplayer;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.es.net.HostedEntityDataHandler;
import de.fuwa.bomberman.game.appstates.FuwaManGuiHolderAppState;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.multiplayer.messages.*;
import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.state.GameStateListener;
import de.fuwa.bomberman.game.utils.Player;
import de.fuwa.bomberman.network.ConnectionListener;
import de.fuwa.bomberman.network.HostedConnection;
import de.fuwa.bomberman.network.MessageListener;
import de.fuwa.bomberman.network.Server;
import de.fuwa.bomberman.network.messages.AbstractMessage;

import java.util.HashMap;
import java.util.Map;

public class GameServer extends BaseAppState implements ConnectionListener, MessageListener<HostedConnection> {

    private static final int MIN_PLAYERS = 1; // JUST FOR TESTING --> GOING TO BE REMOVED LATER

    private int playerCounter = 0;
    private int readyCounter = 0;
    private AppStateManager stateManager;
    private Server server;
    private HostedEntityDataHandler hostedEntityDataHandler;
    private int port;
    private MainGameAppState mainGameAppState;
    private Map<HostedConnection, Player> connectionPlayerMap = new HashMap<>();

    private float updateInterval = 0.1f; // 10 updates per second
    private float updateTimer = 0;

    /**
     * Will create a FuwaMan server at the specified port
     *
     * @param port the port of the server
     */
    public GameServer(int port) {
        this.port = port;
        // init server
        this.server = new Server(this.port);
        this.server.addConnectionListener(this);
        this.server.addMessageListener(this);
        this.server.start();
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void setEntityData(DefaultEntityData entityData) {
        // create handler which takes care of the entity system messages
        this.hostedEntityDataHandler = new HostedEntityDataHandler(entityData);
        this.server.addMessageListener(hostedEntityDataHandler);
        this.server.addConnectionListener(hostedEntityDataHandler);
    }

    public void setMainGameAppState(MainGameAppState mainGameAppState) {
        this.mainGameAppState = mainGameAppState;
        // this will inform all players about in-game events
        this.mainGameAppState.addGameStateListener(new ServerSideGameStateListener());
    }

    @Override
    public void update(float tpf) {
        if (hostedEntityDataHandler != null && (updateTimer += tpf) >= updateInterval) {
            updateTimer = 0;
            hostedEntityDataHandler.sendUpdates();
        }
    }

    @Override
    public void onClientConnected(HostedConnection connection) {

    }

    @Override
    public void onClientDisconnected(HostedConnection connection) {

    }

    @Override
    public void onMessageReceived(HostedConnection source, AbstractMessage m) {
        if (m instanceof JoinGameMessage) {

            // we first check if we even can add more players
            if (++playerCounter >= 5) {
                return;
            }

            // we add the player object contained in the message
            JoinGameMessage jm = (JoinGameMessage) m;
            Player player = jm.getPlayer();
            this.connectionPlayerMap.put(source, player);

            // we add the player to our MainGameAppState instance
            // this will create a game session for that player
            // we later will call the messages of that interface
            // from incoming messages

            mainGameAppState.addPlayer(player);

            // we add the player to the lobby menu
            stateManager.getState(FuwaManGuiHolderAppState.class).getMultiplayerLobbyMenu().addPlayerName(player.getName());

            // to inform the client about the add we send the message back
            source.send(m);


        } else if (m instanceof ReadyForGameStartMessage) {
            readyCounter++;
        }

        // -------------------------------------
        // game session messages
        // -------------------------------------
        if (m instanceof ApplyMoveDirectionMessage) {
            ApplyMoveDirectionMessage am = (ApplyMoveDirectionMessage) m;
            getGameSession(source).applyMoveDirection(am.getMoveDirection());
        } else if (m instanceof PlaceBombMessage) {
            getGameSession(source).placeBomb();
        }


    }

    public boolean areAllPlayersReady() {
        return readyCounter == playerCounter;
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
        public void onStartGame(float matchDuration) {
            sendToAllPlayers(new OnGameStartMessage(matchDuration));
        }

        @Override
        public void onGameDecided(String winnerName) {
            sendToAllPlayers(new OnGameDecidedMessage(winnerName));
        }

        @Override
        public void onCloseGame() {
            sendToAllPlayers(new OnCloseGameMessage());
        }
    }
}
