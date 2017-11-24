package de.fuwa.bomberman.game.appstates.session;

import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.session.GameSession;

/**
 * This app state just holds the GameSession object given in the constructor.
 * Note: There is only one AppState of this type which holds the session of the local player.
 */
public class GameSessionAppState extends BaseAppState {

    private GameSession gameSession;

    /**
     * The AppState will hold the specified GameSession object
     * to make it be accessible for other app states.
     * @param gameSession
     */
    public GameSessionAppState(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    /**
     * @return the game session for this player.
     */
    public GameSession getGameSession() {
        return gameSession;
    }

    @Override
    public void cleanup() {
        this.gameSession = null;
    }
}
