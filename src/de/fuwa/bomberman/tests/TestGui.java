package de.fuwa.bomberman.tests;

import de.fuwa.bomberman.app.AppSettings;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.game.appstates.MainGameAppState;
import de.fuwa.bomberman.game.appstates.multiplayer.GameClient;
import de.fuwa.bomberman.game.appstates.session.GameSessionAppState;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.gui.*;
import de.fuwa.bomberman.game.session.GameSession;
import de.fuwa.bomberman.game.utils.GameConstants;
import de.fuwa.bomberman.game.utils.GameOptions;
import de.fuwa.bomberman.game.utils.Player;

import javax.swing.*;

public class TestGui extends GameApplication {

    private MainGameAppState mainGameAppState = new MainGameAppState();

    private MainMenu mainMenu = new MainMenu();
    private SingleplayerMenu singleplayerMenu = new SingleplayerMenu();
    private MultiplayerConnectMenu multiplayerConnectMenu = new MultiplayerConnectMenu();

    private Player player = new Player("Domenic");

    public static void main(String[] args) {
        TestGui test = new TestGui();
        AppSettings settings = new AppSettings(800, 600, false);
        test.start(settings);
    }

    @Override
    public void initGame() {


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
            getGameContext().setScreen(multiplayerConnectMenu);
        }

        @Override
        public void onClickOptions() {

        }

        @Override
        public void onClickCredits() {

        }

        @Override
        public void onClickExit() {
            addCallable(() -> destroy());
        }

        @Override
        public void onClickCloseGame() {

        }

        @Override
        public void onClickStartGame(GameOptions gameOptions) {
            getStateManager().getGameApplication().addCallable(() -> {

                getStateManager().attachState(mainGameAppState);

                GameStateHandler gameStateHandler = new GameStateHandler();
                getStateManager().attachState(gameStateHandler);

                //     System.out.println(Thread.currentThread());
                //       GameField gameField = GameUtils.createSimpleGameField(); // Todo: use values
                //         getGameContext().createAndDisplayGameField(gameField.getSizeX(), gameField.getSizeY());

                mainGameAppState.addGameStateListener(gameStateHandler);

                // add player
                mainGameAppState.addPlayer(player);

                // add kis
                for (int i = 0; i < gameOptions.getNumberOfKis(); i++) {
                    mainGameAppState.addPlayer(new Player("Ki#" + i, true));
                }

                //      mainGameAppState.setupGame(GameUtils.createComplexGameField(), gameOptions.getSetting());

                GameSession gameSession = mainGameAppState.getGameSession(player);
                getStateManager().attachState(new GameSessionAppState(gameSession));

                mainGameAppState.startGame();
            });


        }

        @Override
        public void onClickReturnToMainMenu() {
            addCallable(() -> getGameContext().setScreen(mainMenu));
        }

        @Override
        public void onClickConnectToGame(String ipAddress) {
            addCallable(() -> {
                GameClient client = new GameClient();
                if (client.connect(ipAddress, GameConstants.PORT)) {
                    GameStateHandler stateHandler = new GameStateHandler();
                    getStateManager().attachState(stateHandler);
                    client.setGameStateListener(stateHandler);
                    getStateManager().attachState(client);
                    client.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Could not connect to this address!");
                }
            });
        }

        @Override
        public void onClickOpenConnectScreen() {

        }
    }
}
