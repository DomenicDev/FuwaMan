package de.fuwa.bomberman.game.gui;

import de.fuwa.bomberman.game.utils.GameOptions;

public interface GameMenuListener {

    void onClickSingleplayer();

    void onClickMultiplayer();

    void onClickOptions();

    void onClickCredits();

    void onClickExit();

    void onClickCloseGame();

    void onClickStartGame(GameOptions gameOptions);

    void onClickReturnToMainMenu();

    void onClickConnectToGame(String ipAddress);

    void onClickOpenConnectScreen();
}
