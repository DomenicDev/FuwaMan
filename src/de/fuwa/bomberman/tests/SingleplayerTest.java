package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.enums.Setting;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.GameUtils;
import de.fuwa.bomberman.game.utils.Player;

public class SingleplayerTest extends GameApplication {

    private Player player = new Player("Bomberman");
    ;

    public static void main(String[] args) {
        SingleplayerTest test = new SingleplayerTest();
        AppSettings settings = new AppSettings(800, 600, false);
        test.start(settings);
    }

    @Override
    public void initGame() {
        MainGameAppState mainGameAppState = new MainGameAppState();
        getStateManager().attachState(mainGameAppState);
        getStateManager().attachState(new GameStateHandler());

        getStateManager().attachState(new Initializer());
    }

    private class Initializer extends BaseAppState {
        @Override
        public void initialize(AppStateManager stateManager) {
            // we add a player and start our session
            MainGameAppState mainGameAppState = stateManager.getState(MainGameAppState.class);
            mainGameAppState.addPlayer(player);
            mainGameAppState.addGameStateListener(stateManager.getState(GameStateHandler.class));

            // get game session and create holder class
            GameSession gameSession = mainGameAppState.getGameSession(player);
            stateManager.attachState(new GameSessionAppState(gameSession));

            //  we now want to setup and run the game
            mainGameAppState.setupGame(GameUtils.createSimpleGameField(), Setting.Classic);
            mainGameAppState.startGame();
        }
    }
}
