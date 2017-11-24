package de.fuwa.bomberman.game.appstates.session;

import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.es.EntityId;
import de.fuwa.bomberman.game.session.GameSession;

import java.util.HashMap;
import java.util.Map;

public class MultipleGameSessionAppState extends BaseAppState {

    private Map<EntityId, GameSession> gameSessionMap = new HashMap<>();

    public void addGameSession(EntityId playerId, GameSession gameSession){
        this.gameSessionMap.put(playerId, gameSession);
    }

    public void removeGameSession(EntityId playerId) {
        this.gameSessionMap.remove(playerId);
    }

    public GameSession getGameSession(EntityId playerId) {
        return this.gameSessionMap.get(playerId);
    }

    @Override
    public void cleanup() {
        this.gameSessionMap.clear();
    }
}
