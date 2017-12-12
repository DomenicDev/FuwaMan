package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.appstates.sound.SoundVolumeAppState;
import de.fuwa.bomberman.game.utils.GameOptions;

public interface GameMenuListener {

    void onClickSingleplayer();

    void onClickMultiplayer();

    void onClickOptions();

    void onClickCredits();

    void onClickExit();

    void onClickOpenLevelEditor();

    void onClickCloseGame();

    void onClickStartGame(GameOptions gameOptions);

    void onClickReturnToMainMenu();

    void onClickConnectToGame(String ipAddress);

    void onClickOpenConnectScreen();

    void onClickFullscreen();

    void onClickWindow();

    void onClickVolumeUp();

    void onClickVolumeDown();

    void onClickSaveChanges();

}
