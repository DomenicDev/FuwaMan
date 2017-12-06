package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.SingleplayerGame;
import de.fuwa.bomberman.game.utils.GameUtils;

public class TestNewSingleplayer extends GameApplication {

    SingleplayerGame game;

    public static void main(String[] args) {
        TestNewSingleplayer test = new TestNewSingleplayer();
        AppSettings settings = new AppSettings(800, 600, false);
        test.start(settings);
    }

    @Override
    public void initGame() {
        this.game = new SingleplayerGame();
        getStateManager().attachState(game);
        getStateManager().attachState(new Init());
    }

    private class Init extends BaseAppState {
        @Override
        public void initialize(AppStateManager stateManager) {
            game.startGame(GameUtils.getTestGameOptions());
        }
    }
}
