package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.enums.MoveDirection;
import de.fuwa.bomberman.game.session.GameSession;

public class InputAppState extends BaseAppState {

    @Override
    public void initialize(AppStateManager stateManager) {
        GameSession gameSession = stateManager.getState(GameSessionAppState.class).getGameSession();

        gameSession.applyMoveDirection(MoveDirection.Left);
    }
}
