package de.fuwa.bomberman.game.appstates.session;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.game.appstates.BombAppState;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.PhysicsCharacterMovementAppState;
import de.fuwa.bomberman.game.components.PlayerComponent;
import de.fuwa.bomberman.game.components.PositionComponent;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;

/**
 * The GameSessionHandler provides the function to create GameSessions for players.
 * The actual implementation of the GameSession interface is done here as well.
 */
public class GameSessionHandler extends BaseAppState {

    private EntityData entityData;
    private BombAppState bombAppState;
    private PhysicsCharacterMovementAppState movementAppState;

    private boolean gameStarted = false;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        this.bombAppState = stateManager.getState(BombAppState.class);
        this.movementAppState = stateManager.getState(PhysicsCharacterMovementAppState.class);
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Creates a new GameSession object for the specified player.
     * Used when player joins a game.
     * @param playerId the player id the game session shall be created for
     * @return the game session created for the specified player
     */
    public GameSession createGameSession(EntityId playerId) {
        if (playerId == null) {
            return null;
        }
        return new DefaultGameSession(playerId);
    }

    // a default implementation of the GameSession interface
    private class DefaultGameSession implements GameSession {

        private EntityId playerId;

        private DefaultGameSession(EntityId playerId) {
            this.playerId = playerId;
        }

        @Override
        public void placeBomb() {
            if (!isGameStarted()) return;
            PositionComponent playerPos = entityData.getComponent(playerId,PositionComponent.class);
            if(playerPos != null){
                PlayerComponent playCom = entityData.getComponent(playerId, PlayerComponent.class);
                bombAppState.placeBomb(playerPos, playCom.getBombStrength(), playCom, playerId);
            }
        }

        @Override
        public void applyMoveDirection(MoveDirection direction) {
            if (isGameStarted()) {
                movementAppState.updateMoveDirection(playerId, direction);
            }
        }
    }

}
