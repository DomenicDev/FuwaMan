package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.Callable;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.app.gui.GameContextFrame;
import de.fuwa.bomberman.game.MultiPlayerGame;
import de.fuwa.bomberman.game.SingleplayerGame;
import de.fuwa.bomberman.game.appstates.multiplayer.GameClient;
import de.fuwa.bomberman.game.appstates.state.GameStateHandler;
import de.fuwa.bomberman.game.gui.*;
import de.fuwa.bomberman.game.utils.GameConstants;
import de.fuwa.bomberman.game.utils.GameOptions;

import javax.swing.*;

public class FuwaManAppState extends BaseAppState implements GameMenuListener {

    private AbstractGame game;
    private GameContextFrame context;
    private AppStateManager stateManager;
    private GameApplication gameApplication;

    private MainMenu mainMenu = new MainMenu();
    private SingleplayerMenu singleplayerMenu = new SingleplayerMenu();
    private MultiplayerConnectMenu multiplayerConnectMenu = new MultiplayerConnectMenu();
    private MultiplayerLobbyMenu multiplayerLobbyMenu = new MultiplayerLobbyMenu();

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
        this.gameApplication = stateManager.getGameApplication();

        // define listener
        BasicFuwaManPanel.setListener(this);

        // go to main menu
        this.context = stateManager.getGameApplication().getGameContext();
        this.context.setScreen(mainMenu);
    }

    private void detachOldGame() {
        if (game != null) {
            stateManager.detachState(game);
        }
    }

    @Override
    public void onClickSingleplayer() {
        gameApplication.addCallable(() -> {
            context.setScreen(singleplayerMenu);

            // create single player game
            detachOldGame();
            game = new SingleplayerGame();
            stateManager.attachState(game);
        });

    }

    @Override
    public void onClickMultiplayer() {
        gameApplication.addCallable(() -> {
            detachOldGame();
            // change screen
            context.setScreen(multiplayerLobbyMenu);
            // create multi player session
            game = new MultiPlayerGame();
            stateManager.attachState(game);
        });

    }

    @Override
    public void onClickOptions() {

    }

    @Override
    public void onClickCredits() {

    }

    @Override
    public void onClickExit() {
        gameApplication.addCallable(() -> this.gameApplication.destroy());
    }

    @Override
    public void onClickStartGame(GameOptions gameOptions) {
        gameApplication.addCallable(() -> this.game.startGame(gameOptions));

    }

    @Override
    public void onClickReturnToMainMenu() {
        gameApplication.addCallable(() -> {
            detachOldGame();
            context.setScreen(mainMenu);
        });

    }

    @Override
    public void onClickConnectToGame(String ipAddress) {
        System.out.println("hier");
        gameApplication.addCallable(new Callable() {
            @Override
            public void run() {
                System.out.println(2d);
                GameClient client = new GameClient();
                if (client.connect(ipAddress, GameConstants.PORT)) {
                    GameStateHandler stateHandler = new GameStateHandler();
                    stateManager.attachState(stateHandler);
                    client.setGameStateListener(stateHandler);
                    stateManager.attachState(client);
                    client.start();
                } else {
                    JOptionPane.showMessageDialog(null, "Could not connect to this address!");
                }
            }
        });
    }

    @Override
    public void onClickOpenConnectScreen() {
        gameApplication.addCallable(() -> this.context.setScreen(multiplayerConnectMenu));
    }
}
