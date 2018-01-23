package de.fuwa.bomberman.game.appstates;

import de.fuwa.bomberman.app.AppStateManager;
import de.fuwa.bomberman.app.BaseAppState;
import de.fuwa.bomberman.app.GameApplication;
import de.fuwa.bomberman.app.gui.GameContextFrame;
import de.fuwa.bomberman.game.MultiPlayerGame;
import de.fuwa.bomberman.game.RemoteMultiplayerGame;
import de.fuwa.bomberman.game.SingleplayerGame;
import de.fuwa.bomberman.game.appstates.multiplayer.GameClient;
import de.fuwa.bomberman.game.appstates.sound.SoundVolumeAppState;
import de.fuwa.bomberman.game.gui.BasicFuwaManPanel;
import de.fuwa.bomberman.game.gui.GameMenuListener;
import de.fuwa.bomberman.game.utils.GameConstants;
import de.fuwa.bomberman.game.utils.GameOptions;

import javax.swing.*;

public class FuwaManAppState extends BaseAppState implements GameMenuListener {

    private AbstractGame game;
    private GameContextFrame context;
    private AppStateManager stateManager;
    private GameApplication gameApplication;
    private FuwaManGuiHolderAppState guiHolder;
    private SoundVolumeAppState soundVolumeAppState;

    @Override
    public void initialize(AppStateManager stateManager) {
        this.stateManager = stateManager;
        this.gameApplication = stateManager.getGameApplication();

        this.soundVolumeAppState = new SoundVolumeAppState();
        stateManager.attachState(soundVolumeAppState);

        this.guiHolder = new FuwaManGuiHolderAppState();
        stateManager.attachState(guiHolder);

        // define listener
        BasicFuwaManPanel.setListener(this);

        // go to main menu
        this.context = stateManager.getGameApplication().getGameContext();
        this.context.setScreen(guiHolder.getMainMenu());
    }

    private void detachOldGame() {
        if (game != null) {
            game.closeGame();
            stateManager.detachState(game);
            this.game = null;
        }
    }
    @Override
    public void onClickFullscreen(){
    context.getGraphicsConfiguration().getDevice().setFullScreenWindow(context);
    }

    @Override
    public void onClickWindow(){
    context.getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
    }

    @Override
    public void onClickSingleplayer() {
        gameApplication.addCallable(() -> {
            context.setScreen(guiHolder.getSingleplayerMenu());

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
            context.setScreen(guiHolder.getMultiplayerLobbyMenu());
            // create multi player session
            game = new MultiPlayerGame();
            stateManager.attachState(game);
        });

    }

    @Override
    public void onClickOptions() {
        gameApplication.addCallable(() -> {
            detachOldGame();
            context.setScreen(guiHolder.getOptionsMenu());
        });
    }
    @Override
    public void onClickSaveChanges(){
        //volume = max-unchanged;
        soundVolumeAppState.setVolume(soundVolumeAppState.getMax()-soundVolumeAppState.getUnchanged());

        gameApplication.addCallable(() -> {
            detachOldGame();
            context.setScreen(guiHolder.getMainMenu());
        });
    }

    @Override
    public void onVolumeChange(float volume){
        soundVolumeAppState.setUnchanged(volume);
    }

    @Override
    public void onClickCredits() {
        gameApplication.addCallable(() -> context.setScreen(guiHolder.getCredits()));
    }

    @Override
    public void onClickExit() {
        gameApplication.addCallable(() -> this.gameApplication.destroy());
    }

    @Override
    public void onClickOpenLevelEditor() {
        gameApplication.addCallable(() -> context.setScreen(guiHolder.getLevelEditorMenu()));
    }

    @Override
    public void onClickCloseGame() {
        gameApplication.addCallable(() -> {
            this.context.setScreen(guiHolder.getMainMenu());
            if (game != null) {
                game.closeGame();
            }
            detachOldGame();
        });
    }

    @Override
    public void onClickStartGame(GameOptions gameOptions) {
        gameApplication.addCallable(() -> this.game.startGame(gameOptions));

    }

    @Override
    public void onClickReturnToMainMenu() {
        soundVolumeAppState.setUnchanged(soundVolumeAppState.getVolume());
        gameApplication.addCallable(() -> {
            detachOldGame();
            context.setScreen(guiHolder.getMainMenu());
        });

    }

    @Override
    public void onClickConnectToGame(String ipAddress) {
        gameApplication.addCallable(() -> {
            GameClient client = new GameClient();
            if (client.connect(ipAddress, GameConstants.PORT)) {
                //   GameStateHandler stateHandler = new GameStateHandler();
                // stateManager.attachState(stateHandler);
                // client.setGameStateListener(stateHandler);
                stateManager.attachState(client);

                detachOldGame();
                game = new RemoteMultiplayerGame(client);
                stateManager.attachState(game);
                context.setScreen(guiHolder.getClientWaitingLobbyMenu());
                client.start();
            } else {
                JOptionPane.showMessageDialog(null, "Could not connect to this address!");
            }
        });
    }

    @Override
    public void onClickOpenConnectScreen() {
        gameApplication.addCallable(() -> this.context.setScreen(guiHolder.getMultiplayerConnectMenu()));
    }
}
