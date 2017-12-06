package de.fuwa.bomberman.game.state;

import de.fuwa.bomberman.game.enums.Setting;

public interface GameStateListener {

    void onSetupGame(Setting setting, int gameFieldSizeX, int gameFieldSizeY);

    void onStartGame();

    void onCloseGame();

}
