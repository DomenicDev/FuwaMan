package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.EntitySet;
import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.game.appstates.session.GameSessionHandler;
import de.fuwa.bomberman.game.appstates.session.MultipleGameSessionAppState;
import de.fuwa.bomberman.game.components.AliveComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.BlockType;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.state.GameStateListener;
import de.fuwa.bomberman.game.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class controls the overall main game state logic.
 * This means that the game can be prepared, started and stopped in here.
 * <p>
 * In a networked environment this class will run on server side
 * and inform all clients about the game state events.
 */
public class MainGameAppState extends BaseAppState {

    private AppStateManager stateManager;
    private GameSessionHandler gameSessionHandler;
    private MultipleGameSessionAppState gameSessionAppState;
    private EntityDataState entityDataState;
    // used as a return value
    private static final Player NONE_WINNER = new Player(null);

    private List<GameStateListener> gameStateListeners = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Map<Player, EntityId> playerEntityIdMap = new HashMap<>();
    private EntityData entityData;
    private GameOptions gameOptions; // current game options
    private boolean gameRunning = false;
    private float remainingTime = -1;
    private EntitySet aliveEntities; // to get notified about players who died



    public MainGameAppState() {

    }

    public MainGameAppState(AppStateManager stateManager) {
        this.stateManager = stateManager;
        init(stateManager);

    }

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
        if (entityData == null && gameSessionAppState == null) {
            init(stateManager);
        }
    }

    public EntityData getEntityData() {
        return entityData;
    }

    private void init(AppStateManager stateManager) {
        this.gameSessionAppState = new MultipleGameSessionAppState();
        stateManager.attachState(gameSessionAppState);

        // we init entity data
        this.entityData = new DefaultEntityData();
        this.entityDataState = new EntityDataState(entityData);
        stateManager.attachState(entityDataState);
    }

    public void addGameStateListener(GameStateListener listener) {
        this.gameStateListeners.add(listener);
    }

    public void addPlayer(Player player) {
        if (this.players.size() < 4) {
            this.players.add(player);
        }
    }

    public int sizeOfAddedPlayers() {
        return players.size();
    }

    public void removePlayer(Player player) {
        if (players.remove(player)) {

        }
    }

    public GameSession getGameSession(Player player) {
        EntityId playerId = playerEntityIdMap.get(player);
        return this.gameSessionAppState.getGameSession(playerId);
    }

    public void setupGame(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
        GameField gameField = gameOptions.getGameField();
        if (gameStateListeners.isEmpty()) {
            System.out.println("cannot start game if no players have joined the session");
            return;
        }

        // first we want to add the kis
        for (int i = 0; i < gameOptions.getNumberOfKis(); i++) {
            addPlayer(new Player("Ki#" + i, true));
        }

        // create alive entity set
        this.aliveEntities = entityData.getEntities(AliveComponent.class);

        // we initialize our logical game field app state
        this.stateManager.attachState(new LogicalGameFieldAppState(gameField.getSizeX(), gameField.getSizeY()));

        // init game logic app states
        GameInitializer.initGameLogicAppStates(stateManager);

        // we create our game session handler
        this.gameSessionHandler = new GameSessionHandler();
        stateManager.attachState(this.gameSessionHandler);

        // we want to create the entities for the for the blocks
        createEntitiesForGameField(entityData, gameField);

        // now we create the game sessions
        for (Player player : players) {
            // create game session
            EntityId playerId = entityData.createEntity();
            GameSession gameSession = gameSessionHandler.createGameSession(playerId);
            //gameSessionMap.put(playerId, gameSession); // deprecated
            this.gameSessionAppState.addGameSession(playerId, gameSession);
            playerEntityIdMap.put(player, playerId);
        }

        // next, we would like to create the players
        // for that we need to get the start positions of the players
        PositionComponent[] startPositions = getStartPositions(players.size(), gameField.getSizeX(), gameField.getSizeY());
        if (startPositions == null) {
            return; // should never be the case hopefully
        }

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            String playerName = player.getName();
            PositionComponent startPos = startPositions[i];
            EntityId playerId = playerEntityIdMap.get(player);
            EntityCreator.createPlayer(entityData, playerId, startPos.getX(), startPos.getY(), playerName, player.isKi());
        }

        for (GameStateListener listener : gameStateListeners) {
            listener.onSetupGame(gameOptions.getSetting(), gameField.getSizeX(), gameField.getSizeY());
        }

    }

    public void startGame() {
        setGameRunning(true);
        this.gameSessionHandler.setGameStarted(true);
        this.remainingTime = gameOptions.getMatchDuration();
        for (GameStateListener l : gameStateListeners) {
            l.onStartGame(gameOptions.getMatchDuration());
        }
    }

    public void closeGame() {
        setGameRunning(false);
        this.gameOptions = null;
        for (GameStateListener l : gameStateListeners) {
            l.onCloseGame();
        }
    }

    private void createEntitiesForGameField(EntityData entityData, GameField gameField) {
        for (int y = 0; y < gameField.getSizeY(); y++) {
            for (int x = 0; x < gameField.getSizeX(); x++) {
                BlockType type = gameField.getBlockType(x, y);
                if (type == BlockType.Destroyable) {
                    EntityCreator.createBlock(entityData, x, y, true);
                } else if (type == BlockType.Undestroyable) {
                    EntityCreator.createBlock(entityData, x, y, false);
                }
            }
        }
    }

    private PositionComponent[] getStartPositions(int amountPlayers, int width, int height) {
        // this algorithm only works for maximum 4 players
        if (amountPlayers > 0 && amountPlayers > 4) {
            System.out.println("cannot create start position for 0 or more than 4 players");
            return null;
        }

        PositionComponent[] startPositions = new PositionComponent[amountPlayers];
        for (int i = 0; i < amountPlayers; i++) {
            PositionComponent pos;
            switch (i) {
                case 0:
                    pos = new PositionComponent(1, 1);
                    break;
                case 1:
                    pos = new PositionComponent(width - 2, 1);
                    break;
                case 2:
                    pos = new PositionComponent(1, height - 2);
                    break;
                case 3:
                    pos = new PositionComponent(width - 2, height - 2);
                    break;
                default:
                    pos = null;
            }
            startPositions[i] = pos;
        }
        return startPositions;
    }

    private void setGameRunning(boolean running) {
        this.gameRunning = running;
    }

    private void checkForWinner() {
        // we check who is alive
        int alivePlayersCount = aliveEntities.size();
        if (alivePlayersCount == 1) {
            for (Map.Entry<Player, EntityId> e : playerEntityIdMap.entrySet()) {
                if (aliveEntities.containsId(e.getValue())) {
                    // we have a winner
                    setGameDecided(e.getKey());
                }
            }
        } else if (alivePlayersCount == 0) {
            // if there is no player alive probably all died in the same explosion
            // we return this static value to indicate that the game is undecided
            setGameDecided(NONE_WINNER);
        }
        // no player has won yet
        // so we do nothing
    }

    private void setGameDecided(Player winner) {
        setGameRunning(false);
        for (GameStateListener l : gameStateListeners) {
            l.onGameDecided(winner.getName());
        }
    }


    @Override
    public void update(float tpf) {
        if (gameRunning) {

            if (aliveEntities.applyChanges()) {
                System.out.println(aliveEntities.size());
                checkForWinner();
            }

            remainingTime -= tpf;
            if (remainingTime <= 0) {
                setGameRunning(false);
                remainingTime = -1;
                setGameDecided(NONE_WINNER); // timeout --> no one has won
            }
        }
    }

    @Override
    public void cleanup() {
        if (aliveEntities != null) {
            this.aliveEntities.close();
            this.aliveEntities.clear();
            this.aliveEntities = null;
        }

        this.gameOptions = null;
        stateManager.detachState(gameSessionHandler);
        stateManager.detachState(entityDataState);
        stateManager.detachState(stateManager.getState(LogicalGameFieldAppState.class));
        stateManager.detachState(gameSessionAppState);
        GameInitializer.removeGameLogicAppStates(stateManager);
    }
}
