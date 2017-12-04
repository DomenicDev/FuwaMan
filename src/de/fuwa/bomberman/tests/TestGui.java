package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.gui.BasicFuwaManPanel;
import de.fuwa.bomberman.game.gui.GameMenuListener;
import de.fuwa.bomberman.game.gui.MainMenu;
import de.fuwa.bomberman.game.gui.SingleplayerMenu;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.GameOptions;
import de.fuwa.bomberman.game.utils.GameUtils;
import de.fuwa.bomberman.game.utils.Player;

public class TestGui extends GameApplication {

    private MainGameAppState mainGameAppState = new MainGameAppState();

    private MainMenu mainMenu = new MainMenu();
    private SingleplayerMenu singleplayerMenu = new SingleplayerMenu();

    private Player player = new Player("Domenic");

    public static void main(String[] args) {
        TestGui test = new TestGui();
        AppSettings settings = new AppSettings(800, 600, false);
        test.start(settings);
    }

    @Override
    public void initGame() {
        getStateManager().attachState(mainGameAppState);
        getStateManager().attachState(new GameStateHandler());

        // set listener
        BasicFuwaManPanel.setListener(new GameMenuHandler());
        getGameContext().setScreen(mainMenu);
    }

    private class GameMenuHandler implements GameMenuListener {

        @Override
        public void onClickSingleplayer() {
            getGameContext().setScreen(singleplayerMenu);
        }

        @Override
        public void onClickMultiplayer() {

        }

        @Override
        public void onClickOptions() {

        }

        @Override
        public void onClickCredits() {

        }

        @Override
        public void onClickExit() {
            destroy();
        }

        @Override
        public void onClickStartSingleplayerGame(GameOptions gameOptions) {
            //       GameField gameField = GameUtils.createSimpleGameField(); // Todo: use values
            //         getGameContext().createAndDisplayGameField(gameField.getSizeX(), gameField.getSizeY());

            mainGameAppState.addGameStateListener(getStateManager().getState(GameStateHandler.class));

            // add player
            mainGameAppState.addPlayer(player);

            // add kis
            for (int i = 0; i < gameOptions.getNumberOfKis(); i++) {
                mainGameAppState.addPlayer(new Player("Ki#" + i, true));
            }

            mainGameAppState.setupGame(GameUtils.createComplexGameField(), gameOptions.getSetting());

            GameSession gameSession = mainGameAppState.getGameSession(player);
            getStateManager().attachState(new GameSessionAppState(gameSession));

            mainGameAppState.startGame();

        }

        @Override
        public void onClickReturnToMainMenu() {
            getGameContext().setScreen(mainMenu);
        }
    }
}
