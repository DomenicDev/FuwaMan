package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.es.base.DefaultEntityData;
import de.fuwa.bomberman.game.appstates.session.GameSessionHandler;
import de.fuwa.bomberman.game.appstates.session.MultipleGameSessionAppState;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.BlockType;
import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.state.GameStateListener;
import de.fuwa.bomberman.game.utils.EntityCreator;
import de.fuwa.bomberman.game.utils.GameField;
import de.fuwa.bomberman.game.utils.GameInitializer;
import de.fuwa.bomberman.game.utils.Player;

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

    private List<GameStateListener> gameStateListeners = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Map<Player, EntityId> playerEntityIdMap = new HashMap<>();

    private EntityData entityData;

    private boolean gameStarted = false;

    public MainGameAppState() {
    }

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;

        this.gameSessionAppState = new MultipleGameSessionAppState();
        stateManager.attachState(gameSessionAppState);

        // we init entity data
        this.entityData = new DefaultEntityData();
        this.entityDataState = new EntityDataState(entityData);
        stateManager.attachState(entityDataState);

        // init game logic app states
        GameInitializer.initGameLogicAppStates(stateManager);

        // we create our game session handler
        this.gameSessionHandler = new GameSessionHandler();
        stateManager.attachState(this.gameSessionHandler);
        stateManager.attachState(new BombAppState());

    }

    public void addGameStateListener(GameStateListener listener) {
        this.gameStateListeners.add(listener);
    }

    public void addPlayer(Player player) {
        if (this.players.size() < 4) {
            this.players.add(player);

            // create game session
            EntityId playerId = entityData.createEntity();
            GameSession gameSession = gameSessionHandler.createGameSession(playerId);
            //gameSessionMap.put(playerId, gameSession); // deprecated
            this.gameSessionAppState.addGameSession(playerId, gameSession);
            playerEntityIdMap.put(player, playerId);
        }
    }

    public void removePlayer(Player player) {
        if (players.remove(player)) {
            EntityId playerId = playerEntityIdMap.remove(player);
            gameSessionAppState.removeGameSession(playerId);
            entityData.removeEntity(playerId);
        }
    }

    public GameSession getGameSession(Player player) {
        EntityId playerId = playerEntityIdMap.get(player);
        return this.gameSessionAppState.getGameSession(playerId);
    }

    public void setupGame(GameField gameField, Setting setting) {
        if (gameStateListeners.isEmpty()) {
            System.out.println("cannot start game if no players have joined the session");
            return;
        }

        // we want to create the entities for the for the blocks
        createEntitiesForGameField(entityData, gameField);

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
            listener.onSetupGame(setting, gameField.getSizeX(), gameField.getSizeY());
        }

    }

    public void startGame() {
        gameSessionHandler.setGameStarted(true);
        for (GameStateListener l : gameStateListeners) {
            l.onStartGame();
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

    @Override
    public void cleanup() {
        stateManager.detachState(gameSessionHandler);
        stateManager.detachState(entityDataState);
        GameInitializer.removeGameLogicAppStates(stateManager);
    }
}
