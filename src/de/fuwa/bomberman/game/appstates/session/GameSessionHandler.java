package de.fuwa.bomberman.game.appstates.session;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.EntityData;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.game.appstates.EntityDataState;
import de.fuwa.bomberman.game.appstates.SimpleMovementAppState;
import de.fuwa.bomberman.game.components.WalkableComponent;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;

/**
 * The GameSessionHandler provides the function to create GameSessions for players.
 * The actual implementation of the GameSession interface is done here as well.
 */
public class GameSessionHandler extends BaseAppState {

    private SimpleMovementAppState simpleMovementAppState;
    private EntityData entityData;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.entityData = stateManager.getState(EntityDataState.class).getEntityData();
        simpleMovementAppState = stateManager.getState(SimpleMovementAppState.class);
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
            // ToDo: implement as soon as missing classes are implemented
        }

        @Override
        public void applyMoveDirection(MoveDirection direction) {
            entityData.setComponent(playerId, new WalkableComponent(direction, 1));
        }
    }

}
